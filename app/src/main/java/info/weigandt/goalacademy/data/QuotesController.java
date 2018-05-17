package info.weigandt.goalacademy.data;

import info.weigandt.goalacademy.classes.Constants;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

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
            Timber.e(Constants.API_ERROR_TEXT, response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<Quote> call, Throwable t) {
        Timber.e(t.getMessage());
    }
}
