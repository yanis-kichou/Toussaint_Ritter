package ritter;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;

public class Ritter {
	
	/*
	 * L’algorithme Ritter permet d’obtenir un cercle d’aire minimum contenant un ensemble de points
	 */
	
	public static Circle Ritter(ArrayList<Point> points){
		
		//1. Prendre un point dummy quelconque appartenant a l’ensemble de points de départ
		
		Point dummy = points.get(0);
		double distance =0;
		
		// 2. Parcourir l’ensemble de points pour trouver un point PPP de distance maximum au point dummy.
		Point p = null;
		for (Point point : points) {
			double d = dummy.distance(point);
			if (d >= distance) {
				distance = d;
				p = point;
			}
		}
		
		// 3. Re-parcourir l’ensemble de points pour trouver un point QQQ de distance maximum au point P  
		Point q = null;
		for (Point point: points) {
			double d = p.distance(point);
			if (d >= distance) {
				distance = d;
				q = point;
			}
		}

		Point c, c_prim = null;
		double distanceCP = 0;

		// Coordonnées du centre du segment [PQ]
		int a = (p.x + q.x) / 2;
		int b = (p.y + q.y) / 2;
		
		// c est le centre du segment [PQ]
		c = new Point(a, b);
				
		// Rayon du cercle de centre c, passant par P et Q (distance CP)
		distanceCP = Point.distance(a, b, p.x, p.y);
				
		// On parcourt la liste de points, et on enlève tout point compris dans ce cercle
		ArrayList<Point> listePointDebordant = new ArrayList<Point>();
		for (Point point : points) {
			if (Point.distance(a, b, point.x, point.y) > distanceCP) {
					listePointDebordant.add(point);
			}
		}
		
		// S'il ne reste plus de points dans la liste
		if (listePointDebordant.isEmpty()) {
			// On renvoie le cercle centré en C, et de rayon CP
			return new Circle(c,(int) distanceCP);
		}
				
		// S'il en reste, soit s un de ces points
		Point s = null;
		
		// Tant que la liste n'est pas vide
		while (!listePointDebordant.isEmpty()) {
			
			ArrayList<Point> l1 = new ArrayList<Point>();
			
			// S est un point quelconque dans la liste des points.
			s = listePointDebordant.get(0);
			
			// On l'enlève de la liste.
			listePointDebordant.remove(s);
			
			// 8.Tracer la droite passant par SSS et CCC. Celle-ci coupe le périmètre du cercle courant en deux points : soit T le point le plus éloigné de S.
			double alpha, beta;
				
			// Distance du centre du cercle à S
			double distanceSC = Point.distance(s.x, s.y, a, b);
			
			// T est sur le cercle,
			double distanceST = distanceSC + distanceCP;
			double distanceSC_prim = distanceST/ 2;
			double distanceCC_prim = distanceSC - distanceSC_prim;
			alpha = distanceSC_prim / distanceSC;
			beta = distanceCC_prim/ distanceSC;

			double c_prim_x =beta*s.x + a - beta *a ;
			double c_prim_y = beta *s.y + b - beta*b;
			c_prim = new Point((int)c_prim_x,(int) c_prim_y);
			for (Point point : listePointDebordant) {
				if (Point.distance(c_prim_x, c_prim_y, point.x, point.y) > distanceSC_prim) {
					l1.add(point);
					}
			}
			if (l1.isEmpty()) {
				Circle finale=new Circle(c_prim,(int) distanceSC_prim);
				return finale;
			}
			else {
				listePointDebordant = (ArrayList<Point>) l1.clone();
				a = (int)c_prim_x;
				b = (int)c_prim_y;
				distanceCP = distanceSC_prim;
			}

		}
		Circle finale=new Circle(c_prim,(int) distanceCP);
		return finale;

	}
	public static  double surfaceCircle(Circle c) {
  		return c.getRadius()*c.getRadius()*Math.PI;
  	}
  
}
	
	

