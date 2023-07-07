package com.apitap.views.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.NotificationBean;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private AdapterClick adapterClick;
    private List<NotificationBean.RESULTBeanX.RESULTBean> notificationList;
    private Context context;


    public NotificationAdapter(List<NotificationBean.RESULTBeanX.RESULTBean> notificationList, AdapterClick adapterClick) {
        this.notificationList = notificationList;
        this.adapterClick = adapterClick;
    }

    public void customNotify(List<NotificationBean.RESULTBeanX.RESULTBean> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (!notificationList.get(position).get_122_114().isEmpty()) {
            if (!notificationList.get(position).get_114_144().isEmpty()) {
                holder.textViewTitle.setText("You have a new message from " + Utils.hexToASCII(
                        notificationList.get(position).get_114_70()) + " related to this item " + Utils.hexToASCII(notificationList.get(position).get_122_128()));

            } else if (!notificationList.get(position).get_123_21().isEmpty()) {
                holder.textViewTitle.setText("You have a new message from " + Utils.hexToASCII(
                        notificationList.get(position).get_114_70()) + " related to this ad " + Utils.hexToASCII(notificationList.get(position).get_120_83()));

            } else if (!notificationList.get(position).get_121_75().isEmpty()) {
                holder.textViewTitle.setText("You have a new message from " + Utils.hexToASCII(
                        notificationList.get(position).get_114_70()) + " related to inovice");

            } else {
                holder.textViewTitle.setText("You have a new message from " + Utils.hexToASCII(
                        notificationList.get(position).get_114_70()) + " relating to your question");

            }
        } else if (!notificationList.get(position).get_114_144().isEmpty()) {
            holder.textViewTitle.setText("You have a new item from " + Utils.hexToASCII(notificationList.get(position).get_114_70())
                    + " called "
                    + Utils.hexToASCII(notificationList.get(position).get_120_83()));

        }


        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + notificationList.get(position).get_121_170()).into(holder.imageViewStore);

        Log.d("imageNotiUrl", ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + notificationList.get(position).get_121_170());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = format.parse(notificationList.get(position).get_124_132());

            Date c = Calendar.getInstance().getTime();
            String formattedDate = format.format(c);

            Date date2 = format.parse(formattedDate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            long diff = date2.getTime() - date1.getTime();
            int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
            //numOfDays+=1;
            if (numOfDays == 0)
                holder.textViewTime.setText("Today");
            else if (numOfDays == 1) {
                holder.textViewTime.setText("A Day ago");
            } else {
                holder.textViewTime.setText(numOfDays + " Days ago");

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewStore;
        private TextView textViewTitle, textViewTime, textViewSubject;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewStore = itemView.findViewById(R.id.ivNotification);
            textViewTitle = itemView.findViewById(R.id.tvTitle);
            textViewTime = itemView.findViewById(R.id.tvTime);
            textViewSubject = itemView.findViewById(R.id.tvSubject);
        }
    }

    public interface AdapterClick {
        void onClick(int position);
    }

    public static long calculateDays(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }

}
