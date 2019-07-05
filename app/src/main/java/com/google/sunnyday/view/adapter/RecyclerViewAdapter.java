package com.google.sunnyday.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.sunnyday.R;
import com.google.sunnyday.service.model.City;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();
    private ArrayList<City> cities= new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<City> passedCities) {
        mContext = context;
        cities = passedCities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final City city = cities.get(position);
//        Glide.with(mContext)
//                .asGif()
//                .load(model.imageURL)
//                .into(viewHolder.imageView);

//        holder.title.setText(city.getName());
//
//        holder.cell_r_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, city.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        RelativeLayout cell_r_layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            title = (TextView) itemView.findViewById(R.id.title_textview);
            cell_r_layout = itemView.findViewById(R.id.cell_layout);
        }
    }
}
