package c.local.com.example;

import c.local.com.example.model.Ip;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HttpBinService {

	@GET("/{path}/{code}")
	Observable<Ip> getError(@Path("path") String path, @Path("code") String code);
}
