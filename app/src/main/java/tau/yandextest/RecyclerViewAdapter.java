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
    private List<Artist> artists;
    private Listener listener;

    public RecyclerViewAdapter(List<Artist> artists) {
        this.artists = artists;
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
        Artist artist = artists.get(position);

        ImageView iv_coverSmall = (ImageView) cardView.findViewById(R.id.cover_small);
        Glide.with(cardView.getContext())
                .load(artists.get(position).getCover().getSmall())
                .centerCrop()
                .crossFade()
                .into(iv_coverSmall);

        TextView tv_artistName = (TextView) cardView.findViewById(R.id.artist_name);
        tv_artistName.setText(artist.getName());

        TextView tv_genres = (TextView) cardView.findViewById(R.id.genres);
        StringBuilder genres = new StringBuilder();
        String prefix = "";
        for (String genre : artist.getGenres()) {
            genres.append(prefix);
            prefix = ", ";
            genres.append(genre);
        }
        tv_genres.setText(genres);

        TextView tv_albumsAndTracksQty = (TextView) cardView.findViewById(R.id.albums_and_tracks_qty);
        String albums = res.getQuantityString(R.plurals.count_of_albums, artist.getAlbums(), artist.getAlbums());
        String tracks = res.getQuantityString(R.plurals.count_of_tracks, artist.getTracks(), artist.getTracks());
        tv_albumsAndTracksQty.setText(String.format(locale, "%s, %s", albums, tracks));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(artists.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return artists.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public interface Listener {
        void onClick(Artist selectedArtist);
    }
}
