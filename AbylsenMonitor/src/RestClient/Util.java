package RestClient;

import RestClient.AbylsenApi.IAbylsenApiRestClient;

public class Util {
	    public static final String BASE_URL = "http://localhost:8080/";
	    
	    public static IAbylsenApiRestClient getAPIService() {
	 
	        return RetrofitClient.getClient(BASE_URL).create(IAbylsenApiRestClient.class);
	    }
}
