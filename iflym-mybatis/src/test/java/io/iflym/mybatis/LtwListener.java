package io.iflym.mybatis;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.stereotype.Component;

/**
 * 用于支持aop的ltw功能
 * Created by flym on 2017/8/28.
 *
 * @author flym
 */
public class LtwListener implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        registerAop(context);
    }


    public static void registerAop(ConfigurableApplicationContext context) {
        if(!(context instanceof BeanDefinitionRegistry)) {
            return;
        }

        DefaultContextLoadTimeWeaver timeWeaver = new DefaultContextLoadTimeWeaver(context.getClassLoader());
        AspectJWeavingEnabler.enableAspectJWeaving(timeWeaver, null);
    }

}
