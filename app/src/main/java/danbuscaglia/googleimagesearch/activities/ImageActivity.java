package danbuscaglia.googleimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import danbuscaglia.googleimagesearch.R;
import danbuscaglia.googleimagesearch.components.TouchImageView;
import danbuscaglia.googleimagesearch.models.GoogleImage;

public class ImageActivity extends ActionBarActivity {

    private TouchImageView detailImage;
    private android.support.v7.widget.ShareActionProvider miShareAction;

    public static void fixMediaDir() {
        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard == null) { return; }
        File dcim = new File(sdcard, "DCIM");
        if (dcim == null) { return; }
        File camera = new File(dcim, "Camera");
        if (camera.exists()) { return; }
        camera.mkdir();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fixMediaDir();
        setContentView(R.layout.activity_image);
        detailImage = (TouchImageView) findViewById(R.id.ivImageDetail);

        Bundle extras = getIntent().getExtras();
        // Extract book object from intent extras
        GoogleImage gi = (GoogleImage) extras.get("googleImage");
        // Use book object to populate data into views
        Picasso.with(this).load(gi.getImageUrl()).into(detailImage, new Callback() {
            @Override
            public void onSuccess() {
                setupShareIntent();
            }

            @Override
            public void onError() {
                Toast.makeText(ImageActivity.this, "The Image Failed to Load", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        Uri bmpUri = getLocalBitmapUri(detailImage); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        try {
            miShareAction.setShareIntent(shareIntent);
        }
        catch (Exception e) {
           Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
        }
    }
}
