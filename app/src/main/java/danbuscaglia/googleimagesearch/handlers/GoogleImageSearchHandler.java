package danbuscaglia.googleimagesearch.handlers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import danbuscaglia.googleimagesearch.interfaces.GoogleImageSearchReceiver;
import danbuscaglia.googleimagesearch.models.GoogleApiResponseFailure;
import danbuscaglia.googleimagesearch.models.GoogleImage;
import danbuscaglia.googleimagesearch.models.GoogleImageSearch;

/**
 * Created by danbuscaglia on 9/26/15.
 */
public class GoogleImageSearchHandler extends JsonHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONArray results;

        try {
            int outcome = response.getInt("responseStatus");
            if (outcome == 200) {
                results = response.getJSONObject("responseData").getJSONArray("results");
                ArrayList<GoogleImage> page = GoogleImage.parseResultsPage(results);
                this._receiver.handleDataCallback(page);
            } else {
                // We are at the end of the page, or some other error.
                String message = response.getString("responseDetails");
                GoogleApiResponseFailure failure = new GoogleApiResponseFailure(outcome, message);
                this._receiver.handleParsedErrorCode(failure);
            }
        } catch (JSONException e) {

            this._receiver.handleDataError(e);
        } catch (Exception e) {
            this._receiver.handleDataError(e);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                          JSONObject errorResponse) {

        this._receiver.handleDataError(throwable);
    }

    private AsyncHttpClient _http;
    private GoogleImageSearchReceiver _receiver;

    public GoogleImageSearchHandler(GoogleImageSearchReceiver caller) {
        this._http = new AsyncHttpClient();
        this._receiver = caller;
    }
    public void executeQuery(GoogleImageSearch query) {
        if (query.getQuery() != "") {
            this._http.get(query.searchUrl(), null, this);
        }
    }

}
