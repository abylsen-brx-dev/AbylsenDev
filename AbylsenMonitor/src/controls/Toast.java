package controls;

import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class Toast extends Label {

	private VBox parent;
	public static double DURATION_LONG = 2;
	public static double DURATION_SHORT = 1;
	public Timer timer;

	public Toast(VBox parent) {
		this.parent = parent;
		
		String css = "-fx-background-color: #c0c0c0;\r\n" + 
				"	-fx-border-radius: 10;\r\n" + 
				"	-fx-border-style: solid inside;\r\n" + 
				"	-fx-border-color: #c0c0c0;\r\n" + 
				"	-fx-border-insets: 5;";
		
		setStyle(css);
		setTextFill(Paint.valueOf("#000000"));
	}

	public void show(String msg, double duration) {
		if(parent == null)
			return;
		
		parent.getChildren().add(this);
		this.setText(msg);
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
