package br.com.wolfchat.gui_m.wolfchat.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codeBase64(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decoBase64(String decoText){
        return new String( Base64.decode(decoText, Base64.DEFAULT));
    }

}
