import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LastFMHandler {

	public static ArrayList<String> artists;
	public static final String APIKEY = "7d7d86e0683f91595c5d6784f12da0c5";
	public static final String NAME = "nikolettk";
	public static final int MAXTAGS = 100;
	public static final int MAXCOUNT = 100;
	public static final int MAXARTISTS = 100;

	public LastFMHandler() {
		artists = new ArrayList<String>();
	}

	private Document newDocument(URL url) {
		try {
			URLConnection connection = url.openConnection();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			final Document document = db.parse(connection.getInputStream());
			return document;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public void filterCount() {

		// ideiglenes tömb az előadókhoz tartozó tag átlag tárolására
		double artistAvg[] = new double[100];

		try {
			File fXmlFile = new File("C:\\new\\artistTags.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			// Az előadók kikeresése az xmlből
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression1 = "/artisttags/artist/@name";
			NodeList artistNodeList = (NodeList) xPath.compile(expression1)
					.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < artistNodeList.getLength(); i++) {
				int avg = 0;
				int sum = 0;

				// Az előadókhoz tartozó tagek countját kapjuk meg itt
				String artistName = artistNodeList.item(i).getNodeValue();
				String expression2 = "/artisttags/artist[@name=\"" + artistName
						+ "\"]/tag/@count";

				XPath xPath2 = XPathFactory.newInstance().newXPath();
				NodeList tagCountList = (NodeList) xPath2.compile(expression2)
						.evaluate(doc, XPathConstants.NODESET);
				// System.out.println(artistName);

				for (int j = 0; j < tagCountList.getLength(); j++) {
					sum = sum
							+ Integer.valueOf(tagCountList.item(j)
									.getNodeValue());
				}

				// Az átlag kiszámolása az adott előadóhoz és az érték
				// ideiglenes tömbbe helyezése
				if (sum != 0) {
					avg = sum / tagCountList.getLength();
					artistAvg[i] = avg;
				}

			}

			// kiválasztjuk az átlagon aluli értékű tageket
			for (int i = 0; i < artistNodeList.getLength(); i++) {

				String artistName = artistNodeList.item(i).getNodeValue();

				XPath xPath3 = XPathFactory.newInstance().newXPath();
				String expression3 = "/artisttags/artist[@name=\"" + artistName
						+ "\"]/tag[@count<=" + artistAvg[i] + "]";
				NodeList badTags = (NodeList) xPath3.compile(expression3)
						.evaluate(doc, XPathConstants.NODESET);


				// töröljük a rossz tageket
				for (int j = 0; j < badTags.getLength(); j++) {
					badTags.item(j).getParentNode()
							.removeChild(badTags.item(j));
				}

				// A törlés után üres sorok jönnek létre, ezért azokat itt
				// töröljük
				XPath xp = XPathFactory.newInstance().newXPath();
				NodeList emptyNodeList = (NodeList) xp.evaluate(
						"//text()[normalize-space(.)='']", doc,
						XPathConstants.NODESET);

				for (int k = 0; k < emptyNodeList.getLength(); ++k) {
					Node emptyNode = emptyNodeList.item(k);
					emptyNode.getParentNode().removeChild(emptyNode);
				}

				// a változások fájlba írása
				docToFile(doc, new File("C:\\new\\artistTags.xml"));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void docToFile(Document doc, File file) throws TransformerException {

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);

	}

	public void fetchArtistsByUser() {
		URL url;

		try {

			url = new URL(
					"http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user="
							+ NAME + "&limit=100&api_key=" + APIKEY);

			NodeList artistList = newDocument(url).getElementsByTagName("name");

			int checkMAX = artistList.getLength();
			if (checkMAX > MAXARTISTS) {
				checkMAX = MAXARTISTS;
			}

			for (int temp = 0; temp < checkMAX; temp++) {
				Node node = artistList.item(temp);
				artists.add(node.getTextContent());
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<String> getArtistNames() {
		return artists;
	}

	public void fetchTags() {

		URL url;

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;

			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("artisttags");
			doc.appendChild(rootElement);

			for (String A : artists) {

				Element t_artist = doc.createElement("artist");
				Attr t_artistName = doc.createAttribute("name");
				t_artistName.setValue(A);
				t_artist.setAttributeNode(t_artistName);
				doc.getDocumentElement().appendChild(t_artist);

				url = new URL(
						"http://ws.audioscrobbler.com/2.0/?method=artist.gettoptags&artist="
								+ URLEncoder.encode(A, "UTF-8") + "&api_key="
								+ APIKEY);

				NodeList list = newDocument(url).getElementsByTagName("tag");

				int checkMAX = list.getLength();
				if (checkMAX > MAXTAGS) {
					checkMAX = MAXTAGS;
				}

				for (int temp = 0; temp < checkMAX; temp++) {

					Node node = list.item(temp);

					Element t_tag = doc.createElement("tag");

					Attr t_tagCount = doc.createAttribute("count");
					t_tagCount.setValue(node.getChildNodes().item(0)
							.getTextContent().toString());
					t_tag.setAttributeNode(t_tagCount);

					Attr t_tagName = doc.createAttribute("name");
					t_tagName.setValue(node.getChildNodes().item(2)
							.getTextContent().toString());
					t_tag.setAttributeNode(t_tagName);

					t_artist.appendChild(t_tag);

				}

				docToFile(doc, new File("C:\\new\\artistTags.xml"));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
