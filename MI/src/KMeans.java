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
	//v�g�n: az kell, hogy az egyes klaszterekben mely pontok vannak egy�tt
	public void createClusters(int k, Point[] initialCentroids){
		//l�trehozunk k �res klasztert
		for (int i = 0; i<k; i++) {
			clusters.add(new Cluster(new ArrayList<Point>(), initialCentroids[i]));
		}
		
		boolean changed = true; //addig megy�nk, am�g el�fordul olyan, hogy 1 pontot m�sik klaszterbe sorolunk
		
		while(changed) {
			
			for (int i = 0; i<points.size(); i++) { //v�gigmegy�nk az �sszes ponton
				
				int distance = 150; //ez b�na, de egyel�re megteszi
				int clusterNum = 0;
				
				for (int j = 0; j<k; j++) { //�s megkeress�k, melyik klaszterhez van k�zelebb
					int tempDistance = Point.distance(points.get(i), clusters.get(j).getCentroid());
					if (tempDistance < distance) {
						distance = tempDistance;
						clusterNum = j;
					}
				}
				
				//TODO: i. pont kiv�tele az aktu�lis klaszter�b�l, ha az m�s, mint a most megtal�lt
				//TODO: changed v�ltoz�t �ll�tani
				
				//megtal�ltuk az i. ponthoz tartoz� legk�zelebbi klasztert (clusterNum), ebbe soroljuk
				clusters.get(clusterNum).addPoint(points.get(i));
			}
			//miut�n minden pontot besoroltunk a megfelel� klaszterbe, kisz�m�tjuk a klaszterek �j centroidj�t
			for (int i = 0; i<k; i++) {
				Point newCentroid = new Point("centroid", clusters.get(i).calculateCentroid());
				clusters.get(i).setCentroid(newCentroid);
			}
		}
	}
	
	
}
