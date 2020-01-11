package test;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.lang.model.element.NestingKind;

import Toussaint.Toussaint;
import algorithms.DefaultTeam;
import ritter.Ritter;
import supportGUI.Circle;

public class Test {

	public static ArrayList<Point> read_file(File file) {
		ArrayList<Point> points = new ArrayList<Point>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			try {
				while ((line = br.readLine()) != null) {
					String[] list = line.split(" ");
					points.add(new Point(Integer.parseInt(list[0]), Integer.parseInt(list[1])));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return points;
	}
	
	public static void main(String[] args) {
		
		//fichier contenant les  resultat de qualiter des algorithme toussaint et ritter 
		String toussaint="/resultats/tousaint.txt";
		String ritter="/resultats/ritter.txt";
		
		// bufferedReader permettant de recuprer les fichier de test   
		BufferedReader out=null; 
		
		// liste de fichier de test 
		ArrayList<String > files =new ArrayList<String>();
		
		
		// recuperations des fichier de test 
		try {
			
			out=new BufferedReader(new FileReader(new File("files.txt")));
			while(out.ready()) {
				files.add(out.readLine());
			}
			
		}catch (IOException  e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// fichier contenant les resultat de la qualiter de l'algorithme toussaint 
		File toussaintQualiter = new File(toussaint);
		
		// fichier contenant les resultat de la qualiter de l'algorithle ritter
		File ritterQualiter = new File(ritter);
		
		// instance de la classe DefaultTeam pour recupere les algorithme de calcule de l'aire du polygone 
		DefaultTeam d1 =new DefaultTeam();
		
		// instance de la classe toussaint pour acceder a l'algorithme 
		Toussaint t =new Toussaint();
		
		// instance de la calsse ritter pour acceder a l'algorithme ritter 
		Ritter r =new Ritter();
		
		BufferedWriter inRitter=null;
		BufferedWriter intoussaint=null;
		
		try {
		for (String s : files ) {
			
			// poins a tester 
			ArrayList<Point> points=read_file(new File("/asserts/Varoumas_benchmark/samples/"+s));
			
			// appel au algorithmes 
			double aire_rectangle =t.aire_rectangle(points);
			
			// aire du cercle 
			double aire_cercle=r.surfaceCircle(Ritter.Ritter(points));
			
			// aire du polygone 
			double aire_polygone = d1.aire_polygone(points);
			
			//calcule de la qualiter de toussaint 
			double qualiter_toussaint = aire_rectangle/aire_polygone -100;
			
			// calcule de la qualiter de ritter
			double qualiter_ritter = aire_cercle/aire_polygone -100;
			
			//nombre de point de l'experimentation
			int nbPoints=points.size();
			
			intoussaint=new BufferedWriter(new FileWriter(toussaintQualiter,false));
			inRitter=new BufferedWriter(new FileWriter(ritterQualiter,false));
		
			//enregistrement des resultat dans le fichier qualiterToussaint 
			intoussaint.append(nbPoints+" "+qualiter_toussaint+"\n");
			
			//enregistrement des resultat dans le fichier qualiterRitter
			inRitter.append(nbPoints+" "+qualiter_ritter+"\n");
			
			
			}
		}catch(IOException e ) {
			e.printStackTrace();	
		}finally {
			if(inRitter!=null && intoussaint!=null) {
				try {
					inRitter.close();
					intoussaint.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
