package com.jiyx.test.test.service;

import com.jiyx.test.test.entity.Person;

import java.util.List;

/**
 * auther: jiyx
 * date: 2018/11/30.
 */
public interface PersonSV {

    Person findById(long id);

    List<Person> findByName(String name);

//    void save(Person person);
}
