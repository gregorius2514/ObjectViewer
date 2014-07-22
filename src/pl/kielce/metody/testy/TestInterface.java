package pl.kielce.metody.testy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface TestInterface {
	public void doTest() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
	public String getFilename();
}
