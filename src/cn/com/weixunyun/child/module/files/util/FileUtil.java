package cn.com.weixunyun.child.module.files.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {

    /**
     * 删除图片文件
     */
    public static boolean deleteImage(String path) {
        String img;

        img = path.contains("@l.") ? path.replace("@l.", ".") : path.replace(".", "@l.");

        new File(path).delete();

        return new File(img).delete();
    }

    /**
     * 根据ID删除图片文件
     *
     * @param path
     * @param id
     * @return
     */
    public static boolean deleteImage(String path, Long id) {
        File f1 = new File(path + id + ".png");
        File f2 = new File(path + id + "@l.png");
        if (f1.exists()) {
            f1.delete();
        }
        if (f2.exists()) {
            f2.delete();
        }
        return true;
    }

    /**
     * 删除所有与id相关文件
     *
     * @param path
     * @return
     */
    public static boolean deleteAll(String path, Long id) {

        File files = new File(path);

        if (files != null) {
            for (String s : files.list()) {
                if (s.startsWith(id + "_") || s.equals(id + ".png") || s.equals(id + "@l.png")) {
                    new File(path + "/" + s).delete();
                }
            }
        }

        return true;
    }

    /**
     * 删除文件、文件夹
     */
    public static boolean deleteAll(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] ff = file.listFiles();
            for (int i = 0; i < ff.length; i++) {
                deleteAll(ff[i].getPath());
            }
        }
        return file.delete();
    }

    /**
     * 新建目录
     *
     * @param folderPath 目录
     * @return 返回目录创建成功消息
     */
    public static Map<String, String> mkDir(String folderPath) {
        Map<String, String> ret = new HashMap<String, String>();
        try {
            File myFilePath = new File(folderPath);
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
                ret.put("code", "0");
            } else {
                ret.put("code", "-1");
                ret.put("message", "该文件或目录已存在");
            }
        } catch (Exception e) {
            ret.put("code", "-1");
            ret.put("message", "创建目录操作出错");
        }
        return ret;
    }

    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldName 原来的文件名
     * @param newName 新文件名
     */
    public static Map<String, String> reName(String path, String oldName, String newName) {
        Map<String, String> ret = new HashMap<String, String>();
        if (!oldName.equals(newName)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            File oldFile = new File(path + File.separator + oldName);
            File newFile = new File(path + File.separator + newName);
            if (!oldFile.exists()) {
                ret.put("code", "-1");
                ret.put("message", "重命名文件不存在");
            }
            if (newFile.exists()) {// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                ret.put("code", "-1");
                ret.put("message", newName + "已经存在！");
            } else {
                oldFile.renameTo(newFile);
                ret.put("code", "0");
            }
        } else {
            ret.put("code", "-1");
            ret.put("message", "新文件名和旧文件名相同...");
        }
        return ret;
    }

    /**
     * 列出指定目录下的所有可见(非隐藏文件)文件
     *
     * @param file 目录
     * @return
     */
    public static File[] listVisibleFiles(File file) {
        File[] tmp = file.listFiles();
        int LENGTH = tmp.length;
        File[] tmpII = new File[LENGTH];
        int di = 0;
        /* 获得所有可见文件数量 */
        for (int si = 0; si < tmp.length; si++)
            if (!tmp[si].isHidden()) {
                tmpII[di] = tmp[si];
                LENGTH = (++di);
            }
        /* 收集所有可见文件的File对象 */
        File[] ret = new File[LENGTH];
        for (int i = 0; i < LENGTH; i++)
            ret[i] = tmpII[i];
        return ret;
    }

    /**
     * 列出目录下所有可见文件
     *
     * @param file
     * @param mode "dir"仅列出目录, "all"列出所有
     * @return
     */
    private List<File> listVisibleFiles(File file, String mode) {
        List<File> fileList = new ArrayList<File>();
        for (File f : file.listFiles()) {
            if (!f.isHidden()) {
                if ("dir".equals(mode)) {
                    if (f.isDirectory()) {
                        fileList.add(f);
                    }
                } else {
                    fileList.add(f);
                }
            }
        }
        return fileList;
    }

    /**
     * 列出目录下(包含子目录)所有可见文件
     *
     * @param file
     * @return
     */
    public static List<File> listAllVisibleFiles(File file) {
        List<File> fileList = new ArrayList<File>();
        for (File f : file.listFiles()) {
            if (!f.isHidden()) {
                if (f.isDirectory()) {
                    fileList.addAll(listAllVisibleFiles(f));
                } else {
                    fileList.add(f);
                }
            }
        }
        return fileList;
    }

    /**
     * 移动目录下所有文件到目标目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     */
    public Map<String, String> moveAll(String srcDir, String destDir) {
        Map<String, String> ret = new HashMap<String, String>();
        try {
            ret = copyAll(srcDir, destDir);
            if (ret.get("code") != null && ret.get("code").equals("0")) {
                deleteAll(srcDir);
            } else {
                return ret;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ret.put("code", "-1");
            ret.put("message", e.getMessage());
        }
        return ret;
    }

    /**
     * 移动单个文件
     *
     * @param srcFile  源文件(含路径)
     * @param destFile 目标文件(含路径)
     */
    public Map<String, String> move(String srcFile, String destFile) {
        Map<String, String> ret = new HashMap<String, String>();
        try {
            copy(srcFile, destFile);
            (new File(srcFile)).delete();
            ret.put("code", "0");
        } catch (Exception e) {
            e.printStackTrace();
            ret.put("code", "-1");
            ret.put("message", e.getMessage());
        }
        return ret;
    }

    /**
     * 将指定目录下的所有文件拷贝到目标目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录；如果目标目录不存在，则新建立之
     */
    public Map<String, String> copyAll(String srcDir, String destDir) {
        Map<String, String> ret = new HashMap<String, String>();

        File src = new File(srcDir);
        File dest = new File(destDir);

        if (!src.exists()) {
            ret.put("code", "-1");
            ret.put("message", "文件不存在!");
            return ret;
        } else if (!src.isDirectory()) {
            copy(srcDir, destDir);
            ret.put("code", "0");
            return ret;
        }
        if (!dest.exists()) {
            dest.mkdir();
        }

		/* 将源目录下所有文件(包括子目录)拷贝到目标目录下 */
        // File[] fileList = listVisibleFiles(src);
        File[] fileList = src.listFiles();

        for (int di = 0; di < fileList.length; di++) {
            String tmpSrc = fileList[di].getPath();
            String tmpDest = dest.getPath() + File.separator + fileList[di].getName();

            if (fileList[di].isDirectory()) {

                copyAll(tmpSrc, tmpDest);

            } else if (fileList[di].isFile()) {
                /* 读源文件,然后写到目标目录下 */
                copy(tmpSrc, tmpDest);
            }
        }
        ret.put("code", "0");
        return ret;
    }

    /**
     * 使用文件通道的方式复制文件 fileChannelCopy
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */
    public void copy(File s, File t) {

        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);

            fo = new FileOutputStream(t);

            in = fi.getChannel();// 得到对应的文件通道

            out = fo.getChannel();// 得到对应的文件通道

            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (fi != null) {
                    fi.close();
                }

                if (in != null) {
                    in.close();
                }

                if (fo != null) {
                    fo.close();
                }

                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    /**
     * 管道方式拷贝单个文件
     *
     * @param src
     * @param dest
     */
    public void copy(String src, String dest) {

        File s = new File(src);

        File t = new File(dest);

        copy(s, t);

    }

    /**
     * 写文件
     *
     * @param path    文件的路径
     * @param content 写入文件的内容
     */
    public static void writerText(String path, String content) {

        File file = new File(path);
        BufferedWriter bw1 = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // new FileWriter(path + "t.txt", true) 这里加入true 可以不覆盖原有TXT文件内容 续写
            bw1 = new BufferedWriter(new FileWriter(path, false));
            bw1.write(content);
            bw1.flush();
            bw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw1 != null) {
                try {
                    bw1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();
        List list = fileUtil.listAllVisibleFiles(new File(
                "E:\\zxcx\\2014-11\\115_SVN\\hospital.war\\WebContent\\files\\2\\contact"));
        System.out.println(list);
        System.out.println(list.size());
    }
}
