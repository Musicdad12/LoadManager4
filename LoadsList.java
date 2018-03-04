package com.jrschugel.loadmanager;

/**
 * Created by seanm on 2/24/2018.
 * Copyright 2017. All rights reserved.
 */
public class LoadsList {
    String LoadNo;
    String Status;
    String ShipName;
    String ShipCity;
    String ShipState;
    String ConsName;
    String ConsCity;
    String ConsState;
    String PickupDate;
    String PickupTime;

    public LoadsList (String LoadNo, String Status, String ShipName, String ShipCity, String ShipState,
                      String ConsName, String ConsCity, String ConsState, String PickupDate, String PickupTime) {
        this.LoadNo = LoadNo;
        this.Status = Status;
        this.ShipName = ShipName;
        this.ShipCity = ShipCity;
        this.ShipState = ShipState;
        this.ConsName = ConsName;
        this.ConsCity = ConsCity;
        this.ConsState = ConsState;
        this.PickupDate = PickupDate;
        this.PickupTime = PickupTime;
    }
}
