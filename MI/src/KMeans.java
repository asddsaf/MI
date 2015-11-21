import java.util.ArrayList;


public class KMeans {

	ArrayList<Point> points;
	ArrayList<Cluster> clusters;
	
	public KMeans(){
		points = new ArrayList<Point>();
		clusters = new ArrayList<Cluster>();
		
		points = DataProcessor.getPoints();
	}
	
	public void createClusters(int k){
		
	}
	
	
}
