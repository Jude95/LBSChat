package com.jude.lbschat.data;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.jude.beam.model.AbsModel;
import com.jude.lbschat.data.server.DaggerServiceModelComponent;
import com.jude.lbschat.data.server.ErrorTransform;
import com.jude.lbschat.data.server.ServiceAPI;
import com.jude.lbschat.domain.Dir;
import com.jude.lbschat.domain.entities.Location;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;

/**
 * Created by Mr.Jude on 2015/1/28.
 * 地理位置的管理
 */
public class LocationModel extends AbsModel {

    private static final String FILENAME = "location";

    public static LocationModel getInstance(){
        return getInstance(LocationModel.class);
    }
    AMapLocationClient mLocationClient;
    private BehaviorSubject<Location> mLocationSubject = BehaviorSubject.create();
    @Inject
    ServiceAPI mServiceAPI;

    public double getDistance(double lat,double lng){
        return JUtils.distance(getCurrentLocation().getLongitude(), getCurrentLocation().getLatitude(), lng, lat);
    }

    @Override
    protected void onAppCreateOnBackThread(Context ctx) {
        DaggerServiceModelComponent.builder().build().inject(this);
        Location mLocation = (Location) JFileManager.getInstance().getFolder(Dir.Object).readObjectFromFile(FILENAME);
        if (mLocation == null)mLocation = new Location();
        mLocationSubject.onNext(mLocation);
        mLocationSubject
                .doOnNext(location -> {
                    if(AccountModel.getInstance().hasLogin())
                        AccountModel.getInstance().getCurrentAccount().setAddress(location.address);
                })
                .debounce(60, TimeUnit.SECONDS)//最快1分钟上传一次
                .subscribe(location1 -> {
                    JFileManager.getInstance().getFolder(Dir.Object).writeObjectToFile(getCurrentLocation(), FILENAME);
                    uploadAddress();
                });
        configLocation(ctx);
        startLocation();
    }

    public Location getCurrentLocation(){
        return mLocationSubject.getValue();
    }

    public BehaviorSubject<Location> getLocationSubject(){
        return mLocationSubject;
    }

    private void configLocation(Context ctx){
        mLocationClient = new AMapLocationClient(ctx);
        mLocationClient.setLocationListener(aMapLocation -> {
            JUtils.Log("GetLocation"+aMapLocation);
            //只有位置变动时才上传
            if (!getCurrentLocation().equals(createLocation(aMapLocation)))
                mLocationSubject.onNext(createLocation(aMapLocation));
        });
        mLocationClient.startLocation();

        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒
        option.setInterval(2000);
        mLocationClient.setLocationOption(option);
    }

    public void startLocation(){
        mLocationClient.startLocation();
    }

    public void stopLocation(){
        mLocationClient.stopLocation();
    }

    private Location createLocation(AMapLocation aMapLocation){
        Location location = new Location();
        location.address = aMapLocation.getAddress();
        location.altitude = aMapLocation.getAltitude();
        location.latitude = aMapLocation.getLatitude();
        location.longitude = aMapLocation.getLongitude();
        location.city = aMapLocation.getCity();
        location.country = aMapLocation.getCountry();
        location.district = aMapLocation.getDistrict();
        location.province = aMapLocation.getProvince();
        if (!aMapLocation.getAdCode().isEmpty())
        location.regionCode = Integer.parseInt(aMapLocation.getAdCode());
        return location;
    }

    public void uploadAddress(){
        JUtils.Log("uploadAddress");
        mServiceAPI.location(getCurrentLocation().latitude,getCurrentLocation().longitude,getCurrentLocation().district,getCurrentLocation().address)
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.NONE))
                .subscribe();
    }
}
