package com.jiyx.test.springMVC;

import lombok.Data;

/**
 * auther: jiyx
 * date: 2018/11/30.
 */
@Data
public class MyModelAndView {

    private String view;
    private MyModelMap modelMap;

    public MyModelAndView(String view) {
        this.view = view;
    }
}
