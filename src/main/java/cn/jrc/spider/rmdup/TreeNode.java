package cn.jrc.spider.rmdup;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * AC Automaton
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/24 17:10
 */
public final class TreeNode {
    public char _char;
    private TreeNode _parent;
    public TreeNode _failure;
    private ArrayList<String> _results;
    private TreeNode[] _transitionsAr;
    private Hashtable<Character,TreeNode> _transHash; //store children hashtable

    public TreeNode(TreeNode parent,char c) {
        this._char = c;
        this._parent = parent;
        this._results = new ArrayList<>(); //array which store all no-repeat patterns
        this._transitionsAr = new TreeNode[]{};
        this._transHash = new Hashtable<>();
    }

    public void addResult(String result){
        if(_results.contains(result)){
            return;
        }
        _results.add(result);
    }

    public void addTransition(TreeNode node){
        _transHash.put(node._char,node);
        TreeNode[] ar = new TreeNode[_transHash.size()];
        Iterator<TreeNode> it = _transHash.values().iterator();
        for (int i = 0; i < ar.length; i++) {
            if(it.hasNext()){
                ar[i] = it.next();
            }
        }
        _transitionsAr = ar;
    }

    public TreeNode getTransition(char c){
        return _transHash.get(c);
    }

    public boolean containsTransition(char c){
        return getTransition(c)!=null;
    }

    public char getChar() {
        return _char;
    }

    public TreeNode parent(){
        return _parent;
    }

    public TreeNode failure(TreeNode value){
        _failure = value;
        return _failure;
    }

    public TreeNode[] transitions(){
        return _transitionsAr;
    }

    public ArrayList<String> results() {
        return _results;
    }
}
