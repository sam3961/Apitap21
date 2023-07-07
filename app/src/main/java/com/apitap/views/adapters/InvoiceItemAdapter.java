package com.apitap.views.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ReturnBean;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.ViewHolder> {
    ArrayList<ReturnBean> listInvoiceItems;
    Context context;
    ReturnItemClick returnItemClick;
    List<String> reasonsList;
    List<String> reasonsIdList;
    public static  ReturnBean returnBean;
    public static  ArrayList<String> reasonIdList= new ArrayList<>();
    public static  ArrayList<String> commentsList= new ArrayList<>();


    public InvoiceItemAdapter(Context context, ArrayList<ReturnBean> list, ReturnItemClick returnItemClick,
                              List<String> reasonsList, List<String> reasonsIdList) {
        this.listInvoiceItems = list;
        this.context = context;
        this.returnItemClick = returnItemClick;
        this.reasonsList = reasonsList;
        this.reasonsIdList = reasonsIdList;
        reasonIdList= new ArrayList<>();
        commentsList= new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invoice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
         returnBean  = listInvoiceItems.get(position);
        String stringQuantity=returnBean.stringQuantity;
        holder.editTextComments.setTag(position);
        holder.txt_title.setText(Utils.hexToASCII(listInvoiceItems.get(position).getItemName()));
        holder.txt_qty.setText(listInvoiceItems.get(position).getStringQuantity());
        holder.tvOption1.setText(listInvoiceItems.get(position).getChoiceOne());
        holder.tvOption2.setText(listInvoiceItems.get(position).getChoiceTwo());
        int quantity= Integer.parseInt(listInvoiceItems.get(position).stringQuantity);
        double price = Double.parseDouble(listInvoiceItems.get(position).amount);
        final double quantityAmount = quantity*price;
        holder.txt_price.setText("$"+Utils.roundOffTo2DecPlaces(quantityAmount));
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,R.layout.spinner_item_new, R.id.text,reasonsList);
        holder.spinnerOption.setAdapter(stringArrayAdapter);
        stringArrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);

        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL) +
                listInvoiceItems.get(position).getImageUrl()).into(holder.imageViewItem);

        holder.linearLayoutDontReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnItemClick.onDontReturnClick(position);
            }
        });

        holder.txtQtyDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(listInvoiceItems.get(position).getStringQuantity());
                double amount = Double.parseDouble(listInvoiceItems.get(position).getAmount());

                if (quantity>1) {
                    quantity = quantity - 1;
                    amount=quantityAmount-amount;
                    returnItemClick.onQuantityDecrease(Double.parseDouble(listInvoiceItems.get(position).getAmount())+"");
                    Log.d("doubless",Double.parseDouble(listInvoiceItems.get(position).getAmount())+"");
                }
                else
                    Log.d("No","Quantity");

                returnBean.setStringQuantity(String.valueOf(quantity));
                holder.txt_qty.setText(quantity+"");
                holder.txt_price.setText("$"+Utils.roundOffTo2DecPlaces(amount));

            }
        });

        holder.txtQtyIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(listInvoiceItems.get(position).getStringQuantity());
                double amount = Double.parseDouble(listInvoiceItems.get(position).getAmount());

                if (Integer.parseInt(listInvoiceItems.get(position).getStringQuantity())<=quantity) {
                    quantity = quantity + 1;
                    amount=quantityAmount+amount;
                    returnItemClick.onQuantityIncrease(Double.parseDouble(listInvoiceItems.get(position).getAmount())+"");
                    Log.d("doubless",Double.parseDouble(listInvoiceItems.get(position).getAmount())+"");
                }
                else
                    Log.d("No","Quantity");

                returnBean.setStringQuantity(String.valueOf(quantity));
                holder.txt_qty.setText(quantity+"");
                holder.txt_price.setText("$"+Utils.roundOffTo2DecPlaces(amount));

            }
        });

        holder.spinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                returnBean.setReasonId(reasonsIdList.get(i));
                Log.d("Reasonssss",returnBean.getReasonId());
                reasonIdList.add(position,reasonsIdList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
/*
        holder.txtQtyIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(listInvoiceItems.get(position).getStringQuantity());
                if (quantity>1)
                    quantity=quantity-1;
                else
                    Log.d("No","Quantity");
                returnBean.setStringQuantity(String.valueOf(quantity));
                holder.txt_qty.setText(quantity+"");
                returnItemClick.onQuantityDecrease(quantity+"");
            }
        });
*/

    }


    @Override
    public int getItemCount() {
        return listInvoiceItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyEditText() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView  tvOption1, tvOption2;
        private final TextView txt_title, txt_qty, txt_price,txtQtyDecrease,txtQtyIncrease;
        private final ImageView imageViewItem;
        private final Spinner spinnerOption;
        private final EditText editTextComments;
        private final LinearLayout linearLayoutDontReturn;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.name);
            imageViewItem = (ImageView) itemView.findViewById(R.id.img);
            tvOption1 = (TextView) itemView.findViewById(R.id.option1);
            tvOption2 = (TextView) itemView.findViewById(R.id.option2);
            txt_qty = (TextView) itemView.findViewById(R.id.textViewQty);
            txt_price = (TextView) itemView.findViewById(R.id.textViewPrice);
            txtQtyDecrease = (TextView) itemView.findViewById(R.id.editTextQtyDecrease);
            txtQtyIncrease = (TextView) itemView.findViewById(R.id.editTextQtyIncrease);
            spinnerOption =  itemView.findViewById(R.id.spinner);
            editTextComments =  itemView.findViewById(R.id.editTextComments);
            linearLayoutDontReturn =  itemView.findViewById(R.id.linearLayoutDontReturn);

            MyTextWatcher textWatcher = new MyTextWatcher(editTextComments);
            editTextComments.addTextChangedListener(textWatcher);

        }
    }
    public class MyTextWatcher implements TextWatcher {
        private EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
             // Do whatever you want with position
        }

        @Override
        public void afterTextChanged(Editable s) {
            int position = (int) editText.getTag();
            final ReturnBean returnBean = listInvoiceItems.get(position);
            returnBean.setComments(s.toString());
            commentsList.add(position,s.toString());

        }
    }

    public interface ReturnItemClick {
        void onDontReturnClick(int position);
        void onQuantityDecrease(String amount);
        void onQuantityIncrease(String amount);
    }
}
