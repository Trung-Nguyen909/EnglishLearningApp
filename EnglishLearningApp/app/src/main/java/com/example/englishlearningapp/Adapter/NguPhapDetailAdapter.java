package com.example.englishlearningapp.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.NguPhap;
import com.example.englishlearningapp.R;
import java.util.List;

public class NguPhapDetailAdapter extends RecyclerView.Adapter<NguPhapDetailAdapter.ViewHolder> {

    private Context context;
    private List<NguPhap> listNguPhap;

    // Biến lưu tên bài học chung cho cả danh sách (nếu muốn truyền từ Activity)
    private String tenBaiHocChung;

    // Constructor cập nhật: Nhận thêm tenBaiHoc
    public NguPhapDetailAdapter(Context context, List<NguPhap> listNguPhap, String tenBaiHoc) {
        this.context = context;
        this.listNguPhap = listNguPhap;
        this.tenBaiHocChung = tenBaiHoc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ngu_phap_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NguPhap np = listNguPhap.get(position);

        holder.tvTen.setText(np.getTenNguPhap());
        holder.tvGiaiThich.setText(np.getGiaiThich());

        // --- XỬ LÝ TÊN BÀI HỌC (Nếu bạn muốn hiển thị nó trong item) ---
        // Ưu tiên 1: Lấy từ Model (nếu API có trả về)
        String tenBai = np.getTenBaiHoc();

        // Ưu tiên 2: Nếu Model null thì lấy từ biến chung truyền vào Constructor
        if (tenBai == null || tenBai.isEmpty()) {
            tenBai = tenBaiHocChung;
        }

        // Ví dụ: Hiển thị tên bài học (Bạn cần thêm TextView tvTenBaiHoc vào item_ngu_phap_detail.xml trước)
        // if (holder.tvTenBaiHoc != null) {
        //     holder.tvTenBaiHoc.setText(tenBai);
        // }

        // Xử lý ví dụ...
        holder.layoutVidu.removeAllViews();
        if (np.getViDu() != null && !np.getViDu().isEmpty()) {
            String[] dsVidu = np.getViDu().split("\n");
            for (String vd : dsVidu) {
                themDongViDu(holder.layoutVidu, vd);
            }
        }
    }

    // ... (Giữ nguyên các hàm themDongViDu và getItemCount) ...
    private void themDongViDu(LinearLayout container, String noiDung) {
        TextView tv = new TextView(context);
        tv.setTextSize(15);
        tv.setTextColor(context.getResources().getColor(R.color.black));
        tv.setPadding(0, 8, 0, 8);
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_outline, 0, 0, 0);
        tv.setCompoundDrawablePadding(16);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(noiDung, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(noiDung));
        }
        container.addView(tv);
    }

    @Override
    public int getItemCount() {
        return (listNguPhap != null) ? listNguPhap.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvGiaiThich, tvCongThuc;
        // TextView tvTenBaiHoc; // Nếu layout có thêm cái này
        LinearLayout layoutVidu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tv_ten_ngu_phap_item);
            tvGiaiThich = itemView.findViewById(R.id.tv_giai_thich_item);
            // tvTenBaiHoc = itemView.findViewById(R.id.tv_ten_bai_hoc_item);
            layoutVidu = itemView.findViewById(R.id.layout_vidu_container);
        }
    }
}