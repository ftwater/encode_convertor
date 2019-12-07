package com.yonyou.convertor.utils;

import com.yonyou.convertor.strategy.ConvertStrategyEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    public static String getExtNameOfFile(String filename){
        int lastIndexOfDot = filename.lastIndexOf(".");
        if (lastIndexOfDot==-1){
            return null;
        }
        String ext = filename.substring(lastIndexOfDot);
        return ext;
    }
    /**
     * 根据支持的扩展名范围确定当前文件是否支持转码
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static boolean isFileSupportive(String fileName) throws Exception {
        String ext = CommonUtil.getExtNameOfFile(fileName);
        // 扩展名
        if (StringUtils.isEmpty(ext)) {
            // 没有扩展名
            // TODO 是否转换的开关,默认不转码
            return false;
        }
        boolean contains = ConvertStrategyEnum.containsCode(ext);
        if (!contains){
            logger.trace("没有找到相应的转码策略："+fileName);
        }
        return contains;
    }
    /**
     * 根据支持的扩展名范围确定当前文件是否支持转码
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static boolean isTextFile(String fileName) throws Exception {
        Set<String> textFilesExts = ConvertStrategyEnum.getTextFilesExts();
        return textFilesExts.contains(CommonUtil.getExtNameOfFile(fileName));
    }
}
