import java.util.ArrayList;

public class TreeNodeList {
	private TreeNode root;
	private boolean rootSet;
	private TreeNode currItem;
	
	public TreeNodeList(){
		root = currItem = new TreeNode();
		rootSet = false;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getCurrItem() {
		return currItem;
	}

	public void setCurrItem(TreeNode currItem) {
		this.currItem = currItem;
	}
	
	public void addRootNode(String data){
		TreeNode rootNode = new TreeNode(data);
		//root = rootNode;
		//root.setData(data);
		//currItem = rootNode;
		this.root = rootNode;
		this.currItem = rootNode;
		//setRoot(rootNode);
		//setCurrItem(rootNode);
		System.out.println("Create root node of " + data);
		System.out.println("Here's your root " + root.getData());
		
	}
	
	public void addBranchNode(String data){
		//check if root
		//if not make child node
		//assign our parent
		//put ourselves in parent.children
		
			TreeNode node = new TreeNode(data);
			//TreeNode temp = currItem;
			
			//node.setParent(temp);
			//currItem = node;
			
			node.setParent(this.currItem);
			this.currItem.setChildren(node);
			this.setCurrItem(node);
			//node.getParent().getChildren().add(node);
			System.out.println("Creating BRANCH of " + data);
			System.out.println("His parent is " + node.getParent().getData());
			//System.out.println("CurrItem Test " + currItem.getData()); 
			//System.out.println("ROOT TEST " + root.getData());
			
			
	}
	
	public void addLeafNode(String data){
		TreeNode node = new TreeNode(data);
		//TreeNode temp = currItem;
		node.setParent(this.currItem);
		this.currItem.setChildren(node);
		this.setCurrItem(node);
	//	this.setCurrItem(node);
		
		
		//currItem.getParent().getChildren().add(node);
		
		
	}
	
	public void climb(){
		if(this.currItem != null){
			this.currItem = this.currItem.getParent();
		}
	}
	
	public void rootChildren(){
		ArrayList<TreeNode> t = root.getChildren();
		for(TreeNode x : t)
			System.out.println(x.getData());
	}
	
	
	
}
