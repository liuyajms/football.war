package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Player;
import cn.com.weixunyun.child.model.dao.DictionaryValueMapper;
import cn.com.weixunyun.child.model.dao.PlayerMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import cn.com.weixunyun.child.model.vo.PlayerVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public class PlayerServiceImpl extends AbstractService implements PlayerService {

    @Override
    public void delete(Long id) {
        super.getMapper(PlayerMapper.class).delete(id);
    }

    @Override
    public void insert(Player record) {
        super.getMapper(PlayerMapper.class).insert(record);
    }

    @Override
    public PlayerVO get(Long id) {
        PlayerVO player = super.getMapper(PlayerMapper.class).get(id);

        //查询数据字典
        List<DictionaryValue> ruleList = super.getMapper(DictionaryValueMapper.class).getValueList("player", "role");
        setRoleList(ruleList, player);

        return player;
    }

    @Override
    public List<PlayerVO> getList(String city, Integer role, Integer beginAge, Integer endAge,
                                  String keyword, long rows, long offset) {
        List<PlayerVO> playerVOList = super.getMapper(PlayerMapper.class)
                .getList(city, role, beginAge, endAge, keyword, rows, offset);

        //查询数据字典
        List<DictionaryValue> ruleList = super.getMapper(DictionaryValueMapper.class).getValueList("player", "role");

        for (PlayerVO player : playerVOList) {
            setRoleList(ruleList, player);
        }
        return playerVOList;
    }

    private PlayerVO setRoleList(List<DictionaryValue> roleList, PlayerVO playerVO) {
        List<String> nameList = new ArrayList<String>();
        for (DictionaryValue value : roleList) {
            if ((Integer.parseInt(value.getCode()) & playerVO.getRole()) > 0) {
                nameList.add(value.getName());
            }
        }
        playerVO.setRoleList(nameList);
        return playerVO;
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
