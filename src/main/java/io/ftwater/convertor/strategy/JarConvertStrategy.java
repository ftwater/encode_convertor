package io.ftwater.convertor.strategy;

import io.ftwater.convertor.utils.CommonUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JarConvertStrategy extends AbstractConvertStrategy {
    private static final Logger logger = LoggerFactory.getLogger(JarConvertStrategy.class);

    @Override
    public void doConvert(File file, String fromCharsetName, String toCharsetName, Set<String> skipExts) throws Exception {
        logger.debug("正在转码："+file.getAbsolutePath());
        this.convertJar(file, fromCharsetName, toCharsetName);
    }

    /**
     * 对jar包内容进行转码
     *
     * @param file            jar包文件
     * @param fromCharsetName 原编码
     * @param toCharsetName   目标编码
     * @throws Exception 异常
     */
    private void convertJar(File file, String fromCharsetName, String toCharsetName) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             JarFile jarFile = new JarFile(file.toString()); // 原jar包
             JarOutputStream jos = new JarOutputStream(bos)// 新jar包的输出流
        ) {
            processJarEntry(jarFile, jos, fromCharsetName, toCharsetName);
            jos.finish();
            bos.writeTo(new FileOutputStream(file.getPath()));
        }
    }

    /**
     * 处理jar包中文件
     *
     * @param jarFile JarFile
     * @param jos  JarOutputStream
     * @param fromCharsetName 源编码
     * @param toCharsetName 目标编码
     * @throws Exception 异常
     */
    private void processJarEntry(JarFile jarFile, JarOutputStream jos, String fromCharsetName, String toCharsetName)
            throws Exception {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            // 读取原jar包中文件到输入流
            InputStream is = jarFile.getInputStream(entry);
            // 按照原文件名向新jar包输入流添加一个entiry
            jos.putNextEntry(new JarEntry(entry.getName()));
            // 对支持的文件类型进行转码：文本文件，如java，xml，properties等
            String fileName = entry.getName();
            if (CommonUtil.isTextFile(fileName)) {
                convertEntry(jos, fromCharsetName, toCharsetName, entry, is);
            } else {
                IOUtils.copy(is, jos);
            }
            jos.closeEntry();
        }
    }

    /**
     * 对jar包中文件进行转码
     *
     * @param jos             jar输出流
     * @param fromCharsetName 原编码
     * @param toCharsetName   目标编码
     * @param entry           jar包中的entry
     * @param is              entry的inputStream
     * @throws Exception 异常
     */
    private void convertEntry(JarOutputStream jos, String fromCharsetName, String toCharsetName, JarEntry entry,
                              InputStream is) throws Exception {
        // 读取文件内容为string
        String str;
        try (InputStreamReader reader = new InputStreamReader(is, fromCharsetName)) {
            char[] chs = new char[(int) entry.getCompressedSize()];
            reader.read(chs);
            str = new String(chs).trim();
        }
        logger.trace("正在转码jarEntry：" + entry.getName());
        // 转码
        String newStr = new String(str.getBytes(fromCharsetName));
        // 写入新jar包输出流
        jos.write(newStr.getBytes(toCharsetName));
    }
}
