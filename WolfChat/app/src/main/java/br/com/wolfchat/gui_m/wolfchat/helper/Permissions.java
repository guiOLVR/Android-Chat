package br.com.wolfchat.gui_m.wolfchat.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean valPermissions(int requestCode, Activity activity, String[] permissions){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> permissionList = new ArrayList<String>();
            for (String perm : permissions){
                Boolean valPerm = ContextCompat.checkSelfPermission(activity, perm) == PackageManager.PERMISSION_GRANTED;
                if(!valPerm){
                    permissionList.add(perm);
                }
            }
            if(permissionList.isEmpty()){
                return true;
            }

            String[] permissionArray = new String[permissionList.size()];
            permissionList.toArray(permissionArray);
            ActivityCompat.requestPermissions(activity, permissionArray, requestCode);
        }

        return false;
    }
}
