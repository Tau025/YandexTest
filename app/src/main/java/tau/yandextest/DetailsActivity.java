package tau.yandextest;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;
/**
 * Created by TAU on 21.04.2016.
 */
public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "extra_id";
    private static final String LOG_TAG = "DetailsActivity";
    private int INFO_IMAGE_HEIGHT = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        int singerId = getIntent().getIntExtra(EXTRA_ID, 0);
        Singer singer = Singer.getSingerById(singerId);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(singer.getName());
        }

        Resources res = getResources();
        Locale locale = res.getConfiguration().locale;

        //prepare ImageView
        ImageView iv_infoImage = (ImageView) findViewById(R.id.info_image);
        Drawable drawable = ContextCompat.getDrawable(this, singer.getImageResourceId());
        iv_infoImage.setImageDrawable(drawable);
        iv_infoImage.setContentDescription(singer.getName());
        measureScreen();
        Log.d(LOG_TAG, "INFO_IMAGE_HEIGHT: " + String.valueOf(INFO_IMAGE_HEIGHT));
        iv_infoImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                INFO_IMAGE_HEIGHT
        ));

        //prepare TextView for singer genres
        TextView tv_genres = (TextView) findViewById(R.id.genres);
        StringBuilder genres = new StringBuilder();
        String prefix = "";
        for (String genre : singer.getGenres()) {
            genres.append(prefix);
            prefix = ", ";
            genres.append(genre);
        }
        tv_genres.setText(genres);

        //prepare TextView for quantity of singer's albums and tracks
        TextView tv_albumsAndTracksQty = (TextView) findViewById(R.id.albums_and_tracks_qty);
        String albums = res.getQuantityString(R.plurals.count_of_albums, singer.getAlbumsQty(), singer.getAlbumsQty());
        String tracks = res.getQuantityString(R.plurals.count_of_tracks, singer.getTracksQty(), singer.getTracksQty());
        tv_albumsAndTracksQty.setText(String.format(locale, "%s  ·  %s", albums, tracks));

        //prepare TextView for Singer's biography
        TextView tv_biographyText = (TextView) findViewById(R.id.biography_text);
        tv_biographyText.setText(singer.getBiography());
    }

    //is used to keep aspect ratio of infoImage
    private void measureScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        INFO_IMAGE_HEIGHT = Math.round(screenWidth / 384 * 245);
    }
}
