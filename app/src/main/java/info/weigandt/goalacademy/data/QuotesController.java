package info.weigandt.goalacademy.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuotesController implements retrofit2.Callback<QuoteResult> {

    final String BASE_URL = NetworkHelper.FORISMATIC_BASE_URL;
    private PostRetrofitQuoteCallListener mPostRetrofitQuoteCallListener;

    public QuotesController(PostRetrofitQuoteCallListener postRetrofitQuoteCallListener) {
        mPostRetrofitQuoteCallListener = postRetrofitQuoteCallListener;
    }

    public void startLoadingQuote() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ForismaticApi forismaticApi = retrofit.create(ForismaticApi.class);
        Call<QuoteResult> call = forismaticApi.loadRandomQuote();
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<QuoteResult> call, Response<QuoteResult> response) {
        if (response.isSuccessful()) {
            QuoteResult quoteResult = response.body();
            // send the results to the MainActivity via Listener
            mPostRetrofitQuoteCallListener.onPostTask(quoteResult);
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<QuoteResult> call, Throwable t) {
        t.printStackTrace();
    }

}
