
public class Main {

	
	private static final int KMEANS = 15;
	
	public static void main(String[] args) {

		LastFMHandler handler = new LastFMHandler();
		System.out.println("Fetching Artists...");
		handler.fetchArtistsByUser();
		System.out.println("Fetching Tags, creating xml file...");
		handler.fetchTags();
		System.out.println("Filtering Tag counts...");
		handler.filterCount();
		
		System.out.println("Processing data for clustering...");
		DataProcessor dp = new DataProcessor();
		dp.process();
		
		System.out.println("K-means clustering...");
		KMeans kmeans = new KMeans(dp.getBinaryPoints());
		kmeans.createClusters(KMEANS, LastFMHandler.MAXTAGS);
		kmeans.writeToFile();

		System.out.println("Done!");
	}
}
