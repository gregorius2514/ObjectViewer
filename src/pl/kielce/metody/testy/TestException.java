package pl.kielce.metody.testy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class Class3 {
	public int value;	
}

public class TestException implements TestInterface{
	private String filename;
	
	public TestException() {
		filename = "Testy/test_exception.ser";
	}
	
	public void doTest() throws IOException{
		// TODO Generate TC_EXCEPTION in binary file
		ObjectOutputStream outputDataWriter = new ObjectOutputStream(new FileOutputStream(filename));
		outputDataWriter.writeObject(new Class3());
		outputDataWriter.close();
	}

	@Override
	public String getFilename() {
		return filename;
	}

}
