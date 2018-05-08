package info.weigandt.goalacademy.data;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ForismaticApi {
    // @GET(NetworkHelper.GET_PARAMETERS)
    @GET(NetworkHelper.GET_PARAMETERS)
    Call<Quote> loadRandomQuote();
}
