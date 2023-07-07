package com.apitap.views.adapters;

/**
 * Created by appzorro on 1/9/16.
 */

/*
public class AddCardAddressAdapter extends BaseAdapter {

    private List<AddressData> response;
    private static LayoutInflater inflater = null;

    private Activity activity;

    public AddCardAddressAdapter( Activity a, List<AddressData> response) {
        this.response = response;
        activity = a;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return response.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi = view;
        vi = inflater.inflate(R.layout.address_row, null);
        TextView txt_type = (TextView)vi.findViewById(R.id.txt_type);

        Log.d("rescponseId",response.get(position).getAddressId());

        txt_type.setText(response.get(position).getAddress_type()+" "+response.get(position).getLine1()+" "+response.get(position).getLine2());

        return vi;
    }

}
*/
