package com.jiyx.test.test.repository;

import com.jiyx.test.spring.annotation.MyRepository;
import com.jiyx.test.test.entity.Person;

import java.util.List;

/**
 * auther: jiyx
 * date: 2018/11/30.
 */
@MyRepository
public interface PersonMapper {
    Person findById(long id);

    List<Person> findByName(String name);

//    void save(Person person);
}
