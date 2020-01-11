package algorithms;



import java.awt.Point;
import java.util.ArrayList;

import Toussaint.Toussaint;
import ritter.Ritter;
import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

	 private double distance(Point p, Point a, Point b) {
	        return Math.abs(crossProduct(a,b,a,p));
	    }
		public double aire_polygone(ArrayList<Point> points) {
			double res = 0.;
			Point pi, pi1;
			int s = points.size();

			for (int i = 0; i < s - 1; i++) {
				pi = points.get(i);
				pi1 = points.get(i + 1);
				res -= pi.x * pi1.y;
				res += pi.y * pi1.x;
			}
			pi = points.get(s - 1);
			pi1 = points.get(0);
			res -= pi.x * pi1.y;
			res += pi.y * pi1.x;
			res *= .5;
			return res;
		}
	private ArrayList<Line> calculPairesAntipodales(ArrayList<Point> points) {
        ArrayList<Point> p = enveloppeConvexe(points); // p est l'enveloppe convexe de points
        int n = p.size();
        ArrayList<Line> antipodales = new ArrayList<Line>();
        int k = 1;
        while (distance(p.get(k),p.get(n-1),p.get(0)) < distance(p.get((k+1)%n),p.get(n-1),p.get(0))) k++;
        int i = 0;
        int j = k;
        while (i<=k && j<n) {
            while (distance(p.get(j),p.get(i),p.get(i+1))<distance(p.get((j+1)%n),p.get(i),p.get(i+1)) && j<n-1) {
                antipodales.add(new Line(p.get(i),p.get(j)));
                j++;
            }
            antipodales.add(new Line(p.get(i),p.get(j)));
            i++;
        }
        return antipodales;
    }
  // calculDiametre: ArrayList<Point> --> Line
  //   renvoie une pair de points de la liste, de distance maximum.
  public Line calculDiametre(ArrayList<Point> points) {
	  if (points.size()<2) {
          return null;
      }

      Point p=points.get(0);
      Point q=points.get(1);

      for (Point s: points) for (Point t: points) if (s.distance(t)>p.distance(q)) {p=s;q=t;}

      return new Line(p,q);
  }

  // calculDiametreOptimise: ArrayList<Point> --> Line
  //   renvoie une pair de points de la liste, de distance maximum.
  public Line calculDiametreOptimise(ArrayList<Point> points) {
	  if (points.size()<2) {
          return null;
      }

      ArrayList<Line> antipodales = calculPairesAntipodales(points);

      Point p=antipodales.get(0).getP();
      Point q=antipodales.get(0).getQ();

      for (Line a: antipodales) if (a.getP().distance(a.getQ())>p.distance(q)) {p=a.getP();q=a.getQ();}

      return new Line(p,q);
  }

  // calculCercleMin: ArrayList<Point> --> Circle
  //   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
  public Circle calculCercleMin(ArrayList<Point> points) {
	  return Ritter.Ritter(points);
  }
  private double crossProduct(Point p, Point q, Point s, Point t){
      return ((q.x-p.x)*(t.y-s.y)-(q.y-p.y)*(t.x-s.x));
  }
  // enveloppeConvexe: ArrayList<Point> --> ArrayList<Point>
  //   renvoie l'enveloppe convexe de la liste.
  
  public ArrayList<Point> enveloppeConvexe(ArrayList<Point> points){
	  return rectangleConvexe(points);
  }  
 
public ArrayList<Point> rectangleConvexe(ArrayList<Point> points){
	return Toussaint.toussaint(points);
}

private ArrayList<Point> rectangleConvexe2(ArrayList<Point> points){
    if (points.size()<4) return points;

    Point ouest = points.get(0);
    Point sud = points.get(0);
    Point est = points.get(0);
    Point nord = points.get(0);
    for (Point p: points){
        if (p.x<ouest.x) ouest=p;
        if (p.y>sud.y) sud=p;
        if (p.x>est.x) est=p;
        if (p.y<nord.y) nord=p;
    }
    ArrayList<Point> result = new ArrayList<Point>();
    result.add(ouest);
    result.add(sud);
    result.add(est);
    result.add(nord);

    ArrayList<Point> rest = (ArrayList<Point>)points.clone();
    for (int i=0;i<rest.size();i++) {
        if (triangleContientPoint(ouest,sud,est,rest.get(i)) ||
                triangleContientPoint(ouest,est,nord,rest.get(i))) {
            rest.remove(i);
            i--;
                }
    }

    for (int i=0;i<result.size();i++) {
        Point a = result.get(i);
        Point b = result.get((i+1)%result.size());
        Point ref = result.get((i+2)%result.size());

        double signeRef = crossProduct(a,b,a,ref);
        double maxValue = 0;
        Point maxPoint = a;

        for (Point p: points) {
            double piki = crossProduct(a,b,a,p);
            if (signeRef*piki<0 && Math.abs(piki)>maxValue) {
                maxValue = Math.abs(piki);
                maxPoint = p;
            }
        }
        if (maxValue!=0){
            for (int j=0;j<rest.size();j++) {
                if (triangleContientPoint(a,b,maxPoint,rest.get(j))){
                    rest.remove(j);
                    j--;
                }
            }
            result.add(i+1,maxPoint);
            i--;
        }
    }
    return result;
	}	  
private boolean triangleContientPoint(Point a, Point b, Point c, Point x) {
    double l1 = ((b.y-c.y)*(x.x-c.x)+(c.x-b.x)*(x.y-c.y))/(double)((b.y-c.y)*(a.x-c.x)+(c.x-b.x)*(a.y-c.y));
    double l2 = ((c.y-a.y)*(x.x-c.x)+(a.x-c.x)*(x.y-c.y))/(double)((b.y-c.y)*(a.x-c.x)+(c.x-b.x)*(a.y-c.y));
    double l3 = 1-l1-l2;
    return (0<l1 && l1<1 && 0<l2 && l2<1 && 0<l3 && l3<1);
}
}	
