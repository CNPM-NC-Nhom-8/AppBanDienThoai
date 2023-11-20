package com.example.appbandienthoai.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbandienthoai.Activity.ChiTietSPActivity;
import com.example.appbandienthoai.Class.DienThoaiMoi;
import com.example.appbandienthoai.R;

import java.text.DecimalFormat;
import java.util.List;

public class DienThoaiAdapter extends RecyclerView.Adapter<DienThoaiAdapter.MyViewHolder> {
    List<DienThoaiMoi> array;
    Context context;

    public DienThoaiAdapter(Context context, List<DienThoaiMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sp_moi, parent, false);
        return new DienThoaiAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DienThoaiMoi sanPhamMoi = array.get(position);

        holder.tensp.setText(sanPhamMoi.getTENSP());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        if (sanPhamMoi.getSOLUONG() <= 0) {
            holder.giatien.setText("TẠM HẾT HÀNG");
            holder.giatien.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.giatien.setText("Giá: " + decimalFormat.format(Double.parseDouble(String.valueOf(sanPhamMoi.getDONGIA()))) + " VNĐ");
            holder.giatien.setTextColor(Color.parseColor("#000000"));
        }
        Glide.with(context).load(sanPhamMoi.getHINHANH()).into(holder.imgHinhanh);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChiTietSPActivity.class);
                i.putExtra("chitiet", sanPhamMoi);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView giatien, tensp;
        ImageView imgHinhanh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            giatien = itemView.findViewById(R.id.tvPrice);
            tensp = itemView.findViewById(R.id.tvTittle);
            imgHinhanh = itemView.findViewById(R.id.imgItemSp);
        }
    }
}
