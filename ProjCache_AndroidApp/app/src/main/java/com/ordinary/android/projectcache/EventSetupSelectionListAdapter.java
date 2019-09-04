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

public class EventSetupSelectionListAdapter
        extends RecyclerView.Adapter<EventSetupSelectionListAdapter.ViewHolder> {

    private Context context;
    private List<TypeObjectModel> typeObjectModelList;
    private EventSetupSelectionListAdapter.mOnItemClickListener listener;

    public EventSetupSelectionListAdapter(Context context, List<TypeObjectModel> modelList, EventSetupSelectionListAdapter.mOnItemClickListener listener) {
        this.context = context;
        this.typeObjectModelList = modelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventSetupSelectionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_condition_action_options, viewGroup, false);
        EventSetupSelectionListAdapter.ViewHolder typeValueViewHolder = new EventSetupSelectionListAdapter.ViewHolder(v, context, listener);
        return typeValueViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventSetupSelectionListAdapter.ViewHolder viewHolder, int i) {
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
        private EventSetupSelectionListAdapter.mOnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, Context context, EventSetupSelectionListAdapter.mOnItemClickListener mOnItemClickListener) {
            super(itemView);

            typeNameTextView = itemView.findViewById(R.id.option_types);
            objectImageView = itemView.findViewById(R.id.options_iconImage);
            this.mOnItemClickListener = mOnItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
