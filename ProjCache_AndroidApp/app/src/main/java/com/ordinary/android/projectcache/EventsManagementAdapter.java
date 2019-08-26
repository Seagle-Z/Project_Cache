package com.ordinary.android.projectcache;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class EventsManagementAdapter
        extends RecyclerView.Adapter<EventsManagementAdapter.EventsManagementViewHolder> {

    private List<EventManagementModel> EventsManagementList;

    public static class EventsManagementViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public Switch activationSwitch;
        public ImageView eventImageView;

        public EventsManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.eventName_textView);
            descriptionTextView = itemView.findViewById(R.id.eventDescription_TextView);
            activationSwitch = itemView.findViewById(R.id.eventActivation_switch);
            eventImageView = itemView.findViewById(R.id.eventImage_imageView);
        }
    }

    public EventsManagementAdapter(List<EventManagementModel> EventsManagementList) {
        this.EventsManagementList = EventsManagementList;
    }

    @NonNull
    @Override
    public EventsManagementViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_management_item, viewGroup, false);
        EventsManagementViewHolder emvh = new EventsManagementViewHolder(v);
        return emvh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsManagementViewHolder eventsManagementViewHolder, int i) {

        EventManagementModel curItem = EventsManagementList.get(i);
        eventsManagementViewHolder.nameTextView.setText(curItem.getEventName());
        eventsManagementViewHolder.descriptionTextView.setText(curItem.getEventDescription());
        eventsManagementViewHolder.activationSwitch.setChecked(curItem.getEventActivated());
        eventsManagementViewHolder.eventImageView.setImageResource(curItem.getEventImage());
    }

    @Override
    public int getItemCount() {
        return EventsManagementList.size();
    }
}
