package com.hap.checkinproc.Interface;

import com.hap.checkinproc.Model_Class.Example;
import com.hap.checkinproc.Model_Class.Location;
import com.hap.checkinproc.Model_Class.Model;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
   /*login*/
    @GET("Db_Hap.php?")
    Call<Model> login(@Query("axn") String axn, @Query("Email") String Email);


    /*shift time*/
    @GET("Db_Native.php?")
    Call<List<Example>>shiftTime(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("Sf_code")String Sf_code);

    /*Locations*/
    @GET("Db_Hap.php?")
    Call<List<Location>>location(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("Sf_code")String Sf_code);

    /*sending data*/
    @FormUrlEncoded
   @POST("Db_Hap.php?")
   Call<JsonObject> JsonSave(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("Sf_code")String Sf_code,@Query("State_Code")String State_code,@Query("desig")String desig, @Field("data") String body);

    @FormUrlEncoded
    @POST("db_activity.php?axn=get/view")
    Call<ResponseBody> getView(@Field("data") String userData);

}
