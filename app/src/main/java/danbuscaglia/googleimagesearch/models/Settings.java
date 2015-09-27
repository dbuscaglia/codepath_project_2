package danbuscaglia.googleimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;

@Table(name = "tbl_settings")
public class Settings extends Model implements Serializable {

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "filetype")
    private String filetype;

    @Column(name = "site")
    private String site;

    public Settings() {
        super();
    }

    public String getColor() {
        return GoogleImageSearch.paramParse(color);
    }

    public String getSize() {
        return GoogleImageSearch.paramParse(size);
    }

    public String getFiletype() {
        return GoogleImageSearch.paramParse(filetype);
    }

    public String getSite() {
        if (this.site == null) {
            return "";
        } else {
            return this.site;
        }
    }

    public void setColor(String color) {
        this.color = GoogleImageSearch.paramParse(color);
    }

    public void setSize(String size) {
        this.size = GoogleImageSearch.paramParse(size);
    }

    public void setFiletype(String filetype) {
        this.filetype = GoogleImageSearch.paramParse(filetype);
    }

    public void setSite(String site) {
        if (site == null) {
            this.site = "";
        } else {
            this.site = site;
        }
    }

    public Settings(GoogleImageSearch query){
        super();
        this.color = query.getColor();
        this.size = query.getSize();
        this.filetype = query.getFileType();
        this.site = query.getSite();
    }

}