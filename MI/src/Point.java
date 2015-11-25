public class Point {

	private String name;
	private int[] artists;

	
	//visszaadja a Hamming távolságot két pont között
	public static int distance(Point p1, Point p2){
		int d = 0;
		
		int p1Array[] = p1.getArtists();
		int p2Array[] = p2.getArtists();
		
		for (int i=0; i<p1.getArtists().length; i++){
			if(p1Array[i]!=p2Array[i]) d++;
		}
		
		return d;
	}
	
	
	public Point(String name, int[] artists) {
		this.name = name;
		this.artists = artists;
	}

	public String getName() {
		return name;
	}

	public int[] getArtists() {
		return artists;
	}

}
