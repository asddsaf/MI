import java.io.File;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataProcessor {

	// egyedi tagek list�ja
	private ArrayList<String> tags;
	//egyedi tagek sz�ma
	private int tagCount;
	// l�trehozott pontok list�ja - bin�ris alakban
	private static ArrayList<BinaryPoint> binaryPoints;
	//l�trehozott pontok list�ja - count alapj�n, decim�lis alakban
	private static ArrayList<DecimalPoint> decimalPoints;

	public DataProcessor() {
		this.tags = new ArrayList<String>();
		DataProcessor.binaryPoints = new ArrayList<BinaryPoint>();
		DataProcessor.decimalPoints = new ArrayList<DecimalPoint>();
	}

	// xml beolvas�sa �s a param�terben megadott tag-ek visszaad�sa
	private NodeList fetchFromXml(String tagName) {

		try {
			File fXmlFile = new File("C:\\new\\artistTags.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName(tagName);

			return nodeList;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	// Az xml f�jlb�l kiolvassuk az �sszes tag-et �s az egyedi tag-eket egy
	// list�ba tessz�k
	private void filterTags() {

		// a tag-ek kiv�laszt�sa, majd list�ba rak�sa
		NodeList nList = fetchFromXml("tag");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node node = nList.item(temp);
			String tagName = node.getAttributes().getNamedItem("name")
					.getTextContent();

			if (!(tags.contains(tagName))) {
				tags.add(tagName);
			}

		}
		
		tagCount = tags.size();

	}

	
	private void createPoints() {

		try {
			File fXmlFile = new File("C:\\new\\artistTags.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression1 = "/artisttags/artist/tag/@name";
			NodeList tagNodeList = (NodeList) xPath.compile(expression1)
					.evaluate(doc, XPathConstants.NODESET);

			// megkeresni azokat az el�ad�kat, akikn�l van az adott tag
			for (int i = 0; i < tagNodeList.getLength(); i++) {

				int binaryArtistVector[] = new int[LastFMHandler.MAXARTISTS];
				int decimalArtistVector[] = new int[LastFMHandler.MAXARTISTS];

				String tagName = tagNodeList.item(i).getNodeValue();

				XPath xPath2 = XPathFactory.newInstance().newXPath();
				String expression2 = "/artisttags/artist[tag[@name=\""
						+ tagName + "\"]]";
				NodeList artistNodeList = (NodeList) xPath2.compile(expression2)
						.evaluate(doc, XPathConstants.NODESET);
				
				
				// t�mbbe be�ll�tjuk az adott indexen az 1-est (bin�ris) �s a s�lyt (decim�lis)
				for (int j = 0; j < artistNodeList.getLength(); j++) {

					int indexOfArtist = LastFMHandler.getArtistNames().indexOf(
							artistNodeList.item(j).getAttributes()
									.getNamedItem("name").getTextContent());
					binaryArtistVector[indexOfArtist] = 1;
					
					//kell: a kiv�lasztott el�ad�kn�l mennyi ennek a tagnek a s�lya
					XPath xPath3 = XPathFactory.newInstance().newXPath();
					String expression3 = "/artisttags/artist[@name=\"" 
							+ artistNodeList.item(j).getAttributes().getNamedItem("name").getTextContent()
							+ "\"]/tag[@name=\"" + tagName + "\"]";
					NodeList countNodeList = (NodeList) xPath3.compile(expression3)
							.evaluate(doc, XPathConstants.NODESET);
					
					decimalArtistVector[indexOfArtist] = Integer.parseInt(
							countNodeList.item(0).getAttributes().getNamedItem("count").getTextContent());

				}

				// A tags-be egyszer m�r kigy�jt�tt�k az egyedi tageket, itt ezt
				// haszn�juk fel �gy, hogy ha m�g nem volt az adott tag, akkor
				// t�r�lj�k bel�le �s hozz�adjuk a pontok halmaz�hoz, �gy ha
				// m�gegyszer ugyanaz a tag k�vetkezne, az m�r nem ker�l bele
				if (tags.contains(tagName)) {
					binaryPoints.add(new BinaryPoint(tagName, binaryArtistVector));
					decimalPoints.add(new DecimalPoint(tagName, decimalArtistVector));
					tags.remove(tags.indexOf(tagName));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	// A feldolgoz�s minden r�sze ebben a f�ggv�nyben fut le
	public void process() {
		filterTags();
		createPoints();
		
	}

	public ArrayList<BinaryPoint> getBinaryPoints(){
		return binaryPoints;
	}
	
	public ArrayList<DecimalPoint> getDecimalPoints(){
		return decimalPoints;
	}
	
	public int getTagCount() {
		if (tagCount>LastFMHandler.MAXTAGS) return LastFMHandler.MAXTAGS;
		return tagCount;
	}
}
