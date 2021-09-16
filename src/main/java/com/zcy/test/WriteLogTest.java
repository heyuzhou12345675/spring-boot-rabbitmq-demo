package com.zcy.test;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhouchunyang
 * @Date: Created in 14:33 2021/9/14
 * @Description:
 */
public class WriteLogTest {

    public static void main(String[] args) {
        try {
            getLogFile("123");
            int i =1/0;
        }catch (Exception e){
            System.out.println("调用ESB基础数据发布接口失败!");
            getLogFile("987");
            getLogFile("654");
            getLogFile("321");
        }

    }




    public static void getLogFile(String userId) {
        String basePath = getResourceBasePath();
        String syncErrorLogPath = new File(basePath, "sync/sync-error.log").getAbsolutePath();
        // 保证目录一定存在
        ensureDirectory(syncErrorLogPath);
        System.out.println("syncErrorLogPath = " + syncErrorLogPath);
        BufferedWriter writer = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(syncErrorLogPath,true)));
            StringBuffer buffer = new StringBuffer();
            buffer.append(simpleDateFormat.format(new Date())+"_"+userId);
            buffer.append("\r\n");
            writer.write(buffer.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取项目根路径
     *
     * @return
     */
    private static String getResourceBasePath() {
        // 获取跟目录
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            // nothing to do
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }
        String pathStr = path.getAbsolutePath();
        // 如果是在eclipse中运行，则和target同级目录,如果是jar部署到服务器，则默认和jar包同级
        pathStr = pathStr.replace("\\target\\classes", "");
        return pathStr;
    }

    /**
     * 保证拷贝的文件的目录一定要存在
     *
     * @param filePath 文件路径
     */
    public static void ensureDirectory(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return;
        }
        filePath = replaceSeparator(filePath);
        if (filePath.indexOf("/") != -1) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/"));
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

    }
    /**
     * 将符号“\\”和“\”替换成“/”,有时候便于统一的处理路径的分隔符,避免同一个路径出现两个或三种不同的分隔符
     *
     * @param str
     * @return
     */
    public static String replaceSeparator(String str) {
        return str.replace("\\", "/").replace("\\\\", "/");
    }


}
