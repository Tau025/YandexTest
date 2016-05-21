package tau.yandextest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements NoInternetDF.NoInternetDFListener {
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkIsOnline()){
            sendRequest();
        } else {
            new NoInternetDF().show(getSupportFragmentManager(), "NoInternetDF");
        }
    }

    public boolean checkIsOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void sendRequest() {
        Log.d(Constants.LOG_TAG, "entered retrofit sendRequest method");
        new ProgressBarDF().show(getSupportFragmentManager(), "ProgressBarDF");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        YandexTestAPI yandexTestClient = retrofit.create(YandexTestAPI.class);

        Callback<List<Artist>> callback = new Callback<List<Artist>>() {
            @Override
            public void onResponse (Call<List<Artist>> call, Response<List<Artist>> response){
                if (response.isSuccessful()) {
                    //получен ответ от сервера об успехе. обработаем полученный результат
                    Log.d(Constants.LOG_TAG, "retrofit response isSuccessful");
                    List<Artist> artistsResponse = response.body();
                    dismissProgressBar();
                    handleSuccess(artistsResponse);
                } else {
                    //ответ пришел, но говорит об ошибке
                    int errorCode = response.code();
                    Log.d(Constants.LOG_TAG, "retrofit response is not successful. errorCode: " + String.valueOf(errorCode));
                    Log.d(Constants.LOG_TAG, "check URL_ENDPOINT and executeRequest parameters if any");
                    ResponseBody errorBody = response.errorBody();
                    dismissProgressBar();
                    handleError(errorBody);
                }
            }

            @Override
            public void onFailure (Call<List<Artist>> call, Throwable t){
                Log.d(Constants.LOG_TAG, "retrofit failure: " + t.getLocalizedMessage());
                Log.d(Constants.LOG_TAG, "check API_BASE_URL and internet connection");
                dismissProgressBar();
                handleFailure(t.getLocalizedMessage());
            }
        };
        Call<List<Artist>> call = yandexTestClient.executeRequest();
        //запрос можно выполнять синхронно методом execute(), или асинхронно методом enqueue()
        call.enqueue(callback);
    }

    private boolean dismissProgressBar(){
        ProgressBarDF dialog = (ProgressBarDF) getSupportFragmentManager().findFragmentByTag("ProgressBarDF");
        if (dialog != null) {
            dialog.dismiss();
            return true;
        } else {
            Log.d(Constants.LOG_TAG, "dialog already dismissed");
            return false;
        }
    }

    private void handleSuccess(List<Artist> artistsResponse){
        //сохраним полученный лист, отсортируем его и передадим адаптеру
        Artist.artistList = artistsResponse;
        Collections.sort(Artist.artistList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(Artist.artistList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setListener(new RecyclerViewAdapter.Listener() {
            @Override
            public void onClick(Artist selectedArtist) {
                //при тапе на элементе запустим новую активность, передав в неё id выбранного артиста
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_ID, selectedArtist.getId());
                startActivity(intent);
            }
        });
    }

    private void handleError(ResponseBody errorBody){
        try {
            Log.d(Constants.LOG_TAG, "errorBody: " + errorBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFailure(String failureMessage){
        Toast.makeText(MainActivity.this, failureMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPositiveButtonClicked(){
        //делаем 6 попыток повторного подключения с интервалом в 0,5 секунды
        new ProgressBarDF().show(getSupportFragmentManager(), "ProgressBarDF");
        counter = 0;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (checkIsOnline()) {
                    Log.d(Constants.LOG_TAG, "online!");
                    dismissProgressBar();
                    sendRequest();
                } else if (counter < 6) {
                    Log.d(Constants.LOG_TAG, "counter: " + String.valueOf(counter));
                    counter++;
                    handler.postDelayed(this, 1000);
                } else if (dismissProgressBar()){
                    //если все попытки не увенчались успехом, показываем диалог еще раз
                    new NoInternetDF().show(getSupportFragmentManager(), "NoInternetDF");
                }
            }
        });
    }
}
