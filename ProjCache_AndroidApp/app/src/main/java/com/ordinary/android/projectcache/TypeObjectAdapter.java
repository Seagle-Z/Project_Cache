package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TypeObjectAdapter
        extends RecyclerView.Adapter<TypeObjectAdapter.ViewHolder> {

    private Context context;
    private List<TypeObjectModel> typeObjectModelList;
    private intentResultCollectingInterface listener;

    public TypeObjectAdapter(Context context, List<TypeObjectModel> modelList, intentResultCollectingInterface listener) {
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

    public interface intentResultCollectingInterface {
        void getIntent(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView typeNameTextView;
        private ImageView objectImageView;
        private ImageView removeImageView;
        private intentResultCollectingInterface mInter;

        public ViewHolder(@NonNull View itemView, final Context context, intentResultCollectingInterface inter) {
            super(itemView);

            typeNameTextView = itemView.findViewById(R.id.selected_type);
            objectImageView = itemView.findViewById(R.id.iconImage);
            removeImageView = itemView.findViewById(R.id.typeModel_remove);
            this.mInter = inter;

            removeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    typeObjectModelList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    Toast.makeText(
                            context,
                            "Deleted",
                            Toast.LENGTH_SHORT).show();

                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mInter.getIntent(getAdapterPosition());
        }
    }
}

