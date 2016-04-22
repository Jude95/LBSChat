package com.jude.lbschat.data.server;


import com.jude.lbschat.domain.body.Exist;
import com.jude.lbschat.domain.body.Info;
import com.jude.lbschat.domain.body.Token;
import com.jude.lbschat.domain.entities.Account;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Mr.Jude on 2015/11/18.
 */
public interface ServiceAPI {
    String SERVER_ADDRESS = "http://123.56.230.6/2.0/";

//    ------------------------Account----------------------------------
    @POST("account/check_account_exist.php")
    @FormUrlEncoded
    Observable<Exist> checkAccountExist(@Field("account") String account);

    @POST("account/register.php")
    @FormUrlEncoded
    Observable<Info> register(
            @Field("account") String account,
            @Field("name") String name,
            @Field("password") String password,
            @Field("code") String code);

    @POST("account/login.php")
    @FormUrlEncoded
    Observable<Account> login(
            @Field("account") String account,
            @Field("password") String password);

    @POST("account/modify_password.php")
    @FormUrlEncoded
    Observable<Info> modifyPassword(
            @Field("account") String account,
            @Field("password") String password,
            @Field("code") String code);


    @GET("account/refresh_account.php")
    Observable<Account> refreshAccount();


//    ------------------------Manager----------------------------------


    @GET("manager/admin_user_list.php")
    Observable<List<Account>> getUserList();

    @POST("manager/admin_authorization.php")
    @FormUrlEncoded
    Observable<Info> authorization(
            @Field("userId") int userId);

//    ------------------------Common----------------------------------


    @GET("qiniu.php")
    Observable<Token> getQiniuToken();


//    ------------------------Data----------------------------------

}
