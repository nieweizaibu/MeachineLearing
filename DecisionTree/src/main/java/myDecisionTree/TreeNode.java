package myDecisionTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/8/8.
 */
public class TreeNode {
    private String attributeValue;
    private List<TreeNode> childTreeNode;
    private List<String> pathName;
    private String targetFunValue;
    private String nodeName;

    public TreeNode(String nodeName){

        this.nodeName = nodeName;
        this.childTreeNode = new ArrayList<TreeNode>();
        this.pathName = new ArrayList<String>();
    }

    public TreeNode(){
        this.childTreeNode = new ArrayList<TreeNode>();
        this.pathName = new ArrayList<String>();
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public List<TreeNode> getChildTreeNode() {
        return childTreeNode;
    }

    public void setChildTreeNode(List<TreeNode> childTreeNode) {
        this.childTreeNode = childTreeNode;
    }

    public String getTargetFunValue() {
        return targetFunValue;
    }

    public void setTargetFunValue(String targetFunValue) {
        this.targetFunValue = targetFunValue;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<String> getPathName() {
        return pathName;
    }

    public void setPathName(List<String> pathName) {
        this.pathName = pathName;
    }
}
