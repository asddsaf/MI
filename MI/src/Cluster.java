import java.util.ArrayList;
import java.util.Random;


public class Cluster<T extends Point> {

	private ArrayList<T> points;
	private T centroid;
	
	public Cluster(ArrayList<T> points, T centroid){
		this.points = points;
		this.centroid = centroid;
	}
	
	public void addPoint(T point){
		this.points.add(point);
	}
	
	public void removePoint(T point){
		this.points.remove(point);
	}
	
	public void setCentroid(T centroid){
		this.centroid = centroid;
	}
	boolean b=true;
	public int[] calculateCentroid() {
			if(b){
			System.out.println("Normal Cluster calculate");
			b=false;
		}
		return null;
	}
	
	public Point getCentroid() {
		return centroid;
	}
	
	public ArrayList<T> getPoints() {
		return points;
	}

	public int getDistance(Cluster<T> cluster) {
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
