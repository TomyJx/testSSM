package com.jiyx.test.springMVC.bindParam;

import com.jiyx.test.springMVC.MyModelAndView;
import com.jiyx.test.springMVC.MyModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * 页面渲染的过程
 * auther: jiyx
 * date: 2018/11/30.
 */
public class BindingRequestAndModel {

    /**
     * 将request和model中的数据绑定
     * @param request
     * @param myModelAndView
     */
    public static void bindingRequestAndModel(HttpServletRequest request, MyModelAndView myModelAndView) {
        MyModelMap modelMap = myModelAndView.getModelMap();
        if (modelMap != null) {
            modelMap.entrySet().stream().forEach(entry -> {
               request.setAttribute(entry.getKey(), entry.getValue());
            });
        }
    }
}
