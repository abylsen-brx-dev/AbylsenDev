package views;

import java.io.IOException;

import com.jfoenix.controls.JFXMasonryPane;

import Dto.EmployeeDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import contexte.MainApplicationContexte;
import controls.AbylsenCard;
import controls.Toast;
import interfaces.IInitializable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.BaseResponse;
import model.GetAllConsultantsResponse;
import okhttp3.Headers;

public class CardBoardController extends AnchorPane implements IInitializable {

	private boolean isInit;
	private String title = "Card board";

	@FXML
	private JFXMasonryPane masonryPane;

	public CardBoardController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CardBoardView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();

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
		isInit = false;

		getEmployees();

		isInit = true;
	}

	@Override
	public boolean isInit() {
		return isInit;
	}

	private void getEmployees() {
		AbylsenApiClient.getInstance().getAllConsultant(new IAbylsenApiListener() {

			@Override
			public void OnResponseRefused(Object response, Headers headers) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						BaseResponse br = (BaseResponse) response;
						if (br == null)
							MainApplicationContexte.getInstance().getMainApp().displayToast("Server not working",
									Toast.DURATION_LONG);
						else
							MainApplicationContexte.getInstance().getMainApp().displayToast(br.message,
									Toast.DURATION_LONG);
					}
				});
			}

			@Override
			public void OnResponseAccepted(Object response, Headers headers) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (response != null) {
							if (response instanceof GetAllConsultantsResponse) {
//								double width;
//								double newWidth;
//								int n;
//								int diff;
//								
//								Random r = new Random();
//								AbylsenCard ac = null;
//								
								for (EmployeeDto e : ((GetAllConsultantsResponse) response).consultants) {
//									ac = new AbylsenCard(e);
									
//									width = ac.getHeight();
//									n = (int) ((width - 50) - width + 1);
//									diff = r.nextInt() % n;
//									
//									newWidth = width + diff;
//									ac.setPrefWidth(newWidth);
//									ac.setPrefHeight(newWidth * ac.getHeight() / width);
									
									masonryPane.getChildren().add(new AbylsenCard(e));
								}
							}
						}
					}
				});
			}
		});
	}
}
