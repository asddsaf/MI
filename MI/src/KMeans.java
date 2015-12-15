import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class KMeans<T extends Point> {

	private final Class<T> type;

	ArrayList<T> points;
	ArrayList<Cluster<T>> clusters;

	public KMeans(Class<T> type, ArrayList<T> pointlist) throws InstantiationException, IllegalAccessException {
		points = new ArrayList<T>();
		clusters = new ArrayList<Cluster<T>>();
		points = pointlist;
		this.type = type;

	}
	
	//visszaadja azon pontok indexét, amik centroidok lesznek
	public ArrayList<Integer> chooseCentroids(int k, int numberOfTags) {

		ArrayList<Integer> centroids = new ArrayList<Integer>();
		Random r = new Random();
		int from = 0;

		for (int i = 0; i < k; i++) {
			int randomIndex = r.nextInt(numberOfTags - from) + from;

			if (!centroids.contains(randomIndex)) {
				centroids.add(randomIndex);
			} else
				i--;
		}

		return centroids;
	}

	// kell k: hány klasztert akarunk, és kellenek a kezdõ centroidok (k db)
	// végén: kell, hogy az egyes klaszterekben mely pontok vannak együtt
	public void createClusters(int k, int numberOfTags, ArrayList<T> initialCentroids)
			throws InstantiationException, IllegalAccessException {

		//ArrayList<T> initialCentroids = chooseCentroids(k, numberOfTags);

		// létrehozunk k üres klasztert
		for (int i = 0; i < k; i++) {
			clusters.add(new Cluster<T>(type,new ArrayList<T>(), initialCentroids
					.get(i)));
		}

		boolean changed = true; // addig megyünk, amíg elõfordul olyan, hogy 1
								// pontot másik klaszterbe sorolunk

		boolean clusterChanged = false; // klaszterváltás jelzése

		while (changed) {

			clusterChanged = false;
			for (int i = 0; i < points.size(); i++) { // végigmegyünk az összes
														// ponton

				int distance = 150000; // ez béna, de egyelõre megteszi
				int nextCluster = 0;
				int actualCluster = -1;
				int foundActualCluster = 0;

				for (int j = 0; j < k; j++) { // és megkeressük, melyik
												// klaszterhez van közelebb

					// eltároljuk, hogy melyik klaszterben van most a pont
					foundActualCluster = clusters.get(j).getPoints()
							.indexOf(points.get(i));
					
					if (foundActualCluster != -1) {
						actualCluster = j;
					}

					int tempDistance = points.get(i).distance(
							clusters.get(j).getCentroid());
					if (tempDistance < distance) {
						distance = tempDistance;
						nextCluster = j;
					}
				}

				// i. pont kivétele az aktuális klaszterébõl, ha az más, mint a
				// most megtalált
				if (actualCluster != -1 && nextCluster != actualCluster) {
					
					// i. pontot besoroljuk a megtalált klaszterbe
					clusters.get(nextCluster).addPoint(points.get(i));
					
					//i. pontot kitöröljük az elõzõ klaszterbõl
					clusters.get(actualCluster).removePoint(points.get(i));

					// jelezzük, hogy történt klaszterváltás
					clusterChanged = true;
				} else if (actualCluster == -1) {
					// i. pontot besoroljuk a megtalált klaszterbe, ha éppen
					// nincs egy klaszterben sem (elsõ iterációnál fordul elõ)
					clusters.get(nextCluster).addPoint(points.get(i));
					clusterChanged = true;
				}
			}
						
			if (clusterChanged) {
				// miután minden pontot besoroltunk a megfelelõ klaszterbe,
				// kiszámítjuk a klaszterek új centroidját
				for (int i = 0; i < k; i++) {

					T newCentroid = getTypeInstance();
					newCentroid.setName("centroid");
					newCentroid.setArtists(clusters.get(i).calculateCentroid());

					clusters.get(i).setCentroid(newCentroid);
				}
			}
			else {
				// ha nem történt átsorolás (egy pont másik klaszterbe tevése),
				// akkor megáll az algoritmus
				changed = false;
			}
		}
	}

	public void writeToFile(String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename + ".txt", "UTF-8");

			for (int i = 0; i < clusters.size(); i++) {
				writer.println("\n\r\n\r" + (int) (i + 1)
						+ ". klaszter: \n\r\n\r");

				ArrayList<T> resultpoints = clusters.get(i).getPoints();
				
				Collections.sort(resultpoints, new Point());

				for (int j = 0; j < resultpoints.size(); j++) {
					writer.println(resultpoints.get(j).getName());
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private T getTypeInstance() throws InstantiationException,
			IllegalAccessException {
		return type.newInstance();
	}

}
