package io.ftwater.convertor;

import io.ftwater.convertor.utils.CompressUtils;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String PATH_KEY = "path";
    private static final String SRC_ENCODE_KEY = "srcEncode";
    private static final String TARGET_ENCODE_KEY = "targetEncode";

    public static void main(String[] args) {
        Map<String, String> param = handleParam(args);
        checkParam(param);
        String startPath = param.get(PATH_KEY);
        String srcEncode = param.get(SRC_ENCODE_KEY);
        String targetEncode = param.get(TARGET_ENCODE_KEY);
        App app = new App();
        // 备份
        logger.info("备份中....");
        app.backup(startPath);
        logger.info("备份完成....");
        logger.info("==============================================");
        // 转码
        logger.info("开始转码...");
        app.convert(startPath, srcEncode, targetEncode);
        logger.info("转码完成");
    }

    private static Map<String, String> handleParam(String[] args) {
        Options options = new Options();
        Option filePathOption = new Option("p", PATH_KEY, true, "文件或目录路径");
        filePathOption.setRequired(true);
        options.addOption(filePathOption);
        Option srcEncodeOption = new Option("se", SRC_ENCODE_KEY, true, "原编码(可选，默认UTF-8)");
        srcEncodeOption.setRequired(false);
        options.addOption(srcEncodeOption);
        Option targetEncodeOption = new Option("te", TARGET_ENCODE_KEY, true, "目标编码(可选，默认GBK)");
        targetEncodeOption.setRequired(false);
        options.addOption(targetEncodeOption);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("convert-name", options);
            System.exit(1);
        }
        Map<String, String> map = new HashMap<>();
        map.put("path", cmd.getOptionValue("path"));
        map.put("srcEncode", cmd.getOptionValue("srcEncode"));
        map.put("targetEncode", cmd.getOptionValue("targetEncode"));
        return map;
    }

    /**
     * 检查参数是否合法
     **/
    private static void checkParam(Map<String, String> param) {
        String path = param.get(PATH_KEY);
        String srcEncode = param.get(SRC_ENCODE_KEY);
        String targetEncode = param.get(TARGET_ENCODE_KEY);
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("请输入要转码的文件或目录路径");
        }
        if (StringUtils.isEmpty(srcEncode) && StringUtils.isEmpty(targetEncode)) {
            // 原编码和目标编码全空的时候，会使用默认值
            param.put(SRC_ENCODE_KEY, "UTF-8");
            param.put(TARGET_ENCODE_KEY, "GBK");
            return;
        } else if (StringUtils.isAnyEmpty(srcEncode, targetEncode)) {
            throw new IllegalArgumentException("原编码和目标编码必须成对出现");
        }
        logger.info("其实路径为：" + path);
        logger.info("原编码：" + srcEncode);
        logger.info("目标编码：" + targetEncode);
    }

    /**
     * 备份指定目录到其父目录
     *
     * @param startPath 起始路径
     */
    private void backup(String startPath) {
        try {
            boolean isDic = Files.isDirectory(Paths.get(startPath));
            if (isDic) {
                CompressUtils.compressDirectoryAtThere(startPath);
            }else{
                CompressUtils.compressFile(startPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("备份失败：" + startPath);
        }
    }

    /**
     * 开始进行转码
     *
     * @param startPath 起始路径
     */
    private void convert(String startPath, String srcEncode, String targetEncode) {
        Convertor.getInstance().skipNoneExtFile().addSkipExtName(new String[]{".bak", ".class"}).convert(startPath, srcEncode, targetEncode);
    }
}
