package com.example.englishlearningapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.NguPhap;
import com.example.englishlearningapp.R;

import java.util.List;

public class NguPhapAdapter extends RecyclerView.Adapter<NguPhapAdapter.ViewHolder> {

    private Context context;
    private List<NguPhap> listNguPhap;

    public NguPhapAdapter(Context context, List<NguPhap> listNguPhap) {
        this.context = context;
        this.listNguPhap = listNguPhap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_ngu_phap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NguPhap np = listNguPhap.get(position);
        holder.tvTen.setText(np.getTenNguPhap());
        holder.tvGiaiThich.setText(np.getGiaiThich());
        holder.tvViDu.setText("Ví dụ: " + np.getViDu());
    }

    @Override
    public int getItemCount() {
        return (listNguPhap != null) ? listNguPhap.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvGiaiThich, tvViDu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tv_ten_ngu_phap);
            tvGiaiThich = itemView.findViewById(R.id.tv_giai_thich_chi_tiet);
            tvViDu = itemView.findViewById(R.id.tv_vi_du);
        }
    }
}