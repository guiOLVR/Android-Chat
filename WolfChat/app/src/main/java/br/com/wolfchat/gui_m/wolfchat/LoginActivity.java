package br.com.wolfchat.gui_m.wolfchat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import br.com.wolfchat.gui_m.wolfchat.config.FirebaseConfig;
import br.com.wolfchat.gui_m.wolfchat.fragment.ContactFragment;
import br.com.wolfchat.gui_m.wolfchat.helper.Base64Custom;
import br.com.wolfchat.gui_m.wolfchat.helper.Permissions;
import br.com.wolfchat.gui_m.wolfchat.helper.Preferences;
import br.com.wolfchat.gui_m.wolfchat.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;

    private User user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private String identifierUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ifUserLoged();

        email = (EditText) findViewById(R.id.editTextEmailLoginId);
        password = (EditText) findViewById(R.id.editTextPasswordId);
        login = (Button) findViewById(R.id.btnLoginId);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                loginValidation();
            }
        });

    }

    public void loginValidation(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    identifierUser = Base64Custom.codeBase64(user.getEmail());
                    databaseReference = FirebaseConfig.getFirebase();
                    databaseReference.child("users").child(identifierUser);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User recoveredUser = dataSnapshot.getValue(User.class);

                            Preferences preferences = new Preferences(LoginActivity.this);
                            preferences.save(identifierUser,recoveredUser.getName());
                            //Toast.makeText(LoginActivity.this, preferences.getName(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(valueEventListener);


                    openMain();
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Erro ao efetuar Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openRegisterUser(View view){
        Intent it = new Intent(LoginActivity.this, RegisterUserActivity.class);
        startActivity(it);
    }

    public void ifUserLoged(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            openMain();
        }
    }

    public void openMain(){
        Intent it = new Intent(LoginActivity.this, MainActivity.class);
        //it.putExtra("userId", identifierUser);
        startActivity(it);
        finish();
    }
}
