package views;

import java.io.IOException;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;

import interfaces.IInitializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainDashboardController extends AnchorPane implements IInitializable, MapComponentInitializedListener {

	private String title = "Main DashBoard";

	@FXML
	private GoogleMapView googleMapView;
	
	private GoogleMap googleMap;
	
	private boolean isInit;
	
	private boolean isMapInit;
	
	public MainDashboardController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainDashboardView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			googleMapView.addMapInializedListener(this);
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
	            .zoom(12);

	    googleMap = googleMapView.createMap(mapOptions);
	    
	    isMapInit = true;
	}

}
