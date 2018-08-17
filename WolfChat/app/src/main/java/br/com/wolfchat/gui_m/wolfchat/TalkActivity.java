package br.com.wolfchat.gui_m.wolfchat;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.wolfchat.gui_m.wolfchat.adpter.MessageAdapter;
import br.com.wolfchat.gui_m.wolfchat.config.FirebaseConfig;
import br.com.wolfchat.gui_m.wolfchat.helper.Base64Custom;
import br.com.wolfchat.gui_m.wolfchat.helper.Preferences;
import br.com.wolfchat.gui_m.wolfchat.model.Message;
import br.com.wolfchat.gui_m.wolfchat.model.Talking;

public class TalkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText message;
    private ImageButton send;

    private ListView listView;
    private ArrayList<Message> messages;
    private ArrayAdapter<Message> adapter;
    private ValueEventListener valueEventListenerMessage;

    private String userNameRecive;
    private String userIdRecive;

    private String userIdSeding;
    private String userNameSending;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        message = findViewById(R.id.et_message);
        send = findViewById(R.id.btn_send);
        listView = findViewById(R.id.lv_talking);

        //dados do usuario logado
        Preferences preferences = new Preferences(TalkActivity.this);
        userIdSeding = preferences.getIdentifier();
        userNameSending = preferences.getName();
        Toast.makeText(TalkActivity.this, userNameSending, Toast.LENGTH_SHORT).show();

        //recuperando dados passados por intent(nome e email do contato)
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            userNameRecive = extra.getString("contactName");
            String emailRecive = extra.getString("contactEmail");
            userIdRecive = Base64Custom.codeBase64(emailRecive);
        }

        //conf toolbar
        toolbar = findViewById(R.id.tb_talk);
        toolbar.setTitle(userNameRecive);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        //Listview das menssagens e adapter
        messages = new ArrayList<>();
       adapter = new MessageAdapter(TalkActivity.this, messages);
        listView.setAdapter(adapter);

        //pegando menssagens do firebase
        databaseReference = FirebaseConfig.getFirebase().child("messages").child(userIdSeding).child(userIdRecive);

        //listener das menssagens
        valueEventListenerMessage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();

                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                    br.com.wolfchat.gui_m.wolfchat.model.Message messagee = dataSnap.getValue(Message.class);
                    messages.add(messagee);
                }

                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addValueEventListener(valueEventListenerMessage);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = message.getText().toString();


                if (message.isAccessibilityFocused()){
                }
                else{
                    br.com.wolfchat.gui_m.wolfchat.model.Message message1 = new br.com.wolfchat.gui_m.wolfchat.model.Message();
                    message1.setIdUser(userIdSeding);
                    message1.setMessage(text);

                    //salvando a mensagem q é enviada
                    Boolean returnErrorSending = savingMessage(userIdSeding, userIdRecive, message1);
                    if (!returnErrorSending){
                        Toast.makeText(TalkActivity.this, "Erro ao enviar a mensagem, tente ovamente!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //salvando a mensagem q é recebida
                        Boolean returnErrorRecive = savingMessage(userIdRecive, userIdSeding, message1);
                        if (!returnErrorRecive){
                            Toast.makeText(TalkActivity.this, "Erro ao enviar a mensagem, tente ovamente!", Toast.LENGTH_LONG).show();
                        }
                    }
                    //salvando conversa
                    Talking talking = new Talking();
                    talking.setUserId(userIdRecive);
                    talking.setName(userNameRecive);
                    talking.setMessage(text);
                    Boolean returnTalkingSending = savingConversation(userIdSeding, userIdRecive, talking);
                    if (!returnTalkingSending){
                        Toast.makeText(TalkActivity.this, "Erro ao salvar a conversa!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        talking = new Talking();
                        talking.setUserId(userIdSeding);
                        talking.setName(userNameSending);
                        talking.setMessage(text);

                        Boolean returnTalkingRecive = savingConversation(userIdRecive, userIdSeding, talking);

                    }

                    message.setText("");
                }
            }
        });
    }

    private boolean savingMessage(String sendingId, String reciveId, Message messager){

        try{

            databaseReference = FirebaseConfig.getFirebase().child("messages");
            databaseReference.child(sendingId).child(reciveId).push().setValue(messager);

            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean savingConversation(String sendingId, String reciveId, Talking talk){

        try{
            databaseReference = FirebaseConfig.getFirebase().child("talking");
            databaseReference.child(sendingId).child(reciveId).setValue(talk);

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerMessage);
    }
}
