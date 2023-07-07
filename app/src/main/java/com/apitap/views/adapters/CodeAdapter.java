package com.apitap.views.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.customclasses.Event_Address;

/**
 * Created by appzorro on 1/9/16.
 */

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.ViewHolder> {

    private Event_Address event;
    AdapterClick adapterClick;

    public CodeAdapter(Event_Address event) {
        this.event = event;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_textview, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String data;
        if (event.getKey() == Constants.GET_COUNTRY_CODE) {
            data = event.getResponse_code().getCountries().get(position).getCountryName();
        } else if (event.getKey() == Constants.GET_STATE_CODE) {
            data = event.getResponse_code().getStates().get(position).getStateName();
        } else if (event.getKey() == Constants.GET_CITY_CODE) {
            data = event.getResponse_code().getCities().get(position).getCityName();
        } else {
            data = "";
        }
        holder.txt.setText(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (event.getKey() == Constants.GET_COUNTRY_CODE) {
            return event.getResponse_code().getCountries().size();
        } else if (event.getKey() == Constants.GET_STATE_CODE) {
            return event.getResponse_code().getStates().size();
        } else if (event.getKey() == Constants.GET_CITY_CODE) {
            return event.getResponse_code().getCities().size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);

        }
    }

    public void setonItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onClick(View view, int position);
    }

}
