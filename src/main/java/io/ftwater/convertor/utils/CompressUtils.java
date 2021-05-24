package io.ftwater.convertor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressUtils {
    private static final Logger logger = LoggerFactory.getLogger(CompressUtils.class);

    public String extracted(String path) {
        File file = new File(path);
        String name = file.getName();
        Path destZipFile = Paths.get(file.getParent(), name + ".bak");
        return destZipFile.toString();
    }

    public static void compressDirectoryAtThere(String srcPath) throws Exception {
        File file = new File(srcPath);
        String name = file.getName();
        String zipFilePath = Paths.get(file.getParent(), name + ".bak").toString();
        compressDirectory(srcPath, zipFilePath);
    }

    public static void compressDirectory(String srcPath, String zipFilePath) throws Exception {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            // 1. 根路径开始遍历
            File rootDir = new File(srcPath);
            String[] list = rootDir.list();
            if (null==list||list.length == 0) {
                logger.info("当前目标路径为空");
                return;
            }
            compressDirectory(zipOutputStream, rootDir, "");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static void compressDirectory(ZipOutputStream zipOut, File file, String parentFileName) throws Exception {
        File[] files = file.listFiles();
        String currentParentFileName ;
        if (null==files||files.length <= 0) {
            addEmptyDictionary(zipOut, file, parentFileName);
            return;
        }
        // 遍历当前目录下所有的文件
        for (File currentFile : files) {
            // 父目录路径
            if (currentFile.isDirectory()) {
                currentParentFileName = StringUtils.isEmpty(parentFileName) ? currentFile.getName()
                        : parentFileName + File.separator + currentFile.getName();
                compressDirectory(zipOut, currentFile, currentParentFileName);
            } else {
                currentParentFileName = StringUtils.isEmpty(parentFileName) ? currentFile.getName()
                        : parentFileName;
                compressFile(zipOut, currentFile, currentParentFileName);
            }
        }
    }

    public static void compressFile(String srcPath) {
        File file = new File(srcPath);
        String name = file.getName();
        String zipFilePath = Paths.get(file.getParent(), name + ".bak").toString();
        compressFile(srcPath, zipFilePath);
    }

    public static void compressFile(String srcPath, String zipFilePath) {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            File file = new File(srcPath);
            compressFile(zipOut, file, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 压缩单个文件
     *
     * @param zipOut ZipOutputStream
     * @param file 文件
     * @param parentFileName 父路径
     */
    private static void compressFile(ZipOutputStream zipOut, File file, String parentFileName) {
        try (FileInputStream input = new FileInputStream(file)) {
            ZipEntry zipEntry;
            if (StringUtils.isEmpty(parentFileName)) {
                zipEntry = new ZipEntry(file.getName());
            } else {
                zipEntry = new ZipEntry(parentFileName + File.separator + file.getName());
            }
            logger.debug("正在压缩:" + file.getName());
            zipOut.putNextEntry(zipEntry);
            IOUtils.copy(input, zipOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addEmptyDictionary(ZipOutputStream zipOut, File file, String parentFileName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(parentFileName + File.separator + file.getName() + File.separator);
        logger.debug("压缩了一个空目录:" + file.getName() + File.separator);
        zipOut.putNextEntry(zipEntry);
    }

}
