package org.qdbd.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qdbd.reggie.entity.AddressBook;
import org.qdbd.reggie.mapper.AddressBookMapper;
import org.qdbd.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
