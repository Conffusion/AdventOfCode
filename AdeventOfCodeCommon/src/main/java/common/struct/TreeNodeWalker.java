package common.struct;

import java.util.function.BiConsumer;

public class TreeNodeWalker<T extends Comparable<T>,R> {
	private TreeNode<T> root;
	private boolean stop=false;
	private R returnValue;
	
	private BiConsumer<TreeNode<T>,T> nodeHandler;
	
	public TreeNodeWalker(TreeNode<T> root) {
		this.root=root;
	}

	public TreeNodeWalker<T,R> withNodeHandler(BiConsumer<TreeNode<T>, T> nodeHandler) {
		this.nodeHandler = nodeHandler;
		return this;
	}
	public TreeNodeWalker<T,R> withDefaultReturnValue(R value) {
		returnValue=value;
		return this;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	public void setReturnValue(R returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * Walk through the tree In order Left->Parent->Right
	 */
	public R walkLPR(boolean consumeNull,R defaultReturn) {
		setReturnValue(defaultReturn);
		TreeNodeUtils.scanInOrderTLR(root,consumeNull,new StopableNodeHandler());
		return returnValue;
	}
	
	private class StopableNodeHandler implements BiConsumer<TreeNode<T>,T> {

		@Override
		public void accept(TreeNode<T> t, T u) {
			if(!stop)
				nodeHandler.accept(t, u);
		}
		
	}
}
