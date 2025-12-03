package common.struct;

/**
 * Een TreeNode is een enkel element van een Tree data structuur.
 * Er bestaat geen type Tree. Van een TreeNode kan er omhoog en omlaag genavigeerd worden. 
 * Elke node kan max 2 childs hebben
 * @param <T>
 */
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

	@Override
	public String toString() {
		return value!=null?value.toString(): "[" + left + "," + right+"]";
	}
	public TreeNode<T> clone() {
		TreeNode<T> result=new TreeNode<>();
		if(value!=null)
			result.value=value;
		if(left!=null)
			result.setLeft(left.clone());
		if(right!=null)
			result.setRight(right.clone());
		return result;
	}
}
