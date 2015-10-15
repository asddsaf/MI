import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LastFMHandler {

	private ArrayList<String> artists;
	public static final String APIKEY = "7d7d86e0683f91595c5d6784f12da0c5";
	public static final String NAME = "nikolettk";
	public static final int MAXTAGS = 10;
	public static final int MAXCOUNT = 10;
	public static final int MAXARTISTS = 10;

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
							+ NAME + "&api_key=" + APIKEY);

			NodeList artistList = newDocument(url).getElementsByTagName("name");

			int checkMAX = 0;
			if (checkMAX < artistList.getLength())
				checkMAX = MAXARTISTS;
			else
				checkMAX = artistList.getLength();

			for (int temp = 0; temp < checkMAX; temp++) {
				Node node = artistList.item(temp);
				artists.add(node.getTextContent());
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<String> getArtistNames() {
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
								+ A + "&api_key=" + APIKEY);

				NodeList list = newDocument(url).getElementsByTagName("tag");

				int checkMAX = 0;
				if (checkMAX < list.getLength())
					checkMAX = MAXTAGS;
				else
					checkMAX = list.getLength();

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

				docToFile(doc, new File("T:\\artistTags.xml"));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
