package pl.kielce.metody.serializer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.TreeItem;

public class ReadSerializedFile {
    private TreeItem<String> rootObject;
    private List<TreeItem<String>> listOfArrayItems;
    private String filename;
    private File file;
    private DataInputStream inputDataReader;
    private ByteBuffer byteConverter;
    private byte[] buffer;
    int referencesToObject = 0;

    public ReadSerializedFile(String filename) {
        rootObject = new TreeItem<String>("Object");
        listOfArrayItems = new ArrayList<TreeItem<String>>();

        if (filename.length() == 0) {
            this.filename = "test.ser";
        }
        else {
            this.filename = filename;
        }

        file = new File(filename);
        try {
            inputDataReader = new DataInputStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e1) {
            System.err.println("File " + filename + " Not Exist");
            e1.printStackTrace();

            try {
                inputDataReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        buffer = new byte[(int) file.length()];
        
        // Loading data from file to byte array
        loadBytes();
        checkFile();
    }

    public int getRefs() {
        return referencesToObject;
    }

    private void loadBytes() {
        try {
            inputDataReader.read(buffer);
        } 
        catch (IOException e) {
            System.err.println("Reading error from file " + filename);
            e.printStackTrace();
        }
        finally {
            try {
                inputDataReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkFile() {
        for (int i = 0; i < buffer.length; i++) {
            if ((0xff & buffer[i]) == CommonTypes.INT) {
                addAttribute(i);
            }
            else if ((0xff & buffer[i]) == CommonTypes.FLOAT) {
                addAttribute(i);
            }
            else if ((0xff & buffer[i]) == CommonTypes.BOOLEAN || (0xff & buffer[i]) == CommonTypes.SHORT || ((0xff & buffer[i]) == CommonTypes.LONG && buffer[i+1] == 0)) {
                byteConverter = ByteBuffer.wrap(buffer, i+2, 2);
                int size = byteConverter.get();
                byte[] value = Arrays.copyOfRange(buffer, i+3, i+3+size);
                String valueString = new String(value);
                TreeItem<String> treeElement = new TreeItem<String>(valueString);
                rootObject.getChildren().add(treeElement);
            }
            else if ((0xff & buffer[i]) == CommonTypes.OBJECT && (0xff & buffer[i + 1]) == 0) {
                byteConverter = ByteBuffer.wrap(buffer, i + 2, 2);
                int size = byteConverter.get();
                byte[] tmp = Arrays.copyOfRange(buffer, i + 3, i + 3 + size);
                String obj = new String(tmp);
                TreeItem<String> attr = new TreeItem<String>(obj);
                rootObject.getChildren().add(attr);

                // size - value - t letter - >> value of attr
                byteConverter = ByteBuffer.wrap(buffer, i + 4 + size + 1, 2);

                // size of object value
                int size2 = byteConverter.get();
                byte[] tmp2 = Arrays.copyOfRange(buffer, i + 7 + size, i + 5
                        + size + size2);
                TreeItem<String> attrValue = new TreeItem<String>(new String(
                            tmp2));
                attr.getChildren().add(attrValue);
            }
            else if ((0xff & buffer[i]) == CommonTypes.ARRAY) {
                /*
                 * Jesli nie dziala ten sposob z dodaniem elementow do listy treeItemow
                 * to sprobuj z statycznym polem gdzie jest sprawdzane ktore pole aktualnie jest obslugiwane
                 * np po oddaniu nowego elementu ustaw ze 1 pole czeka na obrobke
                 * i po dodaniu lementow tablicy zwieksz to pole jesli zmienna_sprawdzajaca < rozmiar_listy-1
                 * i w pliku prawdopodobnie elementy listy sa zapisane w sekwencji tablica 1 : 0,1,2
                 * tablica 2 : 3,4,5 tablica 3: ........ wiec wszystko powinno dzialac
                 */
                if (checkConstType(buffer[i + 1]) == null && (0xff & buffer[i+2]) != 77) {

                    byteConverter = ByteBuffer.wrap(buffer, i + 2, 2);
                    int arrayNameSize = byteConverter.get();
                    // move file pointer
                    i += 3;
                    byte[] arrayName = Arrays.copyOfRange(buffer, i, i
                            + arrayNameSize);
                    TreeItem<String> arrayItem = new TreeItem<String>(new String(arrayName));
                    listOfArrayItems.add(arrayItem);
                    i += arrayNameSize;
                    // check array's type
                    if ((0xff & buffer[i]) == 116) {

                        byteConverter = ByteBuffer.wrap(buffer, i + 2, 2);
                        int arraysTypeSize = byteConverter.get();
                        i += 2+arraysTypeSize;
                        String arraysType = checkConstType(buffer[i]);
                        TreeItem<String> arraysTypeName = new TreeItem<String>("Array type");
                        TreeItem<String> arraysTypeValue = new TreeItem<String>(new String(arraysType));
                        arrayItem.getChildren().add(arraysTypeName);
                        arraysTypeName.getChildren().add(arraysTypeValue);

                    }
                }
                else if (checkConstType(buffer[i + 1]) != null && (0xff & buffer[i+2]) == 77){
                    // check array values
                    // poprawic ta operacje warunkowa
                    for(int j = i; j < buffer.length; j++) {

                        if( (0xff & buffer[j]) == 112){
                            i = j;
                            break;
                        }
                    }

                    i += 4;
                    byteConverter = ByteBuffer.wrap(buffer, i, 2);
                    int arraySize = byteConverter.get();
                    String arrayValues = new String();

                    for(int j = 0; j < arraySize; j++){
                        i += 4;
                        byteConverter = ByteBuffer.wrap(buffer, i, 2);
                        arrayValues += Integer.toString(byteConverter.get());
                        if (j != arraySize-1)
                            arrayValues += ",";

                    }
                    TreeItem<String> arrayElement = new TreeItem<String>(arrayValues);
                    TreeItem<String> arrayItem = listOfArrayItems.get(listOfArrayItems.size()-1);
                    arrayItem.getChildren().add(arrayElement);
                }

            }
            else if((0xff & buffer[i]) == CommonTypes.REFERENCE){
                referencesToObject++;
            }
        }
        for (TreeItem<String> e : listOfArrayItems) {
            rootObject.getChildren().add(e);
        }
    }

    private String checkConstType(int value) {
        switch (value) {
            case 73:
                return "int";
            default:
                return null;
        }
    }

    public TreeItem<String> getTreeRoot() {
        return rootObject;
    }

    private void addAttribute(int position) {
        byteConverter = ByteBuffer.wrap(buffer, position + 2, 2);
        byte[] tt = Arrays.copyOfRange(buffer, position + 3, position + 3 + 1);
        TreeItem<String> attrName = new TreeItem<String>(new String(tt));
        rootObject.getChildren().add(attrName);
    }
}
