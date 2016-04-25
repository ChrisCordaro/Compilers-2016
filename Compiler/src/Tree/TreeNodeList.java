package Tree;

import java.util.ArrayList;

import Lexer.Lexer;

public class TreeNodeList {
	private TreeNode root;
	private boolean rootSet;
	private TreeNode currItem;

	public TreeNodeList() {
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

	public void addRootNode(String data) {
		TreeNode rootNode = new TreeNode(data);
		// root = rootNode;
		// root.setData(data);
		// currItem = rootNode;
		this.root = rootNode;
		this.currItem = rootNode;
		// setRoot(rootNode);
		// setCurrItem(rootNode);
		if (Lexer.getVerbose()) {
			System.out.println("Create root node of " + data);
			System.out.println("Here's your root " + root.getData());
		}
	}

	public void addASTRootNode(String data) {
		TreeNode rootNode = new TreeNode(data);
		// root = rootNode;
		// root.setData(data);
		// currItem = rootNode;
		this.root = rootNode;
		this.currItem = rootNode;
		// setRoot(rootNode);
		// setCurrItem(rootNode);
		if (Lexer.getVerbose()) {
			System.out.println("Create root node of " + data);
			System.out.println("Here's your root " + root.getData());
		}
	}

	public void addBranchNode(String data) {
		// check if root
		// if not make child node
		// assign our parent
		// put ourselves in parent.children

		TreeNode node = new TreeNode(data);
		// TreeNode temp = currItem;

		// node.setParent(temp);
		// currItem = node;

		node.setParent(this.currItem);
		this.currItem.addChildren(node);
		this.setCurrItem(node);
		// node.getParent().getChildren().add(node);
		if (Lexer.getVerbose()) {
			System.out.println("Creating BRANCH of " + data);
			System.out.println("His parent is " + node.getParent().getData());
			// System.out.println("CurrItem Test " + currItem.getData());
			// System.out.println("ROOT TEST " + root.getData());
		}

	}

	public void addASTBranchNode(String data) {
		// check if root
		// if not make child node
		// assign our parent
		// put ourselves in parent.children

		TreeNode node = new TreeNode(data);
		// TreeNode temp = currItem;

		// node.setParent(temp);
		// currItem = node;

		node.setParent(this.currItem);
		this.currItem.addChildren(node);
		this.setCurrItem(node);
		// node.getParent().getChildren().add(node);
		// System.out.println("Creating BRANCH of " + data);
		// System.out.println("His parent is " + node.getParent().getData());
		// System.out.println("CurrItem Test " + currItem.getData());
		// System.out.println("ROOT TEST " + root.getData());

	}

	public void addLeafNode(String data) {
		TreeNode node = new TreeNode(data);
		// TreeNode temp = currItem;
		node.setParent(this.currItem);
		this.currItem.addChildren(node);
		this.setCurrItem(node);
		// this.setCurrItem(node);

		// currItem.getParent().getChildren().add(node);

	}

	public void climb() {
		if (this.currItem != null) {
			this.currItem = this.currItem.getParent();
		}
	}

	public void rootChildren() {
		if (Lexer.getVerbose()) {
			ArrayList<TreeNode> t = root.getChildren();
			for (TreeNode x : t)
				System.out.println(x.getData());
		}
	}

	public void blockChildren() {
		if (Lexer.getVerbose()) {
			ArrayList<TreeNode> t = root.getChildren().get(0).getChildren();
			for (TreeNode x : t) {
				System.out.println(x.getData());
			}
		}
	}

	// In parser this is called and takes in the children of the root node
	// The children of the root node are then printed out
	public void printChildren(ArrayList<TreeNode> t) {
		for (TreeNode x : t) {
			if (Lexer.getVerbose()) {
				System.out.println("");
				System.out.println("Current node being analyzed: " + x.getData());
				System.out.println("The parent of " + x.getData() + " is as follows: " + x.getParent().getData());
				printChildren(x.getChildren());
			}
		}
	}

	public void printNodes() {
		// print out every node and its children
		// this returns block root will ever only have block as its child
		if (Lexer.getVerbose()) {
			ArrayList<TreeNode> t = root.getChildren();
		}
		/*
		 * for(TreeNode x : t){ System.out.println(x.getData());
		 * if(x.getChildren().size() > 0){
		 * System.out.println(x.getChildren().get(0).getData());
		 * 
		 * 
		 * }
		 * 
		 * }
		 */

	}

}
