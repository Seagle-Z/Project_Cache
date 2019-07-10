package com.ordinary.projectcache.projectcache;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Page1Fragment extends Fragment {
    private final String TAG = "Page1Fragment";
    ViewPager viewPager;
    Button addCondition;
    FloatingActionButton forward;
    private final int REQUEST_APP_LIST_CODE = 1001;
    private String triggerCondition = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_setup_page_1, container, false);
        forward = (FloatingActionButton) view.findViewById(R.id.page1Foward);
        viewPager = (ViewPager) getActivity().findViewById(R.id.setup_viewPager);

        addCondition = (Button) view.findViewById(R.id.add_condition);
        addCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent applistActivity = new Intent(getContext(), AppList.class);
//                startActivityForResult(applistActivity, REQUEST_APP_LIST_CODE);
                addCondition.setText("GPS");
                triggerCondition = addCondition.getText().toString();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addCondition.getText().toString().contains("Select"))
                    viewPager.setCurrentItem(1);
                else
                {
                    Toast.makeText(getContext(), "Please add a condition first", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        TextView widget = (TextView) this;
//        Object text = widget.getText();
//        if (text instanceof Spanned) {
//            Spannable buffer = (Spannable) text;
//
//            int action = event.getAction();
//
//            if (action == MotionEvent.ACTION_UP
//                    || action == MotionEvent.ACTION_DOWN) {
//                int x = (int) event.getX();
//                int y = (int) event.getY();
//
//                x -= widget.getTotalPaddingLeft();
//                y -= widget.getTotalPaddingTop();
//
//                x += widget.getScrollX();
//                y += widget.getScrollY();
//
//                Layout layout = widget.getLayout();
//                int line = layout.getLineForVertical(y);
//                int off = layout.getOffsetForHorizontal(line, x);
//
//                ClickableSpan[] link = buffer.getSpans(off, off,
//                        ClickableSpan.class);
//
//                if (link.length != 0) {
//                    if (action == MotionEvent.ACTION_UP) {
//                        link[0].onClick(widget);
//                    } else if (action == MotionEvent.ACTION_DOWN) {
//                        Selection.setSelection(buffer,
//                                buffer.getSpanStart(link[0]),
//                                buffer.getSpanEnd(link[0]));
//                    }
//                    return true;
//                }
//            }
//
//        }
//
//        return false;
        return view;
    }

    public String getTriggerCondition()
    {
        return triggerCondition;
    }
}
