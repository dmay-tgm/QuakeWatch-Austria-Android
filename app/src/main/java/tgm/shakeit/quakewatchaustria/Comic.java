package tgm.shakeit.quakewatchaustria;

/**
 * Model class for Comic rows. Holds a comic, the title and the details.
 *
 * @author Daniel May
 * @version 2016-06-01
 */
class Comic {
    private static final String TAG = "Comic.java";
    private final String title;
    private final String detail;
    private final int icon;

    /**
     * Instantiates a new comic row.
     *
     * @param icon   the resource id of the comic
     * @param title  the comic title
     * @param detail the details of the comic
     */
    public Comic(int icon, String title, String detail) {
        this.icon = icon;
        this.title = title;
        this.detail = detail;
    }

    /**
     * Gets the title from the comic row
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the detail from the comic row
     *
     * @return the details
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Gets the resource id from the comic
     *
     * @return the resource id
     */
    public int getIcon() {
        return icon;
    }
}