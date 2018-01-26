package com.example.michaelmsimon.ihelpseven;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Michael M. Simon on 13.11.2017.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder{


    // VIEWHOLDER


        View mView;
        public TaskViewHolder(View itemView){
            super(itemView);
            mView = itemView;

        }


        public void setLocCity(String taskLoc){
            TextView post_loc = (TextView) mView.findViewById(R.id.tvTaskLocation);
            post_loc.setText(taskLoc);
        }

        public void setTitle(String taskTitle){
            TextView post_title = (TextView) mView.findViewById(R.id.tvTaskTitle);
            String TitleToUC = taskTitle.toUpperCase();
            post_title.setText(TitleToUC);
        }

        public void setCat(String taskCat){
            TextView post_category= (TextView) mView.findViewById(R.id.tvTaskCategory);
            post_category.setText(taskCat);
        }

        public void setPostedDate(String posedDate){
            TextView post_category= (TextView) mView.findViewById(R.id.tvTaskPostedDate);
            post_category.setText(posedDate);
        }
        public void setDesc(String taskDesc){
            TextView post_category= (TextView) mView.findViewById(R.id.tvTaskDisc);
            post_category.setText(taskDesc);
        }
        public void setPostedBy(String taskPostedBy){
            TextView task_posted_by= (TextView) mView.findViewById(R.id.tvTaskPostedBy);
            task_posted_by.setText(taskPostedBy);
        }
        public void setPostedByUserKey(String taskPostedByUserKey){
            TextView task_posted_by= (TextView) mView.findViewById(R.id.userKeyHolder);
            task_posted_by.setText(taskPostedByUserKey);
        }

    }



