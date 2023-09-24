package crm.tranquil_sales_steer.recordings.activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.github.axet.androidlibrary.activities.AppCompatSettingsThemeActivity;
import com.github.axet.androidlibrary.preferences.NameFormatPreferenceCompat;
import com.github.axet.androidlibrary.preferences.OptimizationPreferenceCompat;
import com.github.axet.androidlibrary.preferences.SeekBarPreference;
import com.github.axet.androidlibrary.preferences.StoragePathPreferenceCompat;
import com.github.axet.audiolibrary.widgets.RecordingVolumePreference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.recordings.app.CallApplication;
import crm.tranquil_sales_steer.recordings.app.Storage;
import crm.tranquil_sales_steer.recordings.services.RecordingService;
import crm.tranquil_sales_steer.recordings.widgets.EncodingsPreferenceCompat;
import crm.tranquil_sales_steer.recordings.widgets.MixerPathsPreferenceCompat;
import crm.tranquil_sales_steer.recordings.widgets.RecordingSourcePreferenceCompat;


public class SettingsActivity extends AppCompatSettingsThemeActivity implements PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback {

    public static final int RESULT_FILE = 1;

    public static final String[] CONTACTS = new String[]{
            Manifest.permission.READ_CONTACTS,
    };

    GeneralPreferenceFragment f;
    Storage storage;

    @SuppressWarnings("unchecked")
    public static <T> T[] removeElement(Class<T> c, T[] aa, int i) {
        List<T> ll = Arrays.asList(aa);
        ll = new ArrayList<>(ll);
        ll.remove(i);
        return ll.toArray((T[]) Array.newInstance(c, ll.size()));
    }

    @Override
    public int getAppTheme() {
        return CallApplication.getTheme(this, R.style.RecThemeLight, R.style.RecThemeDark);
    }

    @Override
    public String getAppThemeKey() {
        return CallApplication.PREFERENCE_THEME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getAppTheme());
        super.onCreate(savedInstanceState);
        storage = new Storage(this);

        setupActionBar();

        f = new GeneralPreferenceFragment();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, f).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    @TargetApi(11)
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName) || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);
        if (key.equals(CallApplication.PREFERENCE_STORAGE)) {
            storage.migrateLocalStorageDialog(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        MainActivity.startActivity(this);
    }

    @Override
    public boolean onPreferenceDisplayDialog(PreferenceFragmentCompat caller, Preference pref) {
        if (pref instanceof NameFormatPreferenceCompat) {
            NameFormatPreferenceCompat.show(caller, pref.getKey());
            return true;
        }
        if (pref instanceof SeekBarPreference) {
            RecordingVolumePreference.show(caller, pref.getKey());
            return true;
        }
        return false;
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    public static class GeneralPreferenceFragment extends PreferenceFragmentCompat {
        public GeneralPreferenceFragment() {
        }

        void initPrefs(PreferenceManager manager) {
            final Context context = manager.getContext();

            ListPreference format = (ListPreference) manager.findPreference(CallApplication.PREFERENCE_FORMAT);
            if (!Storage.permitted(context, CONTACTS)) {
                CharSequence[] ee = format.getEntries();
                CharSequence[] vv = format.getEntryValues();
            }
            bindPreferenceSummaryToValue(format);

            OptimizationPreferenceCompat optimization = (OptimizationPreferenceCompat) manager.findPreference(CallApplication.PREFERENCE_OPTIMIZATION);
            optimization.enable(RecordingService.class);

            bindPreferenceSummaryToValue(manager.findPreference(CallApplication.PREFERENCE_THEME));
            bindPreferenceSummaryToValue(manager.findPreference(CallApplication.PREFERENCE_CHANNELS));
            bindPreferenceSummaryToValue(manager.findPreference(CallApplication.PREFERENCE_DELETE));

            final EncodingsPreferenceCompat enc = (EncodingsPreferenceCompat) manager.findPreference(CallApplication.PREFERENCE_ENCODING);
            enc.onResume();

            StoragePathPreferenceCompat s = (StoragePathPreferenceCompat) manager.findPreference(CallApplication.PREFERENCE_STORAGE);
            s.setStorage(new Storage(getContext()));
            s.setPermissionsDialog(this, Storage.PERMISSIONS_RW, RESULT_FILE);
            if (Build.VERSION.SDK_INT >= 21)
                s.setStorageAccessFramework(this, RESULT_FILE);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setHasOptionsMenu(true);
            addPreferencesFromResource(R.xml.call_pref_general);
            initPrefs(getPreferenceManager());
        }

        @Override
        public void onResume() {
            super.onResume();
            OptimizationPreferenceCompat optimization = (OptimizationPreferenceCompat) findPreference(CallApplication.PREFERENCE_OPTIMIZATION);
            optimization.onResume();
            MixerPathsPreferenceCompat mix = (MixerPathsPreferenceCompat) findPreference(CallApplication.PREFERENCE_MIXERPATHS);
            mix.onResume();
            RecordingSourcePreferenceCompat source = (RecordingSourcePreferenceCompat) findPreference(CallApplication.PREFERENCE_SOURCE);
            source.onResume();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            StoragePathPreferenceCompat s = (StoragePathPreferenceCompat) findPreference(CallApplication.PREFERENCE_STORAGE);
            switch (requestCode) {
                case RESULT_FILE:
                    s.onRequestPermissionsResult(permissions, grantResults);
                    break;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            StoragePathPreferenceCompat s = (StoragePathPreferenceCompat) findPreference(CallApplication.PREFERENCE_STORAGE);
            switch (requestCode) {
                case RESULT_FILE:
                    s.onActivityResult(resultCode, data);
                    break;
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
