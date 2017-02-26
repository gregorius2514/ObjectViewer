package pl.kielce.metody.serializer;

import java.awt.List;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class TestClass implements Serializable {
    private static final long serialVersionUID = 1L;

    private int x;
    private float y;
    private String text;
    private Rectangle rect;
    private List lista;
    private int[] array;
    private boolean boolValue;
    private short shortValue;
    private long longValue;

    public TestClass() {
        rect = new Rectangle();
        lista = new List();
    }

    public TestClass(int x, int y, String text) {
        super();
        this.x = x;
        this.y = y;
        this.text = text;
        array = new int[10];
        for(int i = 0; i < array.length; i++)
            array[i] = i;
        boolValue = false;
        shortValue = 1;
        longValue = 1;
    }

    @Override
    public String toString() {
        return "TestClass [x=" + x + ", y=" + y + ", text=" + text + "]";
    }
}

public class CreatSerializedFile {
    public static void main(String[] args) throws IOException {
        TestClass test1 = new TestClass(1, 1, "hello");
        ObjectOutputStream outputDataWriter = new ObjectOutputStream(new FileOutputStream("test.ser"));
        outputDataWriter.writeObject(test1);
        outputDataWriter.writeObject(test1);
        outputDataWriter.close();
    }
}
