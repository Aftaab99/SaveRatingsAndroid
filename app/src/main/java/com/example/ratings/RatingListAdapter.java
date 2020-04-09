package com.example.ratings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ratings.storeratings.Rating;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RatingListAdapter extends RecyclerView.Adapter<RatingListAdapter.RatingViewHolder> {
    private List<Rating> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView ratingDateTimeText;
        public LinearLayout ratingBarRoot;
        public RatingViewHolder(View v) {
            super(v);
            ratingDateTimeText = v.findViewById(R.id.ratingDateTimeText);
            ratingBarRoot=v.findViewById(R.id.ratingBarRoot);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RatingListAdapter(List<Rating> myDataset, Context ctx) {
        mDataset = myDataset;
        mContext=ctx;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RatingListAdapter.RatingViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prev_rating_list_item, parent, false);
        return new RatingViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RatingViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String dateTimeString = getFormattedDate(mDataset.get(position).timestamp);
        holder.ratingDateTimeText.setText(dateTimeString);
        RatingBar ratingbar = new RatingBar(mContext, null, R.attr.ratingBarStyleSmall);

        ratingbar.setNumStars(mDataset.get(position).totalStars);
        ratingbar.setStepSize(1);
        ratingbar.setIsIndicator(true);
        ratingbar.setRating((float) mDataset.get(position).stars);
        holder.ratingBarRoot.removeAllViews();
        holder.ratingBarRoot.addView(ratingbar);


    }
    private String getFormattedDate(long ts){
        Date date = new java.util.Date(ts*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}