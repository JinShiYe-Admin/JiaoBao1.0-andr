package com.jsy_jiaobao.po.sign;

/**
 * 快速签到
 * Created by ShangLinMo on 2017/11/4.
 */

public class QuickSignIn {
    private int UnitID;//单位id
    private String UnitName;//单位名称
    public QuickSignIn(int UnitID, String UnitName) {
        super();
        this.UnitID = UnitID;
        this.UnitName = UnitName;
    }
    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    @Override
    public String toString() {
        return this.getUnitName().toString();
    }
}
