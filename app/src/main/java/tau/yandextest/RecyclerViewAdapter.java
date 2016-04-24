package tau.yandextest;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Locale;
/**
 * Created by TAU on 21.04.2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Singer> singers;
    private Listener listener;

    public RecyclerViewAdapter(List<Singer> singers) {
        this.singers = singers;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Создание нового представления
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        //Заполнение заданного представления данными
        CardView cardView = holder.cardView;
        Resources res = cardView.getContext().getResources();
        Locale locale = res.getConfiguration().locale;
        Singer singer = singers.get(position);

        ImageView iv_coverSmall = (ImageView) cardView.findViewById(R.id.cover_small);
        Glide.with(cardView.getContext())
                .load(singers.get(position).getCover().getSmall())
                .centerCrop()
                .crossFade()
                .into(iv_coverSmall);

        TextView tv_singerName = (TextView) cardView.findViewById(R.id.singer_name);
        tv_singerName.setText(singer.getName());

        TextView tv_genres = (TextView) cardView.findViewById(R.id.genres);
        StringBuilder genres = new StringBuilder();
        String prefix = "";
        for (String genre : singer.getGenres()) {
            genres.append(prefix);
            prefix = ", ";
            genres.append(genre);
        }
        tv_genres.setText(genres);

        TextView tv_albumsAndTracksQty = (TextView) cardView.findViewById(R.id.albums_and_tracks_qty);
        String albums = res.getQuantityString(R.plurals.count_of_albums, singer.getAlbums(), singer.getAlbums());
        String tracks = res.getQuantityString(R.plurals.count_of_tracks, singer.getTracks(), singer.getTracks());
        tv_albumsAndTracksQty.setText(String.format(locale, "%s, %s", albums, tracks));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(singers.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return singers.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public interface Listener {
        void onClick(Singer selectedSinger);
    }
}
