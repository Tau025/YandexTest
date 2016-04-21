package tau.yandextest;

import java.util.ArrayList;
/**
 * Created by TAU on 21.04.2016.
 */
public class Singer {
    private int id;
    private String name;
    private String[] genres;
    private int albumsQty;
    private int tracksQty;
    private int imageResourceId;
    private String biography;

    public static ArrayList<Singer> singers = new ArrayList<>();
    static {
        singers.add(new Singer(1, "Bonobo", new String[]{"dance", "electronics"}, 8, 95, R.drawable.bonobo,
                "long biography of Bonobo"));
        singers.add(new Singer(2, "Tycho", new String[]{"dance", "electronics"}, 6, 70, R.drawable.tycho,
                "long biography of Tycho"));
        singers.add(new Singer(3, "Odesza", new String[]{"dance", "electronics"}, 5, 24, R.drawable.odesza,
                "long biography of Odesza"));
        singers.add(new Singer(4, "Ásgeir", new String[]{"dance", "electronics"}, 5, 44, R.drawable.asgeir,
                "long biography of Ásgeir"));
    }

    public Singer(int id, String name, String[] genres, int albumsQty, int tracksQty, int imageResourceId, String biography) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.albumsQty = albumsQty;
        this.tracksQty = tracksQty;
        this.imageResourceId = imageResourceId;
        this.biography = biography;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String[] getGenres() {
        return genres;
    }
    public int getAlbumsQty() {
        return albumsQty;
    }
    public int getTracksQty() {
        return tracksQty;
    }
    public int getImageResourceId() {
        return imageResourceId;
    }
    public String getBiography() {
        return biography;
    }

    public static Singer getSingerById(int singerId){
        for (Singer singer : singers) {
            if (singerId == singer.id){
                return singer;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
