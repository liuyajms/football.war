package cn.com.weixunyun.child.module.files;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.module.files.util.ZipUtil;
import cn.com.weixunyun.child.module.files.util.tree.TreeNode;
import cn.com.weixunyun.child.module.files.util.tree.TreeUtil;
import cn.com.weixunyun.child.util.ThrowableUtils;
import org.apache.log4j.Logger;
import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONObject;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/files")
@Produces(MediaType.APPLICATION_JSON)
@Description("接口")
public class FilesResource extends AbstractResource {

    private final Logger logger = Logger.getLogger(FilesResource.class);


    @GET
    @Description("查询数据")
    public List<Files> getList(
            @CookieParam("rsessionid") String rsessionid,
            @QueryParam("path") String path) {
//        FileLogService service = super.getService(FileLogService.class);
        FilesService service = super.getService(FilesService.class);
        return service.getList(path);
    }

    @GET
    @Path("tree")
    @Produces(MediaType.APPLICATION_JSON)
    @Description("查询数据")
    public List<TreeNode> getTreeList(
            @CookieParam("rsessionid") String rsessionid,
            @QueryParam("path") String path) throws UnsupportedEncodingException {
        FilesService service = super.getService(FilesService.class);
        System.out.println("PATH:"+path);

        List<FileTree> list = service.getTreeList(path);
//        List<TreeNode> nodes = new ArrayList<>();
//
//        for (FileTree tree : list) { //接口转换
//            nodes.add(tree);
//        }
        TreeUtil.setRootCode(path.replace("/", File.separator));
        return TreeUtil.getJsonList(list);
    }


    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("新建文件夹")
    public Map<String, String> insert(MultivaluedMap<String, String> formData, @Context HttpServletRequest request) throws UnsupportedEncodingException {

        logger.debug("------------formData:" + formData);
        String newName = URLDecoder.decode(formData.getFirst("newName"), "utf-8");
        String parentPath = URLDecoder.decode(formData.getFirst("parentPath"), "utf-8");

        String fullPath = parentPath + java.io.File.separator + newName;

        FilesService service = super.getService(FilesService.class);
        return service.mkDir(fullPath);

    }


    @PUT
    @Path("/rename")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Description("修改")
    public Map<String, String> update(MultivaluedMap<String, String> formData,
                                      @CookieParam("rsessionid") String rsessionid) throws IOException {
        logger.debug("------------formData:" + formData);
        String oldName = URLDecoder.decode(formData.getFirst("oldName"), "utf-8");
        String newName = URLDecoder.decode(formData.getFirst("newName"), "utf-8");
        String parentPath = URLDecoder.decode(formData.getFirst("parentPath"), "utf-8");

        FilesService service = super.getService(FilesService.class);
        return service.reName(parentPath, oldName, newName);

    }

    @GET
    @Path("/move")
    @Description("移动文件")
    public Map<String,String> move(
            @QueryParam("sourceFile") String  sourceFile,
            @QueryParam("targetDir") String targetDir,
            @QueryParam("fileType") String  fileType) {
        logger.debug("------------移动文件formData:");
        logger.debug(sourceFile + "###" + targetDir);
//        FileLogService service = super.getService(FileLogService.class);
        FilesService service = super.getService(FilesService.class);

        return service.moveFile(sourceFile,targetDir,fileType);
    }


    @DELETE
    @Description("删除")
    public void delete(@QueryParam("path") String path, MultivaluedMap<String, String> formData) throws UnsupportedEncodingException {

        String filePath = URLDecoder.decode(formData.getFirst("path"), "utf-8");
        FilesService service = super.getService(FilesService.class);
        service.deleteAll(filePath);
    }


    @POST
    @Path("upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Description("文件上传")
    public DMLResponse insertMulti(@Context HttpServletRequest request, @CookieParam("rsessionid") String rsessionid)
            throws Exception {
//        FileLogService logService = super.getService(FileLogService.class);

        FilesService service = super.getService(FilesService.class);
//        Long id = super.getService(FileLogService.class).sequence();
        try {
            logger.info("------------upload-----------");

            Map<String, PartField> map = super.partMulti(request);

            String path = map.get("path").getValue();

            List<PartFieldFile> fileList = map.get("files").getFileList();
            System.out.println("f" + fileList.size());
            for (PartFieldFile f : fileList) {
                String fullPath = path + java.io.File.separator + f.getOriName();
                logger.debug("-========= fullPath:" + fullPath);
                service.copy(f.getAbsolutePath(), fullPath);

            }

            return new DMLResponse(true, Integer.toString(fileList.size()));
        } catch (Exception e) {
            Throwable throwable = ThrowableUtils.getRootCause(e);
            return new DMLResponse(false, throwable.getMessage());
        }
    }


    @GET
    @Path("download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Description("下載文件")
    public void download(@Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @QueryParam("file") String file
    ) throws IOException {

        // 服务器相对路径
        String fileName = file.substring(file.lastIndexOf(File.separator) + 1);
        // 读取文件名：用于设置客户端保存时指定默认文件名
        ServletOutputStream os = null;

        FileInputStream fis = null;

        try {
            File obj = new File(file);

            if (obj.exists() && !obj.isDirectory()) {

                os = response.getOutputStream();
                // response.setContentType("application/msword");
                // 判断浏览器
                String agent = request.getHeader("USER-AGENT");

                if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
                    response.setHeader("Content-Disposition",
                            "attachment;filename="
                                    + URLEncoder.encode(fileName, "UTF-8"));
                } else {
                    response.setHeader("Content-Disposition",
                            "attachment;filename="
                                    + new String(fileName.getBytes("UTF-8"),
                                    "iso-8859-1"));
                }
                // response.setHeader("Content-Disposition",
                // "attachment;filename=" + newfilename);
                response.setContentType("application/octet-stream");// 定义输出类型

                fis = new FileInputStream(file);

                byte[] bf = new byte[1024];

                int len = 0;

                while ((len = fis.read(bf, 0, 1024)) > 0) {

                    os.write(bf, 0, len);

                }

            } else if ( obj.isDirectory() ) {

                ZipUtil zipUtil = new ZipUtil();

                String tmpPath =  System.getProperty("java.io.tmpdir") + File.separator + fileName + ".zip";

                zipUtil.zip( file, tmpPath );

                download(request,response, tmpPath);

            } else {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("下载错误,文件不存在或文件名为空!");
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }


    /**
     * 字符串转换时间函数
     *
     * @param dateStr 时间字符串
     * @param format  格式： "yyyy-MM-dd HH:mm:ss "
     * @return
     */
    private Date stringToDate(String dateStr, String format) {
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
    private String dateFormat(Date date, String format) {
        String dateStr = null;
        if (format != null && !"".equals(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }
}
