package programnas.com.pertuk.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    String lang;
    String username, password;
    ProgressBar progressBar;
    View view;
    Session session;
    LinearLayout linLogin, linProfile;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        linLogin = view.findViewById(R.id.lin_login);
        linProfile = view.findViewById(R.id.lin_profile);

        Button readButton = view.findViewById(R.id.readButton);
        Button favButton = view.findViewById(R.id.favButton);

        session = new Session(requireActivity());

        if (session.isLogedin()){
            linLogin.setVisibility(View.GONE);
            linProfile.setVisibility(View.VISIBLE);
        }else{
            linLogin.setVisibility(View.VISIBLE);
            linProfile.setVisibility(View.GONE);
        }

        lang = getResources().getConfiguration().locale.toString().substring(0, 2);

        TextView signUpTextView = view.findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/signup?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        TextView copyrightTextView = view.findViewById(R.id.copyrightTextView);
        copyrightTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/library/copyright?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        TextView communityTextView = view.findViewById(R.id.communityTextView);
        communityTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/questions?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        TextView supportTextView = view.findViewById(R.id.supportTextView);
        supportTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/support?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        TextView privacyTextView = view.findViewById(R.id.privacyTextView);
        privacyTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/library/privacy?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        progressBar = view.findViewById(R.id.progressBar);

        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            new LoginTask().execute();
        });

        Button buttonProfile = view.findViewById(R.id.buttonProfile);
        buttonProfile.setText(session.GetName());
        buttonProfile.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/"+session.GetUsername()));
            startActivity(browserIntent);
        });
        buttonProfile.setCompoundDrawablePadding(8);
        AddDrawableButton(buttonProfile);

        TextView copyrightTextView2 = view.findViewById(R.id.copyrightTextView2);
        copyrightTextView2.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/library/copyright?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        TextView communityTextView2 = view.findViewById(R.id.communityTextView2);
        communityTextView2.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/questions?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        TextView supportTextView2 = view.findViewById(R.id.supportTextView2);
        supportTextView2.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/support?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        TextView privacyTextView2 = view.findViewById(R.id.privacyTextView2);
        privacyTextView2.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://programnas.com/library/privacy?ios_android=true&app_lang="+lang));
            startActivity(browserIntent);
        });

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireActivity()).setMessage(getString(R.string.r_u_l)).setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                session.SetUserId("");
                session.SetName("");
                session.SetUsername("");
                session.SetImage("");
                session.SetLogedIn(false);
                requireActivity().finish();
                startActivity(requireActivity().getIntent());
            }).setNegativeButton(getString(R.string.no), null).show();
        });

        readButton.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), FavReadActivity.class).putExtra("type", "read"));
        });

        favButton.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), FavReadActivity.class).putExtra("type", "fav"));
        });

        TextView copyright_text_one = view.findViewById(R.id.copyright_text_one);
        TextView copyright_text_two = view.findViewById(R.id.copyright_text_two);

        copyright_text_one.setText("© " + Year.now().getValue() + " " + getString(R.string.pnl));
        copyright_text_two.setText("© " + Year.now().getValue() + " " + getString(R.string.pnl));

        // Inflate the layout for this fragment
        return view;
    }

    void AddDrawableButton(Button button) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(session.GetImage());
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 64, 64, true);
            Drawable drawable = new BitmapDrawable(getResources(), bitmapScaled);
            button.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,null, null, null
            );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    class LoginTask extends AsyncTask<Void,Void,Void> {

        boolean success = false;
        String error = "";
        String _username, name, image, support_code = "";
        int id = 0;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("https://programnas.com/control/template/php/api/library/login/?API=UPCSCFSJIEFOXHAZ0XASQQOLPXRTXBJVQDHZWKEJRBDG&app_lang="+lang+"&username="+username+"&password="+password);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }

                JSONObject jObj = new JSONObject(String.valueOf(sb));
                JSONObject user = jObj.getJSONObject("user");
                String error = user.getString("error");
                boolean success = user.getBoolean("success");
                if (success){
                    this.success = success;
                    this.error = error;
                    this.id = user.getInt("id");
                    this._username = user.getString("username");
                    this.name = user.getString("name");
                    this.image = user.getString("image");
                    this.support_code = user.getString("support_code");
                }else {
                    this.success = success;
                    this.error = error;
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
            progressBar.setVisibility(View.GONE);
            if (!success){
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }else{
                session.SetUserId(String.valueOf(id));
                session.SetName(name);
                session.SetUsername(_username);
                session.SetImage(image);
                session.SetLogedIn(true);
                requireActivity().finish();
                startActivity(requireActivity().getIntent());
            }
        }
    }

}