package com.example.ratings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ratings.storeratings.Rating;
import com.example.ratings.storeratings.RatingDBHelper;

import java.util.List;

public class ShowRatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rating);
        RecyclerView ratingListView = findViewById(R.id.ratingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ratingListView.setLayoutManager(layoutManager);
        ratingListView.setHasFixedSize(true);

        RatingDBHelper dbHelper = new RatingDBHelper(this);
        List<Rating> ratingItems = dbHelper.getDataFromDB();
        System.out.println("Ratings=" + ratingItems.size());
        RatingListAdapter adapter = new RatingListAdapter(ratingItems, this);
        ratingListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
