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
	
	//k klasztern�l �ll meg, agglomerative, az egyes elemeket vonogatja nagyobb klaszterekk� �ssze
	public void createClusters(int k) {
		//minden elemnek l�trehozunk egy saj�t klasztert
		for (int i = 0; i<points.size(); i++) {
			ArrayList<DecimalPoint> initial = new ArrayList<DecimalPoint>();
			initial.add(points.get(i));
			clusters.add(new Cluster<DecimalPoint>(initial , null));
		}
		int clusterCount = clusters.size();
		while (clusterCount > k) {
			int minDistance = 10000;
			int c1 = 0; //e k�t klaszter k�z�tt a legkisebb a t�vols�g, ezeket kell majd �sszevonni
			int c2 = 0;
			//v�gigmegy�nk az �sszes klaszteren, �s megkeress�k a 2 legkisebb t�vols�g�t
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
			//megtal�ltuk a k�t legk�zelebbi klasztert, �ssze kell vonni �ket
			ArrayList<DecimalPoint> newpoints = clusters.get(c1).getPoints();
			newpoints.addAll(clusters.get(c2).getPoints());
			Collections.sort(newpoints, new Point());
			Cluster<DecimalPoint> cij = new Cluster<DecimalPoint>(newpoints, null);
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
			//System.out.println("aktualis clustercount:" + clusterCount);
		}
	}
	
	
	public void writeToFile(String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("C:\\new\\"+ filename +".txt", "UTF-8");

			for (int i = 0; i < clusters.size(); i++) {
				writer.println("\n\r\n\r"+(int) (i + 1) + ". klaszter: \n\r\n\r");

				ArrayList<DecimalPoint> resultpoints = clusters.get(i).getPoints();

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
