package danbuscaglia.googleimagesearch.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.Map;

import danbuscaglia.googleimagesearch.Listeners.EndlessScrollListener;
import danbuscaglia.googleimagesearch.R;
import danbuscaglia.googleimagesearch.adapters.GoogleImageResultAdapter;
import danbuscaglia.googleimagesearch.clients.SettingsStore;
import danbuscaglia.googleimagesearch.handlers.GoogleImageSearchHandler;
import danbuscaglia.googleimagesearch.helpers.ConnectionHelper;
import danbuscaglia.googleimagesearch.interfaces.GoogleImageSearchReceiver;
import danbuscaglia.googleimagesearch.models.GoogleApiResponseFailure;
import danbuscaglia.googleimagesearch.models.GoogleImage;
import danbuscaglia.googleimagesearch.models.GoogleImageSearch;
import danbuscaglia.googleimagesearch.models.Settings;

public class SearchActivity extends NetworkDetectingActivity implements GoogleImageSearchReceiver {
    /**
     * TODO: make all strings in strings.xml
     */
    private boolean _isSearching;
    private Settings _settings;
    private GoogleImageSearchHandler _searchClient;
    private ArrayList<GoogleImage> _imagePage;
    private GoogleImageResultAdapter _imageAdapter;
    private StaggeredGridView _gvImages;
    private String _activeQuery;
    private int _highestLoadedPage;

    @Override
    protected void onPause() {
        _settings.save();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.initSearch();
    }

    private void initSearch() {
        _highestLoadedPage = 0;
        _activeQuery = "";
        _isSearching = false;
        _gvImages = (StaggeredGridView) findViewById(R.id.gvImageSearchResults);
        _settings = SettingsStore.getSettings();
        _searchClient = new GoogleImageSearchHandler(this);
        _imagePage = new ArrayList<GoogleImage>();
        _imageAdapter = new GoogleImageResultAdapter(this, _imagePage);
        _gvImages.setAdapter(_imageAdapter);
        _gvImages.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (page < GoogleImageSearch.MAX_NUMBER_PAGES) {
                    _highestLoadedPage = page;
                    return queryImages(_activeQuery, page);
                } else {
                    Toast.makeText(_gvImages.getContext(), "End of results.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        _gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoogleImage gImage = _imageAdapter.getItem(position);
                Intent intent = new Intent(SearchActivity.this, ImageActivity.class);
                intent.putExtra("googleImage", gImage);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _imagePage.clear();
                _highestLoadedPage = 0;
                _activeQuery = query;
                queryImages(query, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private boolean queryImages(String queryString, int page) {
        if (!ConnectionHelper.isConnected(this)) {
            Toast.makeText(this, "Cannot search while offline.", Toast.LENGTH_SHORT);
            return false;
        }

        if (_isSearching == false && queryString != "") {
            _highestLoadedPage = page;
            _isSearching = true;
            GoogleImageSearch query = new GoogleImageSearch.with(this)
                                            .query(queryString)
                                            .color(_settings.getColor())
                                            .fileType(_settings.getFiletype())
                                            .site(_settings.getSite())
                                            .filterType("")
                                            .pageSize(GoogleImageSearch.DEFAULT_PAGE_SIZE)
                                            .size(_settings.getSize())
                                            .page(page)
                                            .build();

            this._searchClient.executeQuery(query);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleDataCallback(ArrayList<GoogleImage> resultPage) {
        // ensure we have clean data for the grid adapter
        this.pushDataToImageGridView(resultPage);
        _isSearching = false;
    }

    public void pushDataToImageGridView(ArrayList<GoogleImage> resultPage) {
        this._imagePage.addAll(resultPage);
        this._imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleDataError(Throwable e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
        _isSearching = false;
    }


    public void editFilterSettings(MenuItem mi) {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Search Options")
                .customView(R.layout.modal_settings, wrapInScrollView)
                .positiveText("ok")
                .negativeText("cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        // save settings
                        Spinner size = (Spinner) dialog.getCustomView().findViewById(R.id.spSize);
                        _settings.setSize(size.getSelectedItem().toString());
                        Spinner color = (Spinner) dialog.getCustomView().findViewById(R.id.spColor);
                        _settings.setColor(color.getSelectedItem().toString());
                        Spinner file_type = (Spinner) dialog.getCustomView().findViewById(R.id.spType);
                        _settings.setFiletype(file_type.getSelectedItem().toString());
                        EditText site = (EditText) dialog.getCustomView().findViewById(R.id.etSite);
                        _settings.setSite(site.getText().toString());
                        _settings.save();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        // no op
                    }
                }).build();
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, GoogleImageSearch.GOOGLE_IMAGE_SEARCH_SIZE_OPTONS);
        Spinner size = (Spinner) dialog.getCustomView().findViewById(R.id.spSize);
        size.setAdapter(sizeAdapter);
        int selectionPosition = sizeAdapter.getPosition(_settings.getSize());
        size.setSelection(selectionPosition);

        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, GoogleImageSearch.GOOGLE_IMAGE_SEARCH_COLOR_OPTIONS);
        Spinner color = (Spinner) dialog.getCustomView().findViewById(R.id.spColor);
        color.setAdapter(colorAdapter);
        selectionPosition = colorAdapter.getPosition(_settings.getColor());
        color.setSelection(selectionPosition);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, GoogleImageSearch.GOOGLE_IMAGE_SEARCH_TYPE_OPTIONS);
        Spinner imageType = (Spinner) dialog.getCustomView().findViewById(R.id.spType);
        imageType.setAdapter(typeAdapter);
        selectionPosition = typeAdapter.getPosition(_settings.getFiletype());
        imageType.setSelection(selectionPosition);

        EditText site = (EditText) dialog.getCustomView().findViewById(R.id.etSite);
        site.setText(_settings.getSite());
        dialog.show();
    }
    @Override
    public void handleParsedErrorCode(GoogleApiResponseFailure failure) {
        if (failure.isOutOfPageError() != true) {

            Toast.makeText(this, "Please email danbuscaglia@gmail.com, he sucks.  Error: "
                           + failure.toString() , Toast.LENGTH_LONG).show();
        } else {
            // We are out of results
            Toast.makeText(this, "End of results.", Toast.LENGTH_SHORT).show();
        }
        _isSearching = false;
        this._imageAdapter.notifyDataSetChanged();
    }
}
