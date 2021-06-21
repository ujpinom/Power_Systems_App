package proyectoSistemasDePotencia;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class DibujarCarga {

	static Line lineacarga;
	
	
	public static List<Node> dibujarCargaBanco(Object objecto,String tipo){

		
			if(tipo.equals("C")) {
				Carga banco=((Carga)objecto);
				return dibujarCarga(banco);
			}
			
			else if(tipo.equals("B")){
				Bancos banco=((Bancos)objecto);
				return dibujarBanco(banco);
			}
			
			else {
				CompensadorEstatico compensador= ((CompensadorEstatico)objecto);
				return dibujarCompensador(compensador);
			}

	}
	
	
	public static List<Node> dibujarCompensador(CompensadorEstatico compensador){
		
		List<Node> lineas= new ArrayList<>();
		
		
		if(compensador.getOrientacion().equals(CompensadorEstatico.LEFT)) {
			
			double x1=compensador.getBarra().getCoordenadaCompensador().getX();
			double y1=compensador.getBarra().getCoordenadaCompensador().getY();
			double x2=compensador.getBarra().getCoordenadaCompensador().getX()-10;
			double y2=compensador.getBarra().getCoordenadaCompensador().getY();
			
			lineacarga=new Line(x1,y1,
					x2,y2);
			

			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			
			Circle cirg = new Circle();
			cirg.setRadius(15.5);
			cirg.setCenterX(lineacarga.getEndX()-15);
			cirg.setCenterY(lineacarga.getEndY());
			cirg.setFill(Color.WHITE);
			cirg.setStroke(Color.BLACK);
			
			compensador.setCiculoCompensador(cirg);
			
			Text nombreCompensador= new Text();
			nombreCompensador.setText(compensador.getNombreCompensador());
			nombreCompensador.setFill(Color.BLUE);
			nombreCompensador.setX(cirg.getCenterX()-5);
			nombreCompensador.setY(cirg.getCenterY()+5);
			
			lineas.add(cirg);lineas.add(lineacarga);lineas.add(c1);lineas.add(nombreCompensador);
		}
		
		else if(compensador.getOrientacion().equals(CompensadorEstatico.RIGHT)) {
			
			double x1=compensador.getBarra().getCoordenadaCompensador().getX();
			double y1=compensador.getBarra().getCoordenadaCompensador().getY();
			double x2=compensador.getBarra().getCoordenadaCompensador().getX()+10;
			double y2=compensador.getBarra().getCoordenadaCompensador().getY();
			
			lineacarga=new Line(x1,y1,
					x2,y2);
			

			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			
			Circle cirg = new Circle();
			cirg.setRadius(15.5);
			cirg.setCenterX(lineacarga.getEndX()+15);
			cirg.setCenterY(lineacarga.getEndY());
			cirg.setFill(Color.WHITE);
			cirg.setStroke(Color.BLACK);
			
			compensador.setCiculoCompensador(cirg);
			
			Text nombreCompensador= new Text();
			nombreCompensador.setText(compensador.getNombreCompensador());
			nombreCompensador.setFill(Color.BLUE);
			nombreCompensador.setX(cirg.getCenterX()-5);
			nombreCompensador.setY(cirg.getCenterY()+5);
			
			lineas.add(cirg);lineas.add(lineacarga);lineas.add(c1);lineas.add(nombreCompensador);
	
			
		}
		
		
		
		return lineas;
		
		
	}
	
	
	public static List<Node> dibujarBanco(Bancos banco){
		
		List<Node> lineas= new ArrayList<>();
		
		if(banco.getOrientacion().equals(Bancos.LEFT)) {
			double x1=banco.getBarra().getCoordenadasBanco().getX();
			double y1=banco.getBarra().getCoordenadasBanco().getY();
			double x2=banco.getBarra().getCoordenadasBanco().getX()-50;
			double y2=banco.getBarra().getCoordenadasBanco().getY();

			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			Line line=new Line(lineacarga.getEndX(),lineacarga.getEndY()+10,lineacarga.getEndX(),lineacarga.getEndY()-10);
			line.setStrokeWidth(4);
			lineacarga.setStrokeWidth(4);
			lineas.add(line);lineas.add(lineacarga);lineas.add(c1);
		
		}
		
		else if(banco.getOrientacion().equals(Bancos.RIGHT)) {
			
			double x1=banco.getBarra().getCoordenadasBanco().getX();
			double y1=banco.getBarra().getCoordenadasBanco().getY();
			double x2=banco.getBarra().getCoordenadasBanco().getX()+50;
			double y2=banco.getBarra().getCoordenadasBanco().getY();

			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			
			Line line=new Line(lineacarga.getEndX(),lineacarga.getEndY()+10,lineacarga.getEndX(),lineacarga.getEndY()-10);
			line.setStrokeWidth(4);
			lineacarga.setStrokeWidth(4);
			
			lineas.add(line);lineas.add(lineacarga);lineas.add(c1);
		}
		
		
		else if(banco.getOrientacion().equals(Bancos.ABAJO)) {
			
			double x1=banco.getBarra().getCoordenadasBanco().getX();
			double y1=banco.getBarra().getCoordenadasBanco().getY();
			double x2=banco.getBarra().getCoordenadasBanco().getX();
			double y2=banco.getBarra().getCoordenadasBanco().getY()+50;

			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			
			Line line=new Line(x2-10,y2,x2+10,y2);
			line.setStrokeWidth(4);
			lineacarga.setStrokeWidth(4);
			lineas.add(line);lineas.add(lineacarga);lineas.add(c1);
		}
		else if(banco.getOrientacion().equals(Bancos.ARRIBA)) {
			
			double x1=banco.getBarra().getCoordenadasBanco().getX();
			double y1=banco.getBarra().getCoordenadasBanco().getY();
			double x2=banco.getBarra().getCoordenadasBanco().getX();
			double y2=banco.getBarra().getCoordenadasBanco().getY()-50;

			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			
			Line line=new Line(x2-10,y2,x2+10,y2);
			line.setStrokeWidth(4);
			lineacarga.setStrokeWidth(4);
			lineas.add(line);lineas.add(lineacarga);lineas.add(c1);
		}
		

		return lineas;
		
	}
	
public 	static List<Node> dibujarCarga(Carga banco){
		
		List<Node> lineas=new ArrayList<Node>();
		
		if(banco.getOrientacion().equals(Carga.RIGHT)) {
			
			double x1=banco.getBarra().getCoordenadasCarga().getX();
			double y1=banco.getBarra().getCoordenadasCarga().getY();
			double x2=banco.getBarra().getCoordenadasCarga().getX()+50;
			double y2=banco.getBarra().getCoordenadasCarga().getY();
			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			
			double pendiente=((((double) y1) - (double) y2))
				      / (((double) x1) - (((double) x2)));
			
			  double arctan = Math.atan(pendiente);

			    double set45 = 1.57 / 2;
			    
			    if (x1 < x2) {
			      set45 = -1.57 * 1.5;
			    }

			    int arrlen = 15;

			    Line line1=new Line(x2, y2, (x2 + (Math.cos(arctan + set45) * arrlen)),
			      ((y2)) + (Math.sin(arctan + set45) * arrlen));

			    Line line2=(new Line(x2, y2, (x2 + (Math.cos(arctan - set45) * arrlen)),
			      ((y2)) + (Math.sin(arctan - set45) * arrlen)));
			
			lineacarga.setStrokeWidth(4);
			
			lineas.add(lineacarga);lineas.add(line1);lineas.add(line2);lineas.add(c1);
			
		}
		
		
		else if(banco.getOrientacion().equals(Carga.ABAJO)) {
			
			double x1=banco.getBarra().getCoordenadasCarga().getX();
			double y1=banco.getBarra().getCoordenadasCarga().getY();
			double x2=banco.getBarra().getCoordenadasCarga().getX();
			double y2=banco.getBarra().getCoordenadasCarga().getY()+50;
			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			double pendiente=((((double) y1) - (double) y2))
				      / (((double) x1) - (((double) x2)));
			
			  double arctan = Math.atan(pendiente);

			    double set45 = 1.57 / 2;
			    
			    if (x1 < x2) {
			      set45 = -1.57 * 1.5;
			    }

			    int arrlen = 15;

			    Line line1=new Line(x2, y2, (x2 + (Math.cos(arctan + set45) * arrlen)),
			      ((y2)) + (Math.sin(arctan + set45) * arrlen));

			    Line line2=(new Line(x2, y2, (x2 + (Math.cos(arctan - set45) * arrlen)),
			      ((y2)) + (Math.sin(arctan - set45) * arrlen)));
			
			lineacarga.setStrokeWidth(4);
			
			lineas.add(lineacarga);lineas.add(line1);lineas.add(line2);lineas.add(c1);
			
			
		}
		
		else if(banco.getOrientacion().equals(Carga.ARRIBA)) {
			
			double x1=banco.getBarra().getCoordenadasCarga().getX();
			double y1=banco.getBarra().getCoordenadasCarga().getY();
			double x2=banco.getBarra().getCoordenadasCarga().getX();
			double y2=banco.getBarra().getCoordenadasCarga().getY()-50;
			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			double pendiente=((((double) y1) - (double) y2))
				      / (((double) x1) - (((double) x2)));
			
			  double arctan = Math.atan(pendiente);

			    double set45 = 1.57 / 2;
			    
			    if (x1 < x2) {
			      set45 = -1.57 * 1.5;
			    }

			    int arrlen = 15;

			    Line line1=new Line(x2, y2, (x2 + (Math.cos(arctan + set45) * arrlen)),
			      ((y2)) + (Math.sin(arctan + set45) * arrlen));

			    Line line2=(new Line(x2, y2, (x2 + (Math.cos(arctan - set45) * arrlen)),
			      ((y2)) + (Math.sin(arctan - set45) * arrlen)));
			
			lineacarga.setStrokeWidth(4);
			
			lineas.add(lineacarga);lineas.add(line1);lineas.add(line2);lineas.add(c1);
			
			
			
		}
		
		else if(banco.getOrientacion().equals(Carga.LEFT)) {
			
			double x1=banco.getBarra().getCoordenadasCarga().getX();
			double y1=banco.getBarra().getCoordenadasCarga().getY();
			double x2=banco.getBarra().getCoordenadasCarga().getX()-50;
			double y2=banco.getBarra().getCoordenadasCarga().getY();
			
			lineacarga=new Line(x1,y1,
					x2,y2);
			
			double xMedio=(x1+x2)/2;
			double yMedio=(y1+y2)/2;
			banco.setPuntoMedio(new Point2D(xMedio,yMedio));
			Circle c1= new Circle();
			c1.setRadius(5);
			c1.setCenterX(x1);
			c1.setCenterY(y1);
			c1.setFill(Color.RED);
			double pendiente=((((double) y1) - (double) y2))
				      / (((double) x1) - (((double) x2)));
			
			  double arctan = Math.atan(pendiente);

			    double set45 = 1.57 / 2;
			    
			    if (x1 < x2) {
			      set45 = -1.57 * 1.5;
			    }

			    int arrlen = 15;

			    Line line1=new Line(x2, y2, (x2 + (Math.cos(arctan + set45) * arrlen)),
					      ((y2)) + (Math.sin(arctan + set45) * arrlen));

				Line line2=(new Line(x2, y2, (x2 + (Math.cos(arctan - set45) * arrlen)),
					      ((y2)) + (Math.sin(arctan - set45) * arrlen)));
			
			lineacarga.setStrokeWidth(4);
		
			
			lineas.add(lineacarga);lineas.add(line1);lineas.add(line2);lineas.add(c1);
			
		}
		
		return lineas;
		
	}
	
	public static Text nombreCargaBanco(Object objecto,String tipo) {
		
		if(tipo.equals("C"))
		{
			
			Carga banco=((Carga)objecto);
			
			
			Text nombreC = null;
			
			if(banco.getOrientacion().equals(Carga.RIGHT)) {
				
			nombreC= new Text(banco.getNombreCarga());
			nombreC.setFill(Color.BLACK);
			nombreC.setX(lineacarga.getEndX()+5);
			nombreC.setY(lineacarga.getEndY());
			
		
			}
			
			else if(banco.getOrientacion().equals(Carga.LEFT)) {
				
				nombreC= new Text(banco.getNombreCarga());
				nombreC.setFill(Color.BLACK);
				nombreC.setX(lineacarga.getEndX()-15);
				nombreC.setY(lineacarga.getEndY());
				
			}
			
			else if(banco.getOrientacion().equals(Carga.ABAJO)) {
				
				nombreC= new Text(banco.getNombreCarga());
				nombreC.setFill(Color.BLACK);
				nombreC.setX(lineacarga.getEndX());
				nombreC.setY(lineacarga.getEndY()+15);
			}
			
			else if(banco.getOrientacion().equals(Carga.ARRIBA)) {
				
				nombreC= new Text(banco.getNombreCarga());
				nombreC.setFill(Color.BLACK);
				nombreC.setX(lineacarga.getEndX()-2);
				nombreC.setY(lineacarga.getEndY()-8);
			}
			
			return nombreC;
			
			
		}
		else {
			
			Bancos banco=((Bancos)objecto);
			
			Text nombreC = null;
			
			if(banco.getOrientacion().equals(Carga.RIGHT)) {
				
			nombreC= new Text(banco.getNombreCarga());
			nombreC.setFill(Color.BLACK);
			nombreC.setX(lineacarga.getEndX()+5);
			nombreC.setY(lineacarga.getEndY());
			
		
			}
			
			else if(banco.getOrientacion().equals(Carga.LEFT)) {
				
				nombreC= new Text(banco.getNombreCarga());
				nombreC.setFill(Color.BLACK);
				nombreC.setX(lineacarga.getEndX()-30);
				nombreC.setY(lineacarga.getEndY());
				
			}
			
			else if(banco.getOrientacion().equals(Carga.ABAJO)) {
				
				nombreC= new Text(banco.getNombreCarga());
				nombreC.setFill(Color.BLACK);
				nombreC.setX(lineacarga.getEndX());
				nombreC.setY(lineacarga.getEndY()+15);
			}
			
			else if(banco.getOrientacion().equals(Carga.ARRIBA)) {
				
				nombreC= new Text(banco.getNombreCarga());
				nombreC.setFill(Color.BLACK);
				nombreC.setX(lineacarga.getEndX()-2);
				nombreC.setY(lineacarga.getEndY()-8);
			}
			
			return nombreC;

		}
		
	}
	

}
