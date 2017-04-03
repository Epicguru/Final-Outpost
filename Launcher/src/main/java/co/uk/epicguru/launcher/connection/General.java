package co.uk.epicguru.launcher.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import co.uk.epicguru.launcher.Main;

public final class General {

	private General(){ }
	
	public static URL newURL(String web) throws MalformedURLException{
		URL url;
		try {
			url = new URL(web);
			return url;
		} catch (MalformedURLException e) {
			throw e;
		}
	}
	
	public static InputStreamReader newReaderStream(URL url) throws IOException{
		
		if(url == null){
			throw new IllegalArgumentException("URL value for input stream cannot be null!");
		}
		
		try {
			return new InputStreamReader(url.openStream());
		} catch (IOException e) {
			throw e;
		}
	}
	
	public static void downloadLatestTxt() throws Exception{	
		
        BufferedReader in = new BufferedReader(newReaderStream(newURL(Main.base + Main.latestVersion)));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
	}
}
