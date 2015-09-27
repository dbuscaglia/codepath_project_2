package danbuscaglia.googleimagesearch.models;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by danbuscaglia on 9/22/15.
 */
public class GoogleImageSearch {
    /**
     * Aimed to use builder design pattern like picasso
     */
    public static final String GI_IMAGE_API_ANY_OPTION = "any";

    public static final String IMAGE_TYPE_FACE = "face";
    public static final String IMAGE_TYPE_PHOTO = "photo";
    public static final String IMAGE_TYPE_CLIPART = "clipart";
    public static final String IMAGE_TYPE_LINEART = "lineart";
    // icon range
    public static final String IMAGE_SIZE_ICON = "icon";
    // medium range
    public static final String IMAGE_SIZE_SMALL = "small";
    public static final String IMAGE_SIZE_MEDIUM = "medium";
    public static final String IMAGE_SIZE_LARGE = "large";
    public static final String IMAGE_SIZE_XLARGE = "xlarge";
    // large range
    public static final String IMAGE_SIZE_XXLARGE = "xxlarge";
    // extra large range
    public static final String IMAGE_SIZE_HUGE = "huge";

    public static final ArrayList<String> GOOGLE_IMAGE_SEARCH_SIZE_OPTONS;
    static{
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(GI_IMAGE_API_ANY_OPTION);
        tmp.add(IMAGE_SIZE_ICON);
        tmp.add(IMAGE_SIZE_SMALL);
        tmp.add(IMAGE_SIZE_MEDIUM);
        tmp.add(IMAGE_SIZE_LARGE);
        tmp.add(IMAGE_SIZE_XLARGE);
        tmp.add(IMAGE_SIZE_XXLARGE);
        tmp.add(IMAGE_SIZE_HUGE);
        GOOGLE_IMAGE_SEARCH_SIZE_OPTONS = tmp;
    }

    public static final ArrayList<String> GOOGLE_IMAGE_SEARCH_TYPE_OPTIONS;
    static{
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(GI_IMAGE_API_ANY_OPTION);
        tmp.add(IMAGE_TYPE_FACE);
        tmp.add(IMAGE_TYPE_PHOTO);
        tmp.add(IMAGE_TYPE_CLIPART);
        tmp.add(IMAGE_TYPE_LINEART);
        GOOGLE_IMAGE_SEARCH_TYPE_OPTIONS = tmp;
    }

    public static final ArrayList<String> GOOGLE_IMAGE_SEARCH_COLOR_OPTIONS;
    static{
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(GI_IMAGE_API_ANY_OPTION);
        tmp.add("black");
        tmp.add("blue");
        tmp.add("brown");
        tmp.add("gray");
        tmp.add("green");
        tmp.add("orange");
        tmp.add("pink");
        tmp.add("purple");
        tmp.add("red");
        tmp.add("teal");
        tmp.add("white");
        tmp.add("yellow");
        GOOGLE_IMAGE_SEARCH_COLOR_OPTIONS = tmp;
    }

    private final Context context;
    private String _query;
    private String _filter_type;
    private String _file_type;
    private String _site;
    private String _color;
    private String _size;
    private int _page;
    private int _page_size;

    public String pageResults() {
        return String.valueOf(_page_size);
    }

    public static final int DEFAULT_PAGE_SIZE = 8;
    public static final int MAX_NUMBER_PAGES = 20000;
    public static final String GOOGLE_IMAGE_API_BASE = "https://ajax.googleapis.com/ajax/services/search/images?";
    public static final String GOOGLE_IMAGE_API_VERSION = "1.0";
    public static final String GOOGLE_IMAGE_API_RESULTS_COUNT_KEY = "rsz";

