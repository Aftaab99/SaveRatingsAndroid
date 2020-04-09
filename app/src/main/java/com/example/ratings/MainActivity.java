package com.example.ratings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ratings.storeratings.RatingDBHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Integer minRating = 0, maxRating = 9;
    RatingDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ratingBtn = findViewById(R.id.set_rating_button);
        final SeekBar minRatingSeekBar = findViewById(R.id.minRatingSeekBar);
        final SeekBar maxRatingSeekBar = findViewById(R.id.maxRatingSeekBar);
        final TextView minRatingInc = findViewById(R.id.minRatingIndicatorText);
        final TextView maxRatingInc = findViewById(R.id.maxRatingIndicatorText);
        Button seePrevReviewsBtn = findViewById(R.id.seeReviewsBtn);
        seePrevReviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoShowRating = new Intent(MainActivity.this, ShowRatingActivity.class);
                startActivity(gotoShowRating);
            }
        });

        minRatingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minRating = progress;
                minRatingInc.setText(String.format(Locale.getDefault(), "%d", minRating));
                if (minRating >= maxRating) {
                    if (minRating == 9) {
                        minRating--;
                        maxRating = minRating + 1;
                    } else {
                        maxRating = minRating + 1;
                    }
                    setSeekBar(minRatingSeekBar, minRating);
                    setSeekBar(maxRatingSeekBar, maxRating);
                } else
                    setSeekBar(minRatingSeekBar, minRating);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        maxRatingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxRating = progress;
                maxRatingInc.setText(String.format(Locale.getDefault(), "%d", maxRating));
                if (maxRating <= minRating) {
                    if (maxRating == 0) {
                        maxRating = 1;
                        minRating = 0;
                    } else {
                        minRating = maxRating - 1;
                    }
                    setSeekBar(minRatingSeekBar, minRating);
                    setSeekBar(maxRatingSeekBar, maxRating);
                } else
                    setSeekBar(maxRatingSeekBar, maxRating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rating_dialog);
        dialog.setCanceledOnTouchOutside(true);

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ratingBarRoot = dialog.findViewById(R.id.ratingBarRoot);
                Button ratingSubmitBtn = dialog.findViewById(R.id.ratingDialogSubmitBtn);
                final RatingBar ratingBar;
                if (maxRating <= 5) {
                    ratingBar = new RatingBar(MainActivity.this, null, R.attr.ratingBarStyle);
                } else {
                    ratingBar = new RatingBar(MainActivity.this, null, R.attr.ratingBarStyleIndicator);
                }
                ratingBar.setNumStars(maxRating);
                ratingBar.setStepSize(1);
                ratingBar.setIsIndicator(false);
                ratingBar.setRating((float) minRating);
                ratingBarRoot.removeAllViews();
                ratingBarRoot.addView(ratingBar);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        if (rating < minRating)
                            ratingBar.setRating(minRating);
                    }
                });
                ratingSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long currentTimestamp = System.currentTimeMillis()/1000;
                        int stars = (int)ratingBar.getRating();
                        int totalStars = ratingBar.getNumStars();
                        try {
                            dbHelper.addToDB(currentTimestamp, stars, totalStars);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this, "Rating recorded!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });
        dbHelper = new RatingDBHelper(this);

    }

    private void setSeekBar(SeekBar sb, int val) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sb.setProgress(val, true);
        } else {
            sb.setProgress(val);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
