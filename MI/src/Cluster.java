import java.util.ArrayList;
import java.util.Random;

public class Cluster<T extends Point> {

	private final Class<T> type;

	protected ArrayList<T> points;
	protected T centroid;

	public Cluster(Class<T> type, ArrayList<T> points, T centroid) {
		this.points = points;
		this.centroid = centroid;
		this.type = type;
	}

	public Cluster(ArrayList<T> points, T centroid) {
		this.points = points;
		this.centroid = centroid;
		type = null;
	}

	public void addPoint(T point) {
		this.points.add(point);
	}

	public void removePoint(T point) {
		this.points.remove(point);
	}

	public void setCentroid(T centroid) {
		this.centroid = centroid;
	}

	public int[] calculateCentroid() throws InstantiationException,
			IllegalAccessException {
		if (getTypeInstance().getClass() == BinaryPoint.class) {
			// majority vote alapján
			int voteVector[] = new int[LastFMHandler.MAXARTISTS]; // ezeknél nem
																	// biztos,
																	// hogy jó a
																	// maxartists,
																	// mert
																	// nincs
																	// annyi
			int resultVector[] = new int[LastFMHandler.MAXARTISTS];
			ArrayList<BinaryPoint> points = (ArrayList<BinaryPoint>) getPoints();
			for (int j = 0; j < LastFMHandler.MAXARTISTS; j++) {
				for (int i = 0; i < points.size(); i++) {
					int[] artists = points.get(i).getArtists();
					voteVector[j] += artists[j];
				}

				// ha több mint a felénél az adott bit 1, akkor 1 a result
				// vectorban is (majority)
				// DE: > vagy >= nem mindegy?! ha pont fele-fele 1 és 0, akkor
				// melyik legyen?
				// jzk-be ilyen is olyan is
				if (voteVector[j] > points.size() / 2) {
					resultVector[j] = 1;
				}
			}
			return resultVector;
		} else if (getTypeInstance().getClass() == DecimalPoint.class) {
			ArrayList<DecimalPoint> points = (ArrayList<DecimalPoint>) getPoints();

			int resultVector[] = new int[LastFMHandler.MAXARTISTS]; // nem
																	// biztos,
																	// hogy
																	// maxartists
																	// kell
																	// mindenhol

			for (int j = 0; j < LastFMHandler.MAXARTISTS; j++) {
				for (int i = 0; i < points.size(); i++) {
					int[] artists = points.get(i).getArtists();
					resultVector[j] += artists[j];
				}
				resultVector[j] = resultVector[j] / points.size();
			}

			return resultVector;
		}else

		return null;
	}

	public Point getCentroid() {
		return centroid;
	}

	public ArrayList<T> getPoints() {
		return points;
	}

	public int getDistance(Cluster<T> cluster) {
		// ebben a klaszterban lévõ összes elemet összehasonlítjuk a
		// paraméterben szereplõ klaszter összes elemével
		// és visszaadjuk a minimális távolságot
		int minDist = 10000;
		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < cluster.getPoints().size(); j++) {
				int actDist = points.get(i)
						.distance(cluster.getPoints().get(j));
				if (actDist < minDist) {
					minDist = actDist;
				}
			}
		}
		return minDist;
	}

	private T getTypeInstance() throws InstantiationException,
			IllegalAccessException {
		return type.newInstance();
	}

}
