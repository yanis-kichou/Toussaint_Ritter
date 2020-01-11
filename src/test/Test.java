package test;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Toussaint.Toussaint;
import algorithms.DefaultTeam;
import supportGUI.Circle;

public class Test {

	public ArrayList<Point> read_file(File file) {
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
	public void test_file(String path) {
		DefaultTeam d =new DefaultTeam();
		File file = new File(path);
		ArrayList<Point> points = read_file(file);
		ArrayList<Point> envconv=d.enveloppeConvexe(points);
		ArrayList<Point> rectmin=Toussaint.toussaint(points);
		Circle cmin=d.calculCercleMin(points);
	}
	public static void main(String[] args) {
		
	}
}
