package tau.yandextest;

/**
 * Created by TAU on 20.05.2016.
 */
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
