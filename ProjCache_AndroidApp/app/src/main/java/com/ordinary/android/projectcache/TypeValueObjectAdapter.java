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

public class TypeValueObjectAdapter
        extends RecyclerView.Adapter<TypeValueObjectAdapter.ViewHolder> {

    private Context context;
    private List<TypeValueObjectModel> typeValueObjectModelList;

    public TypeValueObjectAdapter(Context context, List<TypeValueObjectModel> modelList)
    {
        this.context = context;
        typeValueObjectModelList = modelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_type_value,viewGroup,false);
        ViewHolder typeValueViewHolder = new ViewHolder(v,context);
        return typeValueViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TypeValueObjectModel curItem = typeValueObjectModelList.get(i);
        viewHolder.typeNameTextView.setText(curItem.getTypename());
        viewHolder.typeValueTextView.setText(curItem.getValues());
        viewHolder.objectImageView.setImageDrawable(curItem.getIcon());
    }

    @Override
    public int getItemCount() {
        return typeValueObjectModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutInflater inflater;
        private List<TypeValueObjectModel> List;
        private TextView typeNameTextView, typeValueTextView;
        private ImageView objectImageView;

        private ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            typeNameTextView = itemView.findViewById(R.id.selected_type);
            typeValueTextView = itemView.findViewById(R.id.selected_value);
            objectImageView = itemView.findViewById(R.id.iconImage);
        }
    }
}
