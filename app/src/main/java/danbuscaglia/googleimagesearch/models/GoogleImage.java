package danbuscaglia.googleimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 9/26/15.
 */
public class GoogleImage implements Parcelable {
    private String name;
    private String imageUrl;
    private String thumbnailUrl;

    public GoogleImage(JSONObject picture) throws JSONException {
        this.name = picture.getString("title");
        this.imageUrl = picture.getString("url");
        this.thumbnailUrl = picture.getString("tbUrl");
    }

    public static ArrayList<GoogleImage> parseResultsPage (JSONArray page) throws JSONException {
        ArrayList<GoogleImage> results = new ArrayList<GoogleImage>();
        for (int i = 0; i < page.length(); i++) {
            GoogleImage item = new GoogleImage(page.getJSONObject(i));
            results.add(item);
        }
        return results;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeString(this.thumbnailUrl);
    }

    private GoogleImage(Parcel in) {
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<GoogleImage> CREATOR = new Parcelable.Creator<GoogleImage>() {
        public GoogleImage createFromParcel(Parcel source) {
            return new GoogleImage(source);
        }

        public GoogleImage[] newArray(int size) {
            return new GoogleImage[size];
        }
    };
}
