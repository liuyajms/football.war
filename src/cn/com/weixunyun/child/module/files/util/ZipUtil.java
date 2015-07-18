package cn.com.weixunyun.child.module.files.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public void zip(String souceFileName, String destFileName) {
        File file = new File(souceFileName);
        try {
            zip(file, destFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zip(File souceFile, String destFileName) throws IOException {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(destFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipOutputStream out = new ZipOutputStream(fileOut);
        zip(souceFile, out, "");
        out.close();
    }

    private void zip(File souceFile, ZipOutputStream out, String base) throws IOException {

        if (souceFile.isDirectory()) {
            File[] files = souceFile.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (File file : files) {
                zip(file, out, base + file.getName());
            }
        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(souceFile.getName()));
            }
            // System.out.println("filepath=" + souceFile.getPath());
            FileInputStream in = new FileInputStream(souceFile);

            int b;
            byte[] by = new byte[1024];
            while ((b = in.read(by)) != -1) {
                out.write(by, 0, b);
            }
            in.close();
        }
    }

    /**
     * Test
     */
    public static void main(String[] args) {
        ZipUtil z = new ZipUtil();
        z.zip("E:\\zxcx\\2014-09\\924", "E:\\zxcx\\1.zip");
    }
}
