import java.util.Comparator;

public class Point implements Comparator<Point>{

	private String name;
	private int[] artists;

	
	//visszaadja a t�vols�got k�t pont k�z�tt
	public int distance(Point p1) {
		return 0;
	}
	
	public Point() {}
	
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
	
	@Override
	public int compare(Point o1, Point o2) {
		return o1.name.compareTo(o2.name);
	}

}
