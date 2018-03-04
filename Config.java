package com.jrschugel.loadmanager;

public class Config {
    //JSON URL
    static final String DATA_URL = "http://truckersean.com/android/getSpinner.php";
    static final String DATA_SETTLEMENTS = "http://truckersean.com/android/getSettlements.php";
    static final String DATA_STATES_URL = "http://truckersean.com/android/getStates.php";
    static final String DATA_SAVE_STOPS = "http://truckersean.com/android/setLoadStops.php";
    static final String DATA_GET_EXPENSES = "http://truckersean.com/android/getLoadExpenses.php";
    static final String DATA_DELETE_EXPENSE = "http://truckersean.com/android/setDeleteExpense.php";
    static final String DATA_SAVE_CUSTOMER = "http://truckersean.com/android/setSaveLoadCust.php";
    static final String DATA_GET_CUSTOMERS = "http://truckersean.com/android/getCustomerNames.php";
    static final String DATA_SAVE_LOAD = "http://truckersean.com/android/setSaveLoadData-new.php";
    static final String DATA_GET_CUSTID = "http://truckersean.com/android/getSelectedCustomer";

    //Tags used in the JSON String
    static final String TAG_LOADNUMBER = "LoadNumber";
    static final String TAG_CUSTPO = "CustomerPO";
    static final String TAG_WEIGHT = "Weight";
    static final String TAG_PIECES = "Pieces";
    static final String TAG_BOLNUMBER = "BLNumber";
    static final String TAG_TRLRNUMBER = "TrailerNumber";
    static final String TAG_DRVLOAD = "DriverLoad";
    static final String TAG_DRVUNLOAD = "DriverUnload";
    static final String TAG_LOADED = "LoadedMiles";
    static final String TAG_EMPTY = "EmptyMiles";
    static final String TAG_TEMPLOW = "TempLow";
    static final String TAG_TEMPHIGH = "TempHigh";
    static final String TAG_PRELOADED = "Preloaded";
    static final String TAG_DROPHOOK = "DropHook";
    static final String TAG_COMMENTS = "Comments";
    static final String TAG_STATUS = "Status";
    static final String TAG_SETTLEMENTS = "SettlementDate";
    static final String TAG_STATE = "StateName";
    static final String TAG_VIEWEDLOAD = "ViewedLoad";
    static final String TAG_STOPID = "StopID";
    static final String TAG_STOPLOAD = "StopLoadNumber";
    static final String TAG_STOPTYPE = "StopType";
    static final String TAG_STOPCUSTID = "StopCustomerID";
    static final String TAG_STOPCUSTNAME = "StopCustomerName";
    static final String TAG_STOPCUSTADDR = "StopCustomerAddress";
    static final String TAG_STOPCUSTADDR2 = "StopCustomerAddress2";
    static final String TAG_STOPCUSTCITY = "StopCustomerCity";
    static final String TAG_STOPCUSTSTATE = "StopCustomerState";
    static final String TAG_STOPCUSTPHONE = "StopCustomerPhone";
    static final String TAG_STOPCUSTCONTACT = "StopCustomerContact";
    static final String TAG_STOPEARLYDATE = "StopEarlyDate";
    static final String TAG_STOPEARLYTIME = "StopEarlyTime";
    static final String TAG_STOPLATEDATE = "StopLateDate";
    static final String TAG_STOPLATETIME = "StopLateTime";
    static final String TAG_EXPDESC = "ExpenseDescription";
    static final String TAG_EXPTYPE = "ExpensePaymentType";
    static final String TAG_EXPPONUM = "ExpensePONumber";
    static final String TAG_EXPGALLON = "ExpenseGallons";
    static final String TAG_EXPAMOUNT = "ExpenseAmount";
    static final String TAG_EXPLOAD = "ExpenseLoadNumber";
    static final String TAG_SCANLOAD = "ScanLoadNumber";
    static final String TAG_SCANDESC = "ScanDescription";
    static final String TAG_SCANURI = "ScanURI";
    static final String TAG_CUSTNAME = "CustomerName";
    static final String TAG_CUSTADDR1 = "CustomerAddr1";
    static final String TAG_CUSTADDR2 = "CustomerAddr2";
    static final String TAG_CUSTCITY = "CustomerCity";
    static final String TAG_CUSTSTATE = "CustomerState";
    static final String TAG_CUSTPHONE = "CustomerPhone";
    static final String TAG_CUSTCONTACT = "CustomerContact";
    static final String TAG_CUSTCOMMENT = "CustomerComment";
    static final String TAG_SHIPPERID = "ShipperID";

    //JSON array name
    static final String JSON_ARRAY = "Loads";
    static final String JSON_ARRAY_STOPS = "Stops";
    static final String JSON_ARRAY_SETTLEMENT = "Settlements";
    static final String JSON_ARRAY_STATE = "States";
    static final String JSON_ARRAY_EXPENSES = "Expenses";
    static final String JSON_ARRAY_CUSTOMERS = "Customers";

}