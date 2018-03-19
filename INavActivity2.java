package com.jrschugel.loadmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by seanm on 3/3/2018.
 * Copyright 2017. All rights reserved.
 */
public interface INavActivity2 {

    void inflateFragment(Fragment fragment, String fragmentTag, Bundle bundle);

    void sendEmail(Intent intent);
}
