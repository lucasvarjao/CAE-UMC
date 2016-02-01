package com.caeumc.caeumc;

import com.orm.SugarRecord;


/*
 * Created by EDNEI on 08/01/2016.
 */
public class InitializeDataBase extends SugarRecord<InitializeDataBase> {
    private String Initialize;

    public InitializeDataBase () {

    }

    public InitializeDataBase (String initialize) {
        this.Initialize = initialize;
    }

    public void setInitialize (String initialize) {
        this.Initialize = initialize;
    }

    public String getInitialize () {
        return this.Initialize;
    }

}
