package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Court;
import cn.com.weixunyun.child.model.dao.CourtMapper;

/**
 * Created by PC on 2015/7/24.
 */
public interface CourtService extends CourtMapper {
    void insertServe(Court court, String[] serveIds);

    void updateServe(Court court, String[] serveIds);
}
