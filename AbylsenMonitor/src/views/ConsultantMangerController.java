package views;

import java.io.IOException;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import Dto.EmployeeDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import contexte.MainApplicationContexte;
import controls.Toast;
import enums.EmployeeEnums;
import interfaces.IInitializable;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import model.AddEmployeeRequest;
import model.BaseResponse;
import model.GetAllConsultantsResponse;
import model.UpdateEmployeeRequest;
import okhttp3.Headers;

public class ConsultantMangerController extends AnchorPane implements IInitializable {

	private String title = "Consultant Wizard";

	@FXML
	private JFXTreeTableView<EmployeeTreeRow> treeview;

	@FXML
	private JFXTextField searchByNameInput;

	@FXML
	private JFXTextField searchByEmailInput;

	@FXML
	private JFXTextField firstNameTextField;

	@FXML
	private JFXTextField lastNameTextField;

	@FXML
	private JFXTextField emailTextField;

	@FXML
	private JFXTextField phoneNumberTextField;

	@FXML
	private JFXComboBox<String> posteComboBox;

	private ObservableList<EmployeeTreeRow> list;

	private boolean isInit;

	private StringProperty byNameInput;

	private StringProperty byEmailInput;

	public ConsultantMangerController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ConsultantMangerView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			isInit = false;
			list = FXCollections.observableArrayList();

			byNameInput = new SimpleStringProperty(null);
			byEmailInput = new SimpleStringProperty(null);

			byNameInput.bind(searchByNameInput.textProperty());
			byEmailInput.bind(searchByEmailInput.textProperty());

			posteComboBox.setItems(getPostes());

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	class EmployeeTreeRow extends RecursiveTreeObject<EmployeeTreeRow> {

		EmployeeDto e;

		IntegerProperty id;
		StringProperty firstName;
		StringProperty lastName;
		StringProperty email;
		StringProperty poste;
		StringProperty password;

		public EmployeeTreeRow(EmployeeDto e) {
			if (e == null)
				e = new EmployeeDto();

			this.e = e;

			this.id = new SimpleIntegerProperty(this.e.id);
			this.firstName = new SimpleStringProperty(this.e.firstName);
			this.lastName = new SimpleStringProperty(this.e.lastName);
			this.email = new SimpleStringProperty(this.e.email);
			this.poste = new SimpleStringProperty(this.e.poste);
			this.password = new SimpleStringProperty(this.e.password);
		}

		public void clear() {
			id.set(e.id);
			firstName.set(e.firstName);
			lastName.set(e.lastName);
			email.set(e.email);
			poste.set(e.poste);
			password.set(e.password);
		}

		public EmployeeDto getNewDto() {
			EmployeeDto result = new EmployeeDto();

			result.email = email.get();
			result.firstName = firstName.get();
			result.lastName = lastName.get();
			result.id = id.get();
			result.password = password.get();
			result.poste = poste.get();

			return result;
		}
	}

	@Override
	public void init() {
		if (isInit)
			return;

		JFXTreeTableColumn<EmployeeTreeRow, String> firstNameColumn = new JFXTreeTableColumn<EmployeeTreeRow, String>(
				"First Name");
		firstNameColumn.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<EmployeeTreeRow, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<EmployeeTreeRow, String> param) {
						if (param.getValue().getValue() == null)
							return new SimpleStringProperty(null);

						return param.getValue().getValue().firstName;
					}
				});

		JFXTreeTableColumn<EmployeeTreeRow, String> lastNameColumn = new JFXTreeTableColumn<EmployeeTreeRow, String>(
				"Last Name");
		lastNameColumn.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<EmployeeTreeRow, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<EmployeeTreeRow, String> param) {
						if (param.getValue().getValue() == null)
							return new SimpleStringProperty(null);

						return param.getValue().getValue().lastName;
					}
				});

		JFXTreeTableColumn<EmployeeTreeRow, String> emailColumn = new JFXTreeTableColumn<EmployeeTreeRow, String>(
				"Email");
		emailColumn.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<EmployeeTreeRow, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<EmployeeTreeRow, String> param) {
						if (param.getValue().getValue() == null)
							return new SimpleStringProperty(null);

						return param.getValue().getValue().email;
					}
				});

		ChangeListener<String> listenner = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				treeview.setPredicate(new Predicate<TreeItem<EmployeeTreeRow>>() {

					@Override
					public boolean test(TreeItem<EmployeeTreeRow> t) {

						if ((byEmailInput.get() == null || byEmailInput.get().equals(""))
								&& (byNameInput.get() == null || byNameInput.get().equals("")))
							return true;

						return t.getValue().lastName.getValue().contains(byNameInput.get())
								&& t.getValue().email.getValue().contains(byEmailInput.get());
					}
				});

			}
		};

		searchByNameInput.textProperty().addListener(listenner);
		searchByEmailInput.textProperty().addListener(listenner);

		treeview.getColumns().clear();

		treeview.getColumns().add(firstNameColumn);
		treeview.getColumns().add(lastNameColumn);
		treeview.getColumns().add(emailColumn);

		getAllConsultant();

		treeview.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TreeItem<EmployeeTreeRow>>() {

					public void changed(ObservableValue<? extends TreeItem<EmployeeTreeRow>> observable,
							TreeItem<EmployeeTreeRow> oldValue, TreeItem<EmployeeTreeRow> newValue) {

						if (oldValue == null)
							setSelectedEmployee(newValue.getValue(), null);
						else if (newValue == null)
							setSelectedEmployee(null, oldValue.getValue());
						else
							setSelectedEmployee(newValue.getValue(), oldValue.getValue());
					}
				});
		isInit = true;
	}

	private void getAllConsultant() {
		AbylsenApiClient.getInstance().getAllConsultant(new IAbylsenApiListener() {

			@Override
			public void OnResponseRefused(Object response, Headers headers) {

			}

			@Override
			public void OnResponseAccepted(Object response, Headers headers) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (response != null) {
							if (response instanceof GetAllConsultantsResponse) {
								list.clear();

								for (EmployeeDto e : ((GetAllConsultantsResponse) response).consultants) {
									list.add(new EmployeeTreeRow(e));
								}

								TreeItem<EmployeeTreeRow> root = new RecursiveTreeItem<EmployeeTreeRow>(list,
										RecursiveTreeObject::getChildren);
								treeview.setRoot(root);
								treeview.setShowRoot(false);
							}
						}
					}
				});
			}
		});
	}

	private void setSelectedEmployee(EmployeeTreeRow newRow, EmployeeTreeRow oldRow) {
		if (oldRow != null) {
			firstNameTextField.textProperty().unbindBidirectional(oldRow.firstName);
			lastNameTextField.textProperty().unbindBidirectional(oldRow.lastName);
			emailTextField.textProperty().unbindBidirectional(oldRow.email);
			posteComboBox.valueProperty().unbindBidirectional(oldRow.poste);
		}
		if (newRow != null) {
			firstNameTextField.textProperty().bindBidirectional(newRow.firstName);
			lastNameTextField.textProperty().bindBidirectional(newRow.lastName);
			emailTextField.textProperty().bindBidirectional(newRow.email);
			posteComboBox.valueProperty().bindBidirectional(newRow.poste);
		}
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean isInit() {
		return isInit;
	}

	private ObservableList<String> getPostes() {
		ObservableList<String> result = FXCollections.observableArrayList();

		for (String s : EmployeeEnums.POSTE_ARRAY) {
			result.add(s);
		}

		return result;
	}

	@FXML
	private void handleSaveEmployee() {
		if (treeview.getSelectionModel() == null || treeview.getSelectionModel().getSelectedItem() == null) {
			EmployeeTreeRow row = new EmployeeTreeRow(null);

			row.id.set(-1);
			row.firstName.set(firstNameTextField.textProperty().get());
			row.lastName.set(lastNameTextField.textProperty().get());
			row.email.set(emailTextField.textProperty().get());
			row.poste.set(posteComboBox.valueProperty().get());
			row.password.set("1234Abylsen");

			addEmployee(row);
			return;
		}

		EmployeeTreeRow e = treeview.getSelectionModel().getSelectedItem().getValue();
		if (e == null)
			return;

		if (e.id.get() == 0) {
			addEmployee(e);
		} else {
			updateEmployee(e);
		}
	}

	@FXML
	private void handleClearSelection() {
		if (treeview.getSelectionModel().getSelectedItem() != null) {
			setSelectedEmployee(new EmployeeTreeRow(null), treeview.getSelectionModel().getSelectedItem().getValue());
			treeview.getSelectionModel().select(null);
		}
	}

	@FXML
	private void handlCancelEmployee() {
		EmployeeTreeRow e = treeview.getSelectionModel().getSelectedItem().getValue();
		if (e == null)
			return;

		e.clear();
	}

	private void updateEmployee(EmployeeTreeRow e) {
		if (e == null)
			return;

		UpdateEmployeeRequest request = new UpdateEmployeeRequest();
		request.employee = e.getNewDto();

		AbylsenApiClient.getInstance().UpdateEmployee(request, new IAbylsenApiListener() {

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
				BaseResponse br = (BaseResponse) response;

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						MainApplicationContexte.getInstance().getMainApp().displayToast(br.message,
								Toast.DURATION_LONG);
					}
				});
			}
		});
	}

	private void addEmployee(EmployeeTreeRow e) {
		if (e == null)
			return;

		AddEmployeeRequest request = new AddEmployeeRequest();
		request.employee = e.getNewDto();

		AbylsenApiClient.getInstance().addEmployee(request, new IAbylsenApiListener() {
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
				BaseResponse br = (BaseResponse) response;

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						MainApplicationContexte.getInstance().getMainApp().displayToast(br.message,
								Toast.DURATION_LONG);
						
						if(br.statusCode == 200) {
							getAllConsultant();
						}
					}
				});
			}
		});
	}
}
