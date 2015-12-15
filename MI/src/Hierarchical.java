import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Hierarchical {

	ArrayList<DecimalPoint> points;
	ArrayList<Cluster<DecimalPoint>> clusters;

	public Hierarchical(ArrayList<DecimalPoint> pointlist) {
		points = new ArrayList<DecimalPoint>();
		clusters = new ArrayList<Cluster<DecimalPoint>>();

		points = pointlist;
		Collections.sort(points, new Point());
	}

	// k klasztern�l �ll meg, agglomerative, az egyes elemeket vonogatja nagyobb
	// klaszterekk� �ssze
	public void createClusters(int k) {
		// minden elemnek l�trehozunk egy saj�t klasztert
		for (int i = 0; i < points.size(); i++) {
			ArrayList<DecimalPoint> initial = new ArrayList<DecimalPoint>();
			initial.add(points.get(i));
			clusters.add(new Cluster<DecimalPoint>(initial, null));
		}
		int clusterCount = clusters.size();

		Cluster<DecimalPoint> c1 = null; // e k�t klaszter k�z�tt a legkisebb a
											// t�vols�g, ezeket kell majd
											// �sszevonni
		Cluster<DecimalPoint> c2 = null;

		while (clusterCount > k) {

			int minDistance = 1000000;
			c1 = clusters.get(0); // e k�t klaszter k�z�tt a legkisebb a
									// t�vols�g, ezeket kell majd �sszevonni
			c2 = null;
			for (Cluster<DecimalPoint> c : clusters) {
				if (c1 != c) {
					int actDist = c1.getDistance(c);
					if (actDist < minDistance) {
						minDistance = actDist;
						c2 = c;
					}
				}
			}

			ArrayList<DecimalPoint> newpoints = c1.getPoints();
			newpoints.addAll(c2.getPoints());
			Cluster<DecimalPoint> cij = new Cluster<DecimalPoint>(newpoints,
					null);
			clusters.remove(c1);
			clusters.remove(c2);
			clusters.add(cij);
			clusterCount = clusterCount - 1;
		}

	}

	public void writeToFile(String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename + ".txt", "UTF-8");

			for (int i = 0; i < clusters.size(); i++) {
				writer.println("\n\r\n\r" + (int) (i + 1)
						+ ". klaszter: \n\r\n\r");

				ArrayList<DecimalPoint> resultpoints = clusters.get(i)
						.getPoints();

				Collections.sort(resultpoints, new DecimalPoint());
				
				for (int j = 0; j < resultpoints.size(); j++) {
					writer.println(resultpoints.get(j).getName());
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
