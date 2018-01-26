package com.example.michaelmsimon.ihelpseven;

import com.example.michaelmsimon.ihelpseven.DataModelApplicants;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Michael M. Simon on 29.11.2017.
 */

public class CustomAdapterToAcceptAplicant extends ArrayAdapter<DataModel> implements View.OnClickListener {
    private ArrayList<DataModel> dataSet;
    Context mContext;

    //for the applican list
    private static class ViewHolder {
        TextView FName;
        TextView LName;
        ImageButton accept;
        TextView user_ID;
    }

    public CustomAdapterToAcceptAplicant(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.applicants_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;
        switch (v.getId()) {
            case R.id.applicant_name_tobe_accepted:
                Toast.makeText(this.mContext, "Task detail", Toast.LENGTH_SHORT).show();

                break;
        }

    }

    private int lastPosition = -1;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //  DataModelApplicants dataModels = (DataModelApplicants) getItem(position);
        DataModel dataModels = (DataModel) getItem(position);


        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.applicants_row, parent, false);
            viewHolder.FName = (TextView) convertView.findViewById(R.id.applicant_name_tobe_accepted);
            viewHolder.user_ID = (TextView) convertView.findViewById(R.id.uid);
            viewHolder.accept = (ImageButton) convertView.findViewById(R.id.accept_applications_confirm);
            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.FName.setText(dataModels.getApplicantName());
        viewHolder.user_ID.setText(dataModels.getUserID());

        //  viewHolder.accept.setOnClickListener(onClick(););

        return convertView;

    }
}
