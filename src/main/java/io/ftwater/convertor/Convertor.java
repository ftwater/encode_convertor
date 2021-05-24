package io.ftwater.convertor;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.ftwater.convertor.strategy.IFileConvertStrategy;

import io.ftwater.convertor.strategy.ConvertStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Convertor {
    private static final Logger logger = LoggerFactory.getLogger(Convertor.class);

    private Set<String> skipExts = new HashSet<>();
    private static Convertor convertor = null;

    private Convertor() {
    }

    public static Convertor getInstance() {
        if (null == convertor) {
            convertor = new Convertor();
        }
        return convertor;
    }

    public void convert(String srcPath, String fromCharsetName, String toCharsetName) {
        File file = new File(srcPath);
        this.convert(file, fromCharsetName, toCharsetName);
    }

    public Convertor addSkipExtName(String extName) {
        this.skipExts.add(extName);
        return this;
    }

    public Convertor addSkipExtName(Collection<String> skipExts) {
        this.skipExts.addAll(skipExts);
        return this;
    }

    public Convertor addSkipExtName(String[] skipExts) {
        this.skipExts.addAll(Arrays.asList(skipExts));
        return this;
    }

    public Convertor skipNoneExtFile() {
        this.skipExts.add(null);
        return this;
    }

    public void convert(File file, String fromCharsetName, String toCharsetName) {
        try {
            if (file.isDirectory()) {
                // 处理目录
                this.convertDirectory(file, fromCharsetName, toCharsetName);
            } else {
                // 处理文件
                this.convertFile(file, fromCharsetName, toCharsetName,this.skipExts);
            }
        } catch (Exception e) {
            logger.info("文件"+file.getAbsolutePath()+"没有转码,case:"+e.getMessage());
        }
    }
    /**
     * 对单文本文件进行转码
     *
     * @param file            目标文本文件
     * @param fromCharsetName 原编码
     * @param toCharsetName   目标编码
     */

    protected void convertFile(File file, String fromCharsetName, String toCharsetName,Set<String> skipExts) throws Exception{
        IFileConvertStrategy strategy = ConvertStrategyFactory.getInstance().getConvertStrategy(file);
        strategy.convertFile(file, fromCharsetName, toCharsetName,skipExts);
    }
    /**
     * 按照目录进行转码
     *
     * @param file            目录
     * @param fromCharsetName 原编码
     * @param toCharsetName   新编码
     */
    private void convertDirectory(File file, String fromCharsetName, String toCharsetName) {
        File[] files = file.listFiles();
        // 遍历处理
        assert files != null;
        for (File childFile : files) {
            // 对目录下所有file进行转码
            convert(childFile, fromCharsetName, toCharsetName);
        }
    }
}