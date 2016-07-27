package it.TetrisReich.AppNanaCodeBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Download {
	public static String check = null;
	public static Boolean first = false;
	public static String dwn(String apii){
	URL url;
    InputStream is = null;
    BufferedReader br;
    String line;
  //{"dio":"cane","item":["video":{"1":"Frase v1","2":"Frase v2","3":"Frase v3"},"live":{"1":"Frase 1","2":"Frase 2","3":"Frase 3"}}
    try {
        url = new URL(apii);
        is = url.openStream();
        br = new BufferedReader(new InputStreamReader(is));

        while ((line = br.readLine()) != null) {
        	if(first==false){
        		check = line;
        		first = true;
        	} else check = check + line;
        }
    } catch (MalformedURLException mue) {
         mue.printStackTrace();
    } catch (IOException ioe) {
         ioe.printStackTrace();
    } finally {
        try {
            if (is != null) is.close();
        } catch (IOException ioe) {
            // cazzo voi
        }
    }
//String api = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCj9rv97Ms3Ing1g2QJZ3Scg&maxResults=1&order=date&key=AIzaSyBNNBKNYa3LrC4fjPyEMXRBTv9vp54v4S8";
    String checked = check;
    check = "";
    return checked;
	}
}
