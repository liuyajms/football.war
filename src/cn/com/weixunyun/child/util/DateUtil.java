package cn.com.weixunyun.child.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DateUtil {

    public static final long DAY_TYPE = 1000 * 24 * 3600;

    public static void main(String[] args) {
//        Date date = DateUtil.stringToDate("2014-11-21", "yyyy-MM-dd");
//        System.out.println("###:" + date);

        java.sql.Date date1 = java.sql.Date.valueOf("2014-06-22");
        System.out.println(date1);

        int t = getDateMinus(date1, java.sql.Date.valueOf("2014-8-22"));
        System.out.println(t);
    }

    /**
     * 字符串转换时间函数
     *
     * @param dateStr 时间字符串
     * @param format  格式： "yyyy-MM-dd HH:mm:ss "
     * @return
     */
    public static Date stringToDate(String dateStr, String format) {
        Date date = null;
        if (format != null && !"".equals(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 字符串转换时间函数
     *
     * @param date   时间
     * @param format 格式： "yyyy-MM-dd HH:mm:ss "
     * @return
     */
    public static String dateFormat(Date date, String format) {
        String dateStr = null;
        if (format != null && !"".equals(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 获取count个随机数
     *
     * @param count 随机数个数
     * @return
     */
    public static String getRandomStr(int count) {
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num) + ""), "");
        }
        return sb.toString();
    }

    /**
     * 获取当前时间+随机数
     *
     * @return 字符串
     */
    public static String getPicRandom() {
        return new SimpleDateFormat("yyyymmddhhmm").format(new Date()) + getRandomStr(4);
    }

    public static int getDateMinus(Date beginDate, Date endDate, long dayMillis) {
        return getDateMinus(new java.sql.Date(beginDate.getTime()),
                new java.sql.Date(endDate.getTime()), dayMillis);
    }

    public static int getDateMinus(java.sql.Date beginDate, java.sql.Date endDate, long dayMillis) {
        long t1 = new java.sql.Date(beginDate.getTime()).getTime();
        long t2 = new java.sql.Date(endDate.getTime()).getTime();

        long millisecond = t1 > t2 ? t1 - t2 : t2 - t1;

        return (int) (millisecond / dayMillis);
    }

    /**
     * 获取两个java.sql.Date类型的时间间隔天数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDateMinus(java.sql.Date beginDate, java.sql.Date endDate) {
        return getDateMinus(beginDate, endDate, DAY_TYPE);
    }
}
