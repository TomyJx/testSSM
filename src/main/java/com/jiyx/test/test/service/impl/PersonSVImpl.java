package com.jiyx.test.test.service.impl;

import com.jiyx.test.spring.annotation.MyAutowired;
import com.jiyx.test.spring.annotation.MyService;
import com.jiyx.test.test.entity.Person;
import com.jiyx.test.test.repository.PersonMapper;
import com.jiyx.test.test.service.PersonSV;

import java.util.List;

/**
 * auther: jiyx
 * date: 2018/11/30.
 */
@MyService
public class PersonSVImpl implements PersonSV {

    @MyAutowired
    private PersonMapper personMapper;

    @Override
    public Person findById(long id) {
        return personMapper.findById(id);
    }

    @Override
    public List<Person> findByName(String name) {
        return personMapper.findByName(name);
    }

//    @Override
//    public void save(Person person) {
//        personMapper.save(person);
//    }
}
