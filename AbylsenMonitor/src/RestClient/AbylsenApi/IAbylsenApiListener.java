package RestClient.AbylsenApi;

import okhttp3.Headers;

public interface IAbylsenApiListener {
	void OnResponseAccepted(Object response, Headers headers);
	
	void OnResponseRefused(Object response, Headers headers);
}
