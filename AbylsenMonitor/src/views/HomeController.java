package views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

import controls.HomeTile;
import interfaces.IInitializable;

public class HomeController extends AnchorPane implements IInitializable {

	private double height = 0;
	private double width = 0;
	
	private String title = "Home";
	private boolean isInit;
	private boolean isTileInit;
	
	@FXML 
	private HBox masornyPane;
	
	
	public HomeController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/HomeView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();
			
			isInit = false;
			isTileInit = false;
		} catch (IOException exception) { 
			throw new RuntimeException(exception);
		}
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void init() {
		isInit = true;
		
		isInit = false;
	}

	public void initTile() {
		if(isTileInit)
			return;
		
		if(width == 0 || height == 0)
			return;
		
		masornyPane.getChildren().clear();
		
		HomeTile ht1 = new HomeTile(width / 5.1, height, "Configure", "Account");
		HomeTile ht2 = new HomeTile(width / 5.1, height, "let see", "News");
		HomeTile ht3 = new HomeTile(width / 5.1, height, "go to", "Team");
		HomeTile ht4 = new HomeTile(width / 5.1, height, "admire", "Messages");
		HomeTile ht5 = new HomeTile(width / 5.1, height, "eat now", "Abylsen");

		
		masornyPane.getChildren().add(ht1);
		masornyPane.getChildren().add(ht2);
		masornyPane.getChildren().add(ht3);
		masornyPane.getChildren().add(ht4);
		masornyPane.getChildren().add(ht5);

		ht1.init();
		ht2.init();
		ht3.init();
		ht4.init();
		ht5.init();
		
		isTileInit = true;
	}
	
	@Override
	public boolean isInit() {
		return isInit;
	}

	public void setParentSize(double w, double h) {
		this.width = w;
		this.height = h;
	}
}
