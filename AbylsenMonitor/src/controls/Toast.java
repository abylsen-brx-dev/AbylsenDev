package controls;

import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Toast extends StackPane {

	private Label l;
	
	private VBox parent;
	public static double DURATION_LONG = 2;
	public static double DURATION_SHORT = 1;
	public Timer timer;

	public Toast(VBox parent) {
		this.parent = parent;
		
		setStyle(
                "-fx-background-color: #b0b0b0, #b0b0b0;"
                + "-fx-background-insets: 0, 1;"
                + "-fx-background-radius: 100, 99;");
		
		l = new Label();
		l.setTextFill(Color.web("#ffffff"));
		l.setPadding(new Insets(10));
		
		this.getChildren().add(l);
	}

	public void show(String msg, double duration) {
		if(parent == null)
			return;
		
		parent.getChildren().add(this);
		l.setText(msg);
		
		timer = new Timer();
		
		FadeTransition ft = new FadeTransition(Duration.millis(200), this);
		ft.setToValue(1);
		ft.setOnFinished(e -> {
			timer.schedule(new TimerTask() {
				  @Override
				  public void run() {
					  hide();
				  }
				}, (int)(duration*1500));
		});
		
		ft.play();
	}
	
	private void hide() {
		FadeTransition ft = new FadeTransition(Duration.millis(200), this);
		ft.setToValue(0);
		
		ft.setOnFinished(e -> {
			parent.getChildren().remove(this);
		});
		
		ft.play();
	}
}
