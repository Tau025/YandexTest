package tau.yandextest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.concurrent.ExecutionException;
/**
 * Created by TAU on 21.04.2016.
 */
public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "extra_id";
    private static final String LOG_TAG = "DetailsActivity";
    private Singer singer;
    private Bitmap image;
    private int coverBigWidth = 0;
    private int coverBigHeight = 0;
    private ImageView iv_coverBig;
    private LinearLayout moveableLayout;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        int singerId = getIntent().getIntExtra(EXTRA_ID, 0);
        singer = Singer.getSingerById(singerId);
        if (singer != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(singer.getName());
            }

            //подготовим ImageView
            iv_coverBig = (ImageView) findViewById(R.id.cover_big);
            moveableLayout = (LinearLayout) findViewById(R.id.moveableLayout);
            new LoadGlideImageTask().execute();

            //подготовим TextView для жанров
            TextView tv_genres = (TextView) findViewById(R.id.genres);
            StringBuilder genres = new StringBuilder();
            String prefix = "";
            for (String genre : singer.getGenres()) {
                genres.append(prefix);
                prefix = ", ";
                genres.append(genre);
            }
            tv_genres.setText(genres);

            //подготовим TextView для количества альбомов и треков
            TextView tv_albumsAndTracksQty = (TextView) findViewById(R.id.albums_and_tracks_qty);
            Resources res = getResources();
            String albums = res.getQuantityString(R.plurals.count_of_albums, singer.getAlbums(), singer.getAlbums());
            String tracks = res.getQuantityString(R.plurals.count_of_tracks, singer.getTracks(), singer.getTracks());
            tv_albumsAndTracksQty.setText(String.format(res.getConfiguration().locale, "%s  ·  %s", albums, tracks));

            //подготовим TextView для биографии
            TextView tv_biographyText = (TextView) findViewById(R.id.biography_text);
            tv_biographyText.setText(singer.getDescription());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }


    class LoadGlideImageTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(LOG_TAG, "started LoadGlideImageTask");
            //делаем запрос на сервер чтобы получить размеры Cover.big
            try {
                image = Glide.with(getApplicationContext())
                        .load(singer.getCover().getBig())
                        .asBitmap()
                        .into(-1, -1)
                        .get();
            } catch (final ExecutionException | InterruptedException e) {
                Log.d(LOG_TAG, e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
                Log.d(LOG_TAG, "picture load success");
                if (image != null) {
                    //получим размеры загруженной картинки и сохраним ее пропорции вписав в ширину экрана
                    int imageWidth = image.getWidth();
                    int imageHeight = image.getHeight();
                    Log.d(LOG_TAG, "image from server w*h: " + String.valueOf(imageWidth) + "*" + String.valueOf(imageHeight));

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    coverBigWidth = metrics.widthPixels;
                    coverBigHeight = Math.round(((float) coverBigWidth / imageWidth) * imageHeight);


                    //выводим анимацию только если размеры картинки нам еще не известны
                    if (!singer.getCover().isBigCoverDownloaded()) {
                        //строка ниже необходима чтобы ScrollView пересчитал высоту контейнера, внутри которого происходит анимация
                        moveableLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, metrics.heightPixels));
                        animate(iv_coverBig, moveableLayout, 1000, 0, coverBigHeight, 0f, 1f);
                    } else {
                        //если мы уже загрузили картинку раньше, используем анимацию перехода по умолчанию
                        iv_coverBig.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, coverBigHeight));
                        moveableLayout.setAlpha(1f);

                        //повторный вызов with с тем же URL не создаст новую загрузку, а будет использовать кэшированную копию картинки
                        Glide.with(iv_coverBig.getContext())
                                .load(singer.getCover().getBig())
                                .centerCrop()
                                .crossFade()
                                .into(iv_coverBig);
                    }
                    singer.getCover().setBigCoverDownloaded(true);
                }
                Log.d(LOG_TAG, "coverBig w*h: " + String.valueOf(coverBigWidth) + "*" + String.valueOf(coverBigHeight));
            } else {
                Log.d(LOG_TAG, "picture load failed");
                Toast.makeText(DetailsActivity.this, getResources().getText(R.string.picture_load_failed_msg), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void animate(final ImageView scalableView, final View moveableView,
                         int duration, int fromYDelta, int toYDelta,
                         float fromTransparency, final float toTransparency) {
        final int scalableViewHeight = scalableView.getHeight() + toYDelta - fromYDelta;

        ObjectAnimator mover = ObjectAnimator.ofFloat(moveableView, "translationY", (float) fromYDelta, (float) toYDelta);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(moveableView, "alpha", fromTransparency, toTransparency);
        animatorSet = new AnimatorSet();
        animatorSet.play(mover).with(fadeIn);
        animatorSet.setDuration(duration);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(LOG_TAG, "onAnimationStart");
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(LOG_TAG, "onAnimationEnd");

                //необходимо для корректного завершения анимации перемещения
                animator = ObjectAnimator.ofFloat(moveableView, "translationY", 0.0f, 0.0f);
                animator.setDuration(1);
                animator.start();

                scalableView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, scalableViewHeight));
                moveableView.setAlpha(toTransparency);
                moveableView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                //повторный вызов with с тем же URL не создаст новую загрузку, а будет использовать кэшированную копию картинки
                Glide.with(scalableView.getContext())
                        .load(singer.getCover().getBig())
                        .centerCrop()
                        .crossFade()
                        .into(scalableView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(LOG_TAG, "onAnimationCancel");
            }
        });
        animatorSet.start();
    }
}
