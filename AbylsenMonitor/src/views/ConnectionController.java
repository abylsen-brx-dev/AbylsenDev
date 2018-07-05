package views;

import java.io.IOException;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import Dto.EmployeeDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import contexte.MainApplicationContexte;
import controls.Toast;
import enums.EmployeeEnums;
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
import model.GetInfoResponse;
import model.RegistrationRequest;
import okhttp3.Headers;

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

	@FXML
	private JFXToggleButton signinAccountType;
	
	private VBox selectedBox;

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
			
			handleChangeAccountType();
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
			create(
					signinEmail.getText(), 
					signinPassword.getText(), 
					signinFirstName.getText(),
					signinLastName.getText(), 
					signinAccountType.isSelected() ? EmployeeEnums.TYPE_CONSULTANT : EmployeeEnums.TYPE_MANAGER);
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

	@FXML
	private void handleChangeAccountType() {
		if(signinAccountType.isSelected())
			signinAccountType.setText("Manager");
		else
			signinAccountType.setText("Consultant");
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
		AbylsenApiClient.getInstance().keepAlive(new IAbylsenApiListener() {
			@Override
			public void OnResponseAccepted(Object response, Headers headers) {
				BaseResponse br = (BaseResponse)response;
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (br.statusCode == 200) {
							MainApplicationContexte.getInstance().manageHeaders(headers);
							getInfo();
						} else {
							connectionBox.setVisible(true);
							signinBox.setVisible(true);
							new Toast(toastContainer).show(br.message, Toast.DURATION_LONG);
						}
					}
				});
			}
			
			@Override
			public void OnResponseRefused(Object response, Headers headers) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						connectionBox.setVisible(true);
						signinBox.setVisible(true);
						
						BaseResponse br = (BaseResponse)response;
						if(br == null)
							new Toast(toastContainer).show("Server not working", Toast.DURATION_LONG);
						else
							new Toast(toastContainer).show(br.message, Toast.DURATION_LONG);
					}
				});
			}
		});
	}
	
	public void register(String email, String password) {
		RegistrationRequest request = new RegistrationRequest();
		request.email = email;
		request.password = password;
		
		AbylsenApiClient.getInstance(). register(request, new IAbylsenApiListener() {
			@Override
			public void OnResponseAccepted(Object response, Headers headers) {
				BaseResponse br = (BaseResponse)response;
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (br.statusCode == 200) {
							MainApplicationContexte.getInstance().manageHeaders(headers);
							getInfo();
						} else {
							showProgressBar(false);
							wizz(selectedBox);
							new Toast(toastContainer).show(br.message, Toast.DURATION_LONG);
						}
					}
				});
			}
			
			@Override
			public void OnResponseRefused(Object response, Headers headers) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showProgressBar(false);
						wizz(selectedBox);
						
						BaseResponse br = (BaseResponse)response;
						if(br == null)
							new Toast(toastContainer).show("Server not working", Toast.DURATION_LONG);
						else
							new Toast(toastContainer).show(br.message, Toast.DURATION_LONG);
					}
				});
			}
		});
		
		showProgressBar(true);
		selectedBox.setDisable(true);
	}

	public void create(String email, String password, String firstName, String lastName, String accountType) {
		CreateAccountRequest request = new CreateAccountRequest();
		request.account = new EmployeeDto();

		request.account.setEmail(email);
		request.account.setFirstName(firstName);
		request.account.setLastName(lastName);
		request.account.setPassword(password);
		request.account.setPoste(accountType);
		
		AbylsenApiClient.getInstance().create(request, new IAbylsenApiListener() {
			@Override
			public void OnResponseAccepted(Object response, Headers headers) {
				BaseResponse br = (BaseResponse)response;
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (br.statusCode == 200) {
							MainApplicationContexte.getInstance().manageHeaders(headers);
							getInfo();
						} else {
							showProgressBar(false);
							wizz(selectedBox);
							new Toast(toastContainer).show(br.message, Toast.DURATION_LONG);
						}
					}
				});
			}
			
			@Override
			public void OnResponseRefused(Object response, Headers headers) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showProgressBar(false);
						wizz(selectedBox);
						
						BaseResponse br = (BaseResponse)response;
						if(br == null)
							new Toast(toastContainer).show("Server not working", Toast.DURATION_LONG);
						else
							new Toast(toastContainer).show(br.message, Toast.DURATION_LONG);
					}
				});
			}
		});
		
		showProgressBar(true);
		selectedBox.setDisable(true);
	}

	public void getInfo() {
		AbylsenApiClient.getInstance().getInfo(new IAbylsenApiListener() {
			@Override
			public void OnResponseAccepted(Object response, Headers headers) {
				BaseResponse br = (BaseResponse)response;
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (br.statusCode == 200) {
							MainApplicationContexte.getInstance().manageHeaders(headers);
							MainApplicationContexte.getInstance().setUser(((GetInfoResponse)response).account);
							MainApplicationContexte.getInstance().getMainApp().navigateTo(new RootController());
						} else {
							showProgressBar(false);
							wizz(selectedBox);
							new Toast(toastContainer).show(br.message,
									Toast.DURATION_LONG);
						}
					}
				});
			}
			
			@Override
			public void OnResponseRefused(Object response, Headers headers) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showProgressBar(false);
						wizz(selectedBox);
						
						BaseResponse br = (BaseResponse)response;
						if(br == null)
							new Toast(toastContainer).show("Server not working", Toast.DURATION_LONG);
						else
							new Toast(toastContainer).show(br.message, Toast.DURATION_LONG);
					}
				});
			}
		});
	}
	
	private void showProgressBar(boolean visible) {
		progressBarLogin.setVisible(visible);
		progressBarSignin.setVisible(visible);
	}
}
