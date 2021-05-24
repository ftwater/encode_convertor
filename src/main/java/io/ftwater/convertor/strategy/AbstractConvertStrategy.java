package io.ftwater.convertor.strategy;

import io.ftwater.convertor.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;

public class AbstractConvertStrategy implements IFileConvertStrategy {
    private static final Logger logger = LoggerFactory.getLogger(AbstractConvertStrategy.class);
    @Override
    public void convertFile(File file, String fromCharsetName, String toCharsetName, Set<String> skipExts) throws Exception {
        if (needSkip(skipExts,file.getName())){
            logger.debug("跳过文件："+file.getAbsolutePath());
            return;
        }
        doConvert(file,fromCharsetName,toCharsetName,skipExts);

    }
    protected void doConvert(File file, String fromCharsetName, String toCharsetName, Set<String> skipExts) throws Exception {
        throw new RuntimeException("当前文件的转换策略没有实现！！");
    }

    private boolean needSkip(Set<String> skipExts,String name){
        if (skipExts.size() == 0) {
            return false;
        }
        String extNameOfFile = CommonUtil.getExtNameOfFile(name);
        return skipExts.contains(extNameOfFile);
    }
}
