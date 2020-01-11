package Toussaint;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;




public class Toussaint{

	public static ArrayList<Point> toussaint(ArrayList<Point> points) {
		ArrayList<Point> envconv = enveloppeConvexe(points);
		
		Point p;

		int i = 0, j = 0, k = 0, l = 0;

		PointDouble pi = new PointDouble(Double.MAX_VALUE, 0.), pj = new PointDouble(Double.MIN_VALUE, 0.),
				pk = new PointDouble(0., Double.MAX_VALUE), pl = new PointDouble(0., Double.MIN_VALUE);

		int s = envconv.size();
		for (int indx = 0; indx < s; indx++) {
			p = envconv.get(indx);
			if (p.x < pi.x) {
				i = indx;
				pi.move(p.x, p.y);
			}
			if (p.x > pj.x) {
				j = indx;
				pj.move(p.x, p.y);
			}
			if (p.y < pk.y) {
				k = indx;
				pk.move(p.x, p.y);
			}
			if (p.y > pl.y) {
				l = indx;
				pl.move(p.x, p.y);
			}
		}

		PointDouble pid = new PointDouble(pi.x, pi.y + 1.), pjd = new PointDouble(pj.x, pj.y - 1.),
				pkd = new PointDouble(pk.x - 1., pk.y), pld = new PointDouble(pl.x + 1., pl.y);


		PointDouble pin = new PointDouble(0., 0.), pjn = new PointDouble(0., 0.), pkn = new PointDouble(0., 0.),
				pln = new PointDouble(0., 0.);
		double ai, aj, ak, al, teta;
		PointDouble A = intersection(pi, pid, pkd, pk), B = intersection(pk, pkd, pjd, pj),
				C = intersection(pj, pjd, pld, pl), D = intersection(pl, pld, pid, pi);

		double minvol = A.distance(B) * B.distance(C);
		ArrayList<Point> sq = new ArrayList<Point>();

		sq.add(new Point((int) A.x, (int) A.y));
		sq.add(new Point((int) B.x, (int) B.y));
		sq.add(new Point((int) C.x, (int) C.y));
		sq.add(new Point((int) D.x, (int) D.y));

		for (int indx = 1; indx < s; indx++) {
			teta = 0;

			p = envconv.get((i + 1) % s);
			pin.move(p.x, p.y);
			ai = getAngle2(pi, pid, pi, pin);

			p = envconv.get((j + 1) % s);
			pjn.move(p.x, p.y);
			aj = getAngle2(pj, pjd, pj, pjn);

			p = envconv.get((k + 1) % s);
			pkn.move(p.x, p.y);
			ak = getAngle2(pk, pkd, pk, pkn);

			p = envconv.get((l + 1) % s);
			pln.move(p.x, p.y);
			al = getAngle2(pl, pld, pl, pln);

			teta = Double.min(Double.min(ai, aj), Double.min(ak, al));

			rotatePoint(pi, pid, -teta);
			rotatePoint(pj, pjd, -teta);
			rotatePoint(pk, pkd, -teta);
			rotatePoint(pl, pld, -teta);

			ai = getAngle2(pi, pid, pi, pin);
			aj = getAngle2(pj, pjd, pj, pjn);
			ak = getAngle2(pk, pkd, pk, pkn);
			al = getAngle2(pl, pld, pl, pln);
			if (ai == 0.0) {
				i = (i + 1) % s;
				p = envconv.get(i);
				pid.move(p.x + (pid.x - pi.x), p.y + (pid.y - pi.y));
				pi.move(p.x, p.y);
			}
			if (aj == 0.0) {
				j = (j + 1) % s;
				p = envconv.get(j);
				pjd.move(p.x + (pjd.x - pj.x), p.y + (pjd.y - pj.y));
				pj.move(p.x, p.y);
			}
			if (ak == 0.0) {
				k = (k + 1) % s;
				p = envconv.get(k);
				pkd.move(p.x + (pkd.x - pk.x), p.y + (pkd.y - pk.y));
				pk.move(p.x, p.y);
			}
			if (al == 0.0) {
				l = (l + 1) % s;
				p = envconv.get(l);
				pld.move(p.x + (pld.x - pl.x), p.y + (pld.y - pl.y));
				pl.move(p.x, p.y);
			}
			

			A = intersection(pi, pid, pk, pkd);
			B = intersection(pk, pkd, pj, pjd);
			C = intersection(pj, pjd, pl, pld);
			D = intersection(pl, pld, pi, pid);

		
			if (A.distance(B) * B.distance(C) < minvol) {
				minvol = A.distance(B) * B.distance(C);
				sq.clear();
				sq.add(new Point((int) A.x, (int) A.y));
				sq.add(new Point((int) B.x, (int) B.y));
				sq.add(new Point((int) C.x, (int) C.y));
				sq.add(new Point((int) D.x, (int) D.y));
			}
		}
		return sq;
	}
	
