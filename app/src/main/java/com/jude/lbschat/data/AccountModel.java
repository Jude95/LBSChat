package com.jude.lbschat.data;

import android.content.Context;

import com.jude.beam.model.AbsModel;
import com.jude.lbschat.data.server.DaggerServiceModelComponent;
import com.jude.lbschat.data.server.ErrorTransform;
import com.jude.lbschat.data.server.HeaderInterceptors;
import com.jude.lbschat.data.server.SchedulerTransform;
import com.jude.lbschat.data.server.ServiceAPI;
import com.jude.lbschat.domain.Dir;
import com.jude.lbschat.domain.body.Exist;
import com.jude.lbschat.domain.body.Info;
import com.jude.lbschat.domain.entities.Account;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * Created by zhuchenxi on 16/1/21.
 */
public class AccountModel extends AbsModel {
    private static final String FILE_ACCOUNT = "account";
    @Inject
    ServiceAPI mServiceAPI;

    public static AccountModel getInstance() {
        return getInstance(AccountModel.class);
    }


    private BehaviorSubject<Account> mAccountSubject = BehaviorSubject.create();
    private Func1<Account,Boolean> mAccountFilter = account -> {
        return !(account!=null&&account.equals(mAccountSubject.getValue()));
    };

    @Override
    protected void onAppCreate(Context ctx) {
        super.onAppCreate(ctx);
        DaggerServiceModelComponent.builder().build().inject(this);

        //账号持久化
        mAccountSubject.subscribe(account -> {
            if (account==null) JFileManager.getInstance().getFolder(Dir.Object).deleteChild(FILE_ACCOUNT);
            else JFileManager.getInstance().getFolder(Dir.Object).writeObjectToFile(account,FILE_ACCOUNT);
        });
        //token设置
        mAccountSubject.subscribe(account1 -> {
            if (account1!=null) {
                HeaderInterceptors.TOKEN = account1.getToken();
                HeaderInterceptors.UID = account1.getId() + "";
            }else {
                HeaderInterceptors.TOKEN = "";
                HeaderInterceptors.UID = "";
            }
            JUtils.Log("Set Token:"+HeaderInterceptors.TOKEN);
        });
        //初始化账户
        Observable.just((Account) JFileManager.getInstance().getFolder(Dir.Object).readObjectFromFile(FILE_ACCOUNT))
                .doOnNext(account -> mAccountSubject.onNext(account))
                .subscribe();
    }

    public BehaviorSubject<Account> getAccountSubject(){
        return mAccountSubject;
    }

    public boolean hasLogin(){
        return mAccountSubject.getValue()!=null;
    }


    public Observable<Account> login(String account, String password){
        return mServiceAPI.login(account,password)
                .compose(new SchedulerTransform<>())
                .doOnNext(account1 -> mAccountSubject.onNext(account1));
    }

    public Observable<Info> register(String number, String password, String name, String code){
        return mServiceAPI.register(number, name,password, code).compose(new SchedulerTransform<>());
    }

    public Observable<Object> modifyPassword(String number,String password,String code){
        return mServiceAPI.modifyPassword(number, password, code).compose(new SchedulerTransform<>());
    }

    public Observable<Boolean> checkAccount(String number){
        return mServiceAPI.checkAccountExist(number).compose(new SchedulerTransform<>()).map(Exist::isExist);
    }

    public void refreshAccount(){
        mServiceAPI.refreshAccount()
                .compose(new SchedulerTransform<>())
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.NONE))
                .filter(mAccountFilter)
                .subscribe(account -> mAccountSubject.onNext(account));
    }

    public void logout(){
        mAccountSubject.onNext(null);
    }

}
