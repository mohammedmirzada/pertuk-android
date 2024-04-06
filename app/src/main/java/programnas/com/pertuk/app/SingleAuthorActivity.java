package programnas.com.pertuk.app;

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
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SingleAuthorActivity extends AppCompatActivity {

    int adFailedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_author);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        Realm realm = Realm.getInstance(config);
        Database db = new Database(realm);

        Intent intent = getIntent();

        int author_id = intent.getIntExtra("id", 0);

        ImageView authorImage = findViewById(R.id.authorImage);
        TextView authorName = findViewById(R.id.authorName);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        GridLayout gridview = findViewById(R.id.gridview);

        Glide.with(this).load(intent.getStringExtra("image")).fitCenter().circleCrop().into(authorImage);
        authorName.setText(intent.getStringExtra("name"));
        textViewTitle.setText(intent.getStringExtra("name"));

        int width = 0;
        DisplayMetrics displayMetrics = gridview.getResources().getDisplayMetrics();
        if ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            gridview.setColumnCount(3);
            gridview.setColumnCount(3);
            width = (int)(displayMetrics.widthPixels / 3.11);
        }else{
            width = (int)(displayMetrics.widthPixels / 2.11);
        }

        for (Books book: db.GetBooks(author_id, "null").limit(10).findAll()) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams paramsLinearLayout = new LinearLayout.LayoutParams(width, GridLayout.LayoutParams.WRAP_CONTENT);
            paramsLinearLayout.setMargins(14, 20, 14, 18);
            linearLayout.setLayoutParams(paramsLinearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams paramsImageView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Glide.with(this).load(book.GetImage()).fitCenter().apply(new RequestOptions().transforms(new RoundedCorners(30))).into(imageView);
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
                Intent intent1 = new Intent(this, SingleBookActivity.class);
                intent1.putExtra("id", book.GetId());
                startActivity(intent1);
            });

            gridview.addView(linearLayout);
        }

    }

    public void GoBack(View view) { onBackPressed(); }
}