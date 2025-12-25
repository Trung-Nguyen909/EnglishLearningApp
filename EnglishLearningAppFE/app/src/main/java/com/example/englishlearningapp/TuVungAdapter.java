package com.example.englishlearningapp;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.TuVungResponse;

import java.io.IOException;
import java.util.List;

public class TuVungAdapter extends RecyclerView.Adapter<TuVungAdapter.VH> {
    private Context context;
    private List<TuVungResponse> list;
    private String audioBaseUrl; // nếu API trả filename thì cần base url

    private MediaPlayer mediaPlayer;

    public TuVungAdapter(Context ctx, List<TuVungResponse> data, String audioBaseUrl) {
        this.context = ctx;
        this.list = data;
        this.audioBaseUrl = audioBaseUrl;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tuvung, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        TuVungResponse t = list.get(position);
        holder.tvWord.setText(t.getTuTiengAnh());
        holder.tvMeaning.setText(t.getNghiaTiengViet());
        holder.tvType.setText(t.getPhienAm() != null ? t.getPhienAm() : ""); // nếu có loại từ -> set
        holder.tvExample.setText(t.getViDu() != null ? t.getViDu() : "");

        holder.btnPlay.setOnClickListener(v -> {
            String audio = t.getAmThanhPhienAm();
            if (audio == null || audio.isEmpty()) {
                // không có audio
                return;
            }
            String url = audio;
            // nếu API chỉ trả filename, kết hợp với base:
            if (!audio.startsWith("http")) url = audioBaseUrl + audio;
            playAudio(url);
        });
    }

    private void playAudio(String url) {
        releaseMedia();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> releaseMedia());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMedia() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void cleanup() {
        releaseMedia();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvWord, tvMeaning, tvExample, tvPhienAm, tvType;
        ImageButton btnPlay;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvExample = itemView.findViewById(R.id.tvExample);
            tvType = itemView.findViewById(R.id.tvType);
            btnPlay = itemView.findViewById(R.id.btnPlay);
        }
    }
}
