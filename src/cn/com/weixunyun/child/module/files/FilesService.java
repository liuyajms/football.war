package cn.com.weixunyun.child.module.files;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FilesService {

    public List<Files> getList(String path);

    /**
     * 将文件File转换为Files pojo对象
     *
     * @param fileList
     * @return
     */
    public List<Files> getFilesList(List<File> fileList);

    public Map<String, String> mkDir(String fullPath);

    public Map<String, String> reName(String parentPath, String oldName, String newName);

    public void deleteAll(String filePath);

    void copy(String absolutePath, String fullPath);

    List<FileTree> getTreeList(String path);

    Map<String, String> moveFile(String sourceFile, String targetDir, String fileType);

    public Map<String, String> copyFile(String sourceFile, String targetDir, String fileType);
}