    public static final String GI_QUERY_KEY = "q";
    public static final String GI_VERSION_KEY = "v";
    public static final String GI_IMAGE_COLOR_FILTER_PARAM = "imgcolor";
    public static final String GI_SIZE_FILTER_PARAM = "imgsz";
    public static final String GI_IMAGE_TYPE_FILTER_PARAM = "imgtype";
    public static final String GI_IMAGE_SITE_LOCATION_FILTER_PARAM = "as_sitesearch";
    public static final String GI_IMAGE_PAGE_START = "start";



    public String getQuery() {
        return _query;
    }

    public static String paramParse(String param) {
        if (param == null) {
            return GI_IMAGE_API_ANY_OPTION;
        }
        if (param.equals("")) {
            return GI_IMAGE_API_ANY_OPTION;
        } else {
            return param;
        }
    }

    public String getFilter() {
        return paramParse(_filter_type);
    }

    public String getFileType() {
        return paramParse(_file_type);
    }

    public String getSize() {
        return paramParse(_size);
    }

    public String getSite() {
        return _site;
    }

    public String getColor() {
        return paramParse(_color);
    }

    public int getPage() { return _page; }


    public static class with {
        private final Context context;
        private String _filter_type;
        private String _file_type;
        private String _query;
        private String _site;
        private String _color;
        private String _size;
        private int _page_size;
        private int _page;

        public with(Context c) {
            this.context = c;
        }

        public with query(String query) {
            _query = query;
            return this;
        }

        public with filterType(String filter) {
            _filter_type = filter;
            return this;
        }

        public with fileType(String file_type) {
            _file_type = file_type;
            return this;
        }

        public with color(String color) {
            _color = color;
            return this;
        }

        public with site(String site) {
            _site = site;
            return this;
        }

        public with size(String size) {
            _size = size;
            return this;
        }

        public with pageSize(int c) {
            _page_size = c;
            return this;
        }

        public with page(int p) {
            _page = p;
            return this;
        }

        public GoogleImageSearch build() {
            return new GoogleImageSearch(this);
        }
    }

    private GoogleImageSearch(with builder) {
        context = builder.context;
        _query = builder._query;
        _filter_type = builder._filter_type;
        _file_type = builder._file_type;
        _site = builder._site;
        _color = builder._color;
        _size = builder._size;
        _page_size = builder._page_size;
        _page = builder._page;
    }
    public String searchUrl() {
        Uri builtUri = Uri.parse(GOOGLE_IMAGE_API_BASE)
                .buildUpon()
                .appendQueryParameter(GI_VERSION_KEY, GOOGLE_IMAGE_API_VERSION)
                .appendQueryParameter(GOOGLE_IMAGE_API_RESULTS_COUNT_KEY, this.pageResults())
                .build();

        Uri.Builder optionalBuilder = builtUri.buildUpon();
        optionalBuilder.appendQueryParameter(GI_QUERY_KEY, this.getQuery());
        if (this.getColor() != null || !this.getColor().equals(GI_IMAGE_API_ANY_OPTION)) {
            optionalBuilder.appendQueryParameter(GI_IMAGE_COLOR_FILTER_PARAM, this.getColor());
        }
        if (this.getSite() != null || !this.getSite().equals(GI_IMAGE_API_ANY_OPTION)) {
            optionalBuilder.appendQueryParameter(GI_IMAGE_SITE_LOCATION_FILTER_PARAM, this.getSite());
        }
        if (this.getSize() != null || !this.getSize().equals(GI_IMAGE_API_ANY_OPTION)) {
            optionalBuilder.appendQueryParameter(GI_SIZE_FILTER_PARAM, this.getSize());
        }
        if (this.getFileType() != null || !this.getFileType().equals(GI_IMAGE_API_ANY_OPTION)) {
            optionalBuilder.appendQueryParameter(GI_IMAGE_TYPE_FILTER_PARAM, this.getFileType());
        }
        if (this.getPage() > 0) {
            int start = this.getPage() * this._page_size;
            optionalBuilder.appendQueryParameter(GI_IMAGE_PAGE_START, String.valueOf(start));
        }
        builtUri = optionalBuilder.build();
        return builtUri.toString();
    }
}
