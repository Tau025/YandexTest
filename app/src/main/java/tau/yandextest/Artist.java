package tau.yandextest;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by TAU on 21.04.2016.
 */
public class Artist {
    private static final String LOG_TAG = "Artist";

    private int id;
    private String name;
    private String[] genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private Cover cover;

    public static List<Artist> artists = new ArrayList<>();

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGenres(String[] genres) {
        this.genres = genres;
    }
    public void setTracks(int tracks) {
        this.tracks = tracks;
    }
    public void setAlbums(int albums) {
        this.albums = albums;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCover(Cover cover) {
        this.cover = cover;
    }


    //getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String[] getGenres() {
        return genres;
    }
    public int getTracks() {
        return tracks;
    }
    public int getAlbums() {
        return albums;
    }
    public String getLink() {
        return link;
    }
    public String getDescription() {
        return description;
    }
    public Cover getCover() {
        return cover;
    }


    public static Artist getArtistById(int artistId){
        for (Artist artist : artists) {
            if (artistId == artist.id){
                return artist;
            }
        }
        return null;
    }

    public static void printArtistsList() {
        for (Artist artist : artists) {
            Log.d(LOG_TAG, "artist: " + artist.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id: " + id + " ; ");
        builder.append("name: " + name + " ; ");

        StringBuilder genresSB = new StringBuilder();
        String prefix = "";
        for (String genre : genres) {
            genresSB.append(prefix);
            prefix = ", ";
            genresSB.append(genre);
        }

        builder.append("genres: " + genresSB.toString() + " ; ");
        builder.append("tracks: " + tracks + " ; ");
        builder.append("albums: " + albums + " ; ");
        builder.append("link: " + link + " ; ");
        builder.append("description: " + description + " ; ");
        builder.append("cover.small: " + cover.getSmall() + " ; ");
        builder.append("cover.big: " + cover.getBig());

        return builder.toString();
    }

    public class Cover {
        private String small;
        private String big;
        private boolean bigCoverDownloaded;

        //setters
        public void setSmall(String small) {
            this.small = small;
        }
        public void setBig(String big) {
            this.big = big;
        }
        public void setBigCoverDownloaded(boolean wasDownloaded) {
            this.bigCoverDownloaded = wasDownloaded;
        }

        //getters
        public String getSmall() {
            return small;
        }
        public String getBig() {
            return big;
        }
        public boolean isBigCoverDownloaded() {
            return bigCoverDownloaded;
        }
    }
}
