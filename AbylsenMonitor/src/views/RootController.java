package views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import contexte.MainApplicationContexte;
import controls.MinimalistAccountViewer;
import controls.Toast;
import interfaces.IInitializable;
import interfaces.IView;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class RootController extends BorderPane implements IView {

	@FXML
	private Label title;

	@FXML
	private VBox menu;

	@FXML
	private JFXHamburger hamburger;

	@FXML
	private BorderPane mainContainer;

	@FXML
	private Pane drawerHandleContainer;

	@FXML
	private VBox toastContainer;

	private boolean menuOpen;

	private Map<JFXButton, Parent> nodes;

	public RootController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/RootView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			HamburgerSlideCloseTransition t = new HamburgerSlideCloseTransition(hamburger);
			t.setRate(1);

			hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
				t.setRate(t.getRate() * -1);
				t.play();
				showMenu(menuOpen, 200);
			});

			drawerHandleContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
				t.setRate(t.getRate() * -1);
				t.play();
				showMenu(menuOpen, 200);
			});

			initDrawer();

			menuOpen = true;
			showMenu(menuOpen, 0);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void initDrawer() {
		nodes = new HashMap<JFXButton, Parent>();

		menu.getChildren().add(new MinimalistAccountViewer(MainApplicationContexte.getInstance().getUser()));

		ConsultantMangerController parent1 = new ConsultantMangerController();
		JFXButton button1 = getMenuButton(parent1);
		nodes.put(button1, parent1);

		MapViewController parent2 = new MapViewController();
		JFXButton button2 = getMenuButton(parent2);
		nodes.put(button2, parent2);

		ClientWizzardController parent3 = new ClientWizzardController();
		JFXButton button3 = getMenuButton(parent3);
		nodes.put(button3, parent3);

		CardBoardController parent4 = new CardBoardController();
		JFXButton button4 = getMenuButton(parent4);
		nodes.put(button4, parent4);

		HomeController parent5 = new HomeController();

		ChangeListener<Number> stageSizeListener = new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				parent5
					.setParentSize(mainContainer.getWidth(), mainContainer.getHeight());
				
				parent5.initTile();
			}
		};
				

		mainContainer.widthProperty().addListener(stageSizeListener);
		mainContainer.heightProperty().addListener(stageSizeListener);

		JFXButton button5 = getMenuButton(parent5);
		nodes.put(button5, parent5);

		menu.getChildren().add(button5);
		menu.getChildren().add(button2);
		menu.getChildren().add(button1);
		menu.getChildren().add(button3);
		menu.getChildren().add(button4);

		selectMenu(button5);

		menu.prefHeightProperty().bind(mainContainer.heightProperty());
	}

	private JFXButton getMenuButton(IInitializable parent) {
		JFXButton button;

		button = new JFXButton();
		button.setText(parent.getTitle());

		button.setPadding(new Insets(10));
		button.prefWidthProperty().bind(menu.widthProperty());
		button.setOnAction(e -> {
			if (e.getSource() instanceof JFXButton) {
				selectMenu((JFXButton) e.getSource());
			}
		});

		return button;
	}

	private void showMenu(boolean visible, int duration) {
		TranslateTransition tt = new TranslateTransition();
		tt.setDuration(Duration.millis(duration));

		tt.setNode(menu);

		if (!visible) {
			tt.setToX(-menu.getWidth());
			tt.setFromX(0);
		} else {
			tt.setToX(0);
			tt.setFromX(-menu.getWidth());
		}

		tt.setOnFinished(e -> menuOpen = !visible);

		tt.play();

		drawerHandleContainer.setVisible(visible);
	}

	private void selectMenu(JFXButton source) {
		if (!title.getText().equals(source.getText().toUpperCase())) {
			for (JFXButton b : nodes.keySet()) {
				if (b.equals(source))
					makeButtonSelected(b);
				else
					makeButtonUnselected(b);
			}
		}
	}

	private void makeButtonUnselected(JFXButton b) {
		b.setTextFill(Color.WHITE);
		b.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

		mainContainer.getChildren().remove(nodes.get(b));
	}

	private void makeButtonSelected(JFXButton b) {
		b.setTextFill(Color.WHITESMOKE);
		b.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		b.setTextAlignment(TextAlignment.LEFT);
		title.setText(b.getText().toUpperCase());

		mainContainer.setCenter(nodes.get(b));
		if (nodes.get(b) instanceof IInitializable)
			((IInitializable) nodes.get(b)).init();
	}

	public void DisplayToast(String msg, int duration) {
		new Toast(toastContainer).show(msg, duration);
	}

	@Override
	public VBox getToastContainer() {
		return toastContainer;
	}
}