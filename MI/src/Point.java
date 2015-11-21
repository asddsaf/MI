public class Point {

	private String name;
	private int[] artists;

	public Point(String name, int[] artists) {
		this.artists = new int[100];
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
