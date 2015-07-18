package cn.com.weixunyun.child.model.dao;

import java.util.Map;

public class SensitiveMapperProvider extends AbstractMapperProvider {

	public String getListSql(Map<String, Object> paramMap) {
        StringBuffer sb = new StringBuffer();

        sb.append("select * from sensitive" );


        String keyword = (String) paramMap.get("keyword");
        if (keyword != null && !"".equals(keyword)) {
            sb.append(" where  name like '%'||#{keyword}||'%'  ");
        }
        return sb.toString();
    }

    public String getList(Map<String, Object> paramMap) {
        return super.page(getListSql(paramMap), "id");
    }

    public String getListCount(Map<String, Object> paramMap) {
        return "select count(*) from (" + getListSql(paramMap) + ") t_";
    }
    
    public String getSQL(String sql){
    	return sql;
    }
}
