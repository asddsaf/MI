import java.util.ArrayList;

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

		
		
		//Bináris k-means 20 klaszterre
		System.out.println("K-means clustering with binary vectors, 20 clusters...");
		startTime = System.nanoTime();
		KMeans kmeans = new KMeans(BinaryPoint.class, dp.getBinaryPoints());
		ArrayList<Integer> ic2 = kmeans.chooseCentroids(20, dp.getTagCount());
		ArrayList<BinaryPoint> initialCentroids2 = new ArrayList<BinaryPoint>();
		for (int i = 0; i<ic2.size(); i++) {
			initialCentroids2.add(dp.getBinaryPoints().get(ic2.get(i)));
		}
		kmeans.createClusters(20, dp.getTagCount(), initialCentroids2);
		kmeans.writeToFile("kmeans_binary_20");
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("Execution time: " + elapsedTime / 1000000
				+ " milliseconds");
		
		//Bináris k-means 25 klaszterre
		System.out.println("K-means clustering with binary vectors, 25 clusters...");
		startTime = System.nanoTime();
		kmeans = new KMeans(BinaryPoint.class, dp.getBinaryPoints());
		ArrayList<Integer> ic3 = kmeans.chooseCentroids(25, dp.getTagCount());
		ArrayList<BinaryPoint> initialCentroids3 = new ArrayList<BinaryPoint>();
		for (int i = 0; i<ic3.size(); i++) {
			initialCentroids3.add(dp.getBinaryPoints().get(ic3.get(i)));
		}
		kmeans.createClusters(25, dp.getTagCount(), initialCentroids3);
		kmeans.writeToFile("kmeans_binary_25");
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("Execution time: " + elapsedTime / 1000000
				+ " milliseconds");
		
		
		
		//Decimális 25 klaszterre
		//Azonos kezdõpontokra futtatom a bináris-25 és a decimális-25-öt
		 System.out.println("K-means clustering with tagCount vectors, 25 clusters, L2 norm...");
		 startTime = System.nanoTime();
		 kmeans = new KMeans(DecimalPoint.class, dp.getDecimalPoints());
		 ArrayList<DecimalPoint> initialCentroids3d = new ArrayList<DecimalPoint>();
		 for (int i = 0; i<ic3.size(); i++) {
			 initialCentroids3d.add(dp.getDecimalPoints().get(ic3.get(i)));
		 }
		 kmeans.createClusters(15, dp.getTagCount(), initialCentroids3d);
		 kmeans.writeToFile("kmeans_tagcount_25_L2");
		 elapsedTime = System.nanoTime() - startTime;
		 System.out.println("Execution time: " + elapsedTime / 1000000
				 + " milliseconds");
		 
		 
	
//		//Hierarchikus, 20 klaszter, L2 norma
//		 System.out.println("Hierarchical clustering with tagCount vectors, 20 clusters, L2 norm...");
//		 startTime = System.nanoTime();
//		 Hierarchical hierarchical = new Hierarchical(dp.getDecimalPoints());
//		 hierarchical.createClusters(20);
//		 hierarchical.writeToFile("hierarchical_tagcount_20_L2");
//			elapsedTime = System.nanoTime() - startTime;
//			System.out.println("Execution time: " + elapsedTime / 1000000
//					+ " milliseconds");
//				
//		//Hierarchikus, 25 klaszter, L2 norma
//		 System.out.println("Hierarchical clustering with tagCount vectors, 25 clusters, L2 norm...");
//		 startTime = System.nanoTime();
//		 hierarchical = new Hierarchical(dp.getDecimalPoints());
//		 hierarchical.createClusters(25);
//		 hierarchical.writeToFile("hierarchical_tagcount_25_L2");
//			elapsedTime = System.nanoTime() - startTime;
//			System.out.println("Execution time: " + elapsedTime / 1000000
//					+ " milliseconds");
//			
//		//Hierarchikus, 30 klaszter, L2 norma
//		 System.out.println("Hierarchical clustering with tagCount vectors, 30 clusters, L2 norm...");
//		 startTime = System.nanoTime();
//		 hierarchical = new Hierarchical(dp.getDecimalPoints());
//		 hierarchical.createClusters(30);
//		 hierarchical.writeToFile("hierarchical_tagcount_30_L2");
//			elapsedTime = System.nanoTime() - startTime;
//			System.out.println("Execution time: " + elapsedTime / 1000000
//					+ " milliseconds");

		System.out.println("Done!");
	}
}
