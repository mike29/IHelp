package com.example.michaelmsimon.ihelpseven;

/**
 * Created by Michael M. Simon on 28.11.2017.
 */

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapterApplicants extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private ArrayList<DataModel> dataSet;
    Context mContext;


    // View lookup cache
    private static class ViewHolder {
        TextView applicant_name;
        TextView date;
        ImageButton accept_application;
        TextView distance_number;
        TextView taskID;
    }

    public CustomAdapterApplicants(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.applicants_list, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModels = (DataModel) object;
        switch (v.getId()) {
            case R.id.accept_applications_confirm:
                Toast.makeText(this.mContext, "Task detail", Toast.LENGTH_SHORT).show();
                Log.d("from custom", "--------------- ");
                //  Snackbar.make(v, "Release date " +dataModel.getAName(), Snackbar.LENGTH_LONG)
                //         .setAction("No action", null).show();
                break;
        }

    }

    private int lastPosition = -1;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);

        ViewHolder viewHolder;
        DataModel dataModel = (DataModel) getItem(position);


        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.applicants_list, parent, false);
            viewHolder.applicant_name = (TextView) convertView.findViewById(R.id.applicant_name);
            viewHolder.distance_number = (TextView) convertView.findViewById(R.id.distance_number);
            viewHolder.taskID = (TextView) convertView.findViewById(R.id.task_id);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            //  viewHolder.accept_application = (ImageButton) convertView.findViewById(R.id.accept_application);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.applicant_name.setText(dataModel.getAName());
        viewHolder.distance_number.setText(dataModel.getDistance());
        viewHolder.date.setText(dataModel.getDate());
        //The task ID hidden;)
        //viewHolder.taskID.setText(dataModel.getTaskID());
//        viewHolder.accept_application.setOnClickListener(this);
        //     viewHolder.accept_application.setTag(position);
        return convertView;
    }
}




