package Tree;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class TreeNode {

    private  String data;
    private  TreeNode parent;
    private  ArrayList<TreeNode> children;
    
    public TreeNode(){
    	this.data = null;
    	this.parent = null;
    	this.children = null;
    }

    public TreeNode(String data) {
        this.data = data;
        this.parent = null;
        this.children = new ArrayList<TreeNode>();
    }


	public String getData() {
		return data;
		
	}

	public void setData(String data) {
		this.data = data;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeNode> children) {
		this.children = children;
	}
	
	public void addChildren(TreeNode x){
		this.children.add(x);
	}
	
	
	   public void print(String prefix, boolean isTail) {
		   //Please not this method was taken from Vasya Novikov on stackoverflow
		   //URL: http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	        System.out.println(prefix + (isTail ? "└── " : "├── ") + getData());
	        for (int i = 0; i < children.size() - 1; i++) {
	            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
	        }
	        if (children.size() > 0) {
	            children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true);
	        }
	    }

}