package br.com.wolfchat.gui_m.wolfchat.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.wolfchat.gui_m.wolfchat.R;
import br.com.wolfchat.gui_m.wolfchat.model.Contact;

public class ContactAdapter extends ArrayAdapter<Contact>{

    private ArrayList<Contact> contacts;
    private Context c;

    public ContactAdapter(@NonNull Context context, @NonNull ArrayList<Contact> objects) {
        super(context, 0, objects);
        this.contacts = objects;
        this.c = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;
        if (contacts != null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);//inicializa o objeto para montagem da view

            view = inflater.inflate(R.layout.contact_list, parent, false);//Monta a view apartir do xml

            TextView contactName = view.findViewById(R.id.tv_name);//recupera o elemento para exibição
            TextView contactEmail = view.findViewById(R.id.tv_email);

            Contact contact = contacts.get(position);
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }



        return view;
    }
}
