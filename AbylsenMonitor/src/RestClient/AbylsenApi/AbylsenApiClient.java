package RestClient.AbylsenApi;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import Dto.MissionDto;
import RestClient.ErrorApiUtils;
import contexte.MainApplicationContexte;
import enums.HttpHeaders;
import enums.HttpStatus;
import model.*;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class AbylsenApiClient {
	private static AbylsenApiClient instance;

	public static AbylsenApiClient getInstance() {
		if (instance == null)
			instance = new AbylsenApiClient(MainApplicationContexte.getInstance().getAbylsenApiUrl());

		return instance;
	}

	private AbylsenApiClient(String url) {
		OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder();

		oktHttpClient.addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request original = chain.request();
				Request request = original.newBuilder()
						.header(HttpHeaders.HEADER_APIKEY, MainApplicationContexte.getInstance().getApiKey())
						.header(HttpHeaders.HEADER_TOKEN, MainApplicationContexte.getInstance().getToken())
						.method(original.method(), original.body()).build();

				return chain.proceed(request);
			}
		});

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(JacksonConverterFactory.create(objectMapper))
				.client(oktHttpClient.build()).build();
	}

	private Retrofit retrofit;

	public void register(RegistrationRequest request, IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.register(request).enqueue(new Callback<BaseResponse>() {

			@Override
			public void onResponse(Call<BaseResponse> arg0, retrofit2.Response<BaseResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(br, arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}

	public void create(CreateAccountRequest request, IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.create(request).enqueue(new Callback<BaseResponse>() {

			@Override
			public void onResponse(Call<BaseResponse> arg0, retrofit2.Response<BaseResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(br, arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}

	public void keepAlive(IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.keepAlive().enqueue(new Callback<BaseResponse>() {

			@Override
			public void onResponse(Call<BaseResponse> arg0, retrofit2.Response<BaseResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(br, arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}

	public void getInfo(IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.getInfo().enqueue(new Callback<GetInfoResponse>() {

			@Override
			public void onResponse(Call<GetInfoResponse> arg0, retrofit2.Response<GetInfoResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(arg1.body(), arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<GetInfoResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}
	
	public void getAllConsultant(IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.getAllConsultant().enqueue(new Callback<GetAllConsultantsResponse>() {

			@Override
			public void onResponse(Call<GetAllConsultantsResponse> arg0, retrofit2.Response<GetAllConsultantsResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(arg1.body(), arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<GetAllConsultantsResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}
	
	public void getAllClients(IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.getAllClients().enqueue(new Callback<GetAllClientResponse>() {

			@Override
			public void onResponse(Call<GetAllClientResponse> arg0, retrofit2.Response<GetAllClientResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(arg1.body(), arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<GetAllClientResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}
	
	public void addEmployee(AddEmployeeRequest request, IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.addEmployee(request).enqueue(new Callback<BaseResponse>() {

			@Override
			public void onResponse(Call<BaseResponse> arg0, retrofit2.Response<BaseResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(arg1.body(), arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}
	
	public void UpdateEmployee(UpdateEmployeeRequest request, IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.updateEmployee(request).enqueue(new Callback<BaseResponse>() {

			@Override
			public void onResponse(Call<BaseResponse> arg0, retrofit2.Response<BaseResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(arg1.body(), arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<BaseResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}
	
	public void GetMissionByConsultant(GetMissionsByConsultantRequest request, IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.getByConsultant(request).enqueue(new Callback<GetMissionsResponse>() {

			@Override
			public void onResponse(Call<GetMissionsResponse> arg0, retrofit2.Response<GetMissionsResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(arg1.body(), arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<GetMissionsResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}
	
	public void addMission(MissionDto request, IAbylsenApiListener listener) {
		IAbylsenApiRestClient client = retrofit.create(IAbylsenApiRestClient.class);

		client.addMission(request).enqueue(new Callback<GetMissionsResponse>() {

			@Override
			public void onResponse(Call<GetMissionsResponse> arg0, retrofit2.Response<GetMissionsResponse> arg1) {
				BaseResponse br = arg1.body();
				if (br == null) {
					br = ErrorApiUtils.parseError(arg1, retrofit);
				}

				if (br.statusCode == 200) {
					listener.OnResponseAccepted(arg1.body(), arg1.headers());
				} else {
					listener.OnResponseRefused(br, arg1.headers());
				}
			}

			@Override
			public void onFailure(Call<GetMissionsResponse> arg0, Throwable arg1) {
				BaseResponse br = new BaseResponse();
				br.message = arg1.getMessage();
				br.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;

				listener.OnResponseRefused(br, null);
			}
		});
	}
}
