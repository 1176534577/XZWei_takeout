package org.qdbd.reggie.controller;


import lombok.extern.slf4j.Slf4j;
import org.qdbd.reggie.common.CustomExpection;
import org.qdbd.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //  file必须与name相同
        // file是一个临时文件，需要转存，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        // 后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 文件名
        String fileName = UUID.randomUUID() + suffix;

        // 检查目录是否存在，若不存在，则新建
        File dir = new File(basePath);
        if (!dir.exists()) {
            // 目录不存在，新建目录
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(Paths.get("D:\\hello.jpg"))) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } */

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        try (FileInputStream fileInputStream = new FileInputStream(basePath + name);
             ServletOutputStream outputStream = response.getOutputStream()) {

            response.setContentType("image/jpeg");

            byte[] bytes = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

        } catch (Exception e) {
            throw new CustomExpection(e.getMessage());
        }
    }
}
