package com.yonyou.convertor;

import com.yonyou.convertor.utils.CompressUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	public static void main(String[] args) {
		// 检查路径
		checkPath(args);
		String startPath = args[0];
		logger.info("起始路径为："+startPath);
		App app = new App();
		// 备份
		logger.info("备份中....");
		app.backup(startPath);
		logger.info("备份完成....");
		logger.info("==============================================");
		// 转码
		logger.info("开始转码...");
		app.convert(startPath);
		logger.info("转码完成");
	}
	/**
	 * 备份指定目录到其父目录
	 * @param startPath
	 */
	private void backup(String startPath) {
		try {
			CompressUtils.compressDirectoryAtThere(startPath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("备份失败："+startPath);
		}
	}
	/**
	 * 开始进行转码
	 * @param startPath
	 */
	private void convert(String startPath) {
		Convertor.getInstance().skipNoneExtFile().addSkipExtName(new String[]{".bak",".class"}).convert(startPath,"UTF-8","GBK");
	}

	private static void checkPath(String[] startPath) {
		boolean pass = ArrayUtils.isEmpty(startPath);
		if(pass) {
			throw new IllegalArgumentException("请输入要转码的路径！");
		}
		if(startPath.length>1) {
			throw new IllegalArgumentException("目前仅支持单路径处理！");
		}
		File file = new File(startPath[0]);
		if(!file.exists()) {
			throw new IllegalArgumentException("路径不存在！");
		}
	}
}
