package views;

import java.io.IOException;

import com.jfoenix.controls.JFXListView;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import Dto.ClientDto;
import Dto.EmployeeDto;
import RestClient.AbylsenApi.AbylsenApiClient;
import RestClient.AbylsenApi.IAbylsenApiListener;
import interfaces.IInitializable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.GetAllClientResponse;
import okhttp3.Headers;

public class MainDashboardController extends AnchorPane implements IInitializable, MapComponentInitializedListener {

	private String title = "Main DashBoard";

	@FXML
	private GoogleMapView googleMapView;
	
	private GoogleMap googleMap;
	
	private boolean isInit;
	
	private boolean isMapInit;
	
	@FXML
	private JFXListView<ClientLabel> clientList;
	
	@FXML
	private JFXListView<EmployeeLabel> employeeList;
	
	public MainDashboardController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainDashboardView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			googleMapView.addMapInializedListener(this);
			clientList.setExpanded(true);
			clientList.depthProperty().set(1);
			
			employeeList.setExpanded(true);
			employeeList.depthProperty().set(1);
			
			clientList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClientLabel>() {

				public void changed(
						ObservableValue<? extends ClientLabel> observable,
						ClientLabel oldValue, ClientLabel newValue) {
					
					googleMap.setCenter(new LatLong(newValue.c.lng, newValue.c.lat));
					
					employeeList.getItems().clear();
					for(EmployeeDto e : newValue.c.linkedConsultants) {
						employeeList.getItems().add(new EmployeeLabel(e));
					}
				}
			});

			isInit = false;
			
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
		if(isInit || !isMapInit)
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
		//Set the initial properties of the map.
	    MapOptions mapOptions = new MapOptions();

	    mapOptions.center(new LatLong(0, 0))
	            .overviewMapControl(false)
	            .panControl(false)
	            .rotateControl(false)
	            .scaleControl(false)
	            .streetViewControl(false)
	            .zoomControl(false)
	            .mapTypeControl(false)
	            .zoom(7);

	    googleMap = googleMapView.createMap(mapOptions);
	    
	    isMapInit = true;
	    
	    init();
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
						
						for(ClientDto c : ((GetAllClientResponse)response).clients) {
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
	
	class ClientLabel extends Label{
		private Marker m;
		private ClientDto c;
		
		public ClientLabel(ClientDto c) {
			super(c.name);
			
			this.c = c;
			MarkerOptions options = new MarkerOptions();

		    options.position(new LatLong(c.lng, c.lat))
		                .visible(Boolean.TRUE)
		                .title(c.name);
		    
		    m = new Marker(options);
		    googleMap.addMarker(m);
		}
	}
	
	class EmployeeLabel extends Label{
		public EmployeeDto e;
		
		public EmployeeLabel(EmployeeDto e) {
			super(e.lastName + " " + e.firstName);
			
			this.e = e;
		}
	}
}
