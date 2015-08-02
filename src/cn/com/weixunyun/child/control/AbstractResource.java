package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Autowired;
import cn.com.weixunyun.child.NotNull;
import cn.com.weixunyun.child.PropertiesListener;
import cn.com.weixunyun.child.Session;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.service.SequenceService;
import cn.com.weixunyun.child.model.service.ServiceFactory;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import cn.com.weixunyun.child.util.ImageUtils;
import cn.com.weixunyun.child.util.ValidateUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public abstract class AbstractResource {

    protected AbstractResource() {
        System.out.println("===>>Call AbstractResource Constructor: " + this.getClass().getSimpleName());
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Autowired.class)) {
                try {
                    field.set(this, ServiceFactory.getService(field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取以逗号分隔的参数之和
     *
     * @param map
     * @param field
     * @return
     */
    protected Integer getParamValue(Map<String, PartField> map, String field) {
        Integer rule = null;
        if (map.containsKey(field)) {
            String roleValue = map.get(field).getValue();
            if (roleValue != null && !roleValue.equals("")) {
                rule = 0;
                for (String str : roleValue.split(",")) {
                    rule += Integer.parseInt(str);
                }
//                map.remove(field);
            }
        }
        return rule;
    }

    /**
     * 上传图片处理,默认存放目录{moduleName}/id.png
     *
     * @param map
     * @param id
     * @throws IOException
     */
    protected void updateImage(Map<String, PartField> map, Long id) throws IOException {
        String cls = this.getClass().getSimpleName().replace("Resource", "");
        String dir = cls.substring(0, 1).toLowerCase() + cls.substring(1);

        updateImage(map, id, dir);
    }

    protected void updateImage(Map<String, PartField> map, Long id, String dir) throws IOException {

        PartField imageField = map.get("image");
        if (imageField != null) {
            File file = imageField.getFile();
            if (file != null) {
                FileUtils.copyFile(file, new File(getFilePath(), dir + "/" + id + "@l.png"));
                ImageUtils.zoom(file, new File(getFilePath(), dir + "/" + id + ".png"));
            }
        }
    }

    /**
     * 删除默认存放目录的图片
     *
     * @param id
     * @throws IOException
     */
    protected void deleteImage(Long id) {
        String cls = this.getClass().getSimpleName().replace("Resource", "");
        String dir = cls.substring(0, 1).toLowerCase() + cls.substring(1);

        File f1 = new File(getFilePath(), dir + "/" + id + ".png");
        File f2 = new File(getFilePath(), dir + "/" + id + "@l.png");

        if (f1.exists()) {
            f1.delete();
        }
        if (f2.exists()) {
            f2.delete();
        }
    }


    protected <T> T getService(Class<T> cls) {
        return ServiceFactory.getService(cls);
    }

    protected School getAuthedSchool(String rsessionid) {
        return Session.getInstance(rsessionid).get("school");
    }

    protected Long getAuthedSchoolId(String rsessionid) {
        return getAuthedSchool(rsessionid).getId();
    }

    protected Long getAuthedId(String rsessionid) {
        return getAuthedPlayer(rsessionid).getId();
    }

    protected PlayerVO getAuthedPlayer(String rsessionid) {
        return Session.getInstance(rsessionid).get("player");
    }

    protected Map<String, PartField> part(MultivaluedMap<String, String> form) {
        Map<String, PartField> map = new HashMap<String, PartField>();
        try {
            for (String name : form.keySet()) {
                PartField field = new PartField();
                field.setName(name);
                for (String value : form.get(name)) {
                    field.addValue(URLDecoder.decode(value, "UTF-8"));
                }

                map.put(name, field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    protected Map<String, PartField> partMulti(HttpServletRequest request) {
        Map<String, PartField> map = new HashMap<String, PartField>();
        if (true || ServletFileUpload.isMultipartContent(request)) {
            ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
            fileUpload.setHeaderEncoding("utf-8");
            List<FileItem> items = null;
            try {
                items = fileUpload.parseRequest(request);
                for (FileItem item : items) {
                    PartField field = map.get(item.getFieldName());
                    if (field == null) {
                        map.put(item.getFieldName(), field = new PartField());
                    }
                    field.setType(item.getContentType());
                    field.setName(item.getFieldName());
                    if (item.isFormField()) {
                        field.addValue(item.getString("utf-8"));
                    } else if (item.getSize() > 0) {
                        field.addFile(new PartFieldFile(item));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    protected String getFilePath() {
        return PropertiesListener.getProperty("path", null) + "/file/";
    }

    /**
     * 有文件上传的反射方法
     *
     * @param cls
     * @param map
     * @return
     * @see #buildBean(Class, Map, Long)
     * @deprecated
     */
    protected <T> T buildBean(Class<T> cls, Map<String, PartField> map) {
        try {
            T t = cls.newInstance();
            Method[] methods = cls.getMethods();
            String name = "";
            // 反射方法调用
            for (Method method : methods) {
                name = method.getName();
                if (name.startsWith("set")) {
                    name = name.substring(3).replaceFirst(name.substring(3, 4), name.substring(3, 4).toLowerCase());
                    PartField obj = map.get(name);
                    if (obj != null) {
                        if (method.getParameterTypes()[0] == String.class) {
                            method.invoke(t, URLDecoder.decode(obj.getValue(), "UTF-8"));
                        } else if (method.getParameterTypes()[0] == Boolean.class) {
                            method.invoke(t, true);
                        } else if (method.getParameterTypes()[0] == Long.class) {
                            if (!"".equals(obj.getValue())) {
                                method.invoke(t, Long.valueOf(obj.getValue()));
                            }
                        }
                    } else {
                        if (method.getParameterTypes()[0] == Timestamp.class) {
                            method.invoke(t, new Timestamp(System.currentTimeMillis()));
                        }
                    }
                }
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected <T> T buildBean(Class<T> cls, MultivaluedMap<String, String> map, Long id) {
        return buildBean(cls, part(map), id);
    }

    protected <T> T buildBean(Class<T> cls, Map<String, PartField> map, Long id) {
        try {
            T t = cls.newInstance();
            Method[] methods = cls.getMethods();
            String name = "";
            Boolean flag = id == null;
            if (flag) {
                id = getService(SequenceService.class).sequence();
            }
            if (flag) {//初次构建对象时，进行字段校验
                validateField(cls, map);
            }
            for (Method method : methods) {
                name = method.getName();
                if (name.startsWith("set")) {
                    name = name.substring(3).replaceFirst(name.substring(3, 4), name.substring(3, 4).toLowerCase());
                    String str = null;
                    if ((null != map.get(name)) && !"".equals(map.get(name))) {
                        str = map.get(name).getValue();
                    }
                    Class<?> type = method.getParameterTypes()[0];
                    if (type == String.class) {
                        method.invoke(t, str);
                    } else if (type == Boolean.class) {
                        if ("1".equals(str)) {
                            method.invoke(t, true);
                        } else {
                            method.invoke(t, false);
                        }
                    } else if (type == Timestamp.class) {
                        if (flag) {
                            method.invoke(t, new Timestamp(System.currentTimeMillis()));
                        } else {
                            if (!"createTime".equals(name)) {
                                method.invoke(t, new Timestamp(System.currentTimeMillis()));
                            }
                        }

                    } else if (type == Long.class) {
                        if ("id".equals(name)) {
                            method.invoke(t, id);
                        } else if ((str != null) && (!"".equals(str))) {
                            method.invoke(t, Long.valueOf(str));
                        }

                    } else if (type == int.class) {
                        if ("id".equals(name)) {
                            method.invoke(t, id);
                        } else if ((str != null) && (!"".equals(str))) {
                            method.invoke(t, Integer.valueOf(str));
                        }

                    } else if (type == Double.class) {
                        if ((str != null) && (!"".equals(str))) {
                            method.invoke(t, Double.valueOf(str));
                        }
                    } else if (type == Date.class) {
                        if ((str != null) && (!"".equals(str))) {
                            method.invoke(t, Date.valueOf(str));
                        }
                    } else if (type == Integer.class) {
                        if (ValidateUtil.isNumber(str)) {
                            method.invoke(t, Integer.valueOf(str));
                        }
                    } else if (type == java.util.Date.class) {
                        if ((str != null) && (!"".equals(str))) {
                            method.invoke(t, new java.util.Date(Long.parseLong(str)));
                        }
                    }
                }
            }
            return t;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> void validateField(Class<T> cls, Map<String, PartField> map) {

        try {
            T t = cls.newInstance();
            Field[] fields = t.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(NotNull.class)) {
                    if (map.get(field.getName()) == null || StringUtils.isBlank(map.get(field.getName()).getValue())) {
                        throw new WebApplicationException(new IllegalArgumentException(field.getName() + "为必填参数"),
                                HttpStatus.SC_BAD_REQUEST);
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 校验字段属性
     *
     * @param t
     * @param <T>
     */
    private <T> void validateField(T t) throws IllegalAccessException {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(NotNull.class)) {
                if (field.get(t) == null) {
                    throw new WebApplicationException(new IllegalArgumentException(field.getName() + "为必填参数"),
                            HttpStatus.SC_BAD_REQUEST);
                }
            }
        }
    }

    public static class PartField {
        private String name;
        private String type;
        private List<String> valueList;
        private List<PartFieldFile> fileList;

        public PartFieldFile getFile() {
            return fileList == null || fileList.size() == 0 ? null : fileList.get(0);
        }

        public void addFile(PartFieldFile file) {
            if (fileList == null) {
                fileList = new ArrayList<PartFieldFile>();
            }
            fileList.add(file);
        }

        public List<PartFieldFile> getFileList() {
            return fileList;
        }

        public void setFileList(List<PartFieldFile> fileList) {
            this.fileList = fileList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isValueNull() {
            return valueList == null || valueList.size() == 0 || (valueList.size() == 1 && "".equals(valueList.get(0)));
        }

        public String getValue() {
            return valueList == null || valueList.size() == 0 ? null : valueList.get(0);
        }

        public void addValue(String value) {
            if (valueList == null) {
                valueList = new ArrayList<String>();
            }
            valueList.add(value);
        }

        public List<String> getValueList() {
            return valueList;
        }

        public void setValueList(List<String> valueList) {
            this.valueList = valueList;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public static class PartFieldFile extends java.io.File {

        public PartFieldFile(FileItem item) {
            super(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
            if (this.getParentFile().exists() == false) {
                this.getParentFile().mkdirs();
            }

            try {
                item.write(this);

                oriName = URLDecoder.decode(item.getName(), "utf-8");
                size = item.getSize();
                contentType = item.getContentType();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public PartFieldFile(File arg0, String arg1) {
            super(arg0, arg1);
        }

        public PartFieldFile(String arg0, String arg1) {
            super(arg0, arg1);
        }

        public PartFieldFile(String arg0) {
            super(arg0);
        }

        public PartFieldFile(URI arg0) {
            super(arg0);
        }

        private long size;
        private String contentType;
        private String oriName;

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getOriName() {
            return oriName;
        }

        public void setOriName(String oriName) {
            this.oriName = oriName;
        }

    }

    protected class DMLResponse {
        public DMLResponse() {

        }

        public DMLResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        private boolean success;
        private String message;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }


}
