package view.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeNode<T>
		implements Iterable<TreeNode<T>>
{
	T					data;
	TreeNode<T>			parent;
	List<TreeNode<T>>	children;

	public TreeNode( T data )
	{
		this.data = data;
		this.children = new ArrayList<TreeNode<T>>();
	}

	public TreeNode<T> addChild ( T childData )
	{
		TreeNode<T> childNode = new TreeNode<T>( childData );
		childNode.parent = this;
		this.children.add( childNode );
		return childNode;
	}

	public boolean hasChildren ()
	{
		return children.size() > 0;
	}

	@Override
	public Iterator<TreeNode<T>> iterator ()
	{
		return children.iterator();
	}
}
