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
                "Simon Green (born 30 March 1976), known by his stage name Bonobo, is a British musician, producer and DJ based in Los Angeles. " +
                        "Green has recorded and performed solo DJ sets under the name Barakas, and together with Robert Luis " +
                        "from Tru Thoughts as Nirobi and Barakas."));
        singers.add(new Singer(2, "Tycho", new String[]{"dance", "electronics"}, 6, 70, R.drawable.tycho,
                "Tycho (pronounced like Tie-ko, with emphasis on the first syllable) is an American ambient music project " +
                        "led by Scott Hansen (born 1976 or 1977) as primary composer, songwriter and producer. " +
                        "Hailing from San Francisco, California, he is known as ISO50 for his photographic and design works."));
        singers.add(new Singer(3, "Odesza", new String[]{"dance", "electronics"}, 5, 24, R.drawable.odesza,
                "Odesza (stylized as ODESZA) are an American electronic music duo from Seattle consisting of Harrison Mills " +
                        "(CatacombKid) and Clayton Knight (BeachesBeaches). The group was formed in 2012 shortly before " +
                        "Mills and Knight graduated from Western Washington University."));
        singers.add(new Singer(4, "Ásgeir", new String[]{"dance", "electronics"}, 5, 44, R.drawable.asgeir,
                "Ásgeir's debut album is Dýrð í dauðaþögn released in 2012, the lead single from which, \"Sumargestur\", " +
                        "made it to number two on the Tónlist,[3] an unofficial but widely quoted Icelandic Singles Chart, " +
                        "followed up with the single \"Leyndarmál\"[4] (six weeks at number 1 on Tónlist)[3] and the title track " +
                        "\"Dýrð í dauðaþögn\" from the album (three weeks at #1 on Tónlist)."));
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
