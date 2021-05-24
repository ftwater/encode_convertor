package io.ftwater.convertor.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Set;

/**
 * DefaultConvertor
 * 默认转换策略就是文本转换
 */
public class DefaultConvertStrategy extends AbstractConvertStrategy {
    private static final Logger logger = LoggerFactory.getLogger(DefaultConvertStrategy.class);
    /**
     * 对单文本文件进行转码
     * 
     * @param file            目标文本文件
     * @param fromCharsetName 原编码
     * @param toCharsetName   目标编码
     * @param skipExts 需要跳过的扩展名
     */
    @Override
    public void doConvert(File file, String fromCharsetName, String toCharsetName, Set<String> skipExts) {
        logger.debug("正在转码："+file.getName());
        try {
            // 1.用原编码读取文件
            String content = readFileFromCharset(file, fromCharsetName);
            // 2.用目标编码写入文件
            saveFileToCharset(file, toCharsetName, content);
        }catch (IOException ex){
            logger.error("文件转换错误："+file.getAbsolutePath());
        }
    }


    /**
     * 用原编码读取目标转码文件
     * 
     * @param file            目标转码文件
     * @param fromCharsetName 原编码
     * @return 读取的文本内容
     * @throws IOException IO异常
     */
    private String readFileFromCharset(File file, String fromCharsetName) throws IOException {
        if (!Charset.isSupported(fromCharsetName)) {
            throw new UnsupportedCharsetException(fromCharsetName);
        }
        String str;
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), fromCharsetName);
        char[] chs = new char[(int) file.length()];
        reader.read(chs);
        str = new String(chs).trim();
        reader.close();
        return str;
    }

    public String readFileFromCharsetByStream(InputStream is,String fromCharsetName) throws IOException {
        // 读取文件内容为string
        StringBuilder str = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is,fromCharsetName))) {
            String line = reader.readLine();
            str.append(line);
        }
        return str.toString();
    }
    /**
     * 以目标编码保存到文件
     * 
     * @param file          目标文件
     * @param toCharsetName 目标编码
     * @param content       文本内容
     * @throws IOException 异常
     */
    private void saveFileToCharset(File file, String toCharsetName, String content) throws IOException {
        if (!Charset.isSupported(toCharsetName)) {
            throw new UnsupportedCharsetException(toCharsetName);
        }
        OutputStreamWriter outWrite = new OutputStreamWriter(new FileOutputStream(file), toCharsetName);
        outWrite.write(content);
        outWrite.close();
    }


}