package com.apps.gruz.korektorgraficzny.CustomViews;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.apps.gruz.korektorgraficzny.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    public interface RemoveButtonListener{
        void onRemoveButtonClick(int position);
    }


    private RemoveButtonListener listener;


    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CustomAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CustomAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public void setRemoveButtonListener(RemoveButtonListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);

        TextView name = (TextView) view.findViewById(R.id.list_item_text);
        ImageView delButton = (ImageView) view.findViewById(R.id.remove_list_item_button);

        name.setText(getItem(position));
        delButton.setOnClickListener(l->listener.onRemoveButtonClick(position));

        return view;
    }
}
