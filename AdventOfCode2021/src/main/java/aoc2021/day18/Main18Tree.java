package aoc2021.day18;

import java.util.List;
import java.util.function.BiConsumer;

import common.main.AbstractMainMaster;
import common.struct.TreeNode;
import common.struct.TreeNodeUtils;
import common.struct.TreeNodeWalker;

public class Main18Tree extends AbstractMainMaster<Long> {
    private List<TreeNode<Integer>> sfnumbers;

    public static void main(String[] args) {
        new Main18Tree()
 //       .testMode()
        .start();
    }

    @Override
    public void beforeEach() {
        sfnumbers=parseInput(String::toCharArray, this::startParseSFNumber);
    }

    @Override
    public Long star1() {
    	TreeNode<Integer> lroot=sfnumbers.get(0);
        for (int i=1;i<sfnumbers.size();i++) {
        	TreeNode<Integer> root=new TreeNode<>();
        	root.setLevel(0);
        	root.setLeft(lroot);
        	root.setRight(sfnumbers.get(i));
//        	System.out.println("After adding "+i+": "+Main18Tree.toString(root));
        	reduce(root);
//        	System.out.println("After reducing "+i+": "+Main18Tree.toString(root));
        	lroot=root;
        }
        return magnitude(lroot);
    } 
    
    @Override
	public Long star2() {
    	Long highestMagitude=0L;
        for (int i=0;i<sfnumbers.size();i++) {
        	jloop:
            for (int j=0;j<sfnumbers.size();j++) {
            	if(i==j) continue jloop;
            	TreeNode<Integer> root=new TreeNode<>();
            	root.setLeft(sfnumbers.get(i));
            	root.setRight(sfnumbers.get(j));
//            	System.out.println("Checking ("+i+","+j+"):"+root);
            	TreeNode<Integer> temp=root.clone();
            	reduce(temp);
            	Long magnitude=magnitude(temp);
            	if(magnitude>highestMagitude) {
            		highestMagitude=magnitude;
            		System.out.println("New highest ("+magnitude+"):"+root);
            	}
            }
        }
    	return highestMagitude;
	}

	int index=0;
    private TreeNode<Integer> startParseSFNumber(char[] line) {
    	index=0;
    	return parseSFNumber(line);
    }
    private TreeNode<Integer> parseSFNumber(char[] line) {
        TreeNode<Integer> currNode=new TreeNode<>();
    	if(Character.isDigit(line[++index]))
    		currNode.setLeft(new TreeNode<Integer>(line[index]-'0'));
        else {
            currNode.setLeft(parseSFNumber(line));
        }
        // skip comma
    	index++;
        if(Character.isDigit(line[++index]))
        	currNode.setRight(new TreeNode<Integer>(line[index]-'0'));
        else
        	currNode.setRight(parseSFNumber(line));
        // skip closing ]
        index++;
        return currNode;
    }

    private TreeNode<Integer> reduce(TreeNode<Integer> root){
    	while(true) {
	    	// first check on level 5 nodes to explode
	    	TreeNodeWalker<Integer,Boolean> explodeWalker=new TreeNodeWalker<>(root);
	    	explodeWalker.withNodeHandler(new Exploder(explodeWalker));
	    	if(explodeWalker.walkLPR(true,false)) {
//	    		System.out.println("After explode "+Main18Tree.toString(root));
	    		continue;
	    	}
	    	// check on values > 10 to split
	    	TreeNodeWalker<Integer,Boolean> splitWalker=new TreeNodeWalker<>(root);
	    	splitWalker.withNodeHandler(new Splitter(splitWalker));
	    	if(!splitWalker.walkLPR(true,false)) {
//	    		System.out.println("After split "+Main18Tree.toString(root));	    		
	    		break;
	    	}
    	}
    	return root;
    }
    
    private class Exploder implements BiConsumer<TreeNode<Integer>,Integer> {
    	TreeNodeWalker<Integer,Boolean> walker;
    	
		public Exploder(TreeNodeWalker<Integer,Boolean> walker) {
			this.walker = walker;
		}
		
		@Override
		public void accept(TreeNode<Integer> curr, Integer v) {
			if(curr.getLevel()==5) {
				curr=curr.getParent();
//				System.out.println("Level 5 node: "+Main18Tree.toString(curr));
				// process left child number
				TreeNode<Integer> newleft=TreeNodeUtils.findLeftLeaf(curr);
				if(newleft!=null) {
					newleft.setValue(newleft.getValue()+curr.getLeft().getValue());
				}
				// process right child number
				TreeNode<Integer> newright=TreeNodeUtils.findRightLeaf(curr);
				if(newright!=null) {
					newright.setValue(newright.getValue()+curr.getRight().getValue());
				}				
				curr.setValue(0);
				curr.setLeft(null);
				curr.setRight(null);
				walker.setReturnValue(true);
				walker.setStop(true);
			}
		}
    }
    private class Splitter implements BiConsumer<TreeNode<Integer>,Integer> {
    	TreeNodeWalker<Integer,Boolean> walker;

		public Splitter(TreeNodeWalker<Integer, Boolean> walker) {
			this.walker = walker;
		}

		@Override
		public void accept(TreeNode<Integer> curr, Integer v) {
			if(curr.getValue()!=null &&curr.getValue()>=10) {
//				System.out.println("Split needed: "+Main18Tree.toString(curr.getParent()));
				curr.setLeft(new TreeNode<Integer>(curr.getValue()/2));
				curr.setRight(new TreeNode<Integer>(curr.getValue()/2));
				if(curr.getValue()%2==0)
					curr.setRight(new TreeNode<Integer>(curr.getValue()/2));
				else
					curr.setRight(new TreeNode<Integer>(curr.getValue()/2+1));
				curr.setValue(null);
				walker.setReturnValue(true);
				walker.setStop(true);
			}
		}
    }
    
    private long magnitude(TreeNode<Integer> node) {
    	if(node.getValue()!=null)
    		return node.getValue();
    	if(node.getLeft()==null||node.getRight()==null)
    		throw new IllegalArgumentException("tree is corrupt at node "+node);
    	return 3*magnitude(node.getLeft())+2*magnitude(node.getRight());
    }
    
    private static String toString(TreeNode<Integer> node) {
    	if(node.getValue()!=null)
    		return Integer.toString(node.getValue());
    	StringBuilder buf=new StringBuilder();
    	buf.append("[");
    	if(node.getLeft()!=null) 
        	buf.append(toString(node.getLeft()));
    	
    	buf.append(",");
    	if(node.getRight()!=null)
    		buf.append(toString(node.getRight()));
    	buf.append("]");
    	return buf.toString();
    }
}
