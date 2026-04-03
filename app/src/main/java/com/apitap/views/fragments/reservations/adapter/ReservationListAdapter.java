package com.apitap.views.fragments.reservations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.model.reservation.ViewReservationResponseItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ViewHolder> {

    private AdapterClick adapterClick;
    private Context context;
    private final List<ViewReservationResponseItem> list;

    public ReservationListAdapter(List<ViewReservationResponseItem> list, AdapterClick adapterClick) {
        this.list = list;
        this.adapterClick = adapterClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_reservation, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ViewReservationResponseItem item = list.get(position);

        boolean isPastDateTime = false;

        try {
            // Combine date + time
            String dateTimeStr = item.getReservationDate() + " " + item.getStartHour();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            Date reservationDateTime = sdf.parse(dateTimeStr);
            Date currentDateTime = new Date();

            if (reservationDateTime != null && reservationDateTime.before(currentDateTime)) {
                isPastDateTime = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (item.getStatusId() == 5 || isPastDateTime) {

            holder.tvCancelReservation.setVisibility(View.GONE);

            if (item.getStatusId() == 5) {
                holder.tvCancelled.setText("Cancelled");
                holder.tvCancelled.setVisibility(View.VISIBLE);
                holder.linearLayoutTop.setBackgroundColor(
                        context.getColor(R.color.colorCancelledReservation)
                );
            } else {
                // Past but not cancelled
                holder.tvCancelled.setText("");
                holder.tvCancelled.setVisibility(View.GONE);
                holder.linearLayoutTop.setBackgroundColor(
                        context.getColor(R.color.colorWhite)
                );
            }

        } else {
            holder.tvCancelReservation.setVisibility(View.VISIBLE);
            holder.tvCancelled.setVisibility(View.GONE);
            holder.linearLayoutTop.setBackgroundColor(
                    context.getColor(R.color.colorWhite)
            );
        }

        // Date
        holder.tvDate.setText(item.getReservationDate());

        // Time
        holder.tvTime.setText(
                Utils.removeLastChar(item.getStartHour(), 0)
        );

        // Store name
        holder.tvStore.setText(
                item.getCompanyName()
        );

        // People count
        holder.tvPeople.setText(String.valueOf(item.getPeopleQty()));

        // Location
        holder.tvLocation.setText("Location " + item.getLocationId());

        // Address
        holder.tvAddress.setText(item.getAddresFirst());

        // Phone
        holder.tvPhone.setText(item.getConsumerPhone());

        // Notes
        holder.tvNotes.setText("Notes: " + item.getNote());


        // Cancel click
        holder.tvCancelReservation.setOnClickListener(v -> {
            if (adapterClick != null) {
                adapterClick.onCancelClick(item);
            }
        });

        // Card click
        holder.itemView.setOnClickListener(v -> {
            if (adapterClick != null) {
                adapterClick.onItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvTime, tvStore, tvPeople,
                tvLocation, tvAddress, tvPhone, tvNotes,
                tvCancelReservation,tvCancelled;
        LinearLayout linearLayoutTop;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayoutTop = itemView.findViewById(R.id.linearLayoutTop);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStore = itemView.findViewById(R.id.tvStore);
            tvPeople = itemView.findViewById(R.id.tvPeople);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            tvCancelReservation = itemView.findViewById(R.id.tvCancelReservation);
            tvCancelled = itemView.findViewById(R.id.tvCancelled);
        }
    }

    public interface AdapterClick {

        void onItemClick(ViewReservationResponseItem responseItem);

        void onCancelClick(ViewReservationResponseItem responseItem);
    }
}