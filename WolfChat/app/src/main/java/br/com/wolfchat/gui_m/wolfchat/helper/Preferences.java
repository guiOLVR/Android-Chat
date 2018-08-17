package br.com.wolfchat.gui_m.wolfchat.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

    private static final String ARQ_NAME = "chat_preferences";
    private static final int MODE = 0;

    private static final String IDENTIFIER_KEY = "userIdentifier";
    private static final String NAME_KEY = "userName";

    public Preferences(Context context){
        this.context = context;
        preferences = this.context.getSharedPreferences(ARQ_NAME, MODE);
        preferencesEditor = preferences.edit();
    }

    public void save(String identifierUser, String name){
        preferencesEditor.putString(IDENTIFIER_KEY, identifierUser);
        preferencesEditor.putString(NAME_KEY, name);
        preferencesEditor.commit();
    }

    public String getIdentifier(){
        return preferences.getString(IDENTIFIER_KEY, null);
    }
    public String getName(){
        return preferences.getString(NAME_KEY, null);
    }

}
