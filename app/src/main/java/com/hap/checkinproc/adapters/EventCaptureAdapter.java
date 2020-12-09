package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.UpdateTaskActivity;
import com.hap.checkinproc.Interface.EventCaptureInterface;
import com.hap.checkinproc.Model_Class.EventCapture;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class EventCaptureAdapter extends RecyclerView.Adapter<EventCaptureAdapter.ViewHolder> {
    List<EventCapture> eventCapture;
    EventCapture evC;
    Context mContext;
    ArrayList<Uri> uriArrayList;
    EventCaptureInterface eventCaptureInterface;
    int post;


    public EventCaptureAdapter(List<EventCapture> eventCapture, Context mContext) {
        this.eventCapture = eventCapture;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_event_capture, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageSet.setImageURI(Uri.parse(eventCapture.get(position).getTask()));
        holder.txtTitle.setText(eventCapture.get(position).getDesc());
        holder.txtRemarks.setText(eventCapture.get(position).getFinishBy());




    }

    @Override
    public int getItemCount() {
        return eventCapture.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageSet, imageDelete;
        TextView txtTitle;
        TextView txtRemarks;
        LinearLayout editLinear, deleteLinear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSet = itemView.findViewById(R.id.image_captured);
            txtTitle = itemView.findViewById(R.id.edt_title);
            txtRemarks = itemView.findViewById(R.id.edt_remarks);
            imageDelete = itemView.findViewById(R.id.image_delete);
            editLinear = itemView.findViewById(R.id.linear_edt);
            deleteLinear = itemView.findViewById(R.id.linear_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventCapture task = eventCapture.get(getAdapterPosition());
            Intent intent = new Intent(mContext, UpdateTaskActivity.class);
            intent.putExtra("task", task);
            mContext.startActivity(intent);

        }
    }
}
