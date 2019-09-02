package com.ordinary.android.projectcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Map;
public class TypeValueObjectAdapter
        extends RecyclerView.Adapter<TypeValueObjectAdapter.ViewHolder> {

    private Context context;
    private List<TypeValueObjectModel> typeValueObjectModelList;
    private Map<String, String> conditions;
    private intentResultCollectingInterface inter;

    public TypeValueObjectAdapter(Context context, List<TypeValueObjectModel> modelList,
                                  Map<String, String> c, intentResultCollectingInterface inter) {
        this.context = context;
        typeValueObjectModelList = modelList;
        conditions = c;
        this.inter = inter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_type_value, viewGroup, false);
        ViewHolder typeValueViewHolder = new ViewHolder(v, context, inter);
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

    public interface intentResultCollectingInterface
    {
        void getIntent(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView typeNameTextView, typeValueTextView;
        private ImageView objectImageView;
        private ImageView removeImageView;
        private intentResultCollectingInterface mInter;

        public ViewHolder(@NonNull View itemView, final Context contextm, intentResultCollectingInterface inter) {
            super(itemView);
            typeNameTextView = itemView.findViewById(R.id.selected_type);
            typeValueTextView = itemView.findViewById(R.id.selected_value);
            objectImageView = itemView.findViewById(R.id.iconImage);
            removeImageView = itemView.findViewById(R.id.imageView_remove);
            this.mInter = inter;

            removeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    adb.setTitle("Delete");
                    adb.setNegativeButton("No no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,
                                    "Cancelled",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            conditions.remove(typeValueObjectModelList.get(
                                    getAdapterPosition()).getTypename().toUpperCase());
                            typeValueObjectModelList.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            Toast.makeText(
                                    context,
                                    "Deleted",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    adb.show();
                }
            });

            RelativeLayout layout = (RelativeLayout) itemView.findViewById(R.id.typeValue_Relative);
            layout.setOnClickListener(this);

//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = getIntent(getAdapterPosition());
//                    ((Activity) context).startActivityForResult(intent, 1010);
//                    notifyDataSetChanged();
//
//                }
//            });
        }
        @Override
        public void onClick(View v) {
            mInter.getIntent(getAdapterPosition());
        }
    }
}
