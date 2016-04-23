package com.jude.lbschat.presentation;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jude.beam.expansion.BeamBasePresenter;
import com.jude.lbschat.data.AccountModel;
import com.jude.lbschat.data.PersonModel;
import com.jude.lbschat.data.server.ErrorTransform;
import com.jude.utils.JUtils;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Mr.Jude on 2016/4/21.
 */
public class MainPresenter extends BeamBasePresenter<MainActivity> {
    private Subscription subscription;

    @Override
    protected void onCreateView(@NonNull MainActivity view) {
        super.onCreateView(view);
        subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(AccountModel.getInstance().getCurrentAccount().getAvatar())
                ||TextUtils.isEmpty(AccountModel.getInstance().getCurrentAccount().getName())
                ||AccountModel.getInstance().getCurrentAccount().getBirth()==0){
            startActivityWithData(AccountModel.getInstance().getCurrentAccount(),EditActivity.class);
            JUtils.Toast("请完善资料");
        }
    }

    public void subscribe(){
        if (subscription!=null)subscription.unsubscribe();
        PersonModel.getInstance().getAllPerson()
                .doOnNext(placeBriefs -> getView().clearMarker())
                .flatMap(Observable::from)
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH))
                .subscribe(personBrief -> getView().addMarker(personBrief));
    }
}
