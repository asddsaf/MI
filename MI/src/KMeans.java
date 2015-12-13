import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
	


	private ArrayList<T> chooseCentroids(int k, int numberOfTags) {

		ArrayList<T> centroids = new ArrayList<T>();
		Random r = new Random();
		int from = 0;

		for (int i = 0; i < k; i++) {
			int randomIndex = r.nextInt(numberOfTags - from) + from;

			if (!centroids.contains(points.get(randomIndex))) {
				centroids.add(points.get(randomIndex));
			} else
				i--;
		}

		return centroids;
	}

	// kell k: hány klasztert akarunk, és kellenek a kezdõ centroidok (k db)
	// végén: kell, hogy az egyes klaszterekben mely pontok vannak együtt
	public void createClusters(int k, int numberOfTags)
			throws InstantiationException, IllegalAccessException {

		ArrayList<T> initialCentroids = chooseCentroids(k, numberOfTags);

		// létrehozunk k üres klasztert
		for (int i = 0; i < k; i++) {
			clusters.add(new Cluster<T>(type,new ArrayList<T>(), initialCentroids
					.get(i)));
		}

		boolean changed = true; // addig megyünk, amíg elõfordul olyan, hogy 1
								// pontot másik klaszterbe sorolunk

		boolean clusterChanged = false; // klaszterváltás jelzése

		while (changed) {

			for (int i = 0; i < points.size(); i++) { // végigmegyünk az összes
														// ponton

				int distance = 150; // ez béna, de egyelõre megteszi
				int nextCluster = 0;
				int actualCluster = 0;

				for (int j = 0; j < k; j++) { // és megkeressük, melyik
												// klaszterhez van közelebb

					// eltároljuk, hogy melyik klaszterben van most a pont
					actualCluster = clusters.get(j).getPoints()
							.indexOf(points.get(i));

					int tempDistance = points.get(i).distance(
							clusters.get(j).getCentroid());
					if (tempDistance < distance) {
						distance = tempDistance;
						nextCluster = j;
					}
				}

				clusterChanged = false;
				// i. pont kivétele az aktuális klaszterébõl, ha az más, mint a
				// most megtalált
				if (actualCluster != -1 && nextCluster != actualCluster) {

					clusters.get(actualCluster).removePoint(points.get(i));

					// i. pontot besoroljuk a megtalált klaszterbe
					clusters.get(nextCluster).addPoint(points.get(i));

					// jelezzük, hogy történt klaszterváltás
					clusterChanged = true;
				} else if (actualCluster == -1) {
					// i. pontot besoroljuk a megtalált klaszterbe, ha éppen
					// nincs egy klaszterben sem (elsõ iterációnál fordul elõ)
					clusters.get(nextCluster).addPoint(points.get(i));
					clusterChanged = true;
				}

				// ha nem történt átsorolás (egy pont másik klaszterbe tevése),
				// akkor megáll az algoritmus
				if (!clusterChanged) {
					changed = false;
				}
			}
			// TODO
			
				if (clusterChanged) {
					// miután minden pontot besoroltunk a megfelelõ klaszterbe,
					// kiszámítjuk a klaszterek új centroidját
					for (int i = 0; i < k; i++) {

						//System.out.println("cluster calculate");

						T newCentroid = getTypeInstance();
						newCentroid.setName("centroid");
						// TODO
						newCentroid.setArtists(clusters.get(i).calculateCentroid());

						clusters.get(i).setCentroid(newCentroid);
					}
				}

		}
	}

	public void writeToFile(String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("C:\\new\\" + filename + ".txt", "UTF-8");

			for (int i = 0; i < clusters.size(); i++) {
				writer.println("\n\r\n\r" + (int) (i + 1)
						+ ". klaszter: \n\r\n\r");

				ArrayList<T> resultpoints = clusters.get(i).getPoints();

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

	private T getTypeInstance() throws InstantiationException,
			IllegalAccessException {
		return type.newInstance();
	}

}
