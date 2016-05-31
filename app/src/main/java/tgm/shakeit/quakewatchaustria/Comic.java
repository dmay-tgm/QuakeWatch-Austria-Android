package tgm.shakeit.quakewatchaustria;

public class Comic {
    private static final String TAG = "Comic.java";
    private String title;
    private String detail;
    private int icon;

    public Comic() {
        super();
    }

    public Comic(int icon, String title, String detail) {
        super();
        this.icon = icon;
        this.title = title;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getIcon() {
        return icon;
    }
}