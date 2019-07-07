package com.coolron.muxin.util;/**
 * Created by Administrator on 2019/6/23.
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Auther: xf
 * @Date: 2019/6/23 22:04
 * @Description:
 *
 * 手动获取被spring 管理的bean对象
 *
 */
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(null == SpringUtil.applicationContext){
            SpringUtil.applicationContext = applicationContext;
        }
    }

    // 获取 applicationContext
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    // 通过 class 获取 Bean
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    // 通过 name 获取 Bean
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    // 通过 name 及 Clazz 获取 Bean
    public static  <T> T getBean(String name, Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

}
