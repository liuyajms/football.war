package cn.com.weixunyun.child.model.service;

import cn.com.weixunyun.child.model.bean.Team;
import cn.com.weixunyun.child.model.dao.DictionaryValueMapper;
import cn.com.weixunyun.child.model.dao.TeamMapper;
import cn.com.weixunyun.child.model.dao.TeamPlayerMapper;
import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import cn.com.weixunyun.child.model.vo.TeamPlayerVO;
import cn.com.weixunyun.child.model.vo.TeamVO;

import java.util.ArrayList;
import java.util.List;


public class TeamServiceImpl extends AbstractService implements TeamService {
    @Override
    public void delete(Long id) {
        super.getMapper(TeamMapper.class).delete(id);
    }

    @Override
    public void insert(Team record) {
        super.getMapper(TeamMapper.class).insert(record);
    }

    @Override
    public TeamVO get(Long id) {
        TeamVO team = super.getMapper(TeamMapper.class).get(id);

        //查询数据字典
        List<DictionaryValue> ruleList = super.getMapper(DictionaryValueMapper.class).getValueList("team", "rule");
        team.setRuleList(getRuleList(ruleList, team));

        List<DictionaryValue> colorList = super.getMapper(DictionaryValueMapper.class).getValueList("team", "color");
        team.setColorList(getColorList(colorList, team));

        //查询球队成员
        List<TeamPlayerVO> teamPlayerList = super.getMapper(TeamPlayerMapper.class).getList(id, null, null);
        team.setTeamPlayerList(teamPlayerList);

        return team;
    }

    @Override
    public List<TeamVO> getList(String city, Integer rule, Integer beginAge, Integer endAge,
                                String keyword, Long sourceId, long rows, long offset) {
        List<TeamVO> list = super.getMapper(TeamMapper.class)
                .getList(city, rule, beginAge, endAge, keyword, sourceId, rows, offset);


        //查询数据字典
        List<DictionaryValue> ruleList = super.getMapper(DictionaryValueMapper.class).getValueList("player", "rule");

        for (TeamVO team : list) {
            team.setRuleList(getRuleList(ruleList, team));
        }

        return list;
    }

    @Override
    public void update(Team record) {
        super.getMapper(TeamMapper.class).update(record);
    }

    @Override
    public void updated(Long id) {
        super.getMapper(TeamMapper.class).updated(id);
    }

    private List<String> getRuleList(List<DictionaryValue> ruleList, TeamVO teamVO) {
        List<String> nameList = new ArrayList<String>();
        for (DictionaryValue value : ruleList) {
            if ((Integer.parseInt(value.getCode()) & teamVO.getRule()) > 0) {
                nameList.add(value.getName());
            }
        }
        return nameList;
    }

    private List<String> getColorList(List<DictionaryValue> colorList, TeamVO teamVO) {
        List<String> nameList = new ArrayList<String>();
        for (DictionaryValue value : colorList) {
            if ((Integer.parseInt(value.getCode()) & teamVO.getColor()) > 0) {
                nameList.add(value.getName());
            }
        }
        return nameList;
    }
}
