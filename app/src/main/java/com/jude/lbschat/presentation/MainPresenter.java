package com.jude.lbschat.presentation;

import android.support.annotation.NonNull;

import com.jude.beam.expansion.BeamBasePresenter;
import com.jude.lbschat.data.PersonModel;

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

    public void subscribe(){
        if (subscription!=null)subscription.unsubscribe();
        PersonModel.getInstance().getAllPerson()
                .doOnNext(placeBriefs -> getView().clearMarker())
                .flatMap(Observable::from)
                .subscribe(personBrief -> getView().addMarker(personBrief));
    }
}
