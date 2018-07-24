package controls;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import interfaces.IInitializable;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HomeTile extends AnchorPane implements IInitializable {

	private boolean isInit;
	private StringProperty actionName;
	private StringProperty titleProperty;
	
	@FXML
	private JFXButton actionButton;

	@FXML
	private Label title;

	@FXML
	private JFXSpinner spinner;

	public StringProperty actionNameProperty() {
		return this.actionName;
	}

	public String getActionName() {
		return actionName.get();
	}

	public void setActionName(final String actionName) {
		if (this.actionName == null)
			this.actionName = new SimpleStringProperty(actionName);
		else
			this.actionName.set(actionName);
	}

	@Override
	public String getTitle() {
		return titleProperty.get();
	}

	public StringProperty titleProperty() {
		return this.titleProperty;
	}

	public void setTitle(final String title) {
		if (this.titleProperty == null)
			this.titleProperty = new SimpleStringProperty(title);
		else
			this.titleProperty.set(title);
	}

	public HomeTile(double width, double height, String actionName, String title) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/HomeTileView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();

			setPrefSize(width, height);
			this.actionName = new SimpleStringProperty(actionName);
			this.titleProperty = new SimpleStringProperty(title);

			this.actionButton.textProperty().bind(this.actionName);
			this.title.textProperty().bind(this.titleProperty);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void init() {
		isInit = false;

		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean isInit() {
		return isInit;
	}

	@FXML
	private void handleAction() {

	}

}
