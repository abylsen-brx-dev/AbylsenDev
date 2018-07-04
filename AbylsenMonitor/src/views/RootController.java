package views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RootController extends BorderPane {

	@FXML
	private Label title;

	@FXML
	private VBox menu;

	private boolean menuOpen;

	@FXML
	private JFXHamburger hamburger;

	private Map<JFXButton, Parent> nodes;

	public RootController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/RootView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			HamburgerSlideCloseTransition t = new HamburgerSlideCloseTransition(hamburger);
			t.setRate(-1);

			hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
				t.setRate(t.getRate() * -1);
				t.play();
				showMenu(!menuOpen);
			});

			initDrawer();
			
			menu.setVisible(false);
			menuOpen = false;
			
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void initDrawer() {
		nodes = new HashMap<JFXButton, Parent>();

		JFXButton currentNode;
		Label currentParent;
		for (int i = 0; i < 4; i++) {
			currentNode = new JFXButton();
			currentParent = new Label();
			currentNode.setText("Menu " + String.valueOf(i));
			currentParent.setText("MENU " + String.valueOf(i));
			
			currentNode.setPadding(new Insets(10));
			currentNode.setOnAction(e -> {
				if(e.getSource() instanceof JFXButton) {
					selectMenu((JFXButton)e.getSource());
				}
			});
			
			nodes.put(currentNode, currentParent);
			menu.getChildren().add(currentNode);
			
			if(i == 0)
				selectMenu((JFXButton)nodes.keySet().toArray()[0]);
		}
		
	}

	private void showMenu(boolean visible) {
		TranslateTransition tt = new TranslateTransition();
		tt.setDuration(Duration.millis(200));
		tt.setNode(menu);

		if (!visible) {
			tt.setFromX(0);
			tt.setToX(-menu.getWidth());
		} else {
			tt.setFromX(-menu.getWidth());
			tt.setToX(0);
		}

		tt.setOnFinished(e -> {
			menuOpen = visible;
		});
		tt.play();
		
		if(!menu.isVisible())
			menu.setVisible(true);
	}
	
	private void selectMenu(JFXButton source) {
		for(JFXButton b : nodes.keySet()) {
			if(b.equals(source))
				makeButtonSelected(b);
			else
				makeButtonUnselected(b);
		}
	}
	
	private void makeButtonUnselected(JFXButton b) {
		b.setTextFill(Color.GRAY);
		b.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		b.setOpacity(0.7);
	}
	
	private void makeButtonSelected(JFXButton b) {
		b.setTextFill(Color.WHITE);
		b.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		b.setOpacity(0.7);
		
		title.setText(b.getText().toUpperCase());
		setCenter(nodes.get(b));
		//nodes.get(b).init();
	}
}