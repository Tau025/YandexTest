package tau.yandextest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
/**
 * Created by TAU on 20.05.2016.
 */
public interface YandexTestAPI {
    //Base URL: always ends with /
    //@Url: DO NOT start with /
    @GET(Constants.URL_ENDPOINT)
    Call<List<Artist>> executeRequest();
}
