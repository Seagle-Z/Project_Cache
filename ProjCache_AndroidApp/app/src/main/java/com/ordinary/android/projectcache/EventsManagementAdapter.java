package com.ordinary.android.projectcache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class EventsManagementAdapter
        extends RecyclerView.Adapter<EventsManagementAdapter.ViewHolder> {

    Context context;
    private List<Event> EventsManagementList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public Switch activationSwitch;
        public ImageView eventImageView;

        private static final String EVENTS_FILE_NAME = "events.csv";

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.eventName_textView);
            descriptionTextView = itemView.findViewById(R.id.eventDescription_TextView);
            activationSwitch = itemView.findViewById(R.id.eventActivation_switch);
            eventImageView = itemView.findViewById(R.id.eventImage_imageView);

            File eventsFile = new File(context.getFilesDir(), EVENTS_FILE_NAME);
            Events events = new Events(context, eventsFile);

            activationSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activationSwitch.isChecked()) {

                    }
                }
            });
        }
    }

    public EventsManagementAdapter(Context context, List<Event> EventsManagementList) {
        this.context = context;
        this.EventsManagementList = EventsManagementList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_management_item, viewGroup, false);
        ViewHolder emvh = new ViewHolder(v, context);
        return emvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Event curItem = EventsManagementList.get(i);
        viewHolder.nameTextView.setText(curItem.eventName);
        viewHolder.descriptionTextView.setText(curItem.eventDescription);
        viewHolder.activationSwitch.setChecked(curItem.isActivated);
        viewHolder.eventImageView.setImageResource(R.drawable.ic_menu_send/*curItem.eventImage*/);

    }

    @Override
    public int getItemCount() {
        return EventsManagementList.size();
    }
}
