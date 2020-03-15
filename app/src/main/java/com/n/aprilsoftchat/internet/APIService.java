package com.n.aprilsoftchat.internet;

import com.n.aprilsoftchat.model.Message;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("server.php")
    Call<Message> getMessages(@Query("lastId") int lastId);

    @FormUrlEncoded
    @POST("server.php")
    Call<Void> sendMessage(@Field("chat_user") String user,
                           @Field("chat_message") String text);

    @FormUrlEncoded
    @POST("server.php")
    Call<String> login(@Field("login_user") String user,
                       @Field("login_password") String password);
}
