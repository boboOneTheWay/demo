package com.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    public static final void main(String[] args) {
        try {
        	KieServices ks = KieServices.Factory.get();
            // load up the knowledge base
        	String path = "src/main/resources/rules/Sample.drl";
        	KieFileSystem kfs = ks.newKieFileSystem();
            kfs.write(path, readString(path));
            KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
            KieContainer kieContainer = ks.newKieContainer(kieBuilder.getKieModule().getReleaseId());
        	KieSession kSession = kieContainer.newKieSession();

            // go !
            Message message = new Message();
            message.setMessage("Hello World");
            message.setStatus(Message.HELLO);
            kSession.insert(message);
            kSession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    private static String readString(String path)

    {

    String str="";

    File file=new File(path);

    try {

        FileInputStream in=new FileInputStream(file);

        // size  为字串的长度 ，这里一次性读完

        int size=in.available();

        byte[] buffer=new byte[size];

        in.read(buffer);

        in.close();

        str=new String(buffer,"utf-8");

    } catch (IOException e) {

        // TODO Auto-generated catch block

        return null;

    }

    return str;
    }

    public static class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

        private String message;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

}
