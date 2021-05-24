package io.ftwater.convertor.strategy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ConvertFactory
 */
public class ConvertStrategyFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConvertStrategyFactory.class);
    private Map<String, IFileConvertStrategy> strategyMap = new HashMap<>();
    private static ConvertStrategyFactory factory = null;

    public static ConvertStrategyFactory getInstance() {
        if (null == factory) {
            factory = new ConvertStrategyFactory();
        }
        return factory;
    }

    private ConvertStrategyFactory() {
    }

    public IFileConvertStrategy getConvertStrategy(File file){
        String fileName = file.getName();
        String ext = this.getExtOfFile(fileName);
        if (strategyMap.size() == 0) {
            this.initStrategyMap();
        }
        IFileConvertStrategy strategy = strategyMap.get(ext);
        if (null == strategy) {
            throw new UnsupportedOperationException("不支持的文件转换类型！-->" + fileName);
        }
        return strategy;
    }

    public IFileConvertStrategy getConvertStrategy(String ext){
        if (strategyMap.size() == 0) {
            this.initStrategyMap();
        }
        IFileConvertStrategy strategy = strategyMap.get(ext);
        if (null == strategy) {
            throw new UnsupportedOperationException("不支持的文件转换类型！-->" + ext);
        }
        return strategy;
    }

    private String getExtOfFile(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private void initStrategyMap() {
        Map<String, String> strategyClazzMap = ConvertStrategyEnum.getAllConvertStrategyClazz();
        Set<Map.Entry<String, String>> entries = strategyClazzMap.entrySet();
        try {
            for (Map.Entry<String, String> entry : entries) {
                IFileConvertStrategy strategy = (IFileConvertStrategy) Class.forName(entry.getValue()).newInstance();
                strategyMap.put(entry.getKey(), strategy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}