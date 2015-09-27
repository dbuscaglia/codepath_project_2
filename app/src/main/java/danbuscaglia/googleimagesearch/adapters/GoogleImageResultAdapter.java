package danbuscaglia.googleimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import danbuscaglia.googleimagesearch.R;
import danbuscaglia.googleimagesearch.models.GoogleImage;

/**
 * Created by danbuscaglia on 9/26/15.
 */
public class GoogleImageResultAdapter extends ArrayAdapter<GoogleImage> {

    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private static class ViewHolder {
        View grid;
        StaggeredGridView searchGrid;

        View header;
        TextView headerText;
        DynamicHeightImageView picture;
        TextView caption;
        View footer;
        TextView footerText;
    }

    public GoogleImageResultAdapter(Context context, ArrayList<GoogleImage> resultPage) {
        super(context, R.layout.layout_image_result, resultPage);
        this.mRandom = new Random();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GoogleImage so = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_image_result, parent, false);
            viewHolder.grid = inflater.inflate(R.layout.activity_search, null);
            viewHolder.searchGrid = (StaggeredGridView) viewHolder.grid.findViewById(R.id.gvImageSearchResults);
            //viewHolder.caption = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.picture = (DynamicHeightImageView) convertView.findViewById(R.id.ivPicture);
            convertView.setTag(viewHolder);
            viewHolder.header = inflater.inflate(R.layout.list_item_header_footer, null);
            viewHolder.footer = inflater.inflate(R.layout.list_item_header_footer, null);
            viewHolder.headerText = (TextView) viewHolder.header.findViewById(R.id.txt_title);
            viewHolder.footerText =  (TextView) viewHolder.footer.findViewById(R.id.txt_title);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.headerText.setText(Html.fromHtml(so.getName()));
        viewHolder.footerText.setText(Html.fromHtml(so.getName()));
        viewHolder.footerText.setText("THE FOOTER!");
        viewHolder.searchGrid.addHeaderView(viewHolder.header);
        viewHolder.searchGrid.addFooterView(viewHolder.footer);
        //viewHolder.searchGrid.addFooterView(footer);
        //viewHolder.caption.setText(Html.fromHtml(so.getName()));
        // Lookup view for data population
        Picasso.with(getContext()).load(so.getThumbnailUrl())
                .into(viewHolder.picture);
        double positionHeight = getPositionRatio(position);
        viewHolder.picture.setHeightRatio(positionHeight);

        return convertView;
    }
    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }
    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }
}
