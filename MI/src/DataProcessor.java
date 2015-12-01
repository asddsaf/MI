import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataProcessor {

	// egyedi tagek listája
	private ArrayList<String> tags;
	// létrehozott pontok listája - bináris alakban
	private static ArrayList<Point> binaryPoints;
	//létrehozott pontok listája - count alapján, decimális alakban
	private static ArrayList<Point> decimalPoints;

	public DataProcessor() {
		this.tags = new ArrayList<String>();
		this.binaryPoints = new ArrayList<Point>();
	}

	// xml beolvasása ás a paramáterben megadott tag-ek visszaadása
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

	// Az xml fájlból kiolvassuk az összes tag-et és az egyedi tag-eket egy
	// listába tesszük
	private void filterTags() {

		// a tag-ek kiválasztása, majd listába rakása
		NodeList nList = fetchFromXml("tag");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node node = nList.item(temp);
			String tagName = node.getAttributes().getNamedItem("name")
					.getTextContent();

			if (!(tags.contains(tagName))) {
				tags.add(tagName);
			}

		}

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
			
			XPath xPath3 = XPathFactory.newInstance().newXPath();
			String expression3 = "/artisttags/artist/tag/@count";
			NodeList countNodeList = (NodeList) xPath3.compile(expression3)
					.evaluate(doc, XPathConstants.NODESET);

			// megkeresni azokat az elõadókat, akiknél van az adott tag
			for (int i = 0; i < tagNodeList.getLength(); i++) {

				int binaryArtistVector[] = new int[LastFMHandler.MAXARTISTS];

				String tagName = tagNodeList.item(i).getNodeValue();

				XPath xPath2 = XPathFactory.newInstance().newXPath();
				String expression2 = "/artisttags/artist[tag[@name=\""
						+ tagName + "\"]]";
				NodeList artistNodeList = (NodeList) xPath2.compile(expression2)
						.evaluate(doc, XPathConstants.NODESET);

				// System.out.println("Tag: "+tagName+"\n");

				// tömbbe beállítjuk az adott indexen az 1-est
				for (int j = 0; j < artistNodeList.getLength(); j++) {

					int indexOfArtist = LastFMHandler.getArtistNames().indexOf(
							artistNodeList.item(j).getAttributes()
									.getNamedItem("name").getTextContent());
					// System.out.println(indexOfArtist);
					binaryArtistVector[indexOfArtist] = 1;
					// System.out.println(artistNodeList.item(j).getAttributes().getNamedItem("name").getTextContent());

				}

				// A tags-be egyszer már kigyûjtöttük az egyedi tageket, itt ezt
				// hasznájuk fel úgy, hogy ha még nem volt az adott tag, akkor
				// töröljük belõle és hozzáadjuk a pontok halmazához, így ha
				// mégegyszer ugyanaz a tag következne, az már nem kerül bele
				if (tags.contains(tagName)) {
					binaryPoints.add(new BinaryPoint(tagName, binaryArtistVector));
					tags.remove(tags.indexOf(tagName));
				}

				PrintWriter writer = new PrintWriter("C:\\new\\debug.txt",
						"UTF-8");

				for (Point p : binaryPoints) {
					writer.println(p.getName()
							+ "\n****************************");
					for (int k : p.getArtists())
						writer.println(k);
				}

				writer.close();
				// System.out.println("\n*****************************************************");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	// A feldolgozás minden része ebben a függvényben fut le
	public void process() {
		filterTags();
		createPoints();
		
	}

	public ArrayList<Point> getBinaryPoints(){
		return binaryPoints;
	}
	
	public ArrayList<Point> getDecimalPoints(){
		return decimalPoints;
	}
}
