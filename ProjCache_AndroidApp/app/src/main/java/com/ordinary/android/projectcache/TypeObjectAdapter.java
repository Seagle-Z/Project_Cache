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
    private List<Boolean> booleanList;

    public TypeObjectAdapter(Context context, List<TypeObjectModel> modelList, intentResultCollectingInterface listener) {
        this.context = context;
        this.typeObjectModelList = modelList;
        this.listener = listener;
    }

    public TypeObjectAdapter(Context context, List<TypeObjectModel> modelList, intentResultCollectingInterface listener, List<Boolean> booleanList)
    {
        this.context = context;
        this.typeObjectModelList = modelList;
        this.listener = listener;
        this.booleanList = booleanList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_type_model, viewGroup, false);
        ViewHolder typeViewHolder;
        if(booleanList == null)
             typeViewHolder = new ViewHolder(v, context, listener);
        else
             typeViewHolder = new ViewHolder(v, context, listener, true);
        return typeViewHolder;
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

        public ViewHolder(@NonNull View itemView, final Context context, intentResultCollectingInterface inter, Boolean bool) {
            super(itemView);

            typeNameTextView = itemView.findViewById(R.id.selected_type);
            objectImageView = itemView.findViewById(R.id.iconImage);
            removeImageView = itemView.findViewById(R.id.typeModel_remove);
            this.mInter = inter;

            removeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateActivatedHourList(typeObjectModelList.get(getAdapterPosition()).getTypename(), false);
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

        public void updateActivatedHourList(String s, boolean flag) {

            if (s.contains("-")) {
                String[] timeRangeDivider = s.split("-");
                String tempBeginTime = timeRangeDivider[0].trim();
                String tempEndTime = timeRangeDivider[1].trim();
                String[] _tempBeginHour = tempBeginTime.split(":");
                String[] _tempEndhour = tempEndTime.split(":");
                int beginHour = Integer.parseInt(_tempBeginHour[0].trim());
                int beginMinute = Integer.parseInt(_tempBeginHour[1].trim());
                int timeSlot1 = beginHour * 60 + beginMinute;
                int endHour = Integer.parseInt(_tempEndhour[0].trim());
                int endMinute = Integer.parseInt(_tempEndhour[1].trim());
                int timeSlot2 = endHour * 60 + endMinute;

                setTimeRangeBoolean(timeSlot1, timeSlot2, flag);
            } else {
                String[] time = s.split(":");
                int timeslot = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]) % 60;
                booleanList.set(timeslot, flag);
            }
        }

        public void setTimeRangeBoolean(int timeSlot1, int timeSlot2, boolean flag) {
            for (int i = timeSlot1; i <= timeSlot2; i++) {
                booleanList.set(i, flag);
            }
        }
    }
}

