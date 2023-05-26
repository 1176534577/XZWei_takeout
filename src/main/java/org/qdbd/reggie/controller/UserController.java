package org.qdbd.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.qdbd.reggie.common.R;
import org.qdbd.reggie.entity.User;
import org.qdbd.reggie.service.UserService;
import org.qdbd.reggie.utils.SMSUtils;
import org.qdbd.reggie.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final static String TOPIC_NAME = "telephone";

    /**
     * 发送手机验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpSession session, @RequestBody User user) throws ExecutionException, InterruptedException {
        // 获取手机号
        String phone = user.getPhone();


        // 生成随机的4为验证码
        if (StringUtils.isNotEmpty(phone)) {
            ListenableFuture<SendResult<String, String>> key = kafkaTemplate.send(TOPIC_NAME, "key", phone);
            SendResult<String, String> result = key.get();
            System.out.println("发送结果："+result.toString());

            return R.success("手机验证码短信发送成功");
        }

        return R.error("手机验证码短信发送失败");

    }

    /**
     * 移动端用户登陆
     *
     * @param session
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(HttpSession session, @RequestBody Map<String, String> map) {

        // 获取手机号
        String phone = map.get("phone");
        // 获取验证码
        String code = map.get("code");

        // String codeInSession = (String) session.getAttribute(phone);

        // 从redis获取验证码
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);

        if (codeInSession != null && codeInSession.equals(code)) {

            // 判断当前手机号对应的用户为新用户，若为新用户，自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            // 新用户，自动注册
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            // 如果用户登录成功，删除redis中缓存的验证码
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登录失败");

    }

}
