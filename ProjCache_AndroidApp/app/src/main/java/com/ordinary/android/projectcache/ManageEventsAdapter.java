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
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class ManageEventsAdapter
        extends RecyclerView.Adapter<ManageEventsAdapter.ViewHolder> {

    Context context;
    private List<Event> eventsManagementList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public Switch activationSwitch;
        public ImageView eventImageView;
        private final int MODIFY_REUQEST_CODE = 1010;

        public ViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.eventName_textView);
            descriptionTextView = itemView.findViewById(R.id.eventDescription_TextView);
            activationSwitch = itemView.findViewById(R.id.eventActivation_switch);
            eventImageView = itemView.findViewById(R.id.eventImage_imageView);

            Events events = new Events(context);
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

            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startEventSetup =
                            new Intent(contextConstant, EventSetupActivity.class);
                    startEventSetup.putExtra(
                            "MODIFY_EVENT", nameTextView.getText().toString());
                    ((Activity)context).startActivity(
                            startEventSetup);
                }
            });
        }
    }

    public ManageEventsAdapter(Context context, List<Event> EventsManagementList) {
        this.context = context;
        this.eventsManagementList = EventsManagementList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_management_item, viewGroup, false);
        ViewHolder emvh = new ViewHolder(v, context);
        return emvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final ManageEventModel item = new ManageEventModel(eventsManagementList.get(i));
        viewHolder.nameTextView.setText(item.getEventName());
        viewHolder.descriptionTextView.setText(item.getEventDescription());
        viewHolder.activationSwitch.setChecked(item.eventIsActivated());
        viewHolder.eventImageView.setImageResource(R.drawable.icon_event_default/*item.getEventImage()*/);

        viewHolder.eventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelected()) {
                    item.setSelected(true);
                    viewHolder.eventImageView.setImageResource(R.drawable.icon_check);
                } else {
                    item.setSelected(false);
                    viewHolder.eventImageView.setImageResource(R.drawable.icon_event_default);
                }
                viewHolder.itemView.setBackgroundColor(item.isSelected() ? 0xffb2ebf2 : Color.WHITE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsManagementList.size();
    }


}
