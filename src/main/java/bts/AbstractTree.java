package bts;

import java.util.Iterator;

public abstract class AbstractTree<E extends Comparable<E>>  implements Tree<E> {
	
	public void inorder() {
		
	}
	
	public void preorder() {
		
	}
	
	public void postorder() {
		
	}
	
	public Iterator iterator()
	{
		return null;
	}
	
	public boolean isEmpty() {
		boolean bandera= false;
		if(getSize()==0) {
			bandera= true;
		}
		
		return bandera;
	}
}
