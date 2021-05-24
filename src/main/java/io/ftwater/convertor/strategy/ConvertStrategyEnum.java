package io.ftwater.convertor.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum ConvertStrategyEnum {

    /**
     * 文本类型
     */
    TXT(".txt", "DefaultConvertStrategy", true),
    PROPERTIES(".properties", "DefaultConvertStrategy", true),
    XML(".xml", "DefaultConvertStrategy", true),
    UPM(".upm", "DefaultConvertStrategy", true),
    JAVA(".java", "DefaultConvertStrategy", true),
    /**
     * jar包
     */
    JAR(".jar", "JarConvertStrategy", false);

    public String getStrategyCode() {
        return strategyCode;
    }

    private final String strategyCode;
    private final String clazz;
    private final boolean isTextFile;

    private static final Logger logger = LoggerFactory.getLogger(ConvertStrategyEnum.class);
    ConvertStrategyEnum(String strategyCode, String clazz, boolean isTextFile) {
        this.strategyCode = strategyCode;
        this.clazz = clazz;
        this.isTextFile = isTextFile;
    }

    public String getClazz() {
        return clazz;
    }
    public boolean getIsTextFile(){
        return isTextFile;
    }

    public static Map<String, String> getAllConvertStrategyClazz() {
        Map<String, String> map = new HashMap<>();
        for (ConvertStrategyEnum strategy : values()) {
            map.put(strategy.getStrategyCode().trim(), "io.ftwater.convertor.strategy." + strategy.getClazz());
        }
        return map;
    }

    public static boolean containsCode(String strategyCode) {
        boolean contain = false;
        for (ConvertStrategyEnum strategy : values()) {
            if (strategy.getStrategyCode().equals(strategyCode)) {
                contain = true;
                break;
            }
        }
        return contain;
    }
    public static Set<String> getTextFilesExts(){
        Set<String> exts = new HashSet<>();
        for (ConvertStrategyEnum strategy : values()) {
            if (strategy.getIsTextFile()) {
                exts.add(strategy.getStrategyCode());
            }
        }
        return exts;
    }
}
