package tau.yandextest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
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
import retrofit2.http.GET;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";

    //API_BASE_URL всегда завершается /
    private static final String API_BASE_URL = "http://download.cdn.yandex.net/";
    //URL_ENDPOINT не начинается с /
    private static final String URL_ENDPOINT = "mobilization-2016/artists.json";

    private Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = createProgressBar(this);
        sendRequest();
//        setRotationAnimation();
    }

    private Dialog createProgressBar(Context mContext){
        //не следует передавать getApplicationContext() как параметр. вместо этого используйте "this" при вызове из активити
        Dialog dialog = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.progress_bar, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
        dialog.setContentView(view);
        return dialog;
    }

    private void sendRequest() {
        Log.d(LOG_TAG, "entered retrofit sendRequest method");
        progressBar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        YandexTestAPI yandexTestClient = retrofit.create(YandexTestAPI.class);

        Callback<List<Artist>> callback = new Callback<List<Artist>>() {
            @Override
            public void onResponse (Call<List<Artist>> call, Response<List<Artist>> response){
                if (response.isSuccessful()) {
                    //получен ответ от сервера об успехе. обработаем полученный результат
                    Log.d(LOG_TAG, "retrofit response isSuccessful");
                    List<Artist> artistsResponse = response.body();
                    progressBar.dismiss();
                    handleSuccess(artistsResponse);
                } else {
                    //ответ пришел, но говорит об ошибке
                    int errorCode = response.code();
                    Log.d(LOG_TAG, "retrofit response is not successful. errorCode: " + String.valueOf(errorCode));
                    Log.d(LOG_TAG, "check URL_ENDPOINT and executeRequest parameters if any");
                    ResponseBody errorBody = response.errorBody();
                    progressBar.dismiss();
                    handleError(errorBody);
                }
            }

            @Override
            public void onFailure (Call<List<Artist>> call, Throwable t){
                Log.d(LOG_TAG, "retrofit failure: " + t.getLocalizedMessage());
                Log.d(LOG_TAG, "check API_BASE_URL and internet connection");
                progressBar.dismiss();
                handleFailure(t.getLocalizedMessage());
            }
        };
        Call<List<Artist>> call = yandexTestClient.executeRequest();
        //запрос можно выполнять синхронно методом execute(), или асинхронно методом enqueue()
        call.enqueue(callback);
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
            Log.d(LOG_TAG, "errorBody: " + errorBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFailure(String failureMessage){
        Toast.makeText(MainActivity.this, failureMessage, Toast.LENGTH_LONG).show();
    }

    interface YandexTestAPI {
        //Base URL: always ends with /
        //@Url: DO NOT start with /
        @GET(URL_ENDPOINT)
        Call<List<Artist>> executeRequest();
    }

    private boolean setRotationAnimation() {
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        win.setAttributes(winParams);

        winParams.rotationAnimation = LayoutParams.ROTATION_ANIMATION_CROSSFADE;
        win.setAttributes(winParams);
        return true;
    }
}
