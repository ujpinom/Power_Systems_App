package genericos;

public class PruebaGenericStack {

	public static void main(String[] args) {
		
		GenericStack<String> saludos= new GenericStack<>();
		GenericStack<Integer> numeros= new GenericStack<>();
		
		saludos.addElemento("Hola");
		saludos.addElemento("yo");
		saludos.addElemento("hey!");
		saludos.addElemento("que mas");
		saludos.addElemento("Bien o que?");
		
		numeros.addElemento(1);numeros.addElemento(10);numeros.addElemento(100);numeros.addElemento(1000);numeros.addElemento(10000);
		
		
		for(int i=0;i<saludos.getSize();i++) {
			System.out.println(saludos.obtenerElemento(i));
		}
		for(int i=0;i<numeros.getSize();i++) {
			System.out.println(numeros.obtenerElemento(i));
		}
		
	}

}
