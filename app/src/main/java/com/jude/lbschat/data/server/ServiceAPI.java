package com.jude.lbschat.data.server;


import com.jude.lbschat.domain.body.Exist;
import com.jude.lbschat.domain.body.Info;
import com.jude.lbschat.domain.body.Token;
import com.jude.lbschat.domain.entities.Account;
import com.jude.lbschat.domain.entities.PersonBrief;

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
    String SERVER_ADDRESS = "http://123.56.230.6:82/";

//    ------------------------Account----------------------------------
    @POST("account/check_account_exist.php")
    @FormUrlEncoded
    Observable<Exist> checkAccountExist(@Field("number") String number);

    @POST("account/register.php")
    @FormUrlEncoded
    Observable<Info> register(
            @Field("number") String number,
            @Field("name") String name,
            @Field("password") String password,
            @Field("code") String code);

    @POST("account/login.php")
    @FormUrlEncoded
    Observable<Account> login(
            @Field("number") String number,
            @Field("password") String password);

    @POST("account/modify_password.php")
    @FormUrlEncoded
    Observable<Info> modifyPassword(
            @Field("number") String number,
            @Field("password") String password,
            @Field("code") String code);


    @GET("account/refresh_account.php")
    Observable<Account> refreshAccount();


    @POST("account/edit.php")
    @FormUrlEncoded
    Observable<Info> edit(
            @Field("name") String name,
            @Field("intro") String intro,
            @Field("birth") long birth,
            @Field("gender") int gender,
            @Field("avatar") String avatar
            );

//    ------------------------Common----------------------------------


    @GET("qiniu.php")
    Observable<Token> getQiniuToken();

    @POST("location/location.php")
    @FormUrlEncoded
    Observable<Info> location(
            @Field("lat")double lat,
            @Field("lng")double lng,
            @Field("address_brief")String addressBrief,
            @Field("address")String address);

//    ------------------------Data----------------------------------

    @GET("person/persons.php")
    Observable<List<PersonBrief>> getPersons();

}
