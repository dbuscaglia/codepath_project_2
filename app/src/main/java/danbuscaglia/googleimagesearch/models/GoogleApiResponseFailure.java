package danbuscaglia.googleimagesearch.models;

/**
 * Created by danbuscaglia on 9/27/15.
 */
public class GoogleApiResponseFailure {

    private int _responseCode;
    private String _responseMessage;
    private boolean _errorKnown;

    public final String OUT_OF_RANGE = "out of range start";

    public int resposeCode() {
        return _responseCode;
    }

    public String responseMessage() {
        return _responseMessage;
    }

    public GoogleApiResponseFailure(int responseCode, String responseMessage) {
        if (responseCode == 400 && responseMessage.equals(this.OUT_OF_RANGE)) {
            this._errorKnown = true;
        } else {
            this._errorKnown = false;
        }
        this._responseCode = responseCode;
        this._responseMessage = responseMessage;
    }

    public boolean isOutOfPageError() {
        return this._errorKnown;
    }

    @Override
    public String toString() {
        return "Api error " + String.valueOf(this._responseCode) + " with message " + this._responseMessage;
    }
}
