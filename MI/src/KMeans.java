import java.util.ArrayList;


public class KMeans {

	ArrayList<Point> points;
	ArrayList<Cluster> clusters;
	
	public KMeans(){
		points = new ArrayList<Point>();
		clusters = new ArrayList<Cluster>();
		
		points = DataProcessor.getPoints();
	}
	
	//kell k: hány klasztert akarunk, és kellenek a kezdõ centroidok (k db)
	//végén: az kell, hogy az egyes klaszterekben mely pontok vannak együtt
	public void createClusters(int k, Point[] initialCentroids){
		//létrehozunk k üres klasztert
		for (int i = 0; i<k; i++) {
			clusters.add(new Cluster(new ArrayList<Point>(), initialCentroids[i]));
		}
		
		boolean changed = true; //addig megyünk, amíg elõfordul olyan, hogy 1 pontot másik klaszterbe sorolunk
		
		while(changed) {
			
			for (int i = 0; i<points.size(); i++) { //végigmegyünk az összes ponton
				
				int distance = 150; //ez béna, de egyelõre megteszi
				int clusterNum = 0;
				
				for (int j = 0; j<k; j++) { //és megkeressük, melyik klaszterhez van közelebb
					int tempDistance = Point.distance(points.get(i), clusters.get(j).getCentroid());
					if (tempDistance < distance) {
						distance = tempDistance;
						clusterNum = j;
					}
				}
				
				//TODO: i. pont kivétele az aktuális klaszterébõl, ha az más, mint a most megtalált
				//TODO: changed változót állítani
				
				//megtaláltuk az i. ponthoz tartozó legközelebbi klasztert (clusterNum), ebbe soroljuk
				clusters.get(clusterNum).addPoint(points.get(i));
			}
			//miután minden pontot besoroltunk a megfelelõ klaszterbe, kiszámítjuk a klaszterek új centroidját
			for (int i = 0; i<k; i++) {
				Point newCentroid = new Point("centroid", clusters.get(i).calculateCentroid());
				clusters.get(i).setCentroid(newCentroid);
			}
		}
	}
	
	
}
