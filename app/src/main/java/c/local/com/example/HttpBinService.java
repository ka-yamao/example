package c.local.com.example;

import c.local.com.example.adapter.ErrorHandlingAdapter;
import c.local.com.example.model.Ip;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HttpBinService {

	@GET("/{path}/{code}")
	ErrorHandlingAdapter.MyCall<Ip> getError(@Path("path") String path, @Path("code") String code);

	@GET("/{path}")
	ErrorHandlingAdapter.MyCall<Ip> getOK(@Path("path") String path);

	@GET("/delay/30")
	ErrorHandlingAdapter.MyCall<Ip> getDelay();

}
