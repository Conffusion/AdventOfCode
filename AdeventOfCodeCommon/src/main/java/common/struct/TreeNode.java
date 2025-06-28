package common.struct;

public class TreeNode<T extends Comparable<T>> {
	private TreeNode<T> parent;
	private TreeNode<T> left, right;
	private T value;
	private int level=0;
	
	public TreeNode() {
	}
	
	public TreeNode(T value) {
		setValue(value);
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public TreeNode<T> getLeft() {
		return left;
	}

	public void setLeft(TreeNode<T> left) {
		this.left=left;
		if(left!=null) {
			left.parent=this;
			left.setLevel(level+1);
		}
	}

	public TreeNode<T> getRight() {
		return right;
	}

	public void setRight(TreeNode<T> right) {
		this.right=right;
		if(right!=null) {
			right.parent=this;
			right.setLevel(level+1);
		}
	}

	public TreeNode<T> getParent() {
		return parent;
	}

	public int getLevel() {
		return level;
	}
	/**
	 * Change the level of this node.
	 * All child nodes are updated accordingly
	 * @param level
	 */
	public void setLevel(int level) {
		this.level=level;
		if(left!=null)
			left.setLevel(level+1);
		if(right!=null)
			right.setLevel(level+1);
	}
}
