package pl.kielce.metody.testy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

interface InvocationHandler{
	  Object invoke(Object proxy, Method method, Object[] args)
	         throws Throwable;
	}

class MyInvocationHandler implements InvocationHandler, Serializable{
	private static final long serialVersionUID = 1L;

	public Object invoke(Object proxy, Method method, Object[] args)
	  throws Throwable {
		return args;
	  }
	}

public class TestProxyClass implements TestInterface {
	private String filename;

	public TestProxyClass() {
		filename = "Testy/test_proxy.ser";
	}

	@Override
	public void doTest() throws IOException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// TODO Generate TC_PROXYCLASS in binary file
		ObjectOutputStream outputDataWriter = new ObjectOutputStream(
				new FileOutputStream(filename));

		outputDataWriter.writeObject(new MyInvocationHandler());
		outputDataWriter.close();
	}

	@Override
	public String getFilename() {
		return filename;
	}

}
