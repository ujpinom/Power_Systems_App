package bts;

import java.util.Iterator;

public interface Tree<E> extends Comparable {
	
	public boolean busqueda(E e);
	
	public boolean insertar(E e);
	
	
	public boolean borrar(E e);
	
	public void inorder();
	
	public void preorder();
	
	public void postorder();
	
	public int getSize();
	
	public boolean isEmpty();
	
	public Iterator iterator();
	
	public void clear();
	
}
