package tau.yandextest;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;
/**
 * Created by TAU on 21.04.2016.
 */
public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "extra_id";

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

        ImageView imageView = (ImageView) findViewById(R.id.info_image);
        Drawable drawable = ContextCompat.getDrawable(this, singer.getImageResourceId());
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(singer.getName());

        TextView tv_genres = (TextView) findViewById(R.id.genres);
        StringBuilder genres = new StringBuilder();
        String prefix = "";
        for (String genre : singer.getGenres()) {
            genres.append(prefix);
            prefix = ", ";
            genres.append(genre);
        }
        tv_genres.setText(genres);

        TextView tv_albumsAndTracksQty = (TextView) findViewById(R.id.albums_and_tracks_qty);
        String albums = res.getQuantityString(R.plurals.count_of_albums, singer.getAlbumsQty(), singer.getAlbumsQty());
        String tracks = res.getQuantityString(R.plurals.count_of_tracks, singer.getTracksQty(), singer.getTracksQty());
        tv_albumsAndTracksQty.setText(String.format(locale, "%s  ·  %s", albums, tracks));

        TextView tv_biographyText = (TextView) findViewById(R.id.biography_text);
        tv_biographyText.setText(singer.getBiography());
    }
}
