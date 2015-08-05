package cn.com.weixunyun.child.module.easemob;

import cn.com.weixunyun.child.module.easemob.api.EasemobChatGroups;
import cn.com.weixunyun.child.module.easemob.api.EasemobIMUsers;
import cn.com.weixunyun.child.module.easemob.comm.Constants;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PC on 2015/8/4.
 */
public class EasemobHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasemobHelper.class);

    public static void main(String[] args) {
//        EasemobHelper.createUser(324L);
//        createGroup("我的训练赛2", 101L);
//        deleteUser(324L);
        updateGroup("90939382839640520","好好看");
    }

    public static void createUser(Long playerId) {
        /**
         * 注册IM用户[单个]
         */
        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username", playerId);
        datanode.put("password", Constants.DEFAULT_PASSWORD);
        ObjectNode node = EasemobIMUsers.createNewIMUserSingle(datanode);
        handlerResult(node);

    }

    private static void handlerResult(ObjectNode node) {
        System.out.println(node);
        if (null == node) {
            throw new RuntimeException(node.findValue("error_description").toString());
        } else if (!node.findValue("statusCode").toString().equals("200")) {
            throw new RuntimeException(node.findValue("message").toString());
        }
    }

    /**
     * 删除IM用户[单个]
     */
    public static void deleteUser(Long playerId) {
        ObjectNode node = EasemobIMUsers.deleteIMUserByuserName(playerId.toString());
        if (null != node) {
            LOGGER.info("删除IM用户[单个]: " + node.toString());
        }
        handlerResult(node);
    }

    /**
     * 创建群组并返回群组ID
     *
     * @param groupName
     * @param createPlayerId
     */
    public static String createGroup(String groupName, Long createPlayerId) {
        ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
        dataObjectNode.put("groupname", groupName);
        dataObjectNode.put("desc", groupName);
        dataObjectNode.put("approval", true);
        dataObjectNode.put("public", true);
        dataObjectNode.put("maxusers", 333);
        dataObjectNode.put("owner", createPlayerId.toString());
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
//        arrayNode.add("xiaojianguo002");
//        arrayNode.add("xiaojianguo003");
        dataObjectNode.put("members", arrayNode);
        ObjectNode node = EasemobChatGroups.creatChatGroups(dataObjectNode);

        handlerResult(node);
        return node.findValue("data").findValue("groupid").toString();
    }

    /**
     * 删除群组
     *
     * @param groupId
     */
    public static void deleteGroup(String groupId) {
        ObjectNode deleteChatGroupNode = EasemobChatGroups.deleteChatGroups(groupId);
        handlerResult(deleteChatGroupNode);
    }

    /**
     * 修改群组
     *
     * @param groupId
     */
    public static void updateGroup(String groupId, String groupName) {
        ObjectNode dataNode = JsonNodeFactory.instance.objectNode();
        dataNode.put("groupname", groupName);
        ObjectNode deleteChatGroupNode = EasemobChatGroups.updateChatGroups(groupId, dataNode);
        handlerResult(deleteChatGroupNode);
    }

    /**
     * 群组添加用户
     *
     * @param groupId
     * @param playerId
     */
    public static void addUserToGroup(String groupId, Long playerId) {
        ObjectNode addUserToGroupNode = EasemobChatGroups.addUserToGroup(groupId, playerId.toString());
        handlerResult(addUserToGroupNode);
    }

    /**
     * 群组删除用户
     *
     * @param groupId
     * @param playerId
     */
    public static void deleteUserFromGroup(String groupId, Long playerId) {
        ObjectNode deleteUserFromGroupNode = EasemobChatGroups.deleteUserFromGroup(groupId, playerId.toString());
        handlerResult(deleteUserFromGroupNode);
    }


}
