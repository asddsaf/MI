

public class Main {

	public static void main(String[] args) {

		LastFMHandler handler = new LastFMHandler();
		handler.fetchArtistsByUser();
		handler.fetchTags();
		
		
	}
}






/*	Working code

		
		
		URL url;
		try {
			url = new URL(
					"http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user=nikolettk&api_key="
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
			}
			
			// create another document
			
			
			


			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File("T:\\test.xml"));
			transformer.transform(source, result);
			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
			

			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




*/