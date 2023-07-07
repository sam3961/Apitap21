package com.apitap.views.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.GpsLocation;
import com.apitap.model.Utils;
import com.apitap.model.bean.LocationListBean;

import java.util.List;

/**
 * Created by rishav on 27/9/17.
 */

public class LocationListAdpater extends RecyclerView.Adapter<LocationListAdpater.Myholder> {
    public List<LocationListBean.RESULT_> apitaplist;
    Context context;
    private int row_index = -1;
    private AdapterClick adapterClick;
    ImageView navigate_to;
    boolean isNavigate = false;
    LocationListBean.RESULT_ list;
    private String current_long, current_lat;

    public LocationListAdpater(AdapterClick adapterClick, Context context, List<LocationListBean.RESULT_> beanlis, ImageView navigate_to) {
        this.context = context;
        this.apitaplist = beanlis;
        this.adapterClick = adapterClick;
        this.navigate_to = navigate_to;

    }

    public LocationListAdpater(AdapterClick adapterClick, Context context, List<LocationListBean.RESULT_> beanlis) {
        this.adapterClick = adapterClick;
        this.context = context;
        this.apitaplist = beanlis;
    }

    public void updateNotify(List<LocationListBean.RESULT_> beanlis, int pos) {
        this.row_index = pos;
        this.apitaplist = beanlis;
        notifyDataSetChanged();
    }


    public class Myholder extends RecyclerView.ViewHolder {
        TextView Name, Addressone, Addresstwo, mobilenumber, distance, arrow;
        LinearLayout row_view;

        public Myholder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.txtcountry);
            Addressone = (TextView) itemView.findViewById(R.id.txtaddress);
            Addresstwo = (TextView) itemView.findViewById(R.id.addresstwo);
            mobilenumber = (TextView) itemView.findViewById(R.id.phnnumber);
            row_view = (LinearLayout) itemView.findViewById(R.id.rowview);
            distance = (TextView) itemView.findViewById(R.id.distance);
            arrow = (TextView) itemView.findViewById(R.id.arrow);

        }


    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(Myholder holder, final int position) {
        list = apitaplist.get(position);

        holder.Name.setText(list.getAD().getCO().get4718().trim());

        if (!list.getAD().get11412().isEmpty())
            holder.Addressone.setText(Utils.hexToASCII(list.getAD().get11412().trim()));


        if (!list.getAD().get11413().isEmpty())
            holder.Addresstwo.setText(Utils.hexToASCII(list.getAD().get11413()).trim() + " " + list.getAD().getZP().get4717());
        else
            holder.Addresstwo.setText(list.getAD().getCI().get4715().trim() + " " + list.getAD().getCO().get11417() + " " + list.getAD().getST().get_1234() + " " + list.getAD().getZP().get4717());

        if (list.getPH().get4828() != null && !list.getPH().get4828().isEmpty())
            holder.mobilenumber.setText(Utils.FormatStringAsPhoneNumber(list.getPH().get4828().trim()));
        else
            holder.mobilenumber.setText("Phone: N/A");

        Log.d("distancesOf", list.get478().substring(0, list.get478().length() - 10) + "");
        holder.distance.setText(list.get478().substring(0, list.get478().length() - 10));
        if (holder.distance.getText().toString().contains("."))
            holder.arrow.setText("<");

        holder.row_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterClick.onItemClick(view, position);
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
            holder.row_view.setBackgroundColor(context.getResources().getColor(R.color.pocket_color_2));

        } else {
            holder.row_view.setBackgroundColor(Color.parseColor("#ffffff"));

        }
    }

    @Override
    public int getItemCount() {
        if (apitaplist.size() == 1 && apitaplist.get(0).get478() == null)
            return 0;
        else
            return apitaplist.size();
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onItemClick(View v, int position);
    }

    private boolean getloc() {
        boolean isReturn = false;
        GpsLocation gps = new GpsLocation(context);

        if (gps.canGetLocation()) {
            current_lat = String.valueOf(gps.getLatitude());
            current_long = String.valueOf(gps.getLongitude());
            isReturn = true;
        } else {
            gps.showSettingsAlertLocation();
        }
        return isReturn;
    }
}

