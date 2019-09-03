package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TypeObjectAdapter
        extends RecyclerView.Adapter<TypeObjectAdapter.ViewHolder> {

    private final int REQUEST_INFORMATION_CODE = 1010;
    private final String GPS = "GPS Location";
    private final String BT = "Bluetooth";
    private final String WIFI = "Wi-Fi Connection";
    private final String Time = "Time";
    private final String OS_APP = "On-Screen App";
    private View adapterView;
    private Context context;
    private List<TypeObjectModel> typeObjectModelList;
    private mOnItemClickListener listener;

    public TypeObjectAdapter(Context context, List<TypeObjectModel> modelList, mOnItemClickListener listener) {
        this.context = context;
        this.typeObjectModelList = modelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_type_model, viewGroup, false);
        ViewHolder typeValueViewHolder = new ViewHolder(v, context, listener);
        return typeValueViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TypeObjectModel curItem = typeObjectModelList.get(i);
        viewHolder.typeNameTextView.setText(curItem.getTypename());
        viewHolder.objectImageView.setImageDrawable(curItem.getIcon());
    }

    @Override
    public int getItemCount() {
        return typeObjectModelList.size();
    }

    public interface mOnItemClickListener {
        void onItemClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView typeNameTextView;
        private ImageView objectImageView;
        private mOnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, Context context, mOnItemClickListener mOnItemClickListener) {
            super(itemView);
            adapterView = itemView;
            typeNameTextView = itemView.findViewById(R.id.selected_type);
            objectImageView = itemView.findViewById(R.id.iconImage);
            this.mOnItemClickListener = mOnItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}

