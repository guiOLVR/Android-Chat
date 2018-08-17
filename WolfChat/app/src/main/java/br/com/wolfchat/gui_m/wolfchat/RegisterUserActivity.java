package br.com.wolfchat.gui_m.wolfchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.wolfchat.gui_m.wolfchat.config.FirebaseConfig;
import br.com.wolfchat.gui_m.wolfchat.helper.Base64Custom;
import br.com.wolfchat.gui_m.wolfchat.helper.Preferences;
import br.com.wolfchat.gui_m.wolfchat.model.User;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button confirm;

    private User user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        name = (EditText)  findViewById(R.id.editTextNameId);
        email = (EditText) findViewById(R.id.editTextEmailRegisterId);
        password = (EditText) findViewById(R.id.editTextPasswordRegisterId);
        confirm = (Button) findViewById(R.id.btnRegisterId);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----------criando o usuario incluir no Firebase
                user = new User();
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                regUser();
            }
        });
    }

    private void regUser(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        //firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(RegisterUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterUserActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                    //FirebaseUser firebaseUser = task.getResult().getUser();
                    //user.setId(firebaseUser.getUid());

                    String userIdentifier = Base64Custom.codeBase64(user.getEmail());
                    user.setId(userIdentifier);
                    user.save();



                    //firebaseAuth.signOut();
                    //Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);

                    Preferences preferences = new Preferences(RegisterUserActivity.this);
                    preferences.save(userIdentifier, user.getName());
                    openLoginUser();
                }
                else{
                    String error;

                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        error = "Senha muito fraca";
                       // Toast.makeText(RegisterUserActivity.this, "Senha muito fraca", Toast.LENGTH_SHORT).show();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        error = "E-mail inválido";
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        error = "Este e-mail ja está cadastrado!";
                    }
                    catch (Exception e){
                        error = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(RegisterUserActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openLoginUser(){
        Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
