package com.jiyx.test.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * auther: jiyx
 * date: 2018/11/30.
 */
@Data
public class Person {

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }


    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Person(long id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    private long id;

    private String name;

    private int age;

    private String address;

}
