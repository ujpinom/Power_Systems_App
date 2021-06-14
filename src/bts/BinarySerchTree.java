package bts;
import java.util.*;
public class BinarySerchTree<E extends Comparable<E>> extends  AbstractTree<E> {
	private int c1;
	private int c2;
	protected TreeNode<E> root;
	protected int size;
	
	public BinarySerchTree() {
		
	}
	

	public BinarySerchTree(E[] o) {
		for(int i=0;i<o.length;i++) {
			insertar(o[i]);
		}
	}
	
	public TreeNode<E> obtenerRoot(){
		return root;
	}
	
	
	@Override
	public boolean busqueda(E e) {
		
		TreeNode<E> actual= root;
		
	while(actual!=null) {
		
		if(e.compareTo(actual.elemento)>0) {
			
			actual=actual.derecha;
		}
		
		else if(e.compareTo(actual.elemento)<0) {
			actual=actual.izquierda;
		}
		else {
			return true;
		}
	}
		
		return false;
	}

	@Override
	public boolean insertar(E e) {
		
		if(root==null) {
			root=crearNuevoNodo(e);
			
		}
		
		else {
			
			
			TreeNode<E> padre=null;
			TreeNode<E> actual= root;
		
			while(actual!=null) {
				
				if(e.compareTo(actual.elemento)<0) {
					padre=actual;
					actual=actual.izquierda;
				}
				
				else if(e.compareTo(actual.elemento)>0) {
					padre=actual;
					actual=actual.derecha;
				}
				else {
					return false; // no se puede crear el nuevo nodo porque el valor ya es ingual a un nodo existente
				}
			}
			
			if(e.compareTo(padre.elemento)>0) {
				padre.derecha=crearNuevoNodo(e);
				
			}
			else
				padre.izquierda=crearNuevoNodo(e);
			
		}
	
		++size;
		
		
		return true;
	}
	
	
	public TreeNode<E> crearNuevoNodo(E e){
		return new TreeNode<E>(e);
	}
	
	public void inorder() {
		inorder(root);
	}
	
	public void inorder(TreeNode<E> root) {
		
		if(root==null)return;
		
		else {
			inorder(root.izquierda);
			System.out.print(root.elemento+" ");
			inorder(root.derecha);
		}
		
	}
	
	public void postorder() {
		postorder(root);
	}
	
	public void preorder(TreeNode<E> root) {
		
		if(root==null) {
			return;
		}
		else {
			System.out.println(root.elemento+" ");
			postorder(root.izquierda);
			postorder(root.derecha);
		}
		
	}
	

	public void preorder() {
		preorder(root);
	}
	
	public void postorder(TreeNode<E> r) {
		if(r==null)return;
		postorder(r.izquierda);
		postorder(r.derecha);
		System.out.println(r.elemento+" ");
		
	}
	
	public void breadthoirder() {
		breadthoirder(root);
	}
	public void breadthoirder(TreeNode<E>root) {
	 ArrayList<TreeNode<E>> lista= new ArrayList<>();
	ArrayList<E> lista2= new ArrayList<>();
		
		if(root==null) {
			return;
		}
		else {
			int nivel=0;
			int nivel1=0;
			lista.add(root);
			lista2.add(root.elemento);
			
			while(lista.size()<size) {
				
				
				for(int i=0;i<lista.size();i++) {
				
					if(lista2.contains(lista.get(i).izquierda)||lista2.contains(lista.get(i).derecha))
						continue;
					else {
					
					if(lista.get(i).izquierda!=null) {
						nivel++;
					lista.add(lista.get(i).izquierda);
					lista2.add(lista.get(i).izquierda.elemento);}
					
					if(lista.get(i).derecha!=null) {
						nivel1++;
					lista.add(lista.get(i).derecha);
					lista2.add(lista.get(i).derecha.elemento);}
					}
				
				}
				
			}
			
		}
		
		for(int i=0;i<lista.size();i++) {
			System.out.print(lista.get(i).elemento+" ");
		}
		
		

	}
	
	public int numeroHojas() {
		int contador=0;
		
		if(root==null) {
			return 0;
		}
		else {
			
			ArrayList<TreeNode<E>> holaperro= holagonorrea();
			
			for(int i=0;i<holaperro.size();i++) {
				
				if(holaperro.get(i).izquierda==null && holaperro.get(i).derecha==null) {
					++contador;
				}
				else
					continue;
			
			}
		}
		return contador;
	}
	
	public int altura() {
	
		return helper(root);
	
	}
	
	public int helper(TreeNode<E> root) {
		
		if(root==null) {
			return 0;
		}
		else
			return 1+Math.max(helper(root.izquierda), helper(root.derecha));
	}
	
	public boolean esArbolPerfecto() {
		int altura= altura();
		
		if(Math.pow(2,altura)-1==size) {
			return true;
		}
		else
			return false;
	}
	public int getNumberofNonLeaves() {
		return size-numeroHojas();
	}
	