	public double aire_rectangle(ArrayList<Point> points) {
		if(points.size()!=4) {
			return -1;
		}
		else {
			return points.get(0).distance(points.get(1))*points.get(0).distance(points.get(2));
		}
	}
	
	public static  void rotatePoint(PointDouble a, PointDouble b, double angle) {
		double nx, ny;
		nx = a.x + ((b.x - a.x) * Math.cos((angle)) - (b.y - a.y) * Math.sin((angle)));
		ny = a.y + ((b.y - a.y) * Math.cos((angle)) + (b.x - a.x) * Math.sin((angle)));
		b.move(nx, ny);
	}

	private static  double dotProduct(PointDouble p, PointDouble q, PointDouble s, PointDouble t) {
		return ((q.x - p.x) * (t.x - s.x) + (q.y - p.y) * (t.y - s.y));
	}

	private static  double getAngle2(PointDouble a, PointDouble b, PointDouble c, PointDouble d) {
		if (a.equals(b) || c.equals(d))
			return Double.MAX_VALUE;
		double cosTheta = dotProduct(a, b, c, d) / (double) (a.distance(b) * c.distance(d));
		double res = Math.acos(cosTheta);
		return Double.isNaN(res) ? 0.0 : res;
	}

	public  static double getAngle(PointDouble a, PointDouble b, PointDouble c, PointDouble d) {
		/*
		 * -> -> Retourne l'angle entre ab et cd
		 */
		double abdotcd = ((b.x - a.x) * (d.x - c.x) + (b.y - a.y) * (d.y - c.y));
		double ab = a.distance(b);
		double cd = c.distance(d);
		double res = Math.acos(abdotcd / (ab * cd));
		return Double.isNaN(res) ? 0.0 : res;
	}

	public static class PointDouble {
		double x, y;

		public PointDouble(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public void move(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double distance(PointDouble p) {
			return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
		}

	}

	public static PointDouble intersection(PointDouble a, PointDouble b, PointDouble c, PointDouble d) {
		double a1 = b.y - a.y;
		double b1 = a.x - b.x;
		double c1 = a1 * (a.x) + b1 * (b.y);

		double a2 = d.y - c.y;
		double b2 = c.x - d.x;
		double c2 = a2 * (c.x) + b2 * (c.y);

		double determinant = a1 * b2 - a2 * b1;

		if (determinant == 0) {
			return null;
		} else {
			int x = (int) ((b2 * c1 - b1 * c2) / determinant);
			int y = (int) ((a1 * c2 - a2 * c1) / determinant);
			return new PointDouble(x, y);
		}
	}
	

	
	
	

	
private static ArrayList<Point> enveloppeConvexe(ArrayList<Point> points){
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
    private static boolean triangleContientPoint(Point a, Point b, Point c, Point x) {
        double l1 = ((b.y-c.y)*(x.x-c.x)+(c.x-b.x)*(x.y-c.y))/(double)((b.y-c.y)*(a.x-c.x)+(c.x-b.x)*(a.y-c.y));
        double l2 = ((c.y-a.y)*(x.x-c.x)+(a.x-c.x)*(x.y-c.y))/(double)((b.y-c.y)*(a.x-c.x)+(c.x-b.x)*(a.y-c.y));
        double l3 = 1-l1-l2;
        return (0<l1 && l1<1 && 0<l2 && l2<1 && 0<l3 && l3<1);
    }
    
    
	public class ParallelLinesException extends RuntimeException {
		public ParallelLinesException() {
			super();
		}
	}

	public class UnequalSidesException extends RuntimeException {
		public UnequalSidesException() {
			super();
		}
	}
    
    private static double crossProduct(Point p, Point q, Point s, Point t){
        return ((q.x-p.x)*(t.y-s.y)-(q.y-p.y)*(t.x-s.x));
    }
}