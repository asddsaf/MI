import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Hierarchical {

	ArrayList<Point> points;
	ArrayList<Cluster> clusters;
	
	public Hierarchical(ArrayList<Point> pointlist) {
		points = new ArrayList<Point>();
		clusters = new ArrayList<Cluster>();

		points = pointlist;
		Collections.sort(points, new Point());
	}
	
	//k klasztern�l �ll meg, agglomerative, az egyes elemekt vonogatja nagyobb klaszterekk� �ssze
	public void createClusters(int k) {
		//minden elemnek l�trehozunk egy saj�t klasztert
		for (int i = 0; i<points.size(); i++) {
			ArrayList<Point> initial = new ArrayList<Point>();
			initial.add(points.get(i));
			clusters.add(new Cluster(initial , null));
		}
		int clusterCount = 0;
		if (clusterCount < k) {
			//v�gigmegy�nk az �sszes klaszteren, �s megkeress�k a 2 legkisebb t�vols�g�t
			//mivel rendezett a pontok list�ja a c�mke neve szerint, �gy a sorrend megmarad
			for (int i = 0; i<clusters.size(); i++) {
				for (int j = i; j<clusters.size(); j++) {
					clusters.get(i).getDistance(clusters.get(j));
				}
			}
		}
	}
	
	
	public void writeToFile(String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("C:\\new\\"+ filename +".txt", "UTF-8");

			for (int i = 0; i < clusters.size(); i++) {
				writer.println("\n\r\n\r"+(int) (i + 1) + ". klaszter: \n\r\n\r");

				ArrayList<Point> resultpoints = clusters.get(i).getPoints();

				for (int j = 0; j < resultpoints.size(); j++) {
					writer.println(resultpoints.get(j).getName() + ", "
							+ Arrays.toString(resultpoints.get(j).getArtists()));
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
