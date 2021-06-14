package genericos;

import java.util.ArrayList;

public class GenericStack <E>{
	
	private ArrayList<E> array= new ArrayList<E>();
	
	
	public int getSize() {
		return array.size();
	}
	
	public E obtenerElemento(int i) {
		return array.get(i);
	}
	
	
	public void addElemento(E elemento) {
		array.add(elemento);
	}
	
	public void removerTodo() {
		array.removeAll(array);
	}
	

	public E remover() {
		
		E o = array.get(array.size()-1);
		array.remove(array.size()-1);
		return o;
	}
	
	
	public boolean isempty() {
		return array.isEmpty();
	}
	
	

}
