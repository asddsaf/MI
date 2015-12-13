
public class BinaryPoint extends Point {

	public BinaryPoint(String name, int[] artists) {
		super(name, artists);
	}
	
	public BinaryPoint() {}
	
	//Hamming távolság 2 pont között
	@Override
	public int distance(Point p1) {

		int d = 0;
		
		int p1Array[] = p1.getArtists();
		int p2Array[] = this.getArtists();
		
		for (int i=0; i<p1Array.length; i++){
			if(p1Array[i]!=p2Array[i]) d++;
		}
		
		return d;
	}

}
