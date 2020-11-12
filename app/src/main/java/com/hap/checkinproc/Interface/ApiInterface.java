package com.hap.checkinproc.Interface;

import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.Model_Class.Example;
import com.hap.checkinproc.Model_Class.Location;
import com.hap.checkinproc.Model_Class.Model;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    @POST("db_activity.php?axn=get/view")
    Call<ResponseBody> getView(@Field("data") String userData);

    /*LEAVE APPROVAL*/

    @GET("Db_Hap.php?")
    Call<List<Approval>>approval(@Query("axn")String axn,@Query("divisionCode")String divisionCode, @Query("sfCode")String Sf_code,@Query("rSF")String rSf,@Query("State_Code")String State_code);

 @FormUrlEncoded
 @POST("Db_Hap.php?axn=table/list")
 Call<List<Work_Type_Model>> Getworktype(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                         @Query("rSF") String rSF, @Query("State_Code") String StateCode,
                                         @Field("data") String data);
 @FormUrlEncoded
 @POST("Db_Hap.php?axn=dcr/save")
 Call<Object> Tb_Mydayplan(@QueryMap Map<String, String> params, @Field("data") String body);

 @FormUrlEncoded
 @POST("Db_Hap.php?axn=table/list")
 Call<Object> GetRouteObject(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                             @Query("rSF") String rSF, @Query("State_Code") String StateCode,
                             @Field("data") String data);
 @FormUrlEncoded
 @POST("Db_Hap.php")
 Call<Object> GetTPObject(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                             @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("axn") String axn,
                             @Field("data") String data);
 @FormUrlEncoded
 @POST("Db_Hap.php?axn=table/list")
 Call<Object> GettpRespnse(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                             @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("CMonth") String CMonth, @Query("CYr") String CYr,
                             @Field("data") String data);
 @FormUrlEncoded
 @POST("Db_Hap.php?axn=dcr/save")
 Call<JsonObject> GetResponseBody(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                           @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("month") String CMonth, @Query("year") String CYr,
                           @Field("data") String data);
 @FormUrlEncoded
 @POST("Db_Hap.php")
 Call<JsonObject> DCRSave(@QueryMap Map<String, String> params, @Field("data") String body);
    @FormUrlEncoded
    @POST("Db_Hap.php")
    Call<Object> GetTPObject1(@Query("AMod") String Amod, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                              @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Query("axn") String axn,
                              @Field("data") String data);
 @FormUrlEncoded
 @POST("Db_Hap.php?axn=dcr/save")
 Call<JsonObject> leaveSubmit(@Query("sf_name") String SfName, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                              @Query("State_Code") String StateCode, @Query("desig") String desig, @Field("data") String data);

 @POST("Db_Hap.php?axn=get/LeaveAvailabilityCheck")
 Call<Object> remainingLeave(@Query("Year") String Year, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                             @Query("rSF") String rSF, @Query("State_Code") String StateCode);

 @FormUrlEncoded
 @POST("Db_Hap.php?axn=get/tknPerm")
 Call<Object> availabilityLeave(@Query("PDt") String PDT, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Field("data") String data);



 @FormUrlEncoded
 @POST("Db_Hap.php?axn=dcr/save")
 Call<JsonObject> mmDates(@Query("id") String ID, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                          @Query("rSF") String RSF, @Query("State_Code") String StateCode, @Field("data") String data);


 @FormUrlEncoded
 @POST("Db_Hap.php?axn=get/calpriod")
 Call<Object> mmDate(@Query("id") String ID, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                     @Query("rSF") String RSF, @Query("State_Code") String StateCode, @Field("data") String data);


 @FormUrlEncoded
 @POST("Db_Hap.php?axn=GetMissed_Punch")
 Call<Object> missedPunch(@Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                          @Query("rSF") String rSF, @Query("State_Code") String StateCode, @Field("data") String data);


 @FormUrlEncoded
 @POST("Db_Hap.php?axn=dcr/save")
 Call<JsonObject> SubmitmissedPunch(@Query("sf_name") String SFName, @Query("divisionCode") String disvisonCode, @Query("sfCode") String sFCode,
                                    @Query("State_Code") String StateCode, @Query("desig") String desig, @Field("data") String data);
}
