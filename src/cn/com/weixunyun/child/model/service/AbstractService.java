package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.DictionaryValueMapper;
import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.dao.SequenceMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractService {

    private SqlSession session;

    public SqlSession getSession() {
        return session;
    }

    public void setSession(SqlSession session) {
        this.session = session;
    }

    public <T> T getMapper(Class<T> cls) {
        return session.getMapper(cls);
    }


    /**
     * 获取数据字典字段代码对应的名称列表
     *
     * @param tableCode
     * @param fieldCode
     * @param rule
     * @return
     */
    protected List<String> getDicValueList(String tableCode, String fieldCode, Integer rule) {
        List<DictionaryValue> dicList = getMapper(DictionaryValueMapper.class).getValueList(tableCode, fieldCode);

        List<String> nameList = new ArrayList<String>();
        if (rule != null) {
            for (DictionaryValue value : dicList) {
                if ((Integer.parseInt(value.getCode()) & rule) > 0) {
                    nameList.add(value.getName());
                }
            }
        }
        return nameList;
    }

    /**
     * 获取全局配置信息
     *
     * @param codeParent
     * @param code
     * @return
     */
    protected String getGlobalValue(String codeParent, String code) {

        return getMapper(GlobalMapper.class).select(codeParent, code).getValue();
    }

    protected boolean isHuanXinOpen() {
        return getMapper(GlobalMapper.class).select("huanxin", "open").getValue().equals("1");
    }

    protected <T> T buildEntity(Class<T> cls, Map<String, Object> map) {
        return buildEntity(cls, map, null);
    }


    /**
     * excel导入数据时调用
     *
     * @param cls
     * @param map
     * @param <T>
     * @return
     */
    protected <T> T buildEntity(Class<T> cls, Map<String, Object> map, Long id) {
        try {
            T t = cls.newInstance();
            Method[] methods = cls.getMethods();
            String name = "";

            if (id == null) {
                id = getMapper(SequenceMapper.class).sequence();
            }

            for (Method method : methods) {
                name = method.getName();
                if (name.startsWith("set")) {
                    name = name.substring(3).replaceFirst(name.substring(3, 4), name.substring(3, 4).toLowerCase());
                    String str = null;
                    if ((null != map.get(name)) && !"".equals(map.get(name))) {
                        str = map.get(name).toString();
                    }
                    Class<?> type = method.getParameterTypes()[0];
                    if (type == String.class) {
                        method.invoke(t, str);
                    } else if (type == Boolean.class) { // Boolean类型默认false
                        if ("true".equals(str)) {
                            method.invoke(t, true);
                        } else {
                            method.invoke(t, false);
                        }
                    } else if (type == Timestamp.class) {
                        method.invoke(t, new Timestamp(System.currentTimeMillis()));
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
                    } else if (type == Float.class) {
                        if ((str != null) && (!"".equals(str))) {
                            method.invoke(t, Float.valueOf(str));
                        }
                    } else if (type == java.sql.Date.class) {
                        if ((str != null) && (!"".equals(str))) {
                            method.invoke(t, java.sql.Date.valueOf(str));
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
}
