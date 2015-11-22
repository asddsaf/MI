import java.util.ArrayList;


public class KMeans {

	ArrayList<Point> points;
	ArrayList<Cluster> clusters;
	
	public KMeans(){
		points = new ArrayList<Point>();
		clusters = new ArrayList<Cluster>();
		
		points = DataProcessor.getPoints();
	}
	
	//kell k: h�ny klasztert akarunk, �s kellenek a kezd� centroidok (k db)
	//v�g�n: kell, hogy az egyes klaszterekben mely pontok vannak egy�tt
	public void createClusters(int k, Point[] initialCentroids){
		
		//l�trehozunk k �res klasztert
		for (int i = 0; i<k; i++) {
			clusters.add(new Cluster(new ArrayList<Point>(), initialCentroids[i]));
		}
		
		boolean changed = true; //addig megy�nk, am�g el�fordul olyan, hogy 1 pontot m�sik klaszterbe sorolunk
		
		boolean clusterChanged = false; //klaszterv�lt�s jelz�se
		
		while(changed) {
			
			for (int i = 0; i<points.size(); i++) { //v�gigmegy�nk az �sszes ponton
				
				int distance = 150; //ez b�na, de egyel�re megteszi
				int nextCluster = 0;		
				int actualCluster = 0;
				
				for (int j = 0; j<k; j++) { //�s megkeress�k, melyik klaszterhez van k�zelebb
					
					//elt�roljuk, hogy melyik klaszterben van most a pont
					actualCluster = clusters.get(j).getPoints().indexOf(points.get(i));
					
					int tempDistance = Point.distance(points.get(i), clusters.get(j).getCentroid());
					if (tempDistance < distance) {
						distance = tempDistance;
						nextCluster = j;
					}
				}
				
				clusterChanged = false;
				//i. pont kiv�tele az aktu�lis klaszter�b�l, ha az m�s, mint a most megtal�lt
				if (actualCluster != -1 && nextCluster != actualCluster) {
					
					clusters.get(actualCluster).removePoint(points.get(i));
					
					//i. pontot besoroljuk a megtal�lt klaszterbe
					clusters.get(nextCluster).addPoint(points.get(i));
					
					//jelezz�k, hogy t�rt�nt klaszterv�lt�s
					clusterChanged = true;
				}
				else if (actualCluster == -1) {
					//i. pontot besoroljuk a megtal�lt klaszterbe, ha �ppen nincs egy klaszterben sem (els� iter�ci�n�l fordul el�)
					clusters.get(nextCluster).addPoint(points.get(i));
				}
				
				//ha nem t�rt�nt �tsorol�s (egy pont m�sik klaszterbe tev�se), akkor meg�ll az algoritmus
				if (!clusterChanged) {
					changed = false;
				}
			}
			
			if (clusterChanged) {
				//miut�n minden pontot besoroltunk a megfelel� klaszterbe, kisz�m�tjuk a klaszterek �j centroidj�t
				for (int i = 0; i<k; i++) {
					
					Point newCentroid = new Point("centroid", clusters.get(i).calculateCentroid());
					
					clusters.get(i).setCentroid(newCentroid);
				}
			}
		}
	}
	
	
}
