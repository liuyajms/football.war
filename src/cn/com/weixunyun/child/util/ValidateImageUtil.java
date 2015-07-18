package cn.com.weixunyun.child.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ValidateImageUtil {

//    public static final char[] CHARS = {'0','1', '2', '3', '4', '5', '6', '7', '8',
//            '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
//            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    public static final char[] CHARS = {'0','1', '2', '3', '4', '5', '6', '7', '8',
        '9' };

    public static int width=60, height=30;//图片宽高
    public static int x=10, y=20;//验证码坐标位置

    public static Random random = new Random();

    public static String getRandomString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    public static Color getRandomColor() {
        return new Color(random.nextInt(255), random.nextInt(255), random
                .nextInt(255));
    }

    public static Color getReverseColor(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c
                .getBlue());
    }

    public static BufferedImage getBufferedImage(String randomString, int width, int height, int x, int y){
        Color color = getRandomColor();
        Color reverse = getReverseColor(color);
//        String randomString = getRandomString();
        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(reverse);
        g.drawString(randomString, x, y);
        for (int i = 0, n = random.nextInt(10); i < n; i++) {
            g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
        }
        return bi;
    }

    public static BufferedImage getBufferedImage(String randomString ){
        return getBufferedImage(randomString,width,height,x,y);
    }
}
