package com.jude.lbschat.data;

import android.content.Context;

import com.jude.beam.model.AbsModel;
import com.jude.lbschat.data.server.DaggerServiceModelComponent;
import com.jude.lbschat.data.server.SchedulerTransform;
import com.jude.lbschat.data.server.ServiceAPI;
import com.jude.lbschat.domain.entities.PersonBrief;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mr.Jude on 2016/4/21.
 */
public class PersonModel extends AbsModel {
    public static PersonModel getInstance() {
        return getInstance(PersonModel.class);
    }
    @Inject
    ServiceAPI mServiceAPI;
    @Override
    protected void onAppCreate(Context ctx) {
        super.onAppCreate(ctx);
        DaggerServiceModelComponent.builder().build().inject(this);
    }

    public Observable<List<PersonBrief>> getAllPerson(){
        return mServiceAPI.getPersons()
                .flatMapIterable(personBriefs -> personBriefs)
                .filter(personBrief -> personBrief.getId() != AccountModel.getInstance().getCurrentAccount().getId())
                .toList()
                .compose(new SchedulerTransform<>());
    }

    List<PersonBrief> createVirtualPersonBrief(){
        ArrayList<PersonBrief> arrayList = new ArrayList<>();
        arrayList.add(new PersonBrief(1,"苏格兰中二饼",805737600,1,"http://i2.hdslb.com/bfs/face/50297de047c8a122584bf37e7fc1f5b9377299fc.jpg","Come break me down",29.531558,106.609091,"",""));
        arrayList.add(new PersonBrief(2,"午后的my",805707600,0,"http://i0.hdslb.com/bfs/face/2b87048fdeb3899817bf4d72dfa7a6dd6a806c6a.jpg","少年啊！成为奇迹吧！",29.531958,106.605791,"",""));
        arrayList.add(new PersonBrief(3,"猫小璇",805787600,0,"http://i0.hdslb.com/bfs/face/87956c45dc42aa1240a96edf6fa2cb3c2deb79d2.jpg","成分==声控+大叔控+制服控",29.530758,106.619791,"",""));
        arrayList.add(new PersonBrief(4,"涯达酱",804737600,0,"http://i2.hdslb.com/bfs/face/de094684efaf10c8fc0a00cd9573a536d3fc718e.jpg","只想做自己有兴趣的",29.530558,106.618791,"",""));
        arrayList.add(new PersonBrief(5,"新丼葉子",805837600,1,"http://i0.hdslb.com/bfs/face/5969e7849c679215ed0861d6a2aef379aa55b447.jpg","",29.528758,106.629791,"",""));
        return arrayList;
    }
}
