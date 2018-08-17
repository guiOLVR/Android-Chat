package br.com.wolfchat.gui_m.wolfchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.wolfchat.gui_m.wolfchat.R;
import br.com.wolfchat.gui_m.wolfchat.TalkActivity;
import br.com.wolfchat.gui_m.wolfchat.adpter.ContactAdapter;
import br.com.wolfchat.gui_m.wolfchat.config.FirebaseConfig;
import br.com.wolfchat.gui_m.wolfchat.helper.Preferences;
import br.com.wolfchat.gui_m.wolfchat.model.Contact;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contact> contacts;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contacts = new ArrayList<>();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        listView = view.findViewById(R.id.lv_contact);
        /*adapter = new ArrayAdapter(getActivity(), R.layout.contact_list, contacts);
        listView.setAdapter(adapter);*/
        adapter = new ContactAdapter(getActivity(), contacts);
        listView.setAdapter(adapter);

        Preferences preferences = new Preferences(getActivity());
        String identifierLogedUser = preferences.getIdentifier();

        databaseReference = FirebaseConfig.getFirebase().child("contacts").child(identifierLogedUser);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //----------------Clean-----------
                contacts.clear();
                //--------Listar contatos------------
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Contact contact = snapshot.getValue(Contact.class);
                    //String name = contact.getName();
                    contacts.add(contact);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = contacts.get(i);

                Intent it = new Intent(getActivity(), TalkActivity.class);
                it.putExtra("contactName", contact.getName());
                it.putExtra("contactEmail", contact.getEmail());
                startActivity(it);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }
}
