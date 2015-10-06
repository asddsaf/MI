import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
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
	private ArrayList<String> tagNames;
	private ArrayList<String> tagCounts;
	public static final String APIKEY = "7d7d86e0683f91595c5d6784f12da0c5";
	public static final String NAME = "nikolettk";

	private Document doc;
	public LastFMHandler() {
		artists = new ArrayList<String>();
		initDoc();
	}

	public void fetchArtistsByUser() {
		URL url;
		try {
			url = new URL(
					"http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user="+NAME+"&api_key="
							+ APIKEY);

			// Read result into a Document object

			URLConnection connection = url.openConnection();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			final Document document = db.parse(connection.getInputStream());
			// ***
			NodeList list = document.getElementsByTagName("name");

			for (int temp = 0; temp < list.getLength(); temp++) {
				Node node = list.item(temp);
				artists.add(node.getTextContent());
			}
			
			/*
			for (String S : artists) {
				System.out.println(S);
			}

			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public ArrayList<String> getArtistNames(){
		return artists;
	}
	
	public void fetchTags(){
		
		URL url;

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			
			for(String A: artists){
				
				Element artist = doc.createElement("artist");
				artist.setAttribute("name", A);
				
				
				url = new URL(
						"http://ws.audioscrobbler.com/2.0/?method=artist.gettoptags&artist="+A+"&api_key="
								+ APIKEY);
			
				URLConnection connection = url.openConnection();
				
				Document document = db.newDocument();
				
				document = db.parse(connection.getInputStream());
				
				NodeList list = document.getElementsByTagName("tag");

				for (int temp = 0; temp < list.getLength(); temp++) {
					Node node = list.item(temp);
					
					Element tag = doc.createElement("tag");
					doc.appendChild(tag);
					tag.setAttribute("name",  node.getChildNodes().item(1).getTextContent());

				
				
					

				}
			}
				
				

				/*
				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						url.openStream()));
				String strTemp = "";
				while (null != (strTemp = br.readLine())) {
					System.out.println(strTemp);
				}
				
				
				*/
			
			


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	private void initDoc(){
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("artisttags");
			doc.appendChild(rootElement);
			
			
			Element artist = doc.createElement("artist");
			rootElement.appendChild(artist);
			Attr name = doc.createAttribute("name");
			artist.setAttributeNode(name);
			Element tag = doc.createElement("tag");
			rootElement.appendChild(tag);
			Attr count = doc.createAttribute("count");
			tag.setAttributeNode(count);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// root elements


		
	}
	
}
