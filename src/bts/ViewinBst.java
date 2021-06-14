package bts;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class ViewinBst extends JApplet {
	private BinarySerchTree<Integer> numeros= new BinarySerchTree<>();
	private DibujoTree control= new DibujoTree(numeros);
	public ViewinBst() {
		add(control);
	}
}

class DibujoTree extends JPanel{
	private ControlTree panel= new ControlTree();
	
	private BinarySerchTree<Integer> numeros;
	private JLabel eti1=  new JLabel("Ingrese un numero");
	private JTextField campo1= new JTextField(5);
	private JButton btn1= new JButton("Insertar");
	private JButton btn2= new JButton("Borrar");
	private JButton btn3= new JButton("Inorder");
	private  JTextField campo2= new JTextField(28);
	
	public DibujoTree(BinarySerchTree<Integer> numeros) {
		this.numeros=numeros;
		this.setLayout(new BorderLayout());
		JPanel p1= new JPanel(new FlowLayout());
		JPanel p2= new JPanel(new FlowLayout());
		p1.add(eti1);
		p1.add(campo1);
		p1.add(btn1);
		p1.add(btn2);
		p1.add(btn3);
		p2.add(campo2);
		add(panel,BorderLayout.CENTER);
		add(p1,BorderLayout.SOUTH);
		add(p2,BorderLayout.NORTH);
		Listener listener= new Listener();
		btn1.addActionListener(listener);
		btn2.addActionListener(listener);
		btn3.addActionListener(listener);
		
	}
	
	class Listener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent a) {
			
			if(a.getSource()==btn1) {
				Integer  elemento=Integer.parseInt(campo1.getText()) ;
				
				if(numeros.busqueda(elemento)) {
					JOptionPane.showMessageDialog(null,elemento+" Numero ingresado ya existe en el arbol");
				}
				else {
				numeros.insertar(elemento);
				panel.repaint();
				}
					
			}
			
			if(a.getSource()==btn3) {
				StringBuilder hola= new StringBuilder();
				Iterator iterator= numeros.enorden();
				while(iterator.hasNext()) {
					hola.append((Integer)(iterator.next())+" ");
				}
				
				campo2.setText("Inorder: "+ "["+hola.toString()+"]");
				hola.delete(0, hola.capacity());
				
				
			}
			if(a.getSource()==btn2) {
				Integer  elemento=Integer.parseInt(campo1.getText()) ;
				if(numeros.busqueda(elemento)) {
					numeros.borrar(elemento);
					JOptionPane.showMessageDialog(null,elemento+" borrado con exito!");
					panel.repaint();
				}
				else
					JOptionPane.showMessageDialog(null,"No se econtró el elemento");
			}
			
			
		}
		
	}
	
	class ControlTree extends JPanel{
		
		private int radio= 10;
		private int vgap=30;
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if(!numeros.isEmpty()) {
				dibujar(g,getWidth()/2,30,getWidth()/5,numeros.obtenerRoot());
			}
		}
		
		public void dibujar(Graphics g,int x,int y,int hgap,BinarySerchTree.TreeNode root) {
			
			g.drawOval(x-radio, y-radio, 2*radio, 2*radio);
			g.drawString(root.elemento + "", x - 5, y + 4);
		
			if(root.izquierda!=null) {
				
				conectarIzquierda(g,x,y,x-hgap,y+vgap);
				
				
				dibujar(g,x-hgap,y+vgap,hgap/2,root.izquierda);
				
				
			}
			
			if(root.derecha!=null) {
				
				conectarDerecha(g,x,y,x+hgap,y+vgap);
				
				
				dibujar(g,x+hgap,y+vgap,hgap/2,root.derecha);
				
			}
			
		}
		
		public void conectarIzquierda(Graphics g,int x1,int y1,int x2,int y2) {
		
			g.drawLine(x1, y1+radio, x2, y2-radio);
			
		}
		
		public void conectarDerecha(Graphics g,int x1,int y1,int x2,int y2)
		{
			g.drawLine(x1, y1+radio, x2, y2-radio);
		}
		
	}
	
}




