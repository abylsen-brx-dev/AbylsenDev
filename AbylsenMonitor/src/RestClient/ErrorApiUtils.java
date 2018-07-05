package RestClient;

import java.io.IOException;
import java.lang.annotation.Annotation;

import model.BaseResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ErrorApiUtils {

	 public static BaseResponse parseError(Response<?> response, Retrofit retrofit) {
	        Converter<ResponseBody, BaseResponse> converter = 
	        		retrofit.responseBodyConverter(BaseResponse.class, new Annotation[0]);

	        BaseResponse error = new BaseResponse();

	        try {
	            error = converter.convert(response.errorBody());
	        } catch (IOException e) {
	        	error.statusCode = response.code();
	        	error.message = "Error while contacting the server.";
	        }

	        return error;
	    }
}
