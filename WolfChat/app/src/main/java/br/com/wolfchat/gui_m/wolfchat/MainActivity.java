package br.com.wolfchat.gui_m.wolfchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.wolfchat.gui_m.wolfchat.adpter.TabAdapter;
import br.com.wolfchat.gui_m.wolfchat.config.FirebaseConfig;
import br.com.wolfchat.gui_m.wolfchat.helper.Base64Custom;
import br.com.wolfchat.gui_m.wolfchat.helper.Preferences;
import br.com.wolfchat.gui_m.wolfchat.helper.SlidingTabLayout;
import br.com.wolfchat.gui_m.wolfchat.model.Contact;
import br.com.wolfchat.gui_m.wolfchat.model.User;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private String identfierContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("WolfChat");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_page);

        //----------configuração das tabs---------
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorPrimaryVeryDark));

        //----------configuração do adapter-----------
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_out:
                logoutUser();
                return true;

            case R.id.action_settings:
                return true;

            case R.id.action_add:
                openRegisterContact();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logoutUser(){
        firebaseAuth.signOut();
        Intent it = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(it);
        finish();
    }

    public void openRegisterContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Adicionar contato");
        builder.setMessage("Digite o e-mail do contato");
        builder.setCancelable(false);//O usuário não sai do alert clicando fora dele

        //----------------EditText email-----------------------
        final EditText editText = new EditText(MainActivity.this);
        builder.setView(editText);

        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String contactEmail = editText.getText().toString();

                if (contactEmail.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
                }
                else{
                    //---------transforma em base64--------------
                    identfierContact = Base64Custom.codeBase64(contactEmail);

                    //------consultando no firebase em Base64------------
                    databaseReference = FirebaseConfig.getFirebase().child("users").child(identfierContact);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                User userContact = dataSnapshot.getValue(User.class);
                                /*----------Gamby------------------
                                Bundle extra = getIntent().getExtras();
                                String identifierLogedUser = (String) extra.get("userId");
                                //---------------------------------*/

                                //---------------Resgatando usuario logado---------
                                Preferences preferences = new Preferences(MainActivity.this);
                                String identifierLogedUser = preferences.getIdentifier();

                                Toast.makeText(MainActivity.this, identifierLogedUser, Toast.LENGTH_SHORT).show();

                                databaseReference = FirebaseConfig.getFirebase();
                                databaseReference = databaseReference.child("contacts")
                                        .child( identifierLogedUser )
                                        .child( identfierContact );

                                Contact contact = new Contact();
                                contact.setIdentifierUser( identfierContact );
                                contact.setEmail(userContact.getEmail());
                                contact.setName(userContact.getName());

                                databaseReference.setValue( contact );

                            }
                            else {
                                Toast.makeText(MainActivity.this, "E-mail não existente", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create();
        builder.show();

    }

    public String logedUserHelp(){
        Bundle extra = getIntent().getExtras();
        String identifierLogedUser = (String) extra.get("userId");
        return identifierLogedUser;
    }

}
