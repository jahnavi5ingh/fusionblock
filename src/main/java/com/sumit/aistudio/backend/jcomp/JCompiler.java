package com.sumit.aistudio.backend.jcomp;


import com.sumit.aistudio.backend.ptl.VelocityHelper;
import javassist.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
public class JCompiler {
        public static void main(String[] args) throws Exception {

            JCompiler jCompiler = new JCompiler();
            Handler test = jCompiler.compileHandler("System.out.println(\"message\"); output.put(\"message\",\"Hello World\");");
            test.handle(new HashMap<>());
        }
        public Invoker compileInvoker(String codeInput) throws Exception {
            ClassPool pool = ClassPool.getDefault();
            String name = "Invoke_"+ RandomStringUtils.randomAlphabetic(6);
            VelocityHelper velocityHelper = new VelocityHelper();
            Map<String,Object> ctx = new HashMap<>();
            ctx.put("name",name);
            ctx.put("code",codeInput);
            String code = velocityHelper.generateContent("templates/InvokeMethod.vm",ctx);
            CtClass cc = pool.makeClass(name);
            cc.addInterface(pool.get("com.sumit.aistudio.backend.jcomp.Invoker"));
            cc.setSuperclass(pool.get("com.sumit.aistudio.backend.jcomp.Utility"));

            CtMethod newMethod = CtNewMethod.make(code, cc);
            cc.addMethod(newMethod);

              Class<?> modifiedClass = cc.toClass();
            Object instance = modifiedClass.getDeclaredConstructor().newInstance();
            return (Invoker) instance;
        }
    public Handler compileHandler(String codeInput) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        String name = "Invoke_"+ RandomStringUtils.randomAlphabetic(6);
        VelocityHelper velocityHelper = new VelocityHelper();
        Map<String,Object> ctx = new HashMap<>();
        ctx.put("name",name);
        ctx.put("code",codeInput);
        String code = velocityHelper.generateContent("templates/HandlerMethod.vm",ctx);
        System.out.println(code);

        CtClass cc = pool.makeClass(name);
        cc.addInterface(pool.get("com.sumit.aistudio.backend.jcomp.Handler"));
        cc.setSuperclass(pool.get("com.sumit.aistudio.backend.jcomp.Utility"));
        CtMethod newMethod = CtNewMethod.make(code, cc);
        cc.addMethod(newMethod);
        Class<?> modifiedClass = cc.toClass();
        Object instance = modifiedClass.getDeclaredConstructor().newInstance();
        return (Handler) instance;
    }
}