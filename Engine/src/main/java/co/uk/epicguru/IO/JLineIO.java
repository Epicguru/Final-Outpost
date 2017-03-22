package co.uk.epicguru.IO;

import java.util.ArrayList;

import co.uk.epicguru.logging.Log;

public final class JLineIO {

	private static ArrayList<Character> chars = new ArrayList<Character>();
	private static final String TAG = "JLine IO";
	private static StringBuilder translator = new StringBuilder();
	static final char escape = '&';
	private static char[] escs = new char[]{
			escape,
			escape,

			'\n',
			'n',

			'\t',
			't',

			'\\',
			'\\',

			'\b',
			'b',

			'-',
			'-',

			'[',
			'[',

			']',
			']',

			'<',
			'<',

			'>',
			'>'
	};


	public static char[] getEscapableCharacters(){
		return escs;
	}
	
	public static boolean containsESCs(String string){
		for(int i = 0; i < string.length(); i++){
			char c = string.charAt(i);
			for(int j = 0; j < getEscapableCharacters().length; j += 2){
				if(c == getEscapableCharacters()[j])
					return true;
			}
		}
		
		return false;
	}
	
	static char getRealValue(char[] chars, char translated){
		for(int i = 1; i < chars.length; i += 2){
			if(chars[i] == translated){
				try{
					return chars[i - 1];					
				}catch(RuntimeException e){
					Log.error(TAG, "Failed to find non-translated version of char due to a parsing error : Hit start or end if array.\n"
							+ "Array Size : " + chars.length + '\n' 
							+ "Index : " + i + '\n'
							+ "Looking for : " + translated + '\n');
					throw e;
				}
			}
		}

		RuntimeException e = new RuntimeException("Error parsing JLineIO : Could not find translated char.");
		Log.error(TAG, "Failed to find escape character for translated char '" + translated + "'.", e);
		throw e;
	}

	static String translateIn(String raw){
		// Remove escapable characters
		char[] esc = getEscapableCharacters();
		char[] letters = raw.toCharArray();
		chars.clear();		

		for(int i = 0; i < letters.length; i++){

			char current = letters[i];

			boolean found = false;
			
			for(int j = 0; j < esc.length; j += 2){		
				
				if(current == esc[j]){
					// Translate this "bad" letter into a translated one
					char translated = esc[j + 1];
					chars.add(escape);
					chars.add(translated);
					j = esc.length; // The lazy inner break :D
					found = true;
				}
			}


			if(!found){
				chars.add(current);
			}
		}

		translator.setLength(0);

		for(char c : chars){
			translator.append(c);
		}
		
		if(chars.get(chars.size() - 1) == escape){
			translator.append(' ');
		}

		String result = translator.toString();
		translator.setLength(0);
		chars.clear();
		
		return result;
	}

	static String translateOut(String translated){
		char[] esc = getEscapableCharacters();
		char[] letters = translated.toCharArray();
		chars.clear();		

		for(int i = 0; i < letters.length; i++){
			
			char current = letters[i];
			if(current == escape){
				
				if(letters.length <= i + 1){
					RuntimeException e = new RuntimeException("Hit end of line when parsing escape character '" + escape + "'!");
					Log.error(TAG, "Error parsing escape character : No remaining characters to read!", e);
					throw e;
				}
				
				char next = letters[i + 1];				
				char real = getRealValue(esc, next);
				
				i++;
				
				chars.add(real);
				
				continue;
			}
			
			chars.add(current);
		}

		translator.setLength(0);

		for(char c : chars){
			translator.append(c);
		}

		String result = translator.toString();
		translator.setLength(0);
		chars.clear();
		
		return result;
	}
}
