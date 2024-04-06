package programnas.com.pertuk.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FavReadActivity extends AppCompatActivity {

    Database db;
    GridLayout gridview;
    Session session;
    String type;
    ProgressBar Pb;
    ImageView imageEmpty;
    ArrayList<Integer> arrayBookIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_read);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        Realm realm = Realm.getInstance(config);
        db = new Database(realm);
        session = new Session(this);

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        gridview = findViewById(R.id.gridview);
        Pb = findViewById(R.id.progressBar);
        imageEmpty = findViewById(R.id.imageEmpty);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if (type.equals("read")) {
            textViewTitle.setText(getString(R.string.my_read_books));
        } else {
            textViewTitle.setText(getString(R.string.my_favourite_books));
        }

        new GetAll().execute();

    }

    class GetAll extends AsyncTask<Void,Void,Void> {

        ArrayList<Integer> array;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("https://programnas.com/control/template/php/api/library/get/?API=UPCSCFSJIEFOXHAZ0XASQQOLPXRTXBJVQDHZWKEJRBDG&tool=rf&get="+type+"&user_id="+session.GetUserId());
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                JSONObject jsonObject = new JSONObject(String.valueOf(sb));
                JSONArray books = jsonObject.getJSONArray("books");
                array = new ArrayList<>();
                for (int i = 0; i < books.length(); i++) {
                    JSONObject m = books.getJSONObject(i);
                    boolean found = m.getBoolean("found");
                    if (found){
                        int id = m.getInt("book_id");
                        array.add(id);
                    }
                }
                System.out.println(array);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Pb.setVisibility(View.GONE);
            if (array.size() == 0){
                imageEmpty.setVisibility(View.VISIBLE);
            }else{
                imageEmpty.setVisibility(View.GONE);
                arrayBookIds = array;
                GetBooks();
            }
        }

    }

    void GetBooks(){
        int width = 0;
        DisplayMetrics displayMetrics = gridview.getResources().getDisplayMetrics();
        if ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            gridview.setColumnCount(3);
            gridview.setColumnCount(3);
            width = (int)(displayMetrics.widthPixels / 3.2);
        }else{
            width = (int)(displayMetrics.widthPixels / 2.2);
        }

        for (int id : arrayBookIds) {
            Books books = db.GetSingleBook(id);
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(width, GridLayout.LayoutParams.WRAP_CONTENT);
            paramsLinearLayout.setMargins(14, 20, 14, 18);
            linearLayout.setLayoutParams(paramsLinearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams paramsImageView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Glide.with(this).load(books.GetImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().apply(new RequestOptions().transforms(new RoundedCorners(30))).into(imageView);
            imageView.setLayoutParams(paramsImageView);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(books.GetName());
            textView.setGravity(Gravity.CENTER);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(1);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            linearLayout.setOnClickListener(v -> {
                Intent intent = new Intent(this, SingleBookActivity.class);
                intent.putExtra("id", books.GetId());
                startActivity(intent);
            });

            gridview.addView(linearLayout);
        }

    }

    public void GoBack(View view) { onBackPressed(); }

}