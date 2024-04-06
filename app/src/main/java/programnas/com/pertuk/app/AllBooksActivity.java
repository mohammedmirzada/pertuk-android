package programnas.com.pertuk.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AllBooksActivity extends AppCompatActivity {
    Database db;
    GridLayout gridview;
    LinearLayout linearPagination;
    int category_id;
    HorizontalScrollView horizontalPagination;

    private InterstitialAd mInterstitialAd;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        MobileAds.initialize(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-9877420063334339/3263433219", adRequest,
                new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        Realm realm = Realm.getInstance(config);
        db  = new Database(realm);

        Intent intent = getIntent();

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(intent.getStringExtra("title"));
        category_id = intent.getIntExtra("category_id", 0);
        System.out.println(category_id);

        gridview = findViewById(R.id.gridview);
        linearPagination = findViewById(R.id.linearPagination);
        horizontalPagination = findViewById(R.id.horizontalPagination);

        GetBooks(1);

    }

    void GetBooks(int p){
        int width = 0;
        DisplayMetrics displayMetrics = gridview.getResources().getDisplayMetrics();
        if ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            gridview.setColumnCount(3);
            gridview.setColumnCount(3);
            width = (int)(displayMetrics.widthPixels / 3.2);
        }else{
            width = (int)(displayMetrics.widthPixels / 2.2);
        }

        int from, to = 0;
        if (p == 1){
            from = 0;
            to = 50;
        }else{
            from = (50 * p) - 50;
            to = 50 * p;
        }

        int total_rows = 0;
        RealmResults<Books> getBooks;
        if (category_id == 0){
            total_rows = db.GetBooks().findAll().toArray().length;
            getBooks =  db.GetBooks().between("number", from, to).findAll();
        }else{
            total_rows = db.GetBooks(category_id).findAll().toArray().length;
            getBooks =  db.GetBooks(category_id).findAll();
        }
        int number_of_pages = (int) Math.ceil( total_rows / 50 );

        for (Books book: getBooks) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(width, GridLayout.LayoutParams.WRAP_CONTENT);
            paramsLinearLayout.setMargins(14, 20, 14, 18);
            linearLayout.setLayoutParams(paramsLinearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams paramsImageView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Glide.with(this).load(book.GetImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().apply(new RequestOptions().transforms(new RoundedCorners(30))).into(imageView);
            imageView.setLayoutParams(paramsImageView);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(book.GetName());
            textView.setGravity(Gravity.CENTER);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(1);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            linearLayout.setOnClickListener(v -> {
                Intent intent = new Intent(this, SingleBookActivity.class);
                intent.putExtra("id", book.GetId());
                startActivity(intent);
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(this);
                }
            });

            gridview.addView(linearLayout);
        }

        if (category_id != 0){
            horizontalPagination.setVisibility(View.GONE);
        }else{
            for (int i = 1; i <= number_of_pages; i++) {
                Button button = new Button(this);
                LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsLinearLayout.width = 84;
                paramsLinearLayout.height = 84;
                paramsLinearLayout.setMargins(10, 0, 10, 0);
                button.setLayoutParams(paramsLinearLayout);
                button.setGravity(Gravity.CENTER);
                button.setText(String.valueOf(i));
                button.setPadding(0, 0, 0, 0);
                button.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                if (p == i){
                    button.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.background_pagination_selected));
                    button.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                }else{
                    button.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.background_pagination));
                    button.setTextColor(getApplicationContext().getResources().getColor(R.color.button_text_color));
                }
                button.setTextSize(12);
                int finalI = i;
                button.setOnClickListener(v -> {
                    linearPagination.removeAllViews();
                    gridview.removeAllViews();
                    GetBooks(finalI);
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(this);
                    }
                });
                linearPagination.addView(button);
            }
        }
    }

    public void GoBack(View view){
        onBackPressed();
    }

}