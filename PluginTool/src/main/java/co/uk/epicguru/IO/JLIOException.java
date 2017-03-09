package co.uk.epicguru.IO;

public class JLIOException extends Exception{

	public JLIOException(String string) {
		super(string);
	}
	
	public JLIOException(String string, Exception e){
		super(string, e);
	}

	private static final long serialVersionUID = 8569720325104992032L;

}
