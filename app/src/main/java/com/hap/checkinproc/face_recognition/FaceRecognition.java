package com.hap.checkinproc.face_recognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.face_recognition.image.ImageClassificationActivity;
import com.hap.checkinproc.face_recognition.object.FaceRecognitionActivity;

import java.util.ArrayList;
import java.util.List;

public class FaceRecognition extends AppCompatActivity implements AlgoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition_view);
        ArrayList<Algo> arrayList = new ArrayList<>();
        arrayList.add(new Algo(R.drawable.baseline_portrait_black_48, "Face recognition", FaceRecognitionActivity.class));
        AlgoAdapter algoAdapter = new AlgoAdapter(arrayList, this);
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setAdapter(algoAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public void onAlgoSelected(Algo algo) {
        Intent intent = new Intent(this, algo.activityClazz);
        intent.putExtra("name", algo.algoText);
        startActivity(intent);
    }
}

class AlgoAdapter extends RecyclerView.Adapter<AlgoViewHolder> {

    private List<Algo> algoList;
    private AlgoListener algoListener;

    public AlgoAdapter(List<Algo> algoList, AlgoListener listener) {
        this.algoList = algoList;
        this.algoListener = listener;
    }

    @NonNull
    @Override
    public AlgoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icons, parent, false);
        return new AlgoViewHolder(view, algoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlgoViewHolder holder, int position) {
        holder.bind(algoList.get(position));
    }

    @Override
    public int getItemCount() {
        return algoList.size();
    }
}

class AlgoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView iconImageView;
    private TextView algoTextView;
    private AlgoListener algoListener;
    private Algo algo;

    public AlgoViewHolder(@NonNull View itemView, AlgoListener algoListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.algoListener = algoListener;

        iconImageView = itemView.findViewById(R.id.iconImageView);
        algoTextView = itemView.findViewById(R.id.algoTextView);
    }

    public void bind(Algo algo) {
        this.algo = algo;
        iconImageView.setImageResource(algo.iconResourceId);
        algoTextView.setText(algo.algoText);
    }

    @Override
    public void onClick(View v) {
        if (algoListener != null) {
            algoListener.onAlgoSelected(algo);
        }
    }
}

class Algo<T extends ImageClassificationActivity> {
    public int iconResourceId = R.drawable.ic_launcher_background;
    public String algoText = "";
    public Class<T> activityClazz;

    public Algo(int iconResourceId, String algoText, Class<T> activityClazz) {
        this.iconResourceId = iconResourceId;
        this.algoText = algoText;
        this.activityClazz = activityClazz;
    }
}

interface AlgoListener {
    void onAlgoSelected(Algo algo);
}