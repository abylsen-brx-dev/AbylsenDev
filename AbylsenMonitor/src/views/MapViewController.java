package views;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.DirectionsPane;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.directions.TravelModes;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;

import Dto.ClientDto;
import Dto.EmployeeDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import interfaces.IInitializable;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import model.GetAllClientResponse;
import okhttp3.Headers;

public class MapViewController extends AnchorPane
		implements IInitializable, MapComponentInitializedListener, DirectionsServiceCallback {

	private String title = "Map Viewer";

	@FXML
	private GoogleMapView googleMapView;

	private GoogleMap googleMap;

	private GeocodingService geocodingService;

	protected DirectionsService directionsService;

	protected DirectionsPane directionsPane;

	private boolean isInit;

	private boolean isMapInit;

	@FXML
	private JFXListView<ClientLabel> clientList;

	@FXML
	private JFXListView<EmployeeLabel> employeeList;

	@FXML
	private BorderPane sendMessagePane;

	@FXML
	private JFXButton messageShowButton;

	private boolean isMessagePaneVisible;

	@FXML
	private JFXListView<EmployeeLabel> CCContainer;

	@FXML
	private JFXTextField addressSearchTextField;

	@FXML
	private JFXTextField addressToTextField;

	private ObjectProperty<Marker> searchMarker;

	public MapViewController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MapView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			searchMarker = new SimpleObjectProperty<Marker>(null);
			googleMapView.addMapInializedListener(this);
			clientList.setExpanded(true);
			clientList.depthProperty().set(1);

			employeeList.setExpanded(true);
			employeeList.depthProperty().set(1);

			CCContainer.setExpanded(true);
			CCContainer.depthProperty().set(1);
			CCContainer.getItems().clear();
			CCContainer.setMinWidth(50);
			CCContainer.setPrefWidth(50);

			clientList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClientLabel>() {

				public void changed(ObservableValue<? extends ClientLabel> observable, ClientLabel oldValue,
						ClientLabel newValue) {

					googleMap.setCenter(new LatLong(newValue.c.lng, newValue.c.lat));

					employeeList.getItems().clear();
					for (EmployeeDto e : newValue.c.linkedConsultants) {
						employeeList.getItems().add(new EmployeeLabel(e));
					}
				}
			});

			employeeList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EmployeeLabel>() {

				public void changed(ObservableValue<? extends EmployeeLabel> observable, EmployeeLabel oldValue,
						EmployeeLabel newValue) {
					if (isMessagePaneVisible) {
						CCContainer.getItems().add(new EmployeeLabel(newValue.e));
					}
				}
			});

			CCContainer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EmployeeLabel>() {

				public void changed(ObservableValue<? extends EmployeeLabel> observable, EmployeeLabel oldValue,
						EmployeeLabel newValue) {
					// CCContainer.getItems().remove(newValue);
				}
			});

			isMessagePaneVisible = true;
			isInit = false;

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	private void handleMessagePanelShow() {
		showBottomPane(isMessagePaneVisible, sendMessagePane, messageShowButton.getHeight());
		isMessagePaneVisible = !isMessagePaneVisible;
	}

	@FXML
	private void handleSearchButton() {
		if (searchMarker.get() != null) {
			googleMap.removeMarker(searchMarker.get());
			searchMarker.set(null);
		}

		if (addressToTextField.textProperty().get() == null || addressToTextField.textProperty().get().equals("")) {
			geocodingService.geocode(addressSearchTextField.textProperty().get(),
					(GeocodingResult[] results, GeocoderStatus status) -> {

						LatLong latLong = null;

						if (status == GeocoderStatus.ZERO_RESULTS) {
							Alert alert = new Alert(Alert.AlertType.ERROR, "No matching address found");
							alert.show();
							return;
						} else if (results.length > 1) {
							Alert alert = new Alert(Alert.AlertType.WARNING,
									"Multiple results found, showing the first one.");
							alert.show();
							latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(),
									results[0].getGeometry().getLocation().getLongitude());
						} else {
							latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(),
									results[0].getGeometry().getLocation().getLongitude());
						}

						searchMarker.set(
								getMarker(latLong, addressSearchTextField.textProperty().get(), MarkerType.search));
						googleMap.setCenter(latLong);
						googleMap.setZoom(11);
						googleMap.addMarker(searchMarker.get());
					});
		} else {
			DirectionsRequest request = new DirectionsRequest(addressSearchTextField.textProperty().get(),
					addressToTextField.textProperty().get(), TravelModes.DRIVING);
			directionsService.getRoute(request, this,
					new DirectionsRenderer(true, googleMapView.getMap(), directionsPane));
		}
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void init() {
		if (isInit || !isMapInit)
			return;

		getAllClients();
		isInit = true;
	}

	@Override
	public boolean isInit() {
		return isInit;
	}

	@Override
	public void mapInitialized() {
		// Set the initial properties of the map.
		MapOptions mapOptions = new MapOptions();

		mapOptions.center(new LatLong(0, 0)).overviewMapControl(false).panControl(false).rotateControl(false)
				.scaleControl(false).streetViewControl(false).zoomControl(false).mapTypeControl(false).zoom(7);

		googleMap = googleMapView.createMap(mapOptions);
		geocodingService = new GeocodingService();
		directionsService = new DirectionsService();
		directionsPane = googleMapView.getDirec();

		isMapInit = true;

		init();
	}

	@Override
	public void directionsReceived(DirectionsResult results, DirectionStatus status) {

	}

	private void getAllClients() {
		AbylsenApiClient.getInstance().getAllClients(new IAbylsenApiListener() {

			@Override
			public void OnResponseRefused(Object response, Headers headers) {

			}

			@Override
			public void OnResponseAccepted(Object response, Headers headers) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						clientList.getItems().clear();

						for (ClientDto c : ((GetAllClientResponse) response).clients) {
							clientList.getItems().add(getClientNode(c));
						}
					}
				});
			}
		});
	}

	private ClientLabel getClientNode(ClientDto c) {
		ClientLabel l = new ClientLabel(c);

		return l;
	}

	private void showBottomPane(boolean visible, Region n, double offset) {
		TranslateTransition tt = new TranslateTransition();
		tt.setDuration(Duration.millis(200));

		tt.setNode(n);

		if (visible) {
			tt.setToY(n.getHeight() - offset);
			tt.setFromY(0);
		} else {
			tt.setToY(0);
			tt.setFromY(n.getHeight() - offset);
		}

		tt.play();
	}

	private Marker getMarker(LatLong position, String name, MarkerType type) {
		if (position == null)
			return null;

		MarkerOptions options = new MarkerOptions();

		options.position(position).visible(Boolean.TRUE).title(name);

		switch (type) {
		case search:
			options.icon(
					"https://fiverr-res.cloudinary.com/images/t_delivery_thumb,q_auto,f_auto/deliveries/95678264/original/b54a/create-a-vector-flat-avatar-or-portrait-of-you-in-my-style.png");
			break;
		case client:
			options.icon(
					"https://fiverr-res.cloudinary.com/images/t_delivery_thumb,q_auto,f_auto/deliveries/95158665/original/364a/create-a-vector-flat-avatar-or-portrait-of-you-in-my-style.png");
			break;
		default:
			options.icon(
					"https://fiverr-res.cloudinary.com/images/t_delivery_thumb,q_auto,f_auto/deliveries/94921002/original/72b8/create-a-vector-flat-avatar-or-portrait-of-you-in-my-style.png");
			break;
		}
		return new Marker(options);
	}

	class ClientLabel extends Label {
		private Marker m;
		private ClientDto c;

		public ClientLabel(ClientDto c) {
			super(c.name);

			this.c = c;

			m = getMarker(new LatLong(c.lng, c.lat), c.name, MarkerType.client);

			googleMap.addMarker(m);
		}
	}

	class EmployeeLabel extends Label {
		public EmployeeDto e;

		public EmployeeLabel(EmployeeDto e) {
			super(e.lastName + " " + e.firstName);

			this.e = e;
		}
	}

	enum MarkerType {
		search, client
	}
}
