
public class DecimalPoint extends Point{

	public DecimalPoint(String name, int[] artists) {
		super(name, artists);
	}

	
	//L2 norma (euklideszi távolság)
	@Override
	public int distance(Point p1) {

		int d = 0;
		int p1Array[] = p1.getArtists();
		int p2Array[] = this.getArtists();
		
		for (int i = 0; i<p1Array.length; i++) {
			d += Math.pow((p1Array[i] - p2Array[i]), 2);
		}
		
		return (int)Math.sqrt(d);
	}
	
	//L1 norma (manhattan távolság) 
	/*
	@Override
	public int distance(Point p1) {
		int d = 0;
		int p1Array[] = p1.getArtists();
		int p2Array[] = this.getArtists();
		
		for (int i = 0; i<p1Array.length; i++) {
			d += abs(p1Array[i] - p2Array[i]);
		}
		
		return (int)Math.sqrt(d);
	}
	*/
	
	//de lehet még négyzetes euklideszi távolság is
	/*
	 @Override
	public int distance(Point p1) {
		int d = 0;
		int p1Array[] = p1.getArtists();
		int p2Array[] = this.getArtists();
		
		for (int i = 0; i<p1Array.length; i++) {
			d += Math.pow((p1Array[i] - p2Array[i]), 2);
		}
		
		return d;
	}
	 */
	
}
