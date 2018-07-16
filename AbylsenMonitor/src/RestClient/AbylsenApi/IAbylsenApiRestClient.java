package RestClient.AbylsenApi;

import model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IAbylsenApiRestClient {
	
	@POST("/registration/register/")
	Call<BaseResponse> register(@Body RegistrationRequest request);
	
	@POST("/registration/create/")
	Call<BaseResponse> create(@Body CreateAccountRequest request);
	
	@POST("/registration/keepalive/")
	Call<BaseResponse> keepAlive();
	
	@GET("/person/getInfo/")
	Call<GetInfoResponse> getInfo();
	
	@GET("/person/getAllConsultant/")
	Call<GetAllConsultantsResponse> getAllConsultant();
	
	@GET("/client/getAll/")
	Call<GetAllClientResponse> getAllClients();
	
	@POST("/person/addEmployee/")
	Call<BaseResponse> addEmployee(@Body AddEmployeeRequest request);
	
	@POST("/person/updateEmployee/")
	Call<BaseResponse> updateEmployee(@Body UpdateEmployeeRequest request);
}
