package programnas.com.pertuk.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).deleteRealmIfMigrationNeeded().name("Pertuk").build();
        Realm realm = Realm.getInstance(config);
        Database db = new Database(realm);

        ImageView imageSearch = findViewById(R.id.imageSearch);
        EditText searchEditText = findViewById(R.id.searchEditText);
        ListView listView = findViewById(R.id.listView);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2){
                    List<String> arrayNames = new ArrayList<>();
                    List<Integer> arrayIds = new ArrayList<>();
                    for (Books book: db.GetBooks(s.toString()).findAll()) {
                        arrayNames.add(book.GetName());
                        arrayIds.add(book.GetId());
                    }
                    if (arrayNames.size() != 0){
                        imageSearch.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this,
                                android.R.layout.simple_list_item_1, arrayNames);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Intent intent = new Intent(SearchActivity.this, SingleBookActivity.class);
                            intent.putExtra("id", arrayIds.get(position).intValue());
                            startActivity(intent);
                        });
                    }else{
                        imageSearch.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                }else{
                    imageSearch.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void GoBack(View view){
        onBackPressed();
    }

}