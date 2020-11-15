package com.hap.checkinproc.Interface;

import com.google.gson.JsonArray;
import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.Model_Class.Example;
import com.hap.checkinproc.Model_Class.Extended;
import com.hap.checkinproc.Model_Class.Location;
import com.hap.checkinproc.Model_Class.Missed;
import com.hap.checkinproc.Model_Class.Model;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Model_Class.Onduty;
import com.hap.checkinproc.Model_Class.Permission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import javax.xml.transform.Result;

import okhttp3.MultipartBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {
   /*login*/
    @GET("Db_Hap.php?")
    Call<Model> login(@Query("axn") String axn, @Query("Email") String Email);

/*
    shift time*
    @GET("Db_Native.php?")
    Call<List<Example>>shiftTime(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("Sf_code")String Sf_code);
*/
    /*shift time*/
    @GET("Db_Hap.php?")
    Call<JsonArray>shiftTime(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("Sf_code")String Sf_code);

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
    @FormUrlEncoded
    @POST("db_activity.php?axn=get/menu")
    Call<ResponseBody> getMenu(@Field("data") String userData);

    @Multipart
    @POST("db_activity.php?axn=upload/procpic")
    Call<ResponseBody> uploadProcPic(@PartMap() HashMap<String, RequestBody> values, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("db_activity.php?axn=save/view")
    Call<ResponseBody> saveView(@Field("data") String userData);



    /*LEAVE APPROVAL*/

    @GET("Db_Hap.php?")
    Call<List<Approval>>approval(@Query("axn")String axn,@Query("divisionCode")String divisionCode, @Query("sfCode")String Sf_code,@Query("rSF")String rSf,@Query("State_Code")String State_code);

    /*permission approval*/
    @GET("Db_Hap.php?")
    Call<List<Permission>>permission(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("sfCode")String Sf_code, @Query("rSF")String rSf, @Query("State_Code")String State_code);
    /*onduty approval*/

   @GET("Db_Hap.php?")
    Call<List<Onduty>>onduty(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("sfCode")String Sf_code, @Query("rSF")String rSf, @Query("State_Code")String State_code);

   /*missed punch*/

 @GET("Db_Hap.php?")
 Call<List<Missed>>missed(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("sfCode")String Sf_code, @Query("rSF")String rSf, @Query("State_Code")String State_code);

  /*Extended*/
  @GET("Db_Hap.php?")
  Call<List<Extended>>extended(@Query("axn")String axn, @Query("divisionCode")String divisionCode, @Query("sfCode")String Sf_code, @Query("rSF")String rSf, @Query("State_Code")String State_code);

    @Multipart
    @POST("Db_Hap.php")
    Call<Result> uploadImage(@Part MultipartBody.Part file);
}
