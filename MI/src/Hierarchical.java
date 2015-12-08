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
	
	//k klaszternál áll meg, agglomerative, az egyes elemekt vonogatja nagyobb klaszterekké össze
	public void createClusters(int k) {
		//minden elemnek létrehozunk egy saját klasztert
		for (int i = 0; i<points.size(); i++) {
			ArrayList<Point> initial = new ArrayList<Point>();
			initial.add(points.get(i));
			clusters.add(new Cluster(initial , null));
		}
		int clusterCount = 0;
		if (clusterCount < k) {
			//végigmegyünk az összes klaszteren, és megkeressük a 2 legkisebb távolságút
			//mivel rendezett a pontok listája a címke neve szerint, így a sorrend megmarad
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
