import java.util.ArrayList;


public class DecimalCluster extends Cluster<DecimalPoint>{

	public DecimalCluster(ArrayList<DecimalPoint> points, DecimalPoint centroid) {
		super(points, centroid);
	}

	//a clusterben lévõ pontok átlaga
	@Override
	public int[] calculateCentroid() {

		ArrayList<DecimalPoint> points = getPoints();
		
		int resultVector[] = new int[LastFMHandler.MAXARTISTS]; //nem biztos, hogy maxartists kell mindenhol
		
		for (int j = 0; j<LastFMHandler.MAXARTISTS; j++) {
			for (int i = 0; i<points.size(); i++) {
				int[] artists = points.get(i).getArtists();
				resultVector[j] += artists[j];
			}
			resultVector[j] = resultVector[j] / points.size();
		}
		
		return resultVector;
	}

}
