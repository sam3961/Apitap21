package com.apitap.views.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;

import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.bean.getaddress.AddressData;

import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressData> response;
    private AdapterClick adapterClick;

    public AddressAdapter(List<AddressData> response) {
        this.response = response;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recycler_address, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_type.setText(response.get(position).getAddress_type());
        holder.txt_line1.setText(response.get(position).getLine1());
        holder.txt_line2.setText(response.get(position).getLine2());
        holder.txt_city_pin.setText(response.get(position).getcI().getCity() + " / " +
                response.get(position).getzP().getZipcode());
        holder.txt_state.setText(response.get(position).getsT().getState() + " / " +
                response.get(position).getcO().getCountry());
        holder.txt_phone.setText(response.get(position).getPhone());

        if (response.get(position).isChecked()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = holder.checkBox.isChecked();
                for (int i = 0; i < response.size(); i++) {
                    if (i == position) {
                        response.get(position).setChecked(checked);
                    } else {
                        response.get(i).setChecked(false);
                    }
                }
                notifyDataSetChanged();
            }
        });


        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onEditClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt_type, txt_line1, txt_line2, txt_city_pin, txt_state, txt_phone, txt_edit;
        private final CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            txt_line1 = (TextView) itemView.findViewById(R.id.txt_line1);
            txt_line2 = (TextView) itemView.findViewById(R.id.txt_line2);
            txt_city_pin = (TextView) itemView.findViewById(R.id.txt_city_pin);
            txt_state = (TextView) itemView.findViewById(R.id.txt_state);
            txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
        }
    }

    public void setOnEditClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onEditClick(View v, int position);
    }

}
