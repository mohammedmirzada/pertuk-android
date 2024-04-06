package programnas.com.pertuk.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AllAuthorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_authors);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        Realm realm = Realm.getInstance(config);
        Database db  = new Database(realm);

        GridLayout gridview = findViewById(R.id.gridview);

        int width = 0;
        DisplayMetrics displayMetrics = gridview.getResources().getDisplayMetrics();
        if ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            gridview.setColumnCount(3);
            gridview.setColumnCount(3);
            width = (int)(displayMetrics.widthPixels / 3.2);
        }else{
            width = (int)(displayMetrics.widthPixels / 2.2);
        }

        for (Authors author: db.GetAuthors().findAll()) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(width, GridLayout.LayoutParams.WRAP_CONTENT);
            paramsLinearLayout.setMargins(14, 20, 14, 18);
            linearLayout.setLayoutParams(paramsLinearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams paramsImageView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Glide.with(this).load(author.GetImage()).fitCenter().circleCrop().into(imageView);
            imageView.setLayoutParams(paramsImageView);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(author.GetName());
            textView.setGravity(Gravity.CENTER);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(1);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            linearLayout.setOnClickListener(v -> {
                Intent intent = new Intent(this, SingleAuthorActivity.class);
                intent.putExtra("id", author.GetId());
                intent.putExtra("name", author.GetName());
                intent.putExtra("image", author.GetImage());
                startActivity(intent);
                startActivity(intent);
            });

            gridview.addView(linearLayout);
        }

    }

    public void GoBack(View view) {
        onBackPressed();
    }
}