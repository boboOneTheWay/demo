package com.util;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
public class DroolsUtil {

	public static KieSession getKieSession(String path) {
		//KieServices就是一个中心，通过它来获取的各种对象来完成规则构建、管理和执行等操作
		KieServices ks = KieServices.Factory.get();
    	KieFileSystem kfs = ks.newKieFileSystem();
		kfs.write(path, RulesFile.readString(path));
        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
        KieContainer kieContainer = ks.newKieContainer(kieBuilder.getKieModule().getReleaseId());
        //KieSession就是应用程序跟规则引擎进行交互的会话通道。
    	return kieContainer.newKieSession();
	}
}
