package RestClient.AbylsenApi;

import Dto.ClientDto;
import Dto.MissionDto;
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

	@POST("/client/add/")
	Call<BaseResponse> addClient(@Body ClientDto request);

	@POST("/client/update/")
	Call<BaseResponse> updateClient(@Body ClientDto request);
	
	@POST("/person/addEmployee/")
	Call<BaseResponse> addEmployee(@Body AddEmployeeRequest request);
	
	@POST("/person/updateEmployee/")
	Call<BaseResponse> updateEmployee(@Body UpdateEmployeeRequest request);
	
	@POST("/missions/getByConsultant/")
	Call<GetMissionsResponse> getByConsultant(@Body GetMissionsByConsultantRequest request);
	
	@POST("/missions/add/")
	Call<GetMissionsResponse> addMission(@Body MissionDto mission);

}
