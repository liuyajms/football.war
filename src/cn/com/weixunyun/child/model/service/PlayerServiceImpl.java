package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Player;
import cn.com.weixunyun.child.model.dao.MatchMapper;
import cn.com.weixunyun.child.model.dao.PlayerMapper;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import cn.com.weixunyun.child.module.calendar.CalendarMapper;
import cn.com.weixunyun.child.module.easemob.EasemobHelper;
import cn.com.weixunyun.child.util.DateUtil;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

public class PlayerServiceImpl extends AbstractService implements PlayerService {

    @Override
    public void delete(Long id) {
        super.getMapper(PlayerMapper.class).delete(id);

        if (super.getGlobalValue("huanxin", "open").equals("1")) {
            EasemobHelper.deleteUser(id);
        }
    }

    @Override
    public void insert(Player player) {
        super.getMapper(PlayerMapper.class).insert(player);

        if (super.getGlobalValue("huanxin", "open").equals("1")) {
            EasemobHelper.createUser(player.getId());
        }
    }

    @Override
    public PlayerVO get(Long id) {
        PlayerVO playerVO = super.getMapper(PlayerMapper.class).get(id);

        playerVO.setRoleList(super.getDicValueList("player", "role", playerVO.getRole()));

        Date beginDate = DateUtil.getMondayOfThisWeek();
        Date endDate = DateUtil.addDays(beginDate, 14);

        playerVO.setFreeTimeList(super.getMapper(CalendarMapper.class).getListByPlayerId(id, beginDate, endDate));

        playerVO.setMatchList(super.getMapper(MatchMapper.class).getListByPlayerId(id, beginDate, endDate));

        return playerVO;
    }

    @Override
    public List<PlayerVO> getList(String city, Integer role, Integer beginAge, Integer endAge,
                                  String keyword, Double px, Double py, long rows, long offset) {
        List<PlayerVO> playerVOList = super.getMapper(PlayerMapper.class)
                .getList(city, role, beginAge, endAge, keyword, px, py, rows, offset);

        for (PlayerVO playerVO : playerVOList) {
            playerVO.setRoleList(super.getDicValueList("player", "role", playerVO.getRole()));
        }
        //查询数据字典
//        List<DictionaryValue> roleList = super.getMapper(DictionaryValueMapper.class).getValueList("player", "role");
//
//        for (PlayerVO player : playerVOList) {
//            setRoleList(roleList, player);
//        }
        return playerVOList;
    }


    @Override
    public void update(Player record) {
        super.getMapper(PlayerMapper.class).update(record);
    }

    @Override
    public void updated(Long id) {
        super.getMapper(PlayerMapper.class).updated(id);
    }

    @Override
    public PlayerVO login(@Param("username") String username, @Param("password") String password) {
        PlayerVO player = super.getMapper(PlayerMapper.class).login(username, password);
        if (player != null && player.getId() != null) {
            super.getMapper(PlayerMapper.class).updated(player.getId());
        }
        return player;
    }

    @Override
    public int updateInfo(@Param("id") Long id, @Param("password") String password) {
        return super.getMapper(PlayerMapper.class).updateInfo(id, password);
    }

}
