package com.suncart.grocerysuncart.model;

public class AddressModel {

    Long ids;
    String userName, userAddress, exactLoc;
    int isInUse;

    public AddressModel(Long ids, String userName, String userAddress, String exactLoc, int isInUse) {
        this.ids = ids;
        this.userName = userName;
        this.userAddress = userAddress;
        this.exactLoc = exactLoc;
        this.isInUse = isInUse;
    }

    public Long getIds() {
        return ids;
    }

    public void setIds(Long ids) {
        this.ids = ids;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getExactLoc() {
        return exactLoc;
    }

    public void setExactLoc(String exactLoc) {
        this.exactLoc = exactLoc;
    }

    public int getInUse() {
        return isInUse;
    }

    public void setInUse(int inUse) {
        isInUse = inUse;
    }
}
