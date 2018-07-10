package views;

import java.io.IOException;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import Dto.EmployeeDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import interfaces.IInitializable;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import model.GetAllConsultantsResponse;
import okhttp3.Headers;

public class ConsultantMangerController extends AnchorPane implements IInitializable {

	private String title = "Consultant Wizard";

	@FXML
	private JFXTreeTableView<EmployeeTreeRow> treeview;

	private ObservableList<EmployeeTreeRow> list;

	private boolean isInit;

	public ConsultantMangerController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ConsultantMangerView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			isInit = false;
			list = FXCollections.observableArrayList();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	class EmployeeTreeRow extends RecursiveTreeObject<EmployeeTreeRow> {
		IntegerProperty id;
		StringProperty firstName;
		StringProperty lastName;
		StringProperty email;

		public EmployeeTreeRow(int id, String firstName, String lastName, String email) {
			this.id = new SimpleIntegerProperty(id);
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
		}
	}

	@Override
	public void init() {
		if(isInit)
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

		treeview.getColumns().clear();

		treeview.getColumns().add(firstNameColumn);
		treeview.getColumns().add(lastNameColumn);
		treeview.getColumns().add(emailColumn);

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
								list.clear();

								for (EmployeeDto e : ((GetAllConsultantsResponse) response).consultants) {
									list.add(new EmployeeTreeRow(e.id, e.firstName, e.lastName,
											e.email));
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

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean isInit() {
		return isInit;
	}
}
