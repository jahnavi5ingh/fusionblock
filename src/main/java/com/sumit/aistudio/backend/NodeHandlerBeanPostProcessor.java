package com.sumit.aistudio.backend;

import com.sumit.aistudio.backend.plan.IsNodeHandler;
import com.sumit.aistudio.backend.plan.handlers.Handler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NodeHandlerBeanPostProcessor implements BeanPostProcessor {

    private List<Handler> nodeHandlerBeans = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(IsNodeHandler.class)) {
            nodeHandlerBeans.add((Handler)bean);
        }
        return bean;
    }

    public List<Handler> getNodeHandlerBeans() {
        return nodeHandlerBeans;
    }
}