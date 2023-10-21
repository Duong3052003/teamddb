package com.example.teamddb.adap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamddb.R;
import com.example.teamddb.model.acc;

import java.util.List;

public class adap_acc extends RecyclerView.Adapter<adap_acc.MyViewHolder> {

   private Context context;
   private List<acc> a;

    public adap_acc(Context context, List<acc> a) {
        this.context = context;
        this.a = a;
    }

    @NonNull
    @Override
    public adap_acc.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.acc_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull adap_acc.MyViewHolder holder, int position) {
        acc ac = a.get(position);
        holder.ed1.setText(String.valueOf(ac.getId()));
        holder.ed2.setText(ac.getAccName());
        holder.ed3.setText(ac.getPassword());
        holder.ed4.setText(ac.getEmail());
        holder.ed5.setText(ac.getName());
    }

    @Override
    public int getItemCount() {
        return a.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ed1, ed2,ed3,ed4,ed5;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ed1=itemView.findViewById(R.id.textView);
            ed2=itemView.findViewById(R.id.textView2);
            ed3=itemView.findViewById(R.id.textView3);
            ed4=itemView.findViewById(R.id.textView4);
            ed5=itemView.findViewById(R.id.textView5);

        }

    }
}
