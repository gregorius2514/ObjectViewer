package pl.kielce.metody.gui;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import pl.kielce.metody.serializer.ReadSerializedFile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {

	// -------------- GUI Components
	@FXML
	private TreeView<String> treeView;

	@FXML
	private TextArea textArea;

	@FXML
	private Button btnFile;

	@FXML
	private MenuButton btnHelp;

	// --------------- attributes
	private TreeItem<String> fakeRoot;

	// --------------- methods
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnHelp.getStyleClass().add("custom-combobox");
		textArea.getStyleClass().add("custom-textArea");

		fakeRoot = new TreeItem<String>("Root");
		treeView.setRoot(fakeRoot);

		MenuItem menuItemAbout = new MenuItem("About");
		btnHelp.getItems().add(menuItemAbout);

		// -------------- listeners
		menuItemAbout.setOnAction(new EventHandler<ActionEvent>() {
			Parent root;

			@Override
			public void handle(ActionEvent event) {

				try {
					root = FXMLLoader.load(getClass().getResource(
							"About_UI.fxml"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				Stage stage = new Stage();
				stage.setTitle("About");
				stage.setScene(new Scene(root, 305, 78));
				stage.show();
				stage.toFront();
				stage.setX(0);
				stage.setY(0);

				// fix resizable bug
				stage.setMaxHeight(78);
				stage.setMaxWidth(305);
				stage.setMinHeight(78);
				stage.setMinWidth(305);
			}
		});

		btnFile.setOnMouseReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

				fakeRoot.getChildren().clear();
				FileChooser openFile = new FileChooser();

				File file = openFile.showOpenDialog(textArea.getScene()
						.getWindow());

				if (file != null) {
					
					DataInputStream inputDataReader = null;

					try {
						inputDataReader = new DataInputStream(
								new FileInputStream(file));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					byte[] buffer = new byte[(int) file.length()];

					try {
						inputDataReader.read(buffer);
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						inputDataReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					textArea.clear();
					textArea.setEditable(false);
					textArea.appendText(new String(buffer));

					ReadSerializedFile bytesReader = new ReadSerializedFile(
							file.getAbsolutePath());
					fakeRoot.getChildren().add(bytesReader.getTreeRoot());
					addRef(bytesReader.getRefs(), bytesReader.getTreeRoot());
				}
			}
		});

		textArea.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

				String selection = textArea.getSelectedText();
				searchString(treeView.getRoot(), selection);
			}
		});

		treeView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Object>() {

					@Override
					public void changed(final ObservableValue<?> observable,
							final Object oldValue, final Object newValue) {

						final TreeItem<String> selectedNode = (TreeItem<String>) newValue;
						searchString(selectedNode.getValue());
					}
				});
	}

	// -------------- search in TreeView
	private void searchString(TreeItem<String> root, String value) {

		for (TreeItem<String> e : root.getChildren()) {

			if (e.getValue().equalsIgnoreCase(value))
				treeView.getSelectionModel().select(e);
			if (e.getChildren() != null)
				searchString(e, value);
		}
	}

	// -------------- search in TextArea
	private void searchString(String value) {

		String text = textArea.getText();
		int i = 0;
		int checkOperation = 1;

		for (i = 0; i < text.length(); i++) {

			if (value.charAt(0) == text.charAt(i)) {
				checkOperation = 1;
				for (int j = 1; j < value.length(); j++) {
					if (value.charAt(j) == text.charAt(i + j)) {
						checkOperation++;
					}
				}
			}

			if (checkOperation == value.length())
				break;
		}

		textArea.selectRange(i, i + value.length());
	}

	// -------------- add reference to other object
	private void addRef(int refs, TreeItem<String> root) {

		for (int i = 1; i <= refs; i++) {

			TreeItem<String> copy = new TreeItem<String>("Object" + i);
			copy.getChildren().addAll(root.getChildren());
			fakeRoot.getChildren().add(copy);
		}
	}
}
