package programnas.com.pertuk.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences SHPreferences;
    private SharedPreferences.Editor editor;
    public final Context context;
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "pertuk";
    private static final String IS_LOGED_IN = "IsLogedin";
    private static final String USER_ID = "user_id";
    private static final String IMAGE = "image";
    private static final String NAME = "name";
    private static final String USERNAME = "username";

    private static final String LIBRARY_UPDATE_ID = "library_update_id";

    private static final String LANGUAGE = "language";

    public Session(Context context) {
        this.context = context;
        SHPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = SHPreferences.edit();
    }

    public void SetLogedIn(boolean isLogedin) {
        editor.putBoolean(IS_LOGED_IN, isLogedin);
        editor.commit();
    }

    public void SetUserId(String user_id) {
        editor.putString(USER_ID, user_id);
        editor.commit();
    }

    public void SetImage(String image) {
        editor.putString(IMAGE, image);
        editor.commit();
    }

    public void SetName(String name) {
        editor.putString(NAME, name);
        editor.commit();
    }

    public void SetUsername(String username) {
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public void SetLibraryUpdateId(String library_update_id) {
        editor.putString(LIBRARY_UPDATE_ID, library_update_id);
        editor.commit();
    }

    public void SetLanguage(String language) {
        editor.putString(LANGUAGE, language);
        editor.commit();
    }

    public boolean isLogedin(){
        return SHPreferences.getBoolean(IS_LOGED_IN, false);
    }

    public String GetUserId(){
        return SHPreferences.getString(USER_ID,"0");
    }

    public String GetImage(){
        return SHPreferences.getString(IMAGE,"");
    }

    public String GetName(){
        return SHPreferences.getString(NAME, "");
    }

    public String GetUsername(){
        return SHPreferences.getString(USERNAME, "");
    }

    public String GetLibraryUpdateId(){
        return SHPreferences.getString(LIBRARY_UPDATE_ID,"0");
    }

    public String GetLanguage(){
        return SHPreferences.getString(LANGUAGE,"");
    }

}