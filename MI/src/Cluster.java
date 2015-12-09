import java.util.ArrayList;
import java.util.Random;


public class Cluster {

	private ArrayList<Point> points;
	private Point centroid;
	
	public Cluster(ArrayList<Point> points, Point centroid){
		this.points = points;
		this.centroid = centroid;
	}
	
	public void addPoint(Point point){
		this.points.add(point);
	}
	
	public void removePoint(Point point){
		this.points.remove(point);
	}
	
	public void setCentroid(Point centroid){
		this.centroid = centroid;
	}
	
	public int[] calculateCentroid() {
		return null;
	}
	
	public Point getCentroid() {
		return centroid;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}

	public int getDistance(Cluster cluster) {
		//ebben a klaszterban lévõ összes elemet összehasonlítjuk a paraméterben szereplõ klaszter összes elemével
		//és visszaadjuk a minimális távolságot
		int minDist = 10000;
		for (int i = 0; i<points.size(); i++) {
			for (int j = 0; j<cluster.getPoints().size(); j++) {
				int actDist = points.get(i).distance(cluster.getPoints().get(j));
				if (actDist < minDist) {
					minDist = actDist;
				}
			}
		}
		return minDist;		
	}	
}
