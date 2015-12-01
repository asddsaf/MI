
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
		
		System.out.println("K-means clustering with binary vectors...");
		KMeans kmeans = new KMeans(dp.getBinaryPoints());
		kmeans.createClusters(KMEANS, dp.getTagCount());
		kmeans.writeToFile("kmeans_binary");
		
		System.out.println("K-means clustering with tagCount vectors...");
		kmeans = new KMeans(dp.getDecimalPoints());
		kmeans.createClusters(KMEANS, dp.getTagCount());
		kmeans.writeToFile("kmeans_tagcount");


		System.out.println("Done!");
	}
}
