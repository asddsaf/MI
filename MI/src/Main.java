import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;




public class Main {

	public static final String APIKEY = "7d7d86e0683f91595c5d6784f12da0c5";

	public static void main(String[] args) {

	try {
			URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptags&artist=vad%20fruttik&api_key="+APIKEY);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				System.out.println(strTemp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}




	
}
}