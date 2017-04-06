package co.uk.epicguru.launcher.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import co.uk.epicguru.launcher.Main;

public final class General {

	private General(){ }
	private static StringBuilder str = new StringBuilder();
	private static ArrayList<String> strArr = new ArrayList<String>();

	public static boolean isConnected(){
		return checkConnectionTo(Main.base);
	}
	
	public static boolean checkConnectionTo(String location){
		try {
			final URL url = new URL(location);
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
	}

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

	public static String[] readLines(String... path) throws Exception{	
		str.setLength(0);
		for(String s : path){
			if(s != null)
				str.append(s);
		}
		
		String p = str.toString();
		str.setLength(0);
		
		URL url = newURL(p);
		InputStreamReader in = newReaderStream(url);
		BufferedReader reader = new BufferedReader(in);
		strArr.clear();
		
		String line;
		while((line = reader.readLine()) != null){
			strArr.add(line);
		}
		String[] stringArray = strArr.toArray(new String[0]);
		strArr.clear();
		str.setLength(0);
		
		return stringArray;
	}
}
