package com.jiyx.test.springMVC;

import com.jiyx.test.spring.InitBean;
import com.jiyx.test.springMVC.annotation.MyRequestMapping;
import com.jiyx.test.springMVC.bindParam.Binding;
import com.jiyx.test.springMVC.bindParam.BindingRequestAndModel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 前端控制器
 * auther: jiyx
 * date: 2018/11/28.
 */
@WebServlet(name = "dispatcherServlet", urlPatterns = "/hello/hello")
@Slf4j
public class MyDispatcherServlet extends HttpServlet {

    private enum AttriKey{
        BEAN_CONTAINER_MAP,
        HANDLE_MAPPING
    }

    @Override
    public void init() throws ServletException {
        InitBean initBean = new InitBean();
        initBean.initBeans();

        ServletContext servletContext = this.getServletContext();
        // 获取spring容器管理的所有的bean对象
        servletContext.setAttribute(AttriKey.BEAN_CONTAINER_MAP.name(), initBean.getBeanContainerMap());

        // 初始化处理器映射器
        servletContext.setAttribute(AttriKey.HANDLE_MAPPING.name(), HandleMapping.initHandleMapping(initBean.getBeanContainerMap()));

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ServletContext servletContext = this.getServletContext();
        Map<String, Method> handleMapping = (Map<String, Method>) servletContext.getAttribute(AttriKey.HANDLE_MAPPING.name());
        Map<String, Object> beanContainerMap = (Map<String, Object>)servletContext.getAttribute(AttriKey.BEAN_CONTAINER_MAP.name());
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // jsp页面交给默认servlet处理
        if (path.contains("WEB-INF")) {
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            servletContext.getNamedDispatcher("default").forward(request, response);
        }

        if (!handleMapping.entrySet().stream().anyMatch(entry -> entry.getKey().equals(path))) {
            response.setStatus(404);
        }
        handleMapping.entrySet()
                .stream()
                // 这一步是处理器映射器查找过程
                .filter(entry -> entry.getKey().equals(path))
                .forEach(entry -> {
                    Method method = entry.getValue();
                    Class<?> returnType = method.getReturnType();

                    // 返回ModelAndView
                    if ("MyModelAndView".equals(returnType.getSimpleName())) {
                        // 获取对象
                        Object object = beanContainerMap.get(method.getDeclaringClass().getName());
                        ViewResolver viewResolver = (ViewResolver) beanContainerMap.get("com.jiyx.test.springMVC.ViewResolver");
                        String prefix = viewResolver.getPrefix();
                        String suffix = viewResolver.getSuffix();

                        // 获取入参列表
                        List<Object> resultParameters = Binding.bindingMethodParamters(method, request);
                        MyModelAndView myModelAndView = null;
                        try {
                            // 下面的对应处理器适配器的执行过程
                            myModelAndView = (MyModelAndView) method.invoke(object, resultParameters.toArray());
                            // 页面渲染，也就是将返回的一些属性放入到request域里去
                            BindingRequestAndModel.bindingRequestAndModel(request, myModelAndView);
                            // 视图解析器
                            String returnView = myModelAndView.getView();
                            String responseVime = prefix + returnView + suffix;
                            request.getRequestDispatcher(responseVime).forward(request, response);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (ServletException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
