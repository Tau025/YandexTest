package tau.yandextest;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by TAU on 21.04.2016.
 */
public class Artist implements Comparable{
    private static final String LOG_TAG = "Artist";

    private int id;
    private String name;
    private String[] genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private Cover cover;

    public static List<Artist> artistList = new ArrayList<>();

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
        for (Artist artist : artistList) {
            if (artistId == artist.id){
                return artist;
            }
        }
        return null;
    }

    @Override
    public int compareTo(@NonNull Object another) {
        return this.name.compareTo(((Artist) another).name);
    }

    @Override
    public String toString() {
        StringBuilder genresSB = new StringBuilder();
        String prefix = "";
        for (String genre : genres) {
            genresSB.append(prefix);
            prefix = ", ";
            genresSB.append(genre);
        }

        return String.format("id: %s; name: %s; genres: %s; tracks: %s; albums: %s; link: %s; " +
                "description: %s; cover.small: %s; cover.big: %s;",
                id, name, genresSB.toString(), tracks, albums, link, description, cover.getSmall(), cover.getBig());
    }
}
