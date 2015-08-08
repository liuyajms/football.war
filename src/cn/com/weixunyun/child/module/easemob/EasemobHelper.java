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

    private final static int TRY_COUNT = 3;

    public static void main(String[] args) {
//        EasemobHelper.createUser(324L);
        createGroup("神马队", new Long[]{1L});
//        deleteUser(324L);
//        updateGroup("90939382839640520","好好看");
//        createUser(1L);
    }

    public static void createUser(Long playerId) {
        createUser(playerId, TRY_COUNT);
    }

    /**
     * 注册IM用户[单个]
     */
    public static void createUser(Long playerId, int n) {
        try {
            ObjectNode datanode = JsonNodeFactory.instance.objectNode();
            datanode.put("username", playerId);
            datanode.put("password", Constants.DEFAULT_PASSWORD);
            ObjectNode node = EasemobIMUsers.createNewIMUserSingle(datanode);
            handlerResult(node);
        } catch (Exception e) {
            if (n > 0) {
                createUser(playerId, --n);
            } else {
                throw new RuntimeException("创建聊天用户失败:" + e.getMessage());
            }
        }

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
        deleteUser(playerId, TRY_COUNT);
    }

    public static void deleteUser(Long playerId, int n) {
        try {
            ObjectNode node = EasemobIMUsers.deleteIMUserByuserName(playerId.toString());
            if (null != node) {
                LOGGER.info("删除IM用户[单个]: " + node.toString());
            }
            handlerResult(node);
        } catch (Exception e) {
            if (n > 0) {
                deleteUser(playerId, --n);
            } else {
                throw new RuntimeException("删除聊天用户失败:" + e.getMessage());
            }
        }
    }

    /**
     * 创建群组,同时加入群成员并返回群组ID
     *
     * @param groupName
     * @param playerIds
     */
    public static String createGroup(String groupName, Long[] playerIds) {
        return createGroup(groupName, playerIds, TRY_COUNT);
    }

    public static String createGroup(String groupName, Long[] playerIds, int n) {
        try {
            ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
            dataObjectNode.put("groupname", groupName);
            dataObjectNode.put("desc", groupName);
            dataObjectNode.put("approval", true);
            dataObjectNode.put("public", true);
            dataObjectNode.put("maxusers", 333);
            dataObjectNode.put("owner", playerIds[0].toString());
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            for (Long playerId : playerIds) {
                if (!playerId.equals(playerIds[0])) {
                    arrayNode.add(playerId.toString());
                }
            }
            dataObjectNode.put("members", arrayNode);
            ObjectNode node = EasemobChatGroups.creatChatGroups(dataObjectNode);

            handlerResult(node);
            return node.findValue("data").findValue("groupid").toString();
        } catch (Exception e) {
            if (n > 0) {
                createGroup(groupName, playerIds, --n);
            } else {
                e.printStackTrace();
                throw new RuntimeException("新建群组失败:" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 删除群组
     *
     * @param groupId
     */
    public static void deleteGroup(String groupId) {
        deleteGroup(groupId, TRY_COUNT);
    }

    public static void deleteGroup(String groupId, int n) {
        try {
            ObjectNode deleteChatGroupNode = EasemobChatGroups.deleteChatGroups(groupId);
            handlerResult(deleteChatGroupNode);
        } catch (Exception e) {
            if (n > 0) {
                deleteGroup(groupId, --n);
            } else {
                throw new RuntimeException("删除群组失败:" + e.getMessage());
            }
        }
    }

    /**
     * 修改群组
     *
     * @param groupId
     */
    public static void updateGroup(String groupId, String groupName) {
        updateGroup(groupId, groupName, TRY_COUNT);
    }

    public static void updateGroup(String groupId, String groupName, int n) {

        try {
            ObjectNode dataNode = JsonNodeFactory.instance.objectNode();
            dataNode.put("groupname", groupName);
            ObjectNode deleteChatGroupNode = EasemobChatGroups.updateChatGroups(groupId, dataNode);
            handlerResult(deleteChatGroupNode);
        } catch (Exception e) {
            if (n > 0) {
                updateGroup(groupId, groupName, --n);
            } else {
                throw new RuntimeException("修改群名称失败:" + e.getMessage());
            }
        }
    }

    /**
     * 群组添加用户
     *
     * @param groupId
     * @param playerId
     */
    public static void addUserToGroup(String groupId, Long playerId) {
        addUserToGroup(groupId, playerId, TRY_COUNT);
    }


    public static void addUserToGroup(String groupId, Long playerId, int n) {
        try {
            ObjectNode addUserToGroupNode = EasemobChatGroups.addUserToGroup(groupId, playerId.toString());
            handlerResult(addUserToGroupNode);
        } catch (Exception e) {
            if (n > 0) {
                addUserToGroup(groupId, playerId, n);
            } else {
                throw new RuntimeException("添加群用户失败:" + e.getMessage());
            }
        }

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

    public static void deleteUserFromGroup(String groupId, Long playerId, int n) {
        try {
            ObjectNode deleteUserFromGroupNode = EasemobChatGroups.deleteUserFromGroup(groupId, playerId.toString());
            handlerResult(deleteUserFromGroupNode);
        } catch (Exception e) {
            if (n > 0) {
                deleteGroup(groupId, --n);
            } else {
                throw new RuntimeException("删除群用户失败:" + e.getMessage());
            }
        }
    }
}
