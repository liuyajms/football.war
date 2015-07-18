package cn.com.weixunyun.child.module.files.util.tree;

import java.util.List;

public interface TreeNode {
    public String getIdParent_();

    public String getId_();

    public void setNodes(List<TreeNode> nodes);

    public List<TreeNode> getNodes();
}
