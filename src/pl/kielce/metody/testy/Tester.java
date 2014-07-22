package pl.kielce.metody.testy;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import pl.kielce.metody.serializer.CommonTypes;

public class Tester {
	private String filename;
	private File file;
	private DataInputStream inputDataReader;
	private ByteBuffer byteConverter;
	private byte[] buffer;
	
	public Tester() {
	}
	
	void checkFile(String filename) throws Exception {
		this.filename = filename;
		file = new File(filename);
		try {
			inputDataReader = new DataInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			System.err.println("File " + filename + " Not Exist");
			e1.printStackTrace();
			try {
				inputDataReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		buffer = new byte[(int) file.length()];
		
		loadData();
		checkConditions();
	}

	private void checkConditions() throws Exception {
		for (byte value : buffer) {
			if ((0xff & value) == CommonTypes.TC_RESET) {
//				throw new Exception("TC_RESET was found in the file " + this.filename);
				System.out.println("TC_RESET was found");
			}else if((0xff & value) == CommonTypes.TC_EXCEPTION) {
//				throw new Exception("TC_EXCEPTION was found in the file " + this.filename);
				System.out.println("TC_EXCEPTION was found");
			}else if((0xff & value) == CommonTypes.TC_PROXYCLASSDESC) {
//				throw new Exception("TC_PROXYCLASSDESC was found in the file " + this.filename);
				System.out.println("TC_PROXYCLASSDESC was found");
			}
		}
	}

	private void loadData() {
		try {
			inputDataReader.read(buffer);
		} catch (IOException e) {
			System.err.println("Reading error from file " + filename);
			e.printStackTrace();
		} finally {
			try {
				inputDataReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Tester tester = new Tester();
		
		TestReset tcReset = new TestReset();
		TestProxyClass tcProxy = new TestProxyClass();
		TestException tcException = new TestException();
		
//		tcReset.doTest();
//		tcException.doTest();
//		tcProxy.doTest();
		
//		tester.checkFile(tcReset.getFilename());
//		tester.checkFile(tcException.getFilename());
		tester.checkFile(tcProxy.getFilename());
	}

}
