package com.apitap.app.views.fragments.messages.adapter;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.app.R;
import com.apitap.app.model.Utils;
import com.apitap.app.model.bean.MessageListBean;
import com.apitap.app.views.fragments.messages.FragmentMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private AdapterClick adapterClick;
    List<MessageListBean.MessageData> list;
    Activity activity;
    int unreadCount;
    int selected_position = -1;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

    public MessageListAdapter(Activity activity, List<MessageListBean.MessageData> list, int unreadCount) {
        this.activity = activity;
        this.list = list == null ? new ArrayList<>() : list;
        this.unreadCount = unreadCount;
    }

    public void updateList(List<MessageListBean.MessageData> itemlist) {
        list = itemlist == null ? new ArrayList<>() : itemlist;
        notifyDataSetChanged();
    }

    public MessageListBean.MessageData getItem(int position) {
        if (position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MessageListBean.MessageData item = getItem(position);
        if (item == null) {
            holder.txt_store.setText("");
            holder.txt_date.setText("");
            holder.txt_subject.setText("");
            holder.txt_msg.setText("");
            holder.textUnreadCount.setVisibility(View.GONE);
            holder.img_Replied.setImageResource(R.drawable.ring_bg_white);
            return;
        }

        holder.txt_store.setText(decodeHex(item.getSeventy()));
        holder.txt_date.setText(safeDate(item.getCreatedDate()));
        holder.txt_subject.setText(safeText(item.getSubject()));
        if ("0".equals(safeText(item.getUnreadReplies())) || safeText(item.getUnreadReplies()).isEmpty()){
            holder.textUnreadCount.setVisibility(View.GONE);
        }else{
            holder.textUnreadCount.setVisibility(View.VISIBLE);
            holder.textUnreadCount.setText(safeText(item.getUnreadReplies()));

        }

        holder.txt_msg.setText(decodeHex(item.getContextData()));

        if ("false".equalsIgnoreCase(safeText(item.getIsSeen()))) {
            holder.img_Replied.setImageResource(R.drawable.ring_bg_green);
        } else if ("true".equalsIgnoreCase(safeText(item.getReplied()))) {
            holder.img_Replied.setImageResource(R.drawable.ring_bg_white);
        } else {
            holder.img_Replied.setImageResource(R.drawable.ring_bg_white);
        }


        FragmentMessages.view_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getBindingAdapterPosition();
                if (adapterClick != null && adapterPosition != RecyclerView.NO_POSITION) {
                    adapterClick.onItemClick(view, adapterPosition);
                }
            }
        });
        //   mCheckBoxes.add(holder.img_main);
/*
        holder.img_main.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    for (int i = 0; i < mCheckBoxes.size(); i++) {
                        if (mCheckBoxes.get(i) == buttonView)
                            selected_position = i;
                        else
                            mCheckBoxes.get(i).setChecked(false);
                    }

                } else {
                    selected_position = -1;
                }

            }
        });
*/


        // Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) +
        //       list.get(position).getLogoImage()).into(holder.img_main);

        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getBindingAdapterPosition();
                if (adapterClick != null && adapterPosition != RecyclerView.NO_POSITION) {
                    adapterClick.onItemClick(v, adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }

    private String decodeHex(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        try {
            return Utils.hexToASCII(value);
        } catch (Exception e) {
            return value;
        }
    }

    private String safeDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        try {
            return Utils.getDateFromMsg(value);
        } catch (Exception e) {
            return value;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt_date, txt_msg, txt_store, txt_subject,textUnreadCount;
        private final LinearLayout lin_main;
        private final ImageView img_Replied;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            textUnreadCount = (TextView) itemView.findViewById(R.id.textUnreadCount);
            txt_subject = (TextView) itemView.findViewById(R.id.txt_subject);
            txt_msg = (TextView) itemView.findViewById(R.id.txt_msg);
            txt_store = (TextView) itemView.findViewById(R.id.txtstore);
            lin_main = (LinearLayout) itemView.findViewById(R.id.mainlayout);
            img_Replied = (ImageView) itemView.findViewById(R.id.is_reply);


        }
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onItemClick(View v, int position);
    }

}
