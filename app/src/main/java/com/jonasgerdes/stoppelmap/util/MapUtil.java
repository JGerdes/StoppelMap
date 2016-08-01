package com.jonasgerdes.stoppelmap.util;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.shared.GeoLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jonas on 16.07.2016.
 */
public class MapUtil {

    public static boolean isPointInPolygon(LatLng tap, List<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }


    private static boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }

    public static boolean isPointInGeoPolygon(LatLng tap, List<GeoLocation> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private static boolean rayCastIntersect(LatLng tap, GeoLocation vertA, GeoLocation vertB) {

        double aY = vertA.getLat();
        double bY = vertB.getLat();
        double aX = vertA.getLng();
        double bX = vertB.getLng();
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }


    public static class CameraChangeMultiplexer implements GoogleMap.OnCameraChangeListener {
        private List<GoogleMap.OnCameraChangeListener> mListener;

        public CameraChangeMultiplexer() {
            mListener = new ArrayList<>();
        }

        public void add(GoogleMap.OnCameraChangeListener listener) {
            mListener.add(listener);
        }

        public void remove(GoogleMap.OnCameraChangeListener listener) {
            mListener.remove(listener);
        }

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            for (GoogleMap.OnCameraChangeListener onCameraChangeListener : mListener) {
                onCameraChangeListener.onCameraChange(cameraPosition);
            }
        }
    }

    public static Comparator<MapEntity> getMapEntityDistanceComparator(final LatLng center) {
        final float ldist[] = new float[1];
        final float rdist[] = new float[1];
        return new Comparator<MapEntity>() {
            @Override
            public int compare(MapEntity lhs, MapEntity rhs) {
                GeoLocation left = lhs.getOrigin();
                GeoLocation right = rhs.getOrigin();
                Location.distanceBetween(left.getLat(), left.getLng(), center.latitude, center.longitude, ldist);
                Location.distanceBetween(right.getLat(), right.getLng(), center.latitude, center.longitude, rdist);
                return (int) Math.signum(ldist[0] - rdist[0]);
            }
        };
    }
}
