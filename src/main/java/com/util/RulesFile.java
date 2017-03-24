package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RulesFile {

	public static String readString(String path)

    {
    String str="";
    File file=new File(path);

    try {
        FileInputStream in=new FileInputStream(file);
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

}