	public ArrayList<TreeNode<E>> holagonorrea(){
		
		ArrayList<TreeNode<E>> listaa= new ArrayList<>();
		ArrayList<E> lista2= new ArrayList<>();
	
			int nivel=0;
			int nivel1=0;
			listaa.add(root);
			lista2.add(root.elemento);
			
			while(listaa.size()<size) {
				
				
				for(int i=0;i<listaa.size();i++) {
				
					if(lista2.contains(listaa.get(i).izquierda)||lista2.contains(listaa.get(i).derecha))
						continue;
					else {
					
					if(listaa.get(i).izquierda!=null) {
						nivel++;
					listaa.add(listaa.get(i).izquierda);
					lista2.add(listaa.get(i).izquierda.elemento);}
					
					if(listaa.get(i).derecha!=null) {
						nivel1++;
					listaa.add(listaa.get(i).derecha);
					lista2.add(listaa.get(i).derecha.elemento);}
					}
				
				}
				
			}
		
		return listaa;
		
	}
	


	
	public ArrayList<TreeNode<E>> trayectoria(E e){
		ArrayList<TreeNode<E>> lista= new ArrayList<>();TreeNode<E> actual=root;
		
		while(actual!=null) {
			lista.add(actual);
		
			if(e.compareTo(actual.elemento)<0) actual=actual.izquierda;
			
			else if(e.compareTo(actual.elemento)>0)actual=actual.derecha;
			else break;
		}
		
		return lista;
	}
	
	@Override
	public boolean borrar(E e) {
		
		TreeNode<E> actual=root;
		TreeNode<E> padre=null;
		
		while(actual!=null) {
			
			if(e.compareTo(actual.elemento)<0) {
				padre=actual;
				actual=actual.izquierda;
			}
			else if(e.compareTo(actual.elemento)>0) {
				padre=actual;
				actual=actual.derecha;
			}
			else break;
			
		}
		
		if(actual==null) {
			return false;
		}
		
		if(actual.izquierda==null) {
			
			if(padre==null) {
				root=actual.derecha;
			}
			else {
				
				if(e.compareTo(padre.elemento)<0) padre.izquierda=actual.derecha;
					
				else padre.derecha=actual.derecha;
			}
		}
		
		else {
			
			TreeNode<E> prm=actual;
			TreeNode<E> rm=actual.izquierda;
			
			while(rm.derecha!=null) {
				prm=rm;
				rm=prm.derecha;
			}
		
			actual.elemento=rm.elemento;
			
				if(prm.derecha==rm) {
					prm.derecha=rm.izquierda;
				}
				else {
					prm.izquierda=rm.izquierda;
				}
		}
		--size;
		
		return true;
	}
	
	public void remover(E e) {
		borrar(e);
		
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public void clear() {
		root=null;
		size=0;
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}
	
	
	public Iterator enorden() {
		return new Enorden();
	}
	
	public Iterator breadthoirderr() {
		return new Breadthoirder();
	}
	
	
	
	
	
	
	class Breadthoirder implements Iterator{
		private  ArrayList<TreeNode<E>> listaa= new ArrayList<>();
		private ArrayList<E> lista2= new ArrayList<>();
		private int actual=0;
		public Breadthoirder(){
			breadthoirder(root);
			
		}
		public void breadthoirder(TreeNode<E>root) {
				if(root==null) {
					return;
				}
				else {
					int nivel=0;
					int nivel1=0;
					listaa.add(root);
					lista2.add(root.elemento);
					
					while(listaa.size()<size) {
						
						
						for(int i=0;i<listaa.size();i++) {
						
							if(lista2.contains(listaa.get(i).izquierda)||lista2.contains(listaa.get(i).derecha))
								continue;
							else {
							
							if(listaa.get(i).izquierda!=null) {
								nivel++;
							listaa.add(listaa.get(i).izquierda);
							lista2.add(listaa.get(i).izquierda.elemento);}
							
							if(listaa.get(i).derecha!=null) {
								nivel1++;
							listaa.add(listaa.get(i).derecha);
							lista2.add(listaa.get(i).derecha.elemento);}
							}
						
						}
						
					}
					
				}
		}
		
		
		@Override
		public boolean hasNext() {
			
			if(actual<lista2.size()) {
				return true;
			}
			
			return false;
		}

		@Override
		public Object next() {
			// TODO Auto-generated method stub
			return lista2.get(actual++);
		}
		
	}
	
	
	class Enorden implements Iterator{
		
		private ArrayList<E> lista= new ArrayList<>();
		private int actual=0;
		
		public Enorden() {
			
			enorden(root);
			
		}
		
		public void enorden(TreeNode<E> root) {
			
			if(root==null)return;
			enorden(root.izquierda);
			lista.add(root.elemento);
			enorden(root.derecha);
			
		}
		

		
		
		public void Borrar() {
			borrar(lista.get(actual));
			lista.clear();
			enorden(root);
			
		}
		
		
		@Override
		public boolean hasNext() {
			
			if(actual<lista.size()) {
				return true;
			}
			return false;
		}

		@Override
		public Object next() {
			// TODO Auto-generated method stub
			return lista.get(actual++);
		}
		
	}
	
	class TreeNode<E extends Comparable<E>>{
		
		E elemento;
		
		TreeNode<E> derecha;
		TreeNode<E> izquierda;
		
		public TreeNode(E e) {
			elemento=e;
		}
	}

}
