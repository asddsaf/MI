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
		//majority vote alapj�n
		int voteVector[] = new int[LastFMHandler.MAXARTISTS]; //ezekn�l nem biztos, hogy j� a maxartists, mert nincs annyi
		int resultVector[] = new int[LastFMHandler.MAXARTISTS];
		for (int j = 0; j<LastFMHandler.MAXARTISTS; j++) {
			for (int i = 0; i<points.size(); i++) {
				int[] artists = points.get(i).getArtists();
				voteVector[j] += artists[j];
			}
			
			//ha t�bb mint a fel�n�l az adott bit 1, akkor 1 a result vectorban is (majority)
			//DE: > vagy >= nem mindegy?! ha pont fele-fele 1 �s 0, akkor melyik legyen?
			//jzk-be ilyen is olyan is
			if (voteVector[j] > points.size()/2) { 
				resultVector[j] = 1;
			}
		}
		return resultVector;
	}
	
	public Point getCentroid() {
		return centroid;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	
}
