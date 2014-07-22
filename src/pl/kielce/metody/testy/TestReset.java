package pl.kielce.metody.testy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Class1 implements Serializable {
	private static final long serialVersionUID = 1L;
	private int value = 1;
}

public class TestReset implements TestInterface {
	private String filename;
	
	public TestReset() {
		filename = "Testy/test_reset.ser";
	}
	
	@Override
	public void doTest() throws IOException {
		// TODO Generate TC_RESET in binary file
		ObjectOutputStream outputDataWriter = new ObjectOutputStream(new FileOutputStream(filename));
		outputDataWriter.writeObject(new Class1());
		outputDataWriter.close();
	}

	@Override
	public String getFilename() {
		return filename;
	}

}
