package com.apitap.app.views.fragments.itemDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.app.R;
import com.apitap.app.model.Utils;
import com.apitap.app.model.bean.ProductOptionsBean;

import java.util.List;
import java.util.Set;

public class BottomSheetOptionsAdapter extends RecyclerView.Adapter<BottomSheetOptionsAdapter.ViewHolder> {
        private String selectedChoiceId;

        public interface OnItemClick {
            void onClick(ProductOptionsBean item);
        }

        private List<ProductOptionsBean> list;
        private Set<String> enabledChoiceIds;
        private OnItemClick listener;

        public BottomSheetOptionsAdapter(List<ProductOptionsBean> list,
                                         Set<String> enabledChoiceIds,
                                         String selectedChoiceId,
                                         OnItemClick listener) {
            this.list = list;
            this.enabledChoiceIds = enabledChoiceIds;
            this.selectedChoiceId = selectedChoiceId;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_bottomsheet_options, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            ProductOptionsBean item = list.get(position);

            String cleanId = Utils.removeLeadingZeros(
                    Utils.getElevenDigitId(item.getChoice_id())
            );

            if (cleanId.equals(selectedChoiceId)) {
                holder.imgCheck.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.ic_icon_radio_button));
            } else {
                holder.imgCheck.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.ic_icon_blank_radio_button));
            }

            String name = Utils.hexToASCII(item.getChoice_name());
            double price = Double.parseDouble(item.getChoice_price());
            String priceFormat = "$" + String.format("%.2f", price);

            boolean enabled = enabledChoiceIds.contains(item.getChoice_id());

            holder.txtType.setText(name + " + " + priceFormat);
            holder.txtType.setEnabled(enabled);
            holder.txtType.setAlpha(enabled ? 1f : 0.4f);

            holder.itemView.setOnClickListener(v -> {
                if (enabled) {
                    listener.onClick(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtType;
            ImageView imgCheck;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtType = itemView.findViewById(R.id.txt_type);
                imgCheck = itemView.findViewById(R.id.imgCheck);
            }
        }
    }
