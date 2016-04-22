package com.jude.lbschat.data.server;


import com.jude.lbschat.data.AccountModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhuchenxi on 16/1/25.
 */
@Singleton
@Component(modules = {ServiceAPIModule.class})
public interface ServiceModelComponent {
    void inject(AccountModel model);
}
