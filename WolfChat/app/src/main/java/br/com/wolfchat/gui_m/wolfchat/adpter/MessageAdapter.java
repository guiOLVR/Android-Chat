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

import br.com.wolfchat.gui_m.wolfchat.R;
import br.com.wolfchat.gui_m.wolfchat.helper.Preferences;
import br.com.wolfchat.gui_m.wolfchat.model.Message;

public class MessageAdapter extends ArrayAdapter<Message>{

    private Context c;
    private ArrayList<Message> messages;

    public MessageAdapter(@NonNull Context context, @NonNull ArrayList<Message> objects) {
        super(context, 0, objects);
        this.c = context;
        this.messages = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (messages != null){
            Preferences preferences = new Preferences(c);
            String userIdSending = preferences.getIdentifier();

            LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);

            Message message = messages.get(position);

            if (userIdSending.equals(message.getIdUser())){
                view = inflater.inflate(R.layout.right_message, parent, false);
            }
            else {
                view = inflater.inflate(R.layout.left_message, parent, false);
            }

            TextView textMessage = view.findViewById(R.id.tv_message);
            textMessage.setText(message.getMessage());
        }

        return view;
    }
}
