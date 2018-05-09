package info.weigandt.goalacademy.data;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuotesController implements retrofit2.Callback<Quote> {

    private PostRetrofitQuoteCallListener mPostRetrofitQuoteCallListener;

    public QuotesController(PostRetrofitQuoteCallListener postRetrofitQuoteCallListener) {
        mPostRetrofitQuoteCallListener = postRetrofitQuoteCallListener;
    }

    public void startLoadingQuote() {
        Retrofit retrofit = ApiClient.getClient();
        ForismaticApi forismaticApi = retrofit.create(ForismaticApi.class);
        Call<Quote> call = forismaticApi.loadRandomQuote();
        call.enqueue(this); // "this" passes the onResponse callback
    }
    @Override
    public void onResponse(Call<Quote> call, Response<Quote> response) {
        if (response.isSuccessful()) {
            Quote quote = response.body();
            // send the results to the MainActivity via Listener
            mPostRetrofitQuoteCallListener.onPostApiCall(quote);
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<Quote> call, Throwable t) {
        t.printStackTrace();
    }   // TODO improve error handling

}
