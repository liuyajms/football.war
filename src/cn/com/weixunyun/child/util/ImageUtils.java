package cn.com.weixunyun.child.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

	private static final int IMAGE_W = 200;
	private static final int IMAGE_H = 200;

	public static void zoom(File srcFile, File dstFile) {
		zoom(srcFile, dstFile, IMAGE_W, IMAGE_H);
	}

	public static void zoom(File srcFile, File dstFile, int w, int h) {
		try {
            if(!dstFile.getParentFile().exists()){
                dstFile.getParentFile().mkdirs();
            }
			Thumbnails.of(srcFile).size(w, h).toFile(dstFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
