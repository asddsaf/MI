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
	
	//k klaszternál áll meg, agglomerative, az egyes elemeket vonogatja nagyobb klaszterekké össze
	public void createClusters(int k) {
		//minden elemnek létrehozunk egy saját klasztert
		for (int i = 0; i<points.size(); i++) {
			ArrayList<Point> initial = new ArrayList<Point>();
			initial.add(points.get(i));
			clusters.add(new Cluster(initial , null));
		}
		int clusterCount = clusters.size();
		while (clusterCount > k) {
			int minDistance = 10000;
			int c1 = 0; //e két klaszter között a legkisebb a távolság, ezeket kell majd összevonni
			int c2 = 0;
			//végigmegyünk az összes klaszteren, és megkeressük a 2 legkisebb távolságút
			for (int i = 0; i<clusters.size(); i++) {
				for (int j = i+1; j<clusters.size(); j++) {
					int actDist = clusters.get(i).getDistance(clusters.get(j));
					if (actDist < minDistance) {
						minDistance = actDist;
						c1 = i;
						c2 = j;
					}
				}
			}
			//megtaláltuk a két legközelebbi klasztert, össze kell vonni õket
			ArrayList<Point> newpoints = clusters.get(c1).getPoints();
			newpoints.addAll(clusters.get(c2).getPoints());
			Collections.sort(newpoints, new Point());
			Cluster cij = new Cluster(newpoints, null);
			if (c1 < c2) {
				clusters.remove(c2);
				clusters.remove(c1);
				clusters.add(c1, cij);
			}
			else {
				clusters.remove(c1);
				clusters.remove(c2);
				clusters.add(c2, cij);
			}
			clusterCount = clusterCount - 1;
			System.out.println("aktualis clustercount:" + clusterCount);
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
