package com.rommel.payconiqapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.data.RepositoryObject;

import java.util.ArrayList;

/**
 * Custom adapter for passing repository objects to a ListView element.
 */
public class RepositoriesAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private ArrayList<RepositoryObject> data;

    public RepositoriesAdapter(Context context, int resource, ArrayList<RepositoryObject> data) {

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
    public RepositoryObject getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * Add a list of repository items to the existing data set.
     * @param data the list of repositories to be added
     */
    public void updateDataSet(ArrayList<RepositoryObject> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    /**
     * Set object data to item view renderer.
     * @param view the view object
     * @param data the data to be displayed
     */
    private void updateItemView(View view, RepositoryObject data) {

        TextView repositoryIdLabel = (TextView) view.findViewById(R.id.repository_id_label);
        TextView repositoryNameLabel = (TextView) view.findViewById(R.id.repository_name_label);

        repositoryIdLabel.setText(data.getId());
        repositoryNameLabel.setText(data.getName());
    }
}
