package com.jiyx.test.spring;

import com.jiyx.test.common.constant.FileNameConstant;
import com.jiyx.test.common.util.AnnotationUtils;
import com.jiyx.test.mybatis.MySqlSession;
import com.jiyx.test.spring.annotation.MyAutowired;
import com.jiyx.test.test.entity.Person;
import com.jiyx.test.test.repository.PersonMapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * auther: jiyx
 * date: 2018/11/25.
 */
@Slf4j
public class InitBean extends BeanDefinition {

    /**
     * 所有的实例存放的容器，key是接口名
     */
    private static Map<String, Object> beanContainerMap = new ConcurrentHashMap<>();

    /**
     * 初始话容器
     * 注意：扫描的bean会覆盖xml中配置的bean，spring也是这样，扫描的注入注入和装配都是在xml之后的
     * MyAutowired暂时是根据名称装配和扫描
     */
    public void initBeans() {
        initXmlBeans(FileNameConstant.contextConfigLocation);
        initXmlBeans(FileNameConstant.springMVCConfigLocation);
        initAutowiredBeans(FileNameConstant.contextConfigLocation);
    }

    /**
     * 初始化xml配置文件中的bean
     *
     * @param contextConfigLocation
     */
    private void initXmlBeans(String contextConfigLocation) {

        ApplicationContext context = new ApplicationContext(contextConfigLocation);

        Map<String, GenericBeanDefinition> beanDefinitionMap = super.getBeanDefinitionMap(contextConfigLocation);
        for (Map.Entry<String, GenericBeanDefinition> definitionEntry : beanDefinitionMap.entrySet()) {
            String beanId = definitionEntry.getKey();
            String beanClass = definitionEntry.getValue().getClassName();
            Class clazz = null;
            try {
                clazz = Class.forName(beanClass);
            } catch (ClassNotFoundException e) {
                log.error("xml中的{}实例化异常", beanClass);
            }

            beanContainerMap.put(beanClass, clazz.cast(context.getBean(beanId)));
        }

    }

    /**
     * 初始化扫描表下的bean
     *
     * @param mybatisConfigLocation
     */
    private void initAutowiredBeans(String mybatisConfigLocation) {
        List<String> componentScanList = getComponentScanList(mybatisConfigLocation);

        try {
            for (String className : componentScanList) {
                Class clazz = Class.forName(className);
                // 如果是接口的话，要生成代理类
                if (clazz.isInterface()) {
                    beanContainerMap.put(className, createProxy(clazz));
                    continue;
                }

                Class[] interfaces = clazz.getInterfaces();

                if (interfaces == null || interfaces.length == 0) {
                    noInterfaceInit(className, className);
                    continue;
                }

                // 在service和control中的属性引用都是使用的接口声明，
                // 而不是具体的类，多态引用，方便切换实现类
                // 这里就不区分到底实现的接口是否在扫描的包中
                for (Class anInterface : interfaces) {
                    // 容器中如果有对象的话，还需要覆盖掉已有的老对象，一个接口类只能有一个实体对象
                    if (existInBeanContainer(anInterface)) {
                        beanContainerMap.put(anInterface.getName(), clazz.newInstance());
                    } else {
                        noInterfaceInit(className, anInterface.getName());
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            log.error("没有找到类");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    private void noInterfaceInit(String className, String interfaceName) {
        try {
            Class<?> clazz = Class.forName(className);
            log.info("init bean {}", className);
            Object instance = clazz.newInstance();

            // 有Autowired的属性注入
            // 因为这里初始换的顺序已经在getComponentScanList确定了，所以后面的肯定能获取前面的实例
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                Arrays.stream(fields)
                        .filter(InitBean::hasAnnotationMyAutowired)
                        .forEach(field -> {
                            field.setAccessible(true);
                            fieldSetValue(instance, field);

                        });
            }

            beanContainerMap.put(interfaceName, instance);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给字段赋值
     *
     * @param instance
     * @param field
     */
    private void fieldSetValue(Object instance, Field field) {
        for (Map.Entry<String, Object> entry : beanContainerMap.entrySet()) {
            if (entry.getKey().equals(field.getType().getName())) {
                try {
                    field.set(instance, entry.getValue());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断字段是否有MyAutowired注解
     *
     * @param field
     * @return
     */
    private static boolean hasAnnotationMyAutowired(Field field) {
        return !AnnotationUtils.isEmpty(field.getAnnotation(MyAutowired.class));
    }

    /**
     * 判断class对象的实例是否已经存在于容器中
     *
     * @param anInterface
     * @return
     */
    private boolean existInBeanContainer(Class anInterface) {
        for (Map.Entry<String, Object> entry : beanContainerMap.entrySet()) {
            if (entry.getKey().equals(anInterface.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建代理对象，这个可能是mybatis，也可能是Jpa，或者其他的，
     * 但是一般的service和control都不是接口，而是有具体的实现类的。
     *
     * @param clazz
     * @return
     */
    private Object createProxy(Class clazz) {
        // TODO 应该提供一个借口，然后根据不同的接入方，有不同的实现
        /*return new PersonMapper() {
            @Override
            public Person findById(long id) {
                return new Person(id, "xiaohei", 25, "大牛坊一期");
            }

            @Override
            public List<Person> findByName(String name) {
                return Arrays.asList(new Person(88, name, 25, "大牛坊一期"));
            }

            @Override
            public void save(Person person) {
                System.out.println("insert into person success");
            }
        };*/
//        return null;
        MySqlSession sqlSession = new MySqlSession();
        return sqlSession.getMapper(clazz, FileNameConstant.mybatisConfigLocation);
    }

    public static Map<String, Object> getBeanContainerMap() {
        return beanContainerMap;
    }

}
