package RestClient;

import java.io.IOException;

import enums.HttpHeaders;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {

	private static Retrofit retrofit = null;

	public static Retrofit getClient(String baseUrl) {
		if (retrofit == null) {

			OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder();

			oktHttpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header(HttpHeaders.HEADER_APIKEY, "devApiKey123456")
                            .header(HttpHeaders.HEADER_TOKEN, "devApiKey123456")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });
			
			retrofit = new Retrofit.Builder()
					.baseUrl(baseUrl)
					.addConverterFactory(JacksonConverterFactory.create())
					.client(oktHttpClient.build())
					.build();
		}

		return retrofit;
	}
}
