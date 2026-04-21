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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        this.list = list;
        this.unreadCount = unreadCount;
    }

    public void updateList(List<MessageListBean.MessageData> itemlist) {
        list = itemlist;
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

        //  holder.txt_title.setText(list.get(position).getName());
        holder.txt_store.setText(Utils.hexToASCII(list.get(position).getSeventy()));
        SimpleDateFormat sdf_old = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        holder.txt_date.setText(Utils.getDateFromMsg(list.get(position).getCreatedDate()));
        holder.txt_subject.setText(list.get(position).getSubject());
        if (list.get(position).getUnreadReplies().equals("0")){
            holder.textUnreadCount.setVisibility(View.GONE);
        }else{
            holder.textUnreadCount.setVisibility(View.VISIBLE);
            holder.textUnreadCount.setText(list.get(position).getUnreadReplies());

        }

        holder.txt_msg.setText(Utils.hexToASCII(list.get(position).getContextData()));

        if (list.get(position).getIsSeen().equals("false")) {
            holder.img_Replied.setImageResource(R.drawable.ring_bg_green);
        } else if (list.get(position).getReplied().equals("true")) {
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
        return list.size();
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
