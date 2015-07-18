package cn.com.weixunyun.child.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class ImageUtils {

	public static void zoom(File srcFile, File dstFile, int w, int h) {
		// Thumbnails压缩出来的大小有几百K，如果压缩到几十K，质量有很糟糕
		// Thumbnails.of(srcFile).outputQuality(quality).size(w,
		// h).outputFormat(format)
		// .toOutputStream(new FileOutputStream(dstFile));
		try {
			if (dstFile.getParentFile() != null && !dstFile.getParentFile().exists()) {
				dstFile.getParentFile().mkdirs();
			}

			BufferedImage srcImage = ImageIO.read(srcFile);

			double sx = (double) w / srcImage.getWidth();
			double sy = (double) h / srcImage.getHeight();

			double s = Math.min(sx, sy);
			if (s > 1) {
				FileUtils.copyFile(srcFile, dstFile);
			} else {
				BufferedImage dstImage = null;

				int type = srcImage.getType();
				if (type == BufferedImage.TYPE_CUSTOM) {
					ColorModel cm = srcImage.getColorModel();
					WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
					boolean alphaPremultiplied = cm.isAlphaPremultiplied();
					dstImage = new BufferedImage(cm, raster, alphaPremultiplied, null);
				} else {
					dstImage = new BufferedImage((int) (s * srcImage.getWidth()), (int) (s * srcImage.getHeight()),
							type);
				}

				Graphics2D g = dstImage.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g.drawRenderedImage(srcImage, AffineTransform.getScaleInstance(s, s));
				g.dispose();

				ImageIO.write(dstImage, "jpg", dstFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		long st = System.currentTimeMillis();
		zoom(new File("IMG_20141009_143435.jpg"), new File("a.png"), 800, 800);
		System.out.println(System.currentTimeMillis() - st);
		st = System.currentTimeMillis();
		zoom(new File("a.png"), new File("b.png"), 800, 800);
		System.out.println(System.currentTimeMillis() - st);

	}
}
