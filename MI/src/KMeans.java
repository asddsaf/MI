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
	//végén: kell, hogy az egyes klaszterekben mely pontok vannak együtt
	public void createClusters(int k, Point[] initialCentroids){
		
		//létrehozunk k üres klasztert
		for (int i = 0; i<k; i++) {
			clusters.add(new Cluster(new ArrayList<Point>(), initialCentroids[i]));
		}
		
		boolean changed = true; //addig megyünk, amíg elõfordul olyan, hogy 1 pontot másik klaszterbe sorolunk
		
		boolean clusterChanged = false; //klaszterváltás jelzése
		
		while(changed) {
			
			for (int i = 0; i<points.size(); i++) { //végigmegyünk az összes ponton
				
				int distance = 150; //ez béna, de egyelõre megteszi
				int nextCluster = 0;		
				int actualCluster = 0;
				
				for (int j = 0; j<k; j++) { //és megkeressük, melyik klaszterhez van közelebb
					
					//eltároljuk, hogy melyik klaszterben van most a pont
					actualCluster = clusters.get(j).getPoints().indexOf(points.get(i));
					
					int tempDistance = Point.distance(points.get(i), clusters.get(j).getCentroid());
					if (tempDistance < distance) {
						distance = tempDistance;
						nextCluster = j;
					}
				}
				
				clusterChanged = false;
				//i. pont kivétele az aktuális klaszterébõl, ha az más, mint a most megtalált
				if (actualCluster != -1 && nextCluster != actualCluster) {
					
					clusters.get(actualCluster).removePoint(points.get(i));
					
					//i. pontot besoroljuk a megtalált klaszterbe
					clusters.get(nextCluster).addPoint(points.get(i));
					
					//jelezzük, hogy történt klaszterváltás
					clusterChanged = true;
				}
				else if (actualCluster == -1) {
					//i. pontot besoroljuk a megtalált klaszterbe, ha éppen nincs egy klaszterben sem (elsõ iterációnál fordul elõ)
					clusters.get(nextCluster).addPoint(points.get(i));
				}
				
				//ha nem történt átsorolás (egy pont másik klaszterbe tevése), akkor megáll az algoritmus
				if (!clusterChanged) {
					changed = false;
				}
			}
			
			if (clusterChanged) {
				//miután minden pontot besoroltunk a megfelelõ klaszterbe, kiszámítjuk a klaszterek új centroidját
				for (int i = 0; i<k; i++) {
					
					Point newCentroid = new Point("centroid", clusters.get(i).calculateCentroid());
					
					clusters.get(i).setCentroid(newCentroid);
				}
			}
		}
	}
	
	
}
