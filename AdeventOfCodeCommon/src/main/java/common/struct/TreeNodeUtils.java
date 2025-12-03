package common.struct;

import java.util.function.BiConsumer;

/**
 * Bevat utility methodes om een Tree te processen 
 */
public class TreeNodeUtils {

	/**
	 * Doorloop de Tree in volgorde: Top-Left-Right
	 * @param <T>
	 * @param root
	 * @param consumeNull als true wordt handleNode ook aangeroepen wanneer de value van de node null is.
	 * @param handleNode 
	 */
	public static <T extends Comparable<T>> void scanInOrderTLR(TreeNode<T> node, boolean consumeNull, BiConsumer<TreeNode<T>,T> handleNode) {
		if(node.getValue()!=null)
			handleNode.accept(node,node.getValue());
		else if (consumeNull)
			handleNode.accept(node,null);
		if(node.getLeft()!=null)
			scanInOrderTLR(node.getLeft(),consumeNull,handleNode);
		if(node.getRight()!=null)
			scanInOrderTLR(node.getRight(),consumeNull,handleNode);
	}
	
	/**
	 * Ga naar het eerste blad aan de linkerzijde.
	 * <pre>
	 *                  A
	 *                 /  \
	 *                B    C
	 *               / \  / \
	 *              D  E  F G
	 * </pre>
	 * <li>Voor A,C,F,G is dit E
	 * <li>Voor B is dit D
	 * <li>Voor G is dit F
	 * @param <T>
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> TreeNode<T> findLeftLeaf(TreeNode<T> node) {
		// ga naar boven tot eerste knoop waar huidige rechts is en er een linkse knoop is
		TreeNode<T> oldnode=node;
		TreeNode<T> temp=node.getParent();
		while(temp!=null&&temp.getLeft()==oldnode) {
			oldnode=temp;
			temp=temp.getParent();
		}
		if(temp==null)
			return null;
		temp=temp.getLeft();
		// zak af steeds rechter node nemen
		while(temp.getRight()!=null)
			temp=temp.getRight();
		return temp;
	}
	
	/**
	 * Ga naar het eerste blad aan de rechterzijde.
	 * <pre>
	 *                  A
	 *                 /  \
	 *                B    C
	 *               / \  / \
	 *              D  E  F G
	 * </pre>
	 * <li>Voor A,B is dit F
	 * <li>Voor D is dit E
	 * <li>Voor C,F is dit G
	 * <li>Voor G is dit null
	 * @param <T>
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> TreeNode<T> findRightLeaf(TreeNode<T> node) {
		// ga naar boven tot eerste knoop waar huidige links is en er een rechtse knoop is
		TreeNode<T> oldnode=node;
		TreeNode<T> temp=node.getParent();
		while(temp!=null&&temp.getRight()==oldnode) {
			oldnode=temp;
			temp=temp.getParent();
		}
		if(temp==null)
			return null;
		temp=temp.getRight();
		// zak af steeds linker node nemen
		while(temp.getLeft()!=null)
			temp=temp.getLeft();
		return temp;
	}	
}
