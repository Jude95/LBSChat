package com.jude.lbschat.presentation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.jude.beam.expansion.data.BeamDataActivityPresenter;
import com.jude.lbschat.data.AccountModel;
import com.jude.lbschat.data.ImageModel;
import com.jude.lbschat.data.server.ErrorTransform;
import com.jude.lbschat.domain.entities.PersonBrief;
import com.jude.lbschat.utils.ProgressDialogTransform;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;
import com.jude.utils.JUtils;

import java.io.File;

import rx.Observable;

/**
 * Created by Mr.Jude on 2016/4/23.
 */
public class EditPresenter extends BeamDataActivityPresenter<EditActivity,PersonBrief> {
    private ImageProvider provider;
    OnImageSelectListener listener = new OnImageSelectListener() {

        @Override
        public void onImageSelect() {
            getView().getExpansion().showProgressDialog("加载中");
        }

        @Override
        public void onImageLoaded(Uri uri) {
            getView().getExpansion().dismissProgressDialog();
            provider.corpImage(uri, 300, 300, new OnImageSelectListener() {
                @Override
                public void onImageSelect() {

                }

                @Override
                public void onImageLoaded(Uri uri) {
                    getData().setAvatar(uri.toString());
                    refresh();
                }

                @Override
                public void onError() {
                    getView().getExpansion().dismissProgressDialog();
                    JUtils.Toast("加载错误");
                }
            });

        }

        @Override
        public void onError() {
            getView().getExpansion().dismissProgressDialog();
            JUtils.Toast("加载错误");
        }
    };
    @Override
    protected void onCreate(EditActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        provider = new ImageProvider(getView());
        setData(AccountModel.getInstance().getCurrentAccount());
    }

    public void editLogo(int style) {
        switch (style) {
            case 0:
                provider.getImageFromCamera(listener);
                break;
            case 1:
                provider.getImageFromAlbum(listener);
                break;
            case 2:
                provider.getImageFromNet(listener);
                break;
        }
    }

    public void upload(){
        if(TextUtils.isEmpty(getData().getAvatar())){
            JUtils.Toast("请选择头像");
            return;
        }
        if(TextUtils.isEmpty(getData().getName())){
            JUtils.Toast("请填写昵称");
            return;
        }
        if(getData().getBirth() == 0){
            JUtils.Toast("请填写生日");
            return;
        }
        Observable.just(getData())
                .flatMap(line -> {
                    if (!Uri.parse(line.getAvatar()).getScheme().equals("http")){
                        return ImageModel.getInstance().putImageSync(new File(Uri.parse(line.getAvatar()).getPath()))
                                .map(s -> {
                                    getData().setAvatar(s);
                                    return getData();
                                });
                    }
                    return Observable.just(line);
                })
                .flatMap(line -> AccountModel.getInstance().edit(line.getName(),line.getIntro(),line.getAvatar(),line.getBirth(),line.getGender()))
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .compose(new ProgressDialogTransform<>(getView(),"上传中"))
                .subscribe(o -> {
                    JUtils.Toast("上传成功");
                    getView().setResult(Activity.RESULT_OK);
                    getView().finish();
                });
    }

    @Override
    protected void onResult(int requestCode, int resultCode, Intent data) {
        super.onResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }
}
