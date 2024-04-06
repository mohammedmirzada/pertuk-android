package programnas.com.pertuk.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    Session session;

    ProgressBar Pb;
    LinearLayout LinearAll, LinearError;

    String lang;

    Realm realm;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        realm = Realm.getInstance(config);
        db = new Database(realm);

        lang = getResources().getConfiguration().locale.toString().substring(0, 2);

        Pb = findViewById(R.id.progressBar);
        LinearAll = findViewById(R.id.linear_all);
        LinearError = findViewById(R.id.linear_error);

        if (!Objects.equals(session.GetLanguage(), lang)){
            session.SetLibraryUpdateId("0");
        }
        new WebTask().execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void GoToSearchActivity(View view){
        Intent myIntent = new Intent(this, SearchActivity.class);
        this.startActivity(myIntent);
    }

    public void RefreshWeb(View view) {
        Pb.setVisibility(View.VISIBLE);
        LinearAll.setVisibility(View.INVISIBLE);
        LinearError.setVisibility(View.INVISIBLE);
        new WebTask().execute();
    }

    class WebTask extends AsyncTask<Void,Void,Void> {

        boolean isLoded = false;
        boolean library_update_status = false;
        ArrayList<Categories> arrayCategories;
        ArrayList<Authors> arrayAuthors;
        ArrayList<Books> arrayBooks;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("https://programnas.com/control/template/php/api/library/tools/?API=UPCSCFSJIEFOXHAZ0XASQQOLPXRTXBJVQDHZWKEJRBDG&tool=internet&lang="+lang+"&system=android&library_update_id="+session.GetLibraryUpdateId());
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode() <= 299) {
                    isLoded = true;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    JSONObject jsonObject = new JSONObject(String.valueOf(sb));

                    String library_update_id = jsonObject.get("library_update_id").toString();

                    if (!library_update_id.equals(session.GetLibraryUpdateId())) {

                        JSONArray jsonArrayAuthors = jsonObject.getJSONArray("authors");
                        arrayAuthors = new ArrayList<>();
                        for (int i = 0; i < jsonArrayAuthors.length(); i++) {
                            JSONObject m = jsonArrayAuthors.getJSONObject(i);
                            String id = m.getString("id");
                            String name = m.getString("name");
                            String image = m.getString("image");
                            Authors authors = new Authors();
                            authors.SetId(Integer.parseInt(id));
                            authors.SetName(name);
                            authors.SetImage(image);
                            arrayAuthors.add(authors);
                        }

                        JSONArray jsonArrayBooks = jsonObject.getJSONArray("books");
                        arrayBooks = new ArrayList<>();
                        for (int i = 0; i < jsonArrayBooks.length(); i++) {
                            JSONObject m = jsonArrayBooks.getJSONObject(i);
                            String id = m.getString("id");
                            String name = m.getString("name");
                            String image = m.getString("image");
                            String author_id = m.getString("author_id");
                            String category_ids = m.getString("category_ids");
                            String book1 = m.getString("book1");
                            String book2 = m.getString("book2");
                            String book3 = m.getString("book3");
                            String book4 = m.getString("book4");
                            String book5 = m.getString("book5");
                            String book6 = m.getString("book6");
                            String book7 = m.getString("book7");
                            String book8 = m.getString("book8");
                            String book9 = m.getString("book9");
                            String book10 = m.getString("book10");
                            String downloads = m.getString("downloads");
                            String views = m.getString("views");
                            Books books = new Books();
                            books.SetNumber(i == 0 ? 1 : i);
                            books.SetId(Integer.parseInt(id));
                            books.SetName(name);
                            books.SetImage(image);
                            books.SetAuthorId(Integer.parseInt(author_id));
                            books.SetCategoryIds(Integer.parseInt(category_ids));
                            books.SetBook1(book1);
                            books.SetBook2(book2);
                            books.SetBook3(book3);
                            books.SetBook4(book4);
                            books.SetBook5(book5);
                            books.SetBook6(book6);
                            books.SetBook7(book7);
                            books.SetBook8(book8);
                            books.SetBook9(book9);
                            books.SetBook10(book10);
                            books.SetDownloads(downloads);
                            books.SetViews(views);
                            arrayBooks.add(books);
                        }

                        JSONArray jsonArrayCtegories = jsonObject.getJSONArray("categories");
                        arrayCategories = new ArrayList<>();
                        for (int i = 0; i < jsonArrayCtegories.length(); i++) {
                            JSONObject m = jsonArrayCtegories.getJSONObject(i);
                            String id = m.getString("id");
                            String name = m.getString("name");
                            String image = m.getString("image");
                            Categories categories = new Categories();
                            categories.SetId(Integer.parseInt(id));
                            categories.SetName(name);
                            categories.SetImage(image);
                            arrayCategories.add(categories);
                        }

                        session.SetLibraryUpdateId(library_update_id);
                        session.SetLanguage(lang);
                        library_update_status = true;

                    }else{
                        session.SetLibraryUpdateId(library_update_id);
                        library_update_status = false;
                    }

                    br.close();
                }

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
            if (isLoded){
                if (library_update_status) {
                    db.DeleteAll();
                    for (Authors authors : arrayAuthors) {
                        db.SetAuthors(authors.GetId(), authors.GetName(), authors.GetImage());
                    }
                    for (Books books : arrayBooks) {
                        db.SetBooks(
                                books.number, books.id, books.name, books.image, books.author_id, books.category_ids,
                                books.book1, books.book2, books.book3, books.book4, books.book5, books.book6,
                                books.book7, books.book8, books.book9, books.book10, books.downloads, books.views
                        );
                    }
                    for (Categories category : arrayCategories) {
                        db.SetCategories(category.GetId(), category.GetName(), category.GetImage());
                    }
                }

                Pb.setVisibility(View.INVISIBLE);
                LinearAll.setVisibility(View.VISIBLE);
                LinearError.setVisibility(View.INVISIBLE);

                tabLayout = findViewById(R.id.tabLayout);
                viewPager = findViewById(R.id.viewPager);

                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.home)));
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.settings)));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                BadgeDrawable badgeDrawable = Objects.requireNonNull(tabLayout.getTabAt(1)).getOrCreateBadge();
                badgeDrawable.setBackgroundColor(Color.RED);
                badgeDrawable.setVisible(!session.isLogedin());

                final MyAdapter adapter = new MyAdapter(MainActivity.this,getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }else {
                Pb.setVisibility(View.INVISIBLE);
                LinearAll.setVisibility(View.INVISIBLE);
                LinearError.setVisibility(View.VISIBLE);
            }
        }
    }

}