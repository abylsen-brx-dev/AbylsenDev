package views;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import Dto.ClientDto;
import Dto.EmployeeDto;
import Dto.MissionDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import contexte.MainApplicationContexte;
import controls.Toast;
import enums.EmployeeEnums;
import interfaces.IInitializable;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import model.GetMissionsByConsultantRequest;
import model.GetMissionsResponse;
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

	@FXML
	private JFXTreeTableView<MissionTreeRow> missionsView;

	@FXML
	private JFXTextField missionNameTextField;

	@FXML
	private JFXTextField missionDescriptionTextField;

	@FXML
	private JFXDatePicker missionstartDate;

	@FXML
	private JFXDatePicker missionEndDate;

	private ObservableList<EmployeeTreeRow> employeelist;

	private ObservableList<MissionTreeRow> missionList;

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

			employeelist = FXCollections.observableArrayList();
			missionList = FXCollections.observableArrayList();

			byNameInput = new SimpleStringProperty(null);
			byEmailInput = new SimpleStringProperty(null);

			byNameInput.bind(searchByNameInput.textProperty());
			byEmailInput.bind(searchByEmailInput.textProperty());

			posteComboBox.setItems(getPostes());

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

			treeview.getColumns().add(firstNameColumn);
			treeview.getColumns().add(lastNameColumn);
			treeview.getColumns().add(emailColumn);

			JFXTreeTableColumn<MissionTreeRow, String> nameColumn = new JFXTreeTableColumn<MissionTreeRow, String>(
					"Name");
			nameColumn.setCellValueFactory(
					new Callback<TreeTableColumn.CellDataFeatures<MissionTreeRow, String>, ObservableValue<String>>() {

						@Override
						public ObservableValue<String> call(CellDataFeatures<MissionTreeRow, String> param) {
							if (param.getValue().getValue() == null)
								return new SimpleStringProperty(null);

							return param.getValue().getValue().name;
						}
					});

			JFXTreeTableColumn<MissionTreeRow, String> clientColumn = new JFXTreeTableColumn<MissionTreeRow, String>(
					"Client");
			clientColumn.setCellValueFactory(
					new Callback<TreeTableColumn.CellDataFeatures<MissionTreeRow, String>, ObservableValue<String>>() {

						@Override
						public ObservableValue<String> call(CellDataFeatures<MissionTreeRow, String> param) {
							if (param.getValue().getValue() == null)
								return new SimpleStringProperty(null);

							return new SimpleStringProperty(param.getValue().getValue().client.get().name);
						}
					});

			JFXTreeTableColumn<MissionTreeRow, LocalDate> endDateColumn = new JFXTreeTableColumn<MissionTreeRow, LocalDate>(
					"End");
			endDateColumn.setCellValueFactory(
					new Callback<TreeTableColumn.CellDataFeatures<MissionTreeRow, LocalDate>, ObservableValue<LocalDate>>() {

						@Override
						public ObservableValue<LocalDate> call(CellDataFeatures<MissionTreeRow, LocalDate> param) {
							if (param.getValue().getValue() == null)
								return new SimpleObjectProperty<LocalDate>(null);

							return param.getValue().getValue().to;
						}
					});

			missionsView.getColumns().add(nameColumn);
			missionsView.getColumns().add(clientColumn);
			missionsView.getColumns().add(endDateColumn);

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

			missionsView.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener<TreeItem<MissionTreeRow>>() {

						public void changed(ObservableValue<? extends TreeItem<MissionTreeRow>> observable,
								TreeItem<MissionTreeRow> oldValue, TreeItem<MissionTreeRow> newValue) {

							if (oldValue == null)
								setSelectedMission(newValue.getValue(), null);
							else if (newValue == null)
								setSelectedMission(null, oldValue.getValue());
							else
								setSelectedMission(newValue.getValue(), oldValue.getValue());
						}
					});

			TreeItem<EmployeeTreeRow> root = new RecursiveTreeItem<EmployeeTreeRow>(employeelist,
					RecursiveTreeObject::getChildren);
			treeview.setRoot(root);
			treeview.setShowRoot(false);

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
		StringProperty phoneNumber;

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
			this.phoneNumber = new SimpleStringProperty(this.e.phoneNumber);
		}

		public void clear() {
			id.set(e.id);
			firstName.set(e.firstName);
			lastName.set(e.lastName);
			email.set(e.email);
			poste.set(e.poste);
			password.set(e.password);
			phoneNumber.set(e.phoneNumber);
		}

		public EmployeeDto getNewDto() {
			EmployeeDto result = new EmployeeDto();

			result.email = email.get();
			result.firstName = firstName.get();
			result.lastName = lastName.get();
			result.id = id.get();
			result.password = password.get();
			result.poste = poste.get();
			result.phoneNumber = phoneNumber.get();
			
			return result;
		}
	}

	class MissionTreeRow extends RecursiveTreeObject<MissionTreeRow> {

		MissionDto m;

		IntegerProperty id;
		StringProperty name;
		StringProperty desc;
		ObjectProperty<EmployeeDto> consultant;
		ObjectProperty<LocalDate> from;
		ObjectProperty<LocalDate> to;
		ObjectProperty<ClientDto> client;

		public MissionTreeRow(MissionDto m) {
			if (m == null)
				m = new MissionDto();

			this.m = m;

			this.id = new SimpleIntegerProperty(this.m.id);
			this.name = new SimpleStringProperty(this.m.name);
			this.desc = new SimpleStringProperty(this.m.description);
			this.consultant = new SimpleObjectProperty<EmployeeDto>(this.m.consultant);
			if (this.m.from != null)
				this.from = new SimpleObjectProperty<LocalDate>(
						this.m.from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			else
				this.from = new SimpleObjectProperty<LocalDate>(null);

			if (this.m.from != null)
				this.to = new SimpleObjectProperty<LocalDate>(
						this.m.to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			else
				this.to = new SimpleObjectProperty<LocalDate>(null);

			this.client = new SimpleObjectProperty<ClientDto>(this.m.client);
		}

		public void clear() {
			id.set(m.id);
			name.set(m.name);
			desc.set(m.description);
			consultant.set(m.consultant);
			from.set(m.from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			to.set(m.to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			client.set(m.client);
		}

		public MissionDto getNewDto() {
			MissionDto result = new MissionDto();

			result.name = name.get();
			result.description = desc.get();
			result.consultant = consultant.get();
			result.from = Date.from(from.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
			result.to = Date.from(to.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
			result.client = client.get();

			return result;
		}
	}

	@Override
	public void init() {
		isInit = false;

		getAllConsultant();

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
								employeelist.clear();

								for (EmployeeDto e : ((GetAllConsultantsResponse) response).consultants) {
									employeelist.add(new EmployeeTreeRow(e));
								}
							}
						}
					}
				});
			}
		});
	}

	private void getMissions(EmployeeDto e) {
		if (treeview.getSelectionModel() == null || treeview.getSelectionModel().getSelectedItem() == null)
			return;

		if (e == null)
			return;

		GetMissionsByConsultantRequest request = new GetMissionsByConsultantRequest();
		request.employee = e;

		AbylsenApiClient.getInstance().GetMissionByConsultant(request, new IAbylsenApiListener() {

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
							if (response instanceof GetMissionsResponse) {
								missionList.clear();

								for (MissionDto m : ((GetMissionsResponse) response).missions) {
									missionList.add(new MissionTreeRow(m));
								}

								TreeItem<MissionTreeRow> root = new RecursiveTreeItem<MissionTreeRow>(missionList,
										RecursiveTreeObject::getChildren);
								missionsView.setRoot(root);
								missionsView.setShowRoot(false);
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
			phoneNumberTextField.textProperty().unbindBidirectional(oldRow.phoneNumber);
			
			missionList.clear();
		}
		if (newRow != null) {
			firstNameTextField.textProperty().bindBidirectional(newRow.firstName);
			lastNameTextField.textProperty().bindBidirectional(newRow.lastName);
			emailTextField.textProperty().bindBidirectional(newRow.email);
			posteComboBox.valueProperty().bindBidirectional(newRow.poste);
			phoneNumberTextField.textProperty().bindBidirectional(newRow.phoneNumber);

			getMissions(newRow.getNewDto());
		}
	}

	private void setSelectedMission(MissionTreeRow newRow, MissionTreeRow oldRow) {
		if (oldRow != null) {
			missionNameTextField.textProperty().unbindBidirectional(oldRow.name);
			missionDescriptionTextField.textProperty().unbindBidirectional(oldRow.desc);
			missionstartDate.valueProperty().unbindBidirectional(oldRow.from);
			missionEndDate.valueProperty().unbindBidirectional(oldRow.to);
		}
		if (newRow != null) {
			missionNameTextField.textProperty().bindBidirectional(newRow.name);
			missionDescriptionTextField.textProperty().bindBidirectional(newRow.desc);
			missionstartDate.valueProperty().bindBidirectional(newRow.from);
			missionEndDate.valueProperty().bindBidirectional(newRow.to);
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
			row.phoneNumber.set(phoneNumberTextField.textProperty().get());

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
		if (treeview.getSelectionModel() != null && treeview.getSelectionModel().getSelectedItem() != null) {
			setSelectedEmployee(new EmployeeTreeRow(null), treeview.getSelectionModel().getSelectedItem().getValue());
			treeview.getSelectionModel().select(null);
		}
	}

	@FXML
	private void handleClearSelection2() {
		if (missionsView.getSelectionModel() != null && missionsView.getSelectionModel().getSelectedItem() != null) {
			setSelectedMission(new MissionTreeRow(null), missionsView.getSelectionModel().getSelectedItem().getValue());
			missionsView.getSelectionModel().select(null);
		}
	}

	@FXML
	private void handleCancelEmployee() {
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

						if (br.statusCode == 200) {
							getAllConsultant();
						}
					}
				});
			}
		});
	}
}
