import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;




public class Main {

	public static final String APIKEY = "7d7d86e0683f91595c5d6784f12da0c5";

	public static void main(String[] args) {

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("data.xml"));
			//Get top artists of nikolettk
			URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user=nikolettk&limit=10&api_key="+APIKEY);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String strTemp = "";
			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			bw.write("<root>");
			while (null != (strTemp = br.readLine())) {
				int startIndex = strTemp.indexOf("<name>");
				if ( startIndex != -1) {
					int endIndex = strTemp.indexOf("</name>");
					String artist = strTemp.substring(startIndex+6, endIndex);
					URL artistUrl = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptags&artist=" + artist + "&api_key="+APIKEY);					
					BufferedReader artistBr = new BufferedReader(new InputStreamReader(artistUrl.openStream()));
					String artistTemp = "";
					while ((artistTemp = artistBr.readLine())!= null) {
						if (artistTemp.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>") != -1) {
							artistTemp = artistBr.readLine();
						}
						if ((artistTemp.indexOf("<lfm") != -1)) {
							artistTemp = artistTemp.replace("<lfm status=\"ok\">", "");
						}
						if ((artistTemp.indexOf("</lfm") != -1)) {
							artistTemp = artistTemp.replace("</lfm>", "");
						}
						System.out.println(artistTemp);
						bw.write(artistTemp);
					}
				}
			}
			bw.write("</root>");
			bw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
}