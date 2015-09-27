package danbuscaglia.googleimagesearch.interfaces;

import org.json.JSONException;

import java.util.ArrayList;

import danbuscaglia.googleimagesearch.models.GoogleApiResponseFailure;
import danbuscaglia.googleimagesearch.models.GoogleImage;

/**
 * Created by danbuscaglia on 9/26/15.
 */
public interface GoogleImageSearchReceiver {
    void handleDataCallback(ArrayList<GoogleImage> resultPage);
    void handleDataError(Throwable e);
    void handleParsedErrorCode(GoogleApiResponseFailure failure);
}
