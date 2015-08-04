package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.dao.DictionaryValueMapper;
import cn.com.weixunyun.child.model.dao.GlobalMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

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
     * @param codeParent
     * @param code
     * @return
     */
    protected String getGlobalValue(String codeParent, String code){

        return getMapper(GlobalMapper.class).select(codeParent, code).getValue();
    }
}
