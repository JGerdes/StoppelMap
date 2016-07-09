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
                new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(52.713417, 8.284717),
                        new RealmList<>(
                                new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                        generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                        )),
                new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(52.713882, 8.281387),
                        new RealmList<>(
                                new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                        generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                        )),
                new Station(stationId++, "Achtern Diek", new GeoLocation(52.717482, 8.279499),
                        new RealmList<>(
                                new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                        generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                        )),
                new Station(stationId++, "Romberg Str./Finanzamt", new GeoLocation(52.720968, 8.279297),
                        new RealmList<>(
                                new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                        generateDepatures(DepatureDay.DAY_THURSDAY, 18, 36, 2, 36, 30), null)
                        ))
        ));

        Route vechtaStadt = new Route();
        vechtaStadt.setId(routeId++);
        vechtaStadt.setName("Vechta Stadt");
        vechtaStadt.setStations(new RealmList<>(
                new Station(stationId++, "Sgundek", new GeoLocation(52.704820, 8.298044),
                        new RealmList<>(
                                new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                        generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                        )),
                new Station(stationId++, "Am Schützenplatz", new GeoLocation(52.713650, 8.291864),
                        new RealmList<>(
                                new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                        generateDepatures(DepatureDay.DAY_THURSDAY, 18, 31, 2, 31, 30), null)
                        )),
                new Station(stationId++, "Münsterstr., Wessel", new GeoLocation(52.717931, 8.286829),
                        new RealmList<>(
                                new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                        generateDepatures(DepatureDay.DAY_THURSDAY, 18, 33, 2, 33, 30), null)
                        ))
        ));
        realm.copyToRealmOrUpdate(vechtaWest);
        realm.copyToRealmOrUpdate(vechtaStadt);

        realm.copyToRealm(new Route(
                routeId++,
                "Vechta Flugplatz",
                new RealmList<>(
                        new Station(stationId++, "Vechtaer Marsch/Straßburger Str.", new GeoLocation(52.725289, 8.266709),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                                )),
                        new Station(stationId++, "Vechtaer Marsch/Dornier Str.", new GeoLocation(52.728275, 8.269377),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                                )),
                        new Station(stationId++, "Vechtaer Marsch/Famila Markt", new GeoLocation(52.730041, 8.267408),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                                ))
                ),
                null)
        );

        realm.copyToRealm(new Route(
                routeId++,
                "Vechta Süd",
                new RealmList<>(
                        new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(52.713417, 8.284717),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                                )),
                        new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(52.713882, 8.281387),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                                )),
                        new Station(stationId++, "Achtern Diek", new GeoLocation(52.717482, 8.279499),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                                ))
                ),
                null)
        );

        realm.copyToRealm(new Route(
                routeId++,
                "Langförden",
                new RealmList<>(
                        new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(52.713417, 8.284717),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                                )),
                        new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(52.713882, 8.281387),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                                )),
                        new Station(stationId++, "Achtern Diek", new GeoLocation(52.717482, 8.279499),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                                ))
                ),
                null)
        );

        realm.copyToRealm(new Route(
                routeId++,
                "Telbrake",
                new RealmList<>(
                        new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(52.713417, 8.284717),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                                )),
                        new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(52.713882, 8.281387),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                                )),
                        new Station(stationId++, "Achtern Diek", new GeoLocation(52.717482, 8.279499),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                                ))
                ),
                null)
        );

        realm.copyToRealm(new Route(
                routeId++,
                "Bakum",
                new RealmList<>(
                        new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(52.713417, 8.284717),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                                )),
                        new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(52.713882, 8.281387),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                                )),
                        new Station(stationId++, "Achtern Diek", new GeoLocation(52.717482, 8.279499),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                                ))
                ),
                null)
        );

        realm.copyToRealm(new Route(
                routeId++,
                "Diepholz",
                new RealmList<>(
                        new Station(stationId++, "Dersastr./Lohner Str.", new GeoLocation(52.713417, 8.284717),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 30, 2, 30, 30), null)
                                )),
                        new Station(stationId++, "Dersastr./Gerbert Str.", new GeoLocation(52.713882, 8.281387),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                                )),
                        new Station(stationId++, "Achtern Diek", new GeoLocation(52.717482, 8.279499),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                                ))
                ),
                null)
        );

        realm.copyToRealm(new Route(
                routeId++,
                "Visbek",
                new RealmList<>(
                        new Station(stationId++, "Hagstedt, Mittlere Siedlungsstr.", new GeoLocation(52.811557, 8.255042),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 17, 19, 17, 30), null)
                                )),
                        new Station(stationId++, "Erlte, Schule", new GeoLocation(52.827184, 8.284968),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 32, 2, 32, 30), null)
                                )),
                        new Station(stationId++, "Visbek, Uhlenkamp", new GeoLocation(52.837477, 8.305528),
                                new RealmList<>(
                                        new DepatureDay(depatureDayId++, DepatureDay.DAY_THURSDAY,
                                                generateDepatures(DepatureDay.DAY_THURSDAY, 18, 34, 2, 34, 30), null)
                                ))
                ),
                null)
        );

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
