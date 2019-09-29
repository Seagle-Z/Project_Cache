package com.ordinary.android.projectcache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ManageEventsAdapter
        extends RecyclerView.Adapter<ManageEventsAdapter.ViewHolder> {

    private Context context;
    private List<Event> eventsManagementList;
    private List<Integer> selectedList;
    private mOnItemClickListener OnItemClickListener;
    private ManageEventModel item;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Integer curEventID;
        public TextView nameTextView;
        public TextView descriptionTextView;
        public Switch activationSwitch;
        public ImageView eventImageView;

        public RelativeLayout infoLayout;
        private mOnItemClickListener mListener;
        private final int MODIFY_REUQEST_CODE = 1010;

        public ViewHolder(@NonNull View itemView, final Context context, mOnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.eventName_textView);
            descriptionTextView = itemView.findViewById(R.id.eventDescription_TextView);
            activationSwitch = itemView.findViewById(R.id.eventActivation_switch);
            eventImageView = itemView.findViewById(R.id.eventImage_imageView);
            this.mListener = listener;

            infoLayout = itemView.findViewById(R.id.eventInfo_relativeLayout);

            final Context contextConstant = context;

            activationSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activationSwitch.isChecked()) {
                        Events events = new Events(contextConstant);
                        events.updateEventActivationStatus(nameTextView.getText().toString(), true);
                    } else {
                        Events events = new Events(contextConstant);
                        events.updateEventActivationStatus(nameTextView.getText().toString(), false);
                    }
                }
            });

            infoLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(getAdapterPosition(), curEventID);
        }
    }

    public ManageEventsAdapter(Context context, List<Event> EventsManagementList, List<Integer> selectedList, mOnItemClickListener clickListener) {
        this.context = context;
        this.eventsManagementList = EventsManagementList;
        this.selectedList = selectedList;
        this.OnItemClickListener = clickListener;

    }

    public interface mOnItemClickListener
    {
        void onItemClick(int position, int key);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.event_management_item, viewGroup, false);
        ViewHolder emvh = new ViewHolder(v, context, OnItemClickListener);
        return emvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        ToolFunctions TF = new ToolFunctions();

        item = new ManageEventModel(eventsManagementList.get(i));
        viewHolder.curEventID = item.getEventID();
        viewHolder.nameTextView.setText(TF.textDecoder(item.getEventName()));
        viewHolder.descriptionTextView.setText(TF.textDecoder(item.getEventDescription()));
        viewHolder.activationSwitch.setChecked(item.eventIsActivated());
        if (!selectedList.contains(item.getEventID())) {
            viewHolder.eventImageView.setImageResource(R.drawable.icon_event_default/*item.getEventImage()*/);
        } else {
            viewHolder.eventImageView.setImageResource(R.drawable.icon_selected);
        }
        viewHolder.itemView.setBackgroundColor(
                selectedList.contains(item.getEventID()) ? 0xffb3e5fc : Color.WHITE);

        viewHolder.eventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedList.contains(viewHolder.curEventID)) {
                    selectedList.add(viewHolder.curEventID);
                    viewHolder.eventImageView.setImageResource(R.drawable.icon_selected);
                } else {
                    selectedList.remove(Integer.valueOf(viewHolder.curEventID));
                    viewHolder.eventImageView.setImageResource(R.drawable.icon_event_default);
                }
                viewHolder.itemView.setBackgroundColor(
                        selectedList.contains(viewHolder.curEventID) ? 0xffb3e5fc : Color.WHITE);
            }
        });

        viewHolder.infoLayout.setLongClickable(true);
        viewHolder.infoLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!selectedList.contains(viewHolder.curEventID)) {
                    selectedList.add(viewHolder.curEventID);
                    viewHolder.eventImageView.setImageResource(R.drawable.icon_selected);
                } else {
                    selectedList.remove(Integer.valueOf(viewHolder.curEventID));
                    viewHolder.eventImageView.setImageResource(R.drawable.icon_event_default);
                }
                viewHolder.itemView.setBackgroundColor(
                        selectedList.contains(viewHolder.curEventID) ? 0xffb3e5fc : Color.WHITE);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsManagementList.size();
    }


}




// sort eventsList by eventName in ascending order
//        Collections.sort(eventsList, new Comparator<Event>() {
//@Override
//public int compare(Event e1, Event e2) {
//        return e1.eventName.compareTo(e2.eventName);
//        }
//        });