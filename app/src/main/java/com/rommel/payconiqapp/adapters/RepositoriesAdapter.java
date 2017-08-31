package com.rommel.payconiqapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.dto.RepositoryDTO;

import java.util.ArrayList;

/**
 * Created by Rommel on 8/31/2017.
 */
public class RepositoriesAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private ArrayList<RepositoryDTO> data;

    public RepositoriesAdapter(Context context, int resource, ArrayList<RepositoryDTO> data) {

        super();

        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        updateItemView(convertView, data.get(position));

        return convertView;
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public RepositoryDTO getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    private void updateItemView(View view, RepositoryDTO data) {

        TextView repositoryIdLabel = (TextView) view.findViewById(R.id.repository_id_label);
        TextView repositoryNameLabel = (TextView) view.findViewById(R.id.repository_name_label);

        repositoryIdLabel.setText(data.id);
        repositoryNameLabel.setText(data.name);
    }
}
