package com.yonyou.convertor.strategy;

import java.io.File;
import java.util.Set;

/**
 * IConvert
 */
public interface IFileConvertStrategy {
    void convertFile(File file, String fromCharsetName, String toCharsetName, Set<String> skipExts) throws Exception;
}