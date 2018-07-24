package views;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import Dto.ClientDto;
import Dto.EmployeeDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import contexte.MainApplicationContexte;
import controls.Toast;
import interfaces.IInitializable;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import model.BaseResponse;
import model.GetAllClientResponse;
import okhttp3.Headers;

public class ClientWizzardController extends AnchorPane implements IInitializable {

	class ClientTreeRow extends RecursiveTreeObject<ClientTreeRow> {

		ClientDto c;

		IntegerProperty id;
		StringProperty name;
		StringProperty address;
		DoubleProperty lng;
		DoubleProperty lat;
		ObservableList<EmployeeTreeRow> linkedEmployee;

		public ClientTreeRow(ClientDto c) {
			if (c == null)
				c = new ClientDto();

			this.c = c;

			this.id = new SimpleIntegerProperty(this.c.id);
			this.name = new SimpleStringProperty(this.c.name);
			this.address = new SimpleStringProperty(this.c.address);
			this.lng = new SimpleDoubleProperty(this.c.lng);
			this.lat = new SimpleDoubleProperty(this.c.lat);
			this.linkedEmployee = FXCollections.observableArrayList();

			if (c.linkedConsultants != null) {
				for (EmployeeDto e : c.linkedConsultants) {
					this.linkedEmployee.add(new EmployeeTreeRow(e));
				}
			}

		}

		public void clear() {
			id.set(c.id);
			name.set(c.name);
			address.set(c.address);
			lng.set(c.lng);
			lat.set(c.lat);

			this.linkedEmployee = FXCollections.observableArrayList();

			if (c.linkedConsultants != null) {
				for (EmployeeDto e : c.linkedConsultants) {
					this.linkedEmployee.add(new EmployeeTreeRow(e));
				}
			}
		}

		public ClientDto getNewDto() {
			ClientDto result = new ClientDto();

			result.id = id.get();
			result.name = name.get();
			result.address = address.get();
			result.lng = lng.get();
			result.lat = lat.get();

			result.linkedConsultants = new ArrayList<EmployeeDto>();

			if (linkedEmployee != null) {
				for (EmployeeTreeRow e : linkedEmployee) {
					result.linkedConsultants.add(e.getNewDto());
				}
			}

			return result;
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

	private String title = "Client Wizzard";
	private boolean isInit;
	private ObservableList<ClientTreeRow> clientsList;
	private ObservableList<EmployeeTreeRow> linkedEmployeesList;

	@FXML
	private JFXTreeTableView<ClientTreeRow> clientsView;

	@FXML
	private JFXTextField nameTextField;

	@FXML
	private JFXTextField addressTextField;

	@FXML
	private JFXTreeTableView<EmployeeTreeRow> employeeView;

	public ClientWizzardController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ClientWizzardView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();

			linkedEmployeesList = FXCollections.observableArrayList();
			clientsList = FXCollections.observableArrayList();

			isInit = false;

			JFXTreeTableColumn<ClientTreeRow, String> clientNameColumn = new JFXTreeTableColumn<ClientTreeRow, String>(
					"Name");
			clientNameColumn.setCellValueFactory(
					new Callback<TreeTableColumn.CellDataFeatures<ClientTreeRow, String>, ObservableValue<String>>() {

						@Override
						public ObservableValue<String> call(CellDataFeatures<ClientTreeRow, String> param) {
							if (param.getValue().getValue() == null)
								return new SimpleStringProperty(null);

							return param.getValue().getValue().name;
						}
					});

			JFXTreeTableColumn<ClientTreeRow, String> clientAddressColumn = new JFXTreeTableColumn<ClientTreeRow, String>(
					"Address");
			clientAddressColumn.setCellValueFactory(
					new Callback<TreeTableColumn.CellDataFeatures<ClientTreeRow, String>, ObservableValue<String>>() {

						@Override
						public ObservableValue<String> call(CellDataFeatures<ClientTreeRow, String> param) {
							if (param.getValue().getValue() == null)
								return new SimpleStringProperty(null);

							return param.getValue().getValue().address;
						}
					});

			clientsView.getColumns().add(clientNameColumn);
			clientsView.getColumns().add(clientAddressColumn);

			JFXTreeTableColumn<EmployeeTreeRow, String> employeeNameColumn = new JFXTreeTableColumn<EmployeeTreeRow, String>(
					"Name");
			employeeNameColumn.setCellValueFactory(
					new Callback<TreeTableColumn.CellDataFeatures<EmployeeTreeRow, String>, ObservableValue<String>>() {

						@Override
						public ObservableValue<String> call(CellDataFeatures<EmployeeTreeRow, String> param) {
							if (param.getValue().getValue() == null)
								return new SimpleStringProperty(null);

							return param.getValue().getValue().lastName;
						}
					});

			JFXTreeTableColumn<EmployeeTreeRow, String> employeeEmailColumn = new JFXTreeTableColumn<EmployeeTreeRow, String>(
					"Email");
			employeeEmailColumn.setCellValueFactory(
					new Callback<TreeTableColumn.CellDataFeatures<EmployeeTreeRow, String>, ObservableValue<String>>() {

						@Override
						public ObservableValue<String> call(CellDataFeatures<EmployeeTreeRow, String> param) {
							if (param.getValue().getValue() == null)
								return new SimpleStringProperty(null);

							return param.getValue().getValue().email;
						}
					});
			employeeView.getColumns().add(employeeNameColumn);
			employeeView.getColumns().add(employeeEmailColumn);

			clientsView.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener<TreeItem<ClientTreeRow>>() {

						public void changed(ObservableValue<? extends TreeItem<ClientTreeRow>> observable,
								TreeItem<ClientTreeRow> oldValue, TreeItem<ClientTreeRow> newValue) {

							if (oldValue == null)
								selectClient(newValue.getValue(), null);
							else if (newValue == null)
								selectClient(null, oldValue.getValue());
							else
								selectClient(newValue.getValue(), oldValue.getValue());
						}
					});

			TreeItem<ClientTreeRow> root = new RecursiveTreeItem<ClientTreeRow>(clientsList,
					RecursiveTreeObject::getChildren);
			clientsView.setRoot(root);
			clientsView.setShowRoot(false);

			TreeItem<EmployeeTreeRow> root2 = new RecursiveTreeItem<EmployeeTreeRow>(linkedEmployeesList,
					RecursiveTreeObject::getChildren);
			employeeView.setRoot(root2);
			employeeView.setShowRoot(false);

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

		getClients();

		isInit = true;
	}

	@Override
	public boolean isInit() {
		return isInit;
	}

	@FXML
	private void HandleSave() {
		if (clientsView.getSelectionModel() == null || clientsView.getSelectionModel().getSelectedItem() == null)
			return;

		ClientTreeRow c = clientsView.getSelectionModel().getSelectedItem().getValue();
		if (c == null)
			return;

		if (c.id.get() == 0) {
			addClient(c);
		} else {
			updateClient(c);
		}
	}

	@FXML
	private void handleCancel() {
		if (clientsView.getSelectionModel() != null && clientsView.getSelectionModel().getSelectedItem() != null) {
			ClientTreeRow c = clientsView.getSelectionModel().getSelectedItem().getValue();
			if (c == null)
				return;

			c.clear();
		}
	}

	@FXML
	private void handleClearSelection() {
		if (clientsView.getSelectionModel() != null && clientsView.getSelectionModel().getSelectedItem() != null) {
			selectClient(new ClientTreeRow(null), clientsView.getSelectionModel().getSelectedItem().getValue());
			clientsView.getSelectionModel().select(null);
		}
	}

	private void selectClient(ClientTreeRow newRow, ClientTreeRow oldRow) {
		if (oldRow != null) {
			nameTextField.textProperty().unbindBidirectional(oldRow.name);
			addressTextField.textProperty().unbindBidirectional(oldRow.address);

			linkedEmployeesList.clear();
		}
		if (newRow != null) {
			nameTextField.textProperty().bindBidirectional(newRow.name);
			addressTextField.textProperty().bindBidirectional(newRow.address);

			linkedEmployeesList.clear();
			linkedEmployeesList.addAll(newRow.linkedEmployee);
		}
	}

	private void getClients() {
		AbylsenApiClient.getInstance().getAllClients(new IAbylsenApiListener() {

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
							if (response instanceof GetAllClientResponse) {
								clientsList.clear();

								for (ClientDto c : ((GetAllClientResponse) response).clients) {
									clientsList.add(new ClientTreeRow(c));
								}
							}
						}
					}
				});
			}
		});
	}

	private void addClient(ClientTreeRow c) {
		AbylsenApiClient.getInstance().addClient(c.getNewDto(), new IAbylsenApiListener() {

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

						if (br.statusCode == 200) {
							getClients();
						}
					}
				});
			}
		});
	}

	private void updateClient(ClientTreeRow c) {
		AbylsenApiClient.getInstance().updateClient(c.getNewDto(), new IAbylsenApiListener() {

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
}
