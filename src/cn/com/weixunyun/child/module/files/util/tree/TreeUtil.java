package cn.com.weixunyun.child.module.files.util.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TreeUtil {

    private static String rootCode = "root"; // 根节点ID

    public static void setRootCode(String rootCode) {
        TreeUtil.rootCode = rootCode;
    }

    /**
     * 构造树节点列表,其中根节点ID为0
     *
     * @param dataList 包含节点Id和父节点ID的数据对象,需实现了TreeNode的对象列表
     * @return
     */
    public static List getJsonList(List<? extends TreeNode> dataList) {
        // 大的集合容器,所有节点放入此容器中
        Map<String, List<TreeNode>> subNodes = new TreeMap<String, List<TreeNode>>();
        List<TreeNode> rootNodes = new ArrayList<TreeNode>();
        // 得到最小树集合
        for (TreeNode e : dataList) {

            List<TreeNode> subTree = new ArrayList<TreeNode>();
            if (e.getIdParent_() == null || e.getIdParent_().equals(rootCode)) { // 若此节点为根节点,则加入根节点列表
                rootNodes.add(e);
            } else if (subNodes.get(e.getIdParent_()) == null) { // 若子树集合中没有此节点,则加入子树中
                subTree.add(e);
                subNodes.put(e.getIdParent_(), subTree);
            } else { // 若子树集合中有此节点,则加入对应子数中
                subNodes.get(e.getIdParent_()).add(e);
            }
        }
        // 以上，至此，第一遍遍历完毕，非叶子节点都拥有一个“数组-链表项”，也即“最小的树”已创建完毕
        // 以下，对“数组链表”Map进行遍历，更改“最小的树”的从属关系（更改指针指向），也即把所有小树组装成大树
        // System.out.println("subNodes:"+JSONArray.fromObject(subNodes));

        List<String> removeList = new ArrayList<String>();

        for (Map.Entry<String, List<TreeNode>> nodeList : subNodes.entrySet()) {
            // 获取当前遍历“数组项-链表项”的链表项，并对链表项进行遍历，从“数组-链表”小树中找到它的子节点，并将该子节点加到该小树的children中
            List<TreeNode> subNodeList = nodeList.getValue();
            for (TreeNode e : subNodeList) {
                if (subNodes.get(e.getId_()) != null) {
                    e.setNodes(subNodes.get(e.getId_()));
                    removeList.add(e.getId_());
                }
            }
        }

        for (String key : removeList) { // 移除多余项
            subNodes.remove(key);
        }
        // System.out.println("subNodes:::"+JSONArray.fromObject(subNodes));

        for (TreeNode e : rootNodes) { // 遍历根节点加入对应的子树
            e.setNodes(subNodes.get(e.getId_()));
        }
        return rootNodes;
    }

    /**
     * 以JSON字符串形式返回树节点的数据源
     *
     * @param dataList
     * @return
     */
    public static String getJsonString(List<TreeNode> dataList) {
        // List<TreeNode> rootNodes = getJsonList(dataList);
        // return JSONArray.fromObject(rootNodes).toString();
        return null;
    }
}
