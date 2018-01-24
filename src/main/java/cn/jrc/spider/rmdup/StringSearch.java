package cn.jrc.spider.rmdup;

import java.util.ArrayList;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/24 17:48
 */
public class StringSearch {
    private TreeNode _root;
   // private String[] _keywords;

    public StringSearch(String[] keywords) {
        buildTree(keywords);
        addFailure();
    }

    private void addFailure() {
        ArrayList<TreeNode> nodes = new ArrayList<>();
        for (TreeNode nd : _root.transitions()) {
            nd.failure(_root);
            for (TreeNode trans : nd.transitions()) {
                nodes.add(trans);
            }
        }
        while(nodes.size()!=0){
            ArrayList<TreeNode> newNodes = new ArrayList<>();
            for (TreeNode nd : nodes) {
                TreeNode r = nd.parent()._failure;
                char c = nd.getChar();
                while(r!=null&&!r.containsTransition(c))
                    r = r._failure;
                if(r==null){
                    nd._failure = _root;
                }else {
                    nd._failure = r.getTransition(c);
                    for (String result : nd._failure.results()) {
                        nd.addResult(result);
                    }
                }
                for (TreeNode child : nd.transitions()) {
                    newNodes.add(child);
                }
            }
            nodes = newNodes;
        }
        _root._failure = _root;
    }

    private void buildTree(String[] keywords) {
        _root = new TreeNode(null, ' ');
        for (String keyword : keywords) {
            TreeNode nd = _root;
            for (char c : keyword.toCharArray()) {
                TreeNode ndNew = null;
                for (TreeNode trans : nd.transitions()) {
                    if(trans.getChar()==c){
                        ndNew = trans;
                        break;
                    }
                }
                if(ndNew==null){
                    ndNew = new TreeNode(nd,c);
                    nd.addTransition(ndNew);
                }
                nd = ndNew;
            }
            nd.addResult(keyword);
        }
    }

    /**
     * depth traverse
     * @param node
     * @param ret
     * @param depth
     */
    public void depthSearch(TreeNode node,StringBuilder ret,int depth){
        if(node!=null){
            for (int i = 0; i < depth; i++) {
                ret.append("|  ");
            }
            ret.append(" | ---");
            ret.append(node._char+"\n");
            for (TreeNode child : node.transitions()) {
                int childDepth = depth+1;
                depthSearch(child,ret,childDepth);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("print tree node: \n");
        int depth = 0;
        depthSearch(_root,ret,depth);
        return ret.toString();
    }
}
