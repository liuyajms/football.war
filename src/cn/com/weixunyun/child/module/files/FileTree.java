package cn.com.weixunyun.child.module.files;


import cn.com.weixunyun.child.module.files.util.tree.TreeNode;

import java.util.List;

public class FileTree extends Files implements TreeNode {

    private String code;
    private String parentCode;
    private String text;
    private List<TreeNode> nodes; // 该节点包含的子节点列表

    public void setCode(String code) {
        this.code = code;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    @Override
    public String getIdParent_() {
        return parentCode;
    }

    @Override
    public String getId_() {
        return code;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }
}
