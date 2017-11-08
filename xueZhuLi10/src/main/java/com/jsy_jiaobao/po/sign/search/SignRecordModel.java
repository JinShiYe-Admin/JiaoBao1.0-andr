package com.jsy_jiaobao.po.sign.search;

import java.util.List;

/**
 * Created by rocka on 2017/11/8.
 */

public class SignRecordModel {
    private int rowCount;
    private List<SignRecord> infolist;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<SignRecord> getInfolist() {
        return infolist;
    }

    public void setInfolist(List<SignRecord> infolist) {
        this.infolist = infolist;
    }
}
