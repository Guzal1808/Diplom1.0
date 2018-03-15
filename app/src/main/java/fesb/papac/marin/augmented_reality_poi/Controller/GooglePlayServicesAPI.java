package fesb.papac.marin.augmented_reality_poi.Controller;

/**
 * Created by Гюзаль on 11.03.2018.
 */

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;

public class GooglePlayServicesAPI {

    private static final String TAG = "GooglePlayServicesApi";
    private GoogleApiClient googleApiClient;

    public GooglePlayServicesAPI(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public Observable<Location> getLastLocation() {
        return Observable.defer(() -> {
            @SuppressWarnings("MissingPermission")
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            return Observable.just(location);
        });
    }

    public Observable<List<Place>> getCurrentPlace() {
        return Observable.defer(() -> {
            Log.d(TAG, "Places.PlaceDetectionApi.getCurrentPlace");
            @SuppressWarnings("MissingPermission")
            PendingResult<PlaceLikelihoodBuffer> pendingResult
                    = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
            PlaceLikelihoodBuffer autocompletePredictionBuffer = pendingResult.await();

            Status status = autocompletePredictionBuffer.getStatus();
            if (!status.isSuccess()) {
                throw new RuntimeException(status.getStatusMessage());
            }

            List<Place> list = new ArrayList<>();
            for (PlaceLikelihood placeLikelihood : DataBufferUtils.freezeAndClose(autocompletePredictionBuffer)) {
                list.add(placeLikelihood.getPlace());
            }
            return Observable.just(list);

        });
    }

    public Observable<Place> getPlace(final String placeId) {
        return Observable.defer(() -> {
            Log.d(TAG, "Places.GeoDataApi.getPlaceById");
            PendingResult<PlaceBuffer> pendingResult
                    = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
            PlaceBuffer placeBuffer = pendingResult.await();

            Status status = placeBuffer.getStatus();
            if (!status.isSuccess()) {
                throw new RuntimeException(status.getStatusMessage());
            }

            List<Place> list = DataBufferUtils.freezeAndClose(placeBuffer);
            return Observable.from(list);
        });
    }

    public Observable<List<PlacePhotoMetadata>> getPlacePhotos(final String placeId) {
        return Observable.defer(() -> {
            Log.d(TAG, "Places.GeoDataApi.getPlacePhotos");
            PendingResult<PlacePhotoMetadataResult> pendingResult
                    = Places.GeoDataApi.getPlacePhotos(googleApiClient, placeId);
            PlacePhotoMetadataResult placePhotoMetadataResult = pendingResult.await();

            Status status = placePhotoMetadataResult.getStatus();
            if (!status.isSuccess()) {
                throw new RuntimeException(status.getStatusMessage());
            }

            List<PlacePhotoMetadata> list
                    = DataBufferUtils.freezeAndClose(placePhotoMetadataResult.getPhotoMetadata());
            return Observable.just(list);
        });
    }

    public Observable<Bitmap> getPlacePhotoBitmap(final PlacePhotoMetadata placePhotoMetadata,
                                                  final int width, final int height) {
        return Observable.defer(() -> {
            PendingResult<PlacePhotoResult> pendingResult;
            if (width > 0 && height > 0) {
                Log.d(TAG, String.format(Locale.getDefault(),
                        "PlacePhotoMetadata.getScaledPhoto_w%d_h%d", width, height));
                pendingResult = placePhotoMetadata.getScaledPhoto(googleApiClient, width, height);
            } else {
                Log.d(TAG, "PlacePhotoMetadata.getPhoto");
                pendingResult = placePhotoMetadata.getPhoto(googleApiClient);
            }

            PlacePhotoResult placePhotoResult = pendingResult.await();

            Status status = placePhotoResult.getStatus();
            if (!status.isSuccess()) {
                throw new RuntimeException(status.getStatusMessage());
            }

            return Observable.just(placePhotoResult.getBitmap());
        });
    }
}