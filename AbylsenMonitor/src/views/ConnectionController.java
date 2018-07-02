package views;

import java.io.IOException;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import Dto.EmployeeDto;
import RestClient.Util;
import RestClient.AbylsenApi.IAbylsenApiRestClient;
import contexte.MainApplicationContexte;
import controls.Toast;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.BaseResponse;
import model.CreateAccountRequest;
import model.RegistrationRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnectionController extends BorderPane {

	@FXML
	private VBox connectionBox;

	@FXML
	private VBox signinBox;

	@FXML
	private VBox toastContainer;

	@FXML
	private JFXProgressBar progressBarSignin;

	@FXML
	private JFXProgressBar progressBarLogin;

	@FXML
	private JFXTextField signinEmail;

	@FXML
	private JFXPasswordField signinPassword;

	@FXML
	private JFXTextField signinFirstName;

	@FXML
	private JFXTextField signinLastName;

	@FXML
	private JFXTextField loginEmail;

	@FXML
	private JFXPasswordField loginPassword;

	private VBox selectedBox;

	private IAbylsenApiRestClient client;

	public ConnectionController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ConnectionView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			selectedBox = connectionBox;
			connectionBox.setVisible(false);
			signinBox.setVisible(false);
			client = Util.getAPIService();
			
			keeAlive();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	private void handleSend() {
		if (selectedBox == connectionBox) {
			register(loginEmail.getText(), loginPassword.getText());
		}
		if (selectedBox == signinBox) {
			create(signinEmail.getText(), signinPassword.getText(), signinFirstName.getText(),
					signinLastName.getText());
		}
	}

	@FXML
	private void handleSwitchToSignin() {
		switchCard(connectionBox, signinBox);
		selectedBox = signinBox;
	}

	@FXML
	private void handleSwitchToLogin() {
		switchCard(signinBox, connectionBox);
		selectedBox = connectionBox;
	}

	private void wizz(Node n) {
		n.setDisable(true);
		TranslateTransition tt = new TranslateTransition();
		tt.setDuration(Duration.millis(50));
		tt.setNode(n);

		tt.setByY(4);
		tt.setByX(4);

		tt.setAutoReverse(true);
		tt.setCycleCount(10);

		tt.setOnFinished(e -> {
			n.setDisable(false);
		});
		tt.play();
	}

	private void switchCard(Node toHide, Node toShow) {
		TranslateTransition ttHide = getTranslation(true, 150, toHide);
		TranslateTransition ttShow = getTranslation(false, 150, toShow);

		ttShow.setOnFinished(e -> {
			toShow.toFront();

			TranslateTransition ttShowBis = getTranslation(true, 0, toHide);
			TranslateTransition ttHideBis = getTranslation(false, 0, toShow);

			toHide.setDisable(true);
			toShow.setDisable(false);

			ttShowBis.play();
			ttHideBis.play();
		});

		ttHide.play();
		ttShow.play();
	}

	private TranslateTransition getTranslation(boolean toHide, int x, Node n) {
		TranslateTransition tt = new TranslateTransition();
		tt.setDuration(Duration.millis(300));

		if (toHide)
			x *= -1;

		tt.setToX(x);
		tt.setNode(n);

		tt.setAutoReverse(true);

		return tt;
	}

	public void keeAlive() {
		client.keepAlive().enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> arg0, Response<BaseResponse> arg1) {
				BaseResponse responseTemp = arg1.body();
				if (responseTemp == null) {
					responseTemp = new BaseResponse();
					responseTemp.status = "Error while contacting the server.";
					responseTemp.code = arg1.code();
				}
				final BaseResponse br = responseTemp;

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (br.code == 200) {
							MainApplicationContexte.getInstance().manageHeaders(arg1.headers());
							MainApplicationContexte.getInstance().getMainApp().navigateTo(new RootController());
						} else {
							connectionBox.setVisible(true);
							signinBox.setVisible(true);
						}
					}
				});
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						connectionBox.setVisible(true);
						signinBox.setVisible(true);
						new Toast(toastContainer).show("Server not working", Toast.DURATION_LONG);
					}
				});
			}
		});
	}
	
	public void register(String email, String password) {
		RegistrationRequest request = new RegistrationRequest();
		request.email = email;
		request.password = password;

		client.register(request).enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> arg0, Response<BaseResponse> arg1) {
				BaseResponse responseTemp = arg1.body();
				if (responseTemp == null) {
					responseTemp = new BaseResponse();
					responseTemp.status = "Error while contacting the server.";
					responseTemp.code = arg1.code();
				}
				final BaseResponse br = responseTemp;

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (br.code == 200) {
							MainApplicationContexte.getInstance().manageHeaders(arg1.headers());
							MainApplicationContexte.getInstance().getMainApp().navigateTo(new RootController());
						} else {
							showProgressBar(false);
							wizz(selectedBox);
							new Toast(toastContainer).show(String.valueOf(br.code) + " - " + br.status,
									Toast.DURATION_LONG);
						}
					}
				});
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showProgressBar(false);
						wizz(selectedBox);
						new Toast(toastContainer).show("Server not working", Toast.DURATION_LONG);
					}
				});
			}
		});

		showProgressBar(true);
		selectedBox.setDisable(true);
	}

	public void create(String email, String password, String firstName, String lastName) {
		CreateAccountRequest request = new CreateAccountRequest();
		request.account = new EmployeeDto();

		request.account.setEmail(email);
		request.account.setFirstName(firstName);
		request.account.setLastName(lastName);
		request.account.setPassword(password);

		client.create(request).enqueue(new Callback<BaseResponse>() {
			@Override
			public void onResponse(Call<BaseResponse> arg0, Response<BaseResponse> arg1) {
				BaseResponse responseTemp = arg1.body();
				if (responseTemp == null) {
					responseTemp = new BaseResponse();
					responseTemp.status = "Error while contacting the server.";
					responseTemp.code = arg1.code();
				}
				final BaseResponse br = responseTemp;

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showProgressBar(false);
						selectedBox.setDisable(false);

						if (br.code == 200) {
							MainApplicationContexte.getInstance().manageHeaders(arg1.headers());
							MainApplicationContexte.getInstance().getMainApp().navigateTo(new RootController());
						} else {
							showProgressBar(false);
							wizz(selectedBox);
							new Toast(toastContainer).show(String.valueOf(br.code) + " - " + br.status,
									Toast.DURATION_LONG);
						}
					}
				});
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						showProgressBar(false);
						wizz(selectedBox);
						new Toast(toastContainer).show("Server not working", Toast.DURATION_LONG);
					}
				});
			}
		});

		showProgressBar(true);
		selectedBox.setDisable(true);
	}

	private void showProgressBar(boolean visible) {
		progressBarLogin.setVisible(visible);
		progressBarSignin.setVisible(visible);
	}
}
