package com.yonyou.convertor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        try {
            compressDirectory(srcPath, zipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compressDirectory(String srcPath, String zipFilePath) throws Exception {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath.toString()))) {
            // 1. 根路径开始遍历
            File rootDir = new File(srcPath);
            String[] list = rootDir.list();
            if (list.length == 0) {
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
        String currentParentFileName = null;
        // 遍历当前目录下所有的文件
        for (File currentFile : files) {
            currentParentFileName = StringUtils.isEmpty(parentFileName) ? currentFile.getName()
                    : parentFileName + File.separator + currentFile.getName();
            // 父目录路径
            if (currentFile.isDirectory()) {
                compressDirectory(zipOut, currentFile, currentParentFileName);
            } else {
                compressFile(zipOut, currentFile, currentParentFileName);
            }
        }
    }

    public static void compressFile(String srcPath, String zipFilePath)  {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath.toString()))) {
            File file = new File(srcPath);
            compressFile(zipOut, file, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 压缩单个文件
     *
     * @param zipOut
     * @param file
     * @param parentFileName
     */
    private static void compressFile(ZipOutputStream zipOut, File file, String parentFileName) {
        try (FileInputStream input = new FileInputStream(file)) {
            ZipEntry zipEntry = null;
            if (StringUtils.isEmpty(parentFileName)) {
                zipEntry = new ZipEntry(file.getName());
            } else {
                zipEntry = new ZipEntry(parentFileName + file.getName());
            }
            logger.debug("正在压缩:"+file.getName());
            zipOut.putNextEntry(zipEntry);
            IOUtils.copy(input, zipOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
