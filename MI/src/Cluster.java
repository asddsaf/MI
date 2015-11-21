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
	
}
