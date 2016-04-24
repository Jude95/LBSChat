package com.jude.lbschat.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;

import com.jude.beam.expansion.BeamBasePresenter;
import com.jude.beam.model.AbsModel;
import com.jude.lbschat.domain.entities.PersonBrief;
import com.jude.lbschat.presentation.PersonDetailActivity;
import com.jude.utils.JUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mr.Jude on 2015/7/8.
 */
public class RongYunModel extends AbsModel {

    public static RongYunModel getInstance() {
        return getInstance(RongYunModel.class);
    }

    public BehaviorSubject<Integer> mNotifyBehaviorSubject = BehaviorSubject.create();

    public void loginOut() {
        connectRongYun1("");
    }

    public Subscription registerNotifyCount(Action1<Integer> notify) {
        return mNotifyBehaviorSubject.subscribe(notify);
    }

    public void connectRongYun1(String token) {
        JUtils.Log("token:"+token);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                JUtils.Log("融云Token失效");
            }

            @Override
            public void onSuccess(String s) {
                JUtils.Log("融云连接成功");
                setRongYun();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                JUtils.Log("融云连接失败：" + errorCode.getValue() + ":" + errorCode.getMessage());
            }
        });
    }

    public void setRongYun() {
        try {
            RongIM.setUserInfoProvider(userId -> {
                PersonBrief p = PersonModel.getInstance().findPersonById(Integer.valueOf(userId));
                if (p!=null) return new UserInfo(userId, p.getName(), Uri.parse(ImageModel.getSmallImage(p.getAvatar())));
                else return null;
            }, true);

            RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
                @Override
                public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                    Intent i = new Intent(context, PersonDetailActivity.class);
                    i.putExtra(BeamBasePresenter.KEY_DATA, (Parcelable) PersonModel.getInstance().findPersonById(Integer.valueOf(userInfo.getUserId())));
                    context.startActivity(i);
                    return true;
                }

                @Override
                public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                    return false;
                }

                @Override
                public boolean onMessageClick(Context context, View view, Message message) {
                    return false;
                }

                @Override
                public boolean onMessageLinkClick(Context context, String s) {
                    return false;
                }

                @Override
                public boolean onMessageLongClick(Context context, View view, Message message) {
                    return false;
                }
            });

        } catch (Exception e) {
            JUtils.Log("融云出错");
        }
    }

    public void updateRongYunPersonBrief(PersonBrief p) {
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId() + "", p.getName(), Uri.parse(p.getAvatar())));
    }

    public void chatPerson(Context ctx, String id, String title) {
        RongIM.getInstance().startPrivateChat(ctx, id, title);
    }

    public void chatGroup(Context ctx, String id, String title) {
        RongIM.getInstance().startGroupChat(ctx, id, title);
    }

    public void chatList(Context ctx) {
        RongIM.getInstance().startConversationList(ctx);
    }
}
