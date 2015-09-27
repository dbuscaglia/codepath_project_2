package danbuscaglia.googleimagesearch.clients;

import android.util.Log;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import danbuscaglia.googleimagesearch.models.Settings;

/**
 * Created by danbuscaglia on 9/26/15.
 */
public class SettingsStore {

    private static final SettingsStore _instance = new SettingsStore();

    public static SettingsStore db() {
        return _instance;
    }

    public static Settings getSettings() {
        List settings = new Select().from(Settings.class)
                .orderBy("ID ASC").limit(1).execute();
        if (settings.size() == 0) {
            return new Settings();
        } else {
            Log.i("DEBUG", "TEST");
            return (Settings) settings.get(0);
        }
    }
}
