package programnas.com.pertuk.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class HomeFragment extends Fragment {

    View view;

    private InterstitialAd mInterstitialAd;

    public HomeFragment() {
        // Required empty public constructor
    }

    int adFailedCount = 0;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        MobileAds.initialize(requireActivity());

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(requireActivity(),"ca-app-pub-9877420063334339/3263433219", adRequest,
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

        Realm.init(requireActivity());
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        Realm realm = Realm.getInstance(config);
        Database db  = new Database(realm);

        LinearLayout linearCategories = view.findViewById(R.id.linearCategories);
        GridLayout gridviewBooks = view.findViewById(R.id.gridviewBooks);
        GridLayout gridviewAuthors = view.findViewById(R.id.gridviewAuthors);

        Button buttonViewAllBooks = view.findViewById(R.id.buttonViewAllBooks);
        buttonViewAllBooks.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AllBooksActivity.class);
            intent.putExtra("title", getString(R.string.all_books));
            intent.putExtra("category_id", 0);
            startActivity(intent);
            if (mInterstitialAd != null) {
                mInterstitialAd.show(requireActivity());
            }
        });
        Button buttonViewAllAuthors = view.findViewById(R.id.buttonViewAllAuthors);
        buttonViewAllAuthors.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AllAuthorsActivity.class));
            if (mInterstitialAd != null) {
                mInterstitialAd.show(requireActivity());
            }
        });

        for (Categories category:db.GetCategories().findAll()){
            Button button = new Button(requireActivity());
            button.setText(category.GetName());
            button.setAllCaps(false);
            Resources resources = requireActivity().getResources();
            int resourceId = resources.getIdentifier(category.GetImage(), "drawable",
                    requireActivity().getPackageName());
            Drawable drawable = resources.getDrawable(resourceId);
            button.setCompoundDrawablesWithIntrinsicBounds(drawable,null, null, null);
            button.setCompoundDrawablePadding(8);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), AllBooksActivity.class);
                intent.putExtra("title", category.GetName());
                intent.putExtra("category_id", category.GetId());
                startActivity(intent);
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(requireActivity());
                }
            });
            linearCategories.addView(button);
        }

        int width = 0;
        DisplayMetrics displayMetrics = gridviewBooks.getResources().getDisplayMetrics();
        if ((requireActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            gridviewBooks.setColumnCount(3);
            gridviewAuthors.setColumnCount(3);
            width = (int)(displayMetrics.widthPixels / 3.5);
        }else{
            width = (int)(displayMetrics.widthPixels / 2.5);
        }

        for (Books book: db.GetBooks().limit(10).findAll()) {
            LinearLayout linearLayout = new LinearLayout(requireActivity());
            LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(width, GridLayout.LayoutParams.WRAP_CONTENT);
            paramsLinearLayout.setMargins(14, 20, 14, 18);
            linearLayout.setLayoutParams(paramsLinearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(requireActivity());
            LinearLayout.LayoutParams paramsImageView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Glide.with(requireActivity()).load(book.GetImage()).fitCenter().apply(new RequestOptions().transforms(new RoundedCorners(30))).into(imageView);
            imageView.setLayoutParams(paramsImageView);

            TextView textView = new TextView(requireActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(book.GetName());
            textView.setGravity(Gravity.CENTER);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(1);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            linearLayout.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), SingleBookActivity.class);
                intent.putExtra("id", book.GetId());
                startActivity(intent);
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(requireActivity());
                }
            });

            gridviewBooks.addView(linearLayout);
        }

        for (Authors authors: db.GetAuthors().limit(10).findAll()) {
            LinearLayout linearLayout = new LinearLayout(requireActivity());
            LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(width, GridLayout.LayoutParams.WRAP_CONTENT);
            paramsLinearLayout.setMargins(14, 20, 14, 18);
            linearLayout.setLayoutParams(paramsLinearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(requireActivity());
            LinearLayout.LayoutParams paramsImageView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Glide.with(requireActivity()).load(authors.GetImage()).fitCenter().circleCrop().into(imageView);
            imageView.setLayoutParams(paramsImageView);

            TextView textView = new TextView(requireActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(authors.GetName());
            textView.setGravity(Gravity.CENTER);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(1);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            linearLayout.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), SingleAuthorActivity.class);
                intent.putExtra("id", authors.GetId());
                intent.putExtra("name", authors.GetName());
                intent.putExtra("image", authors.GetImage());
                startActivity(intent);
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(requireActivity());
                }
            });

            gridviewAuthors.addView(linearLayout);
        }

        AdView adViewOne = view.findViewById(R.id.adViewOne);
        adViewOne.loadAd(new AdRequest.Builder().build());
        adViewOne.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adFailedCount += 1;
                if (adFailedCount < 10){
                    adViewOne.loadAd(new AdRequest.Builder().build());
                }
            }
        });

        AdView adViewTwo = view.findViewById(R.id.adViewTwo);
        adViewTwo.loadAd(new AdRequest.Builder().build());
        adViewTwo.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adFailedCount += 1;
                if (adFailedCount < 10){
                    adViewTwo.loadAd(new AdRequest.Builder().build());
                }
            }
        });

        AdView adViewThree = view.findViewById(R.id.adViewThree);
        adViewThree.loadAd(new AdRequest.Builder().build());
        adViewThree.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adFailedCount += 1;
                if (adFailedCount < 10){
                    adViewThree.loadAd(new AdRequest.Builder().build());
                }
            }
        });

        return view;
    }

}