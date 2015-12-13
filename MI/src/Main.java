public class Main {

	private static final int K = 15;

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException {

		LastFMHandler handler = new LastFMHandler();
		System.out.println("Fetching Artists...");
		long startTime = System.nanoTime();
		handler.fetchArtistsByUser();
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Execution time: " + elapsedTime / 1000000
				+ " milliseconds");

		System.out.println("Fetching Tags, creating xml file...");
		startTime = System.nanoTime();
		handler.fetchTags();
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("Execution time: " + elapsedTime / 1000000
				+ " milliseconds");

		System.out.println("Filtering Tag counts...");
		startTime = System.nanoTime();
		handler.filterCount();
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("Execution time: " + elapsedTime / 1000000
				+ " milliseconds");

		System.out.println("Processing data for clustering...");
		startTime = System.nanoTime();
		DataProcessor dp = new DataProcessor();
		dp.process();
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("Execution time: " + elapsedTime / 1000000
				+ " milliseconds");

		System.out.println("K-means clustering with binary vectors...");
		startTime = System.nanoTime();
		KMeans kmeans = new KMeans(BinaryPoint.class, dp.getBinaryPoints());
		kmeans.createClusters(K, dp.getTagCount());
		kmeans.writeToFile("kmeans_binary");
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("Execution time: " + elapsedTime / 1000000
				+ " milliseconds");

		 System.out.println("K-means clustering with tagCount vectors...");
		 startTime = System.nanoTime();
		 kmeans = new KMeans(DecimalPoint.class, dp.getDecimalPoints());
		 kmeans.createClusters(K, dp.getTagCount());
		 kmeans.writeToFile("kmeans_tagcount");
		 elapsedTime = System.nanoTime() - startTime;
		 System.out.println("Execution time: " + elapsedTime / 1000000
					+ " milliseconds");
		 
		 System.out.println("Hierarchical clustering with tagCount vectors...");
		 startTime = System.nanoTime();
		 Hierarchical hierarchical = new Hierarchical(dp.getDecimalPoints());
		 hierarchical.createClusters(K);
		 hierarchical.writeToFile("hierarchical_tagcount");
			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Execution time: " + elapsedTime / 1000000
					+ " milliseconds");

		System.out.println("Done!");
	}
}
