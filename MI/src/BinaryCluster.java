import java.util.ArrayList;


public class BinaryCluster extends Cluster<BinaryPoint>{

	public BinaryCluster(ArrayList<BinaryPoint> points, BinaryPoint centroid) {
		super(points, centroid);
	}
	boolean b=true;
	@Override
	public int[] calculateCentroid() {
		if(b){
			System.out.println("BinaryCluster calculate");
			b=false;
		}

		//majority vote alapj�n
		int voteVector[] = new int[LastFMHandler.MAXARTISTS]; //ezekn�l nem biztos, hogy j� a maxartists, mert nincs annyi
		int resultVector[] = new int[LastFMHandler.MAXARTISTS];
		ArrayList<BinaryPoint> points = getPoints();
		for (int j = 0; j<LastFMHandler.MAXARTISTS; j++) {
			for (int i = 0; i<points.size(); i++) {
				int[] artists = points.get(i).getArtists();
				voteVector[j] += artists[j];
			}
			
			//ha t�bb mint a fel�n�l az adott bit 1, akkor 1 a result vectorban is (majority)
			//DE: > vagy >= nem mindegy?! ha pont fele-fele 1 �s 0, akkor melyik legyen?
			if (voteVector[j] > points.size()/2) { 
				resultVector[j] = 1;
			}
		}
		return resultVector;
	}

}
