package com.jiyx.test.test.ctrl;

import com.jiyx.test.spring.annotation.MyAutowired;
import com.jiyx.test.spring.annotation.MyController;
import com.jiyx.test.springMVC.MyModelAndView;
import com.jiyx.test.springMVC.MyModelMap;
import com.jiyx.test.springMVC.annotation.MyRequestMapping;
import com.jiyx.test.springMVC.annotation.RequestMethod;
import com.jiyx.test.test.entity.Person;
import com.jiyx.test.test.service.PersonSV;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * auther: jiyx
 * date: 2018/11/30.
 */
@MyController
@MyRequestMapping("/hello")
public class PersonCtrl {

    @MyAutowired
    private PersonSV personSV;

    @MyRequestMapping("/hello")
    public MyModelAndView testMyFramwork() {
        MyModelAndView success = new MyModelAndView("success");

        Person person = personSV.findById(1);

        MyModelMap myModelMap = new MyModelMap();
        myModelMap.addAttribute("test", person);
        success.setModelMap(myModelMap);
        return success;
    }
}
