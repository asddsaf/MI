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
	
	//visszaadja azon pontok index�t, amik centroidok lesznek
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

	// kell k: h�ny klasztert akarunk, �s kellenek a kezd� centroidok (k db)
	// v�g�n: kell, hogy az egyes klaszterekben mely pontok vannak egy�tt
	public void createClusters(int k, int numberOfTags, ArrayList<T> initialCentroids)
			throws InstantiationException, IllegalAccessException {

		//ArrayList<T> initialCentroids = chooseCentroids(k, numberOfTags);

		// l�trehozunk k �res klasztert
		for (int i = 0; i < k; i++) {
			clusters.add(new Cluster<T>(type,new ArrayList<T>(), initialCentroids
					.get(i)));
		}

		boolean changed = true; // addig megy�nk, am�g el�fordul olyan, hogy 1
								// pontot m�sik klaszterbe sorolunk

		boolean clusterChanged = false; // klaszterv�lt�s jelz�se

		while (changed) {

			clusterChanged = false;
			for (int i = 0; i < points.size(); i++) { // v�gigmegy�nk az �sszes
														// ponton

				int distance = 150000; // ez b�na, de egyel�re megteszi
				int nextCluster = 0;
				int actualCluster = -1;
				int foundActualCluster = 0;

				for (int j = 0; j < k; j++) { // �s megkeress�k, melyik
												// klaszterhez van k�zelebb

					// elt�roljuk, hogy melyik klaszterben van most a pont
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

				// i. pont kiv�tele az aktu�lis klaszter�b�l, ha az m�s, mint a
				// most megtal�lt
				if (actualCluster != -1 && nextCluster != actualCluster) {
					
					// i. pontot besoroljuk a megtal�lt klaszterbe
					clusters.get(nextCluster).addPoint(points.get(i));
					
					//i. pontot kit�r�lj�k az el�z� klaszterb�l
					clusters.get(actualCluster).removePoint(points.get(i));

					// jelezz�k, hogy t�rt�nt klaszterv�lt�s
					clusterChanged = true;
				} else if (actualCluster == -1) {
					// i. pontot besoroljuk a megtal�lt klaszterbe, ha �ppen
					// nincs egy klaszterben sem (els� iter�ci�n�l fordul el�)
					clusters.get(nextCluster).addPoint(points.get(i));
					clusterChanged = true;
				}
			}
						
			if (clusterChanged) {
				// miut�n minden pontot besoroltunk a megfelel� klaszterbe,
				// kisz�m�tjuk a klaszterek �j centroidj�t
				for (int i = 0; i < k; i++) {

					T newCentroid = getTypeInstance();
					newCentroid.setName("centroid");
					newCentroid.setArtists(clusters.get(i).calculateCentroid());

					clusters.get(i).setCentroid(newCentroid);
				}
			}
			else {
				// ha nem t�rt�nt �tsorol�s (egy pont m�sik klaszterbe tev�se),
				// akkor meg�ll az algoritmus
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
