
public class Main {

	
	private static final int K = 15;
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {

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
		KMeans kmeans = new KMeans(BinaryPoint.class, dp.getBinaryPoints());
		kmeans.createClusters(K, dp.getTagCount());
		kmeans.writeToFile("kmeans_binary");
		
		System.out.println("K-means clustering with tagCount vectors...");
		kmeans = new KMeans(DecimalPoint.class, dp.getDecimalPoints());
		kmeans.createClusters(K, dp.getTagCount());
		kmeans.writeToFile("kmeans_tagcount");
		
		System.out.println("Hierarchical clustering with tagCount vectors...");
		Hierarchical hierarchical = new Hierarchical(dp.getDecimalPoints());
		hierarchical.createClusters(K);
		hierarchical.writeToFile("hierarchical_tagcount");

		System.out.println("Done!");
	}
}
