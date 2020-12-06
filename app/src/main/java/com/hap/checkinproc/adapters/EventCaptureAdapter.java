package com.hap.checkinproc.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Model_Class.EventCapture;
import com.hap.checkinproc.R;

import java.util.ArrayList;

public class EventCaptureAdapter extends RecyclerView.Adapter<EventCaptureAdapter.ViewHolder> {
    ArrayList<EventCapture> eventCapture;
    EventCapture evC;
    Context mContext;
    ArrayList<Uri> uriArrayList;


    public EventCaptureAdapter(ArrayList<EventCapture> eventCapture, Context mContext) {
        this.eventCapture = eventCapture;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_event_capture, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.imageSet.setImageURI(eventCapture.get(position).getImageUri());
    }

    @Override
    public int getItemCount() {
        return eventCapture.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSet = itemView.findViewById(R.id.image_captured);

        }
    }
}
