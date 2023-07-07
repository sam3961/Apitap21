package com.apitap.model.customclasses;

import com.apitap.model.bean.ShoppingAsstListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thakur on 3/16/2018.
 */

public class SelectedItem {
    List<ShoppingAsstListBean.RESULT_> list= new ArrayList<>();
    public SelectedItem() {

    }
        public SelectedItem( List<ShoppingAsstListBean.RESULT_> list) {
        this.list=list;

    }

    public List<ShoppingAsstListBean.RESULT_> getList() {
        return list;
    }

    public void setList(List<ShoppingAsstListBean.RESULT_> list) {
        this.list = list;
    }
}
