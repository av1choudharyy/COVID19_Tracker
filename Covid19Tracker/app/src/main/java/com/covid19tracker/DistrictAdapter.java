package com.covid19tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> {

    private List districts;

    DistrictAdapter(List districts) {
        this.districts = districts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(districts.get(position));

        Districts du = (Districts) districts.get(position);

        holder.name.setText(du.getName());
        holder.confirmed.setText(String.valueOf(du.getConfirmed()));

    }

    @Override
    public int getItemCount() {
        return districts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        TextView confirmed;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView17);
            confirmed = itemView.findViewById(R.id.textView20);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Districts districts1 = (Districts) view.getTag();

                    Toast.makeText(view.getContext(), districts1.getName() + " " + districts1.getConfirmed(), Toast.LENGTH_SHORT).show();


                }
            });

        }
    }

}