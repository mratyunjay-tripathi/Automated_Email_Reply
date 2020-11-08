package tech.spirit.automatedemailreply;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Application_Class extends Application {

    public static ArrayList<EmailAttr> list;
    public static FirebaseUser user;
    public static FirebaseAuth mAuth;
    public static Boolean content=false;


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
