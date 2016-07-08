package com.jonasgerdes.stoppelmap.model;

import io.realm.Realm;

/**
 * Created by Jonas on 08.07.2016.
 */
public class InitialTransaction implements Realm.Transaction {
    @Override
    public void execute(Realm realm) {

        initTransportation(realm);
    }

    private void initTransportation(Realm realm) {
        
    }
}
