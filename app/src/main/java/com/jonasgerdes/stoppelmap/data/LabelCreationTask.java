package com.jonasgerdes.stoppelmap.data;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;

/**
 * Created by Jonas on 07.08.2015.
 */
public class LabelCreationTask extends AsyncTask<Runnable, Void, Void> {

    private Handler handler;
    private DataController controller;
    private LayoutInflater inflater;
    private Runnable[] actions;

    public LabelCreationTask(DataController controlleer, LayoutInflater inflater, Handler handler){
        this.controller = controlleer;
        this.inflater = inflater;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Runnable... params) {
        controller.createLabels(inflater);
        actions = params;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        for(Runnable action : actions){
            action.run();
        }
    }
}
