package com.jonasgerdes.stoppelmap.model;

import android.util.Log;

import com.jonasgerdes.stoppelmap.model.shared.GeoLocation;
import com.jonasgerdes.stoppelmap.model.transportation.Depature;
import com.jonasgerdes.stoppelmap.model.transportation.DepatureDay;
import com.jonasgerdes.stoppelmap.model.transportation.Route;
import com.jonasgerdes.stoppelmap.model.transportation.Station;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Jonas on 08.07.2016.
 */
public class InitialTransaction implements Realm.Transaction {
    private static final String TAG = "InitialTransaction";
    private int routeId = 0,
            stationId = 0,
            depatureDayId = 0,
            depatureId = 0;

    @Override
    public void execute(Realm realm) {
        initTransportation(realm);
    }

    @SuppressWarnings("UnusedAssignment")
    private void initTransportation(Realm realm) {


        Route vechtaWest = new Route();
        vechtaWest.setId(routeId++);
        vechtaWest.setName("Vechta West");
        vechtaWest.setStations(new RealmList<>(
                new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(), new RealmList<>(
                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                )),
                new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(), new RealmList<>(
                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                )),
                new Station(stationId++, "Achtern Diek", new GeoLocation(), new RealmList<>(
                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                ))
        ));

        Route vechtaStadt = new Route();
        vechtaStadt.setId(routeId++);
        vechtaStadt.setName("Vechta West");
        vechtaStadt.setStations(new RealmList<>(
                new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(), new RealmList<>(
                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                )),
                new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(), new RealmList<>(
                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                )),
                new Station(stationId++, "Achtern Diek", new GeoLocation(), new RealmList<>(
                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                ))
        ));

        realm.copyToRealmOrUpdate(vechtaWest);
        realm.copyToRealmOrUpdate(vechtaStadt);

    }

    private RealmList<Depature> generateDepatures(int day, int startH, int startM, int endH, int endM, int deltaM) {
        RealmList<Depature> result = new RealmList<>();
        int wrappedDays = 0;
        do {
            result.add(new Depature(depatureId++, new Date(2016, 7, day + 11, startH, startM), null));
            startM += deltaM;
            if (startM >= 60) {
                startH += startM / 60;
                startM = startM % 60;
            }
            if (startH >= 24) {
                startH -= 24;
            }
            Log.d(TAG, "generateDepatures() called with: " + "day = [" + day + "], startH = [" + startH + "], startM = [" + startM + "], endH = [" + endH + "], endM = [" + endM + "], deltaM = [" + deltaM + "]");
        } while (startH != endH && startM != endM);
        return result;
    }
}
