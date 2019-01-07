package net.vokacom.lightvote.mData;

public class Contestants {
    public int id;
    public String title;
    public String short_description;
    public String town;
    public String image;

    public Contestants(int id, String title, String short_description, String town, String image ) {
        this.id = id;
        this.title = title;
        this.short_description = short_description;
        this.town = town;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getshort_description() {
        return short_description;
    }

    public String getTown() {
        return town;
    }

    public String getImage() {
        return image;
    }

}
