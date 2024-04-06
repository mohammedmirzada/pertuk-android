package programnas.com.pertuk.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SingleBookActivity extends AppCompatActivity {

    int book_id = 0;
    boolean isFav, isRead = false;
    Session session;
    ImageButton favImage, readImage;
    String name;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);

        session = new Session(this);

        ImageView bookImage = findViewById(R.id.bookImage);
        TextView bookName = findViewById(R.id.bookName);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView downloadsTextView = findViewById(R.id.downloadsTextView);
        TextView viewsTextView = findViewById(R.id.viewsTextView);
        LinearLayout linCatAuth = findViewById(R.id.linCatAuth);
        LinearLayout linRead = findViewById(R.id.linRead);
        LinearLayout linDownload = findViewById(R.id.linDownload);
        favImage = findViewById(R.id.fav);
        readImage = findViewById(R.id.read);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        Realm realm = Realm.getInstance(config);
        Database db = new Database(realm);

        Intent intent = getIntent();
        book_id = intent.getIntExtra("id", 0);

        String image = db.GetSingleBook(book_id).GetImage();
        name = db.GetSingleBook(book_id).GetName();
        String downloads = db.GetSingleBook(book_id).GetDownloads();
        String views = db.GetSingleBook(book_id).GetViews();
        int category_ids = db.GetSingleBook(book_id).GetCategoryIds();
        int author_id = db.GetSingleBook(book_id).GetAuthorId();
        String book1 = db.GetSingleBook(book_id).GetBook1();
        String book2 = db.GetSingleBook(book_id).GetBook2();
        String book3 = db.GetSingleBook(book_id).GetBook3();
        String book4 = db.GetSingleBook(book_id).GetBook4();
        String book5 = db.GetSingleBook(book_id).GetBook5();
        String book6 = db.GetSingleBook(book_id).GetBook6();
        String book7 = db.GetSingleBook(book_id).GetBook7();
        String book8 = db.GetSingleBook(book_id).GetBook8();
        String book9 = db.GetSingleBook(book_id).GetBook9();
        String book10 = db.GetSingleBook(book_id).GetBook10();

        Glide.with(this).load(image).fitCenter().apply(new RequestOptions().transforms(new RoundedCorners(30))).into(bookImage);
        bookName.setText(name);
        textViewTitle.setText(name);
        downloadsTextView.setText(downloads);
        viewsTextView.setText(views);

        if (db.GetSingleBook(book_id).GetCategoryIds() != 0){
            Button button = new Button(this);
            button.setText(db.GetSingleCategories(category_ids).GetName());
            button.setAllCaps(false);
            Resources resources = this.getResources();
            int resourceId = resources.getIdentifier(db.GetSingleCategories(category_ids).GetImage(), "drawable",
                    this.getPackageName());
            Drawable drawable = resources.getDrawable(resourceId);
            button.setCompoundDrawablesWithIntrinsicBounds(drawable,null, null, null);
            button.setCompoundDrawablePadding(8);
            button.setOnClickListener(v -> {
                Intent i = new Intent(this, AllBooksActivity.class);
                i.putExtra("title", db.GetSingleCategories(db.GetSingleBook(book_id).GetCategoryIds()).GetName());
                i.putExtra("category_id", db.GetSingleCategories(db.GetSingleBook(book_id).GetCategoryIds()).GetId());
                startActivity(i);
            });
            linCatAuth.addView(button);
        }

        if (db.GetSingleBook(book_id).GetAuthorId() != 0){
            Button button = new Button(this);
            button.setText(db.GetSingleAuthors(db.GetSingleBook(book_id).GetAuthorId()).GetName());
            button.setAllCaps(false);
            button.setOnClickListener(v -> {
                Intent i = new Intent(this, SingleAuthorActivity.class);
                i.putExtra("id", db.GetSingleAuthors(author_id).GetId());
                i.putExtra("name", db.GetSingleAuthors(author_id).GetName());
                i.putExtra("image", db.GetSingleAuthors(author_id).GetImage());
                startActivity(i);
            });
            linCatAuth.addView(button);
        }

        if (!book1.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_one), book1, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_one), book1, true));
        }

        if (!book2.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_two), book2, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_two), book2, true));
        }

        if (!book3.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_three), book3, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_three), book3, true));
        }

        if (!book4.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_four), book4, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_four), book4, true));
        }

        if (!book5.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_five), book5, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_five), book5, true));
        }

        if (!book6.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_six), book6, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_six), book6, true));
        }

        if (!book7.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_seven), book7, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_seven), book7, true));
        }

        if (!book8.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_eight), book8, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_eight), book8, true));
        }

        if (!book9.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_nine), book9, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_nine), book9, true));
        }

        if (!book10.isEmpty()) {
            linRead.addView(CustomButton(getString(R.string.read_book_ten), book10, false));
            linDownload.addView(CustomButton(getString(R.string.download_book_ten), book10, true));
        }

        favImage.setOnClickListener(v -> {
            if(session.isLogedin()) {
                if (isFav) {
                    isFav = false;
                    favImage.setImageDrawable(getResources().getDrawable(R.drawable.fav, getApplicationContext().getTheme()));
                } else {
                    isFav = true;
                    favImage.setImageDrawable(getResources().getDrawable(R.drawable.fav_fill, getApplicationContext().getTheme()));
                }
                ModifyRF("fav");
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.u_h_r))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), (dialog, id) -> {
                            dialog.cancel();
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        readImage.setOnClickListener(v -> {
            if(session.isLogedin()) {
                if (isRead) {
                    isRead = false;
                    readImage.setImageDrawable(getResources().getDrawable(R.drawable.read, getApplicationContext().getTheme()));
                } else {
                    isRead = true;
                    readImage.setImageDrawable(getResources().getDrawable(R.drawable.read_fill, getApplicationContext().getTheme()));
                }
                ModifyRF("read");
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.u_h_f))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), (dialog, id) -> {
                            dialog.cancel();
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        if (session.isLogedin()){
            CheckRF();
        }

    }

    Button CustomButton(String buttonName, String book, boolean isDownload){
        MaterialButton button = new MaterialButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 8;
        button.setText(buttonName);
        button.setLayoutParams(params);
        if (isDownload){
            button.setOnClickListener(v -> {
                Uri uri = Uri.parse("https://programnas.com/control/template/php/api/library/get/?API=UPCSCFSJIEFOXHAZ0XASQQOLPXRTXBJVQDHZWKEJRBDG&tool=download&book_id=" + book_id + "&file=" + book);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });
        }else{
            button.setOnClickListener(v -> {
                Intent intent = new Intent(this, PDFViewerActivity.class);
                intent.putExtra("url", "https://programnas.com/control/files/" + book);
                intent.putExtra("title", name);
                startActivity(intent);
            });
        }
        return button;
    }

    void ModifyRF(String modify){
        try {
            String str_url = "https://programnas.com/control/template/php/api/library/get/?API=UPCSCFSJIEFOXHAZ0XASQQOLPXRTXBJVQDHZWKEJRBDG&tool=modify_rf&get="+modify+"&user_id="+session.GetUserId()+"&book_id="+book_id;
            URL url = new URL(str_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void CheckRF(){
        try {
            String str_url = "https://programnas.com/control/template/php/api/library/get/?API=UPCSCFSJIEFOXHAZ0XASQQOLPXRTXBJVQDHZWKEJRBDG&tool=check_rf&user_id="+session.GetUserId()+"&book_id="+book_id;
            URL url = new URL(str_url);
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
            JSONObject jObj = new JSONObject(String.valueOf(sb));
            JSONObject checked = jObj.getJSONObject("checked");

            boolean read = checked.getBoolean("read");
            boolean favourite = checked.getBoolean("favourite");

            if (read){
                isRead = true;
                readImage.setImageDrawable(getResources().getDrawable(R.drawable.read_fill, getApplicationContext().getTheme()));
            }else{
                isRead = false;
                readImage.setImageDrawable(getResources().getDrawable(R.drawable.read, getApplicationContext().getTheme()));
            }

            if (favourite){
                isFav = true;
                favImage.setImageDrawable(getResources().getDrawable(R.drawable.fav_fill, getApplicationContext().getTheme()));
            }else{
                isFav = false;
                favImage.setImageDrawable(getResources().getDrawable(R.drawable.fav, getApplicationContext().getTheme()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void GoBack(View view) { onBackPressed(); }

}