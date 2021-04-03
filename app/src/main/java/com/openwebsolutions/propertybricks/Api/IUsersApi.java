package com.openwebsolutions.propertybricks.Api;

import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.Model.AddComplexModel;
import com.openwebsolutions.propertybricks.Model.AddPropertyModel;
import com.openwebsolutions.propertybricks.Model.AddSubmitProperty;
import com.openwebsolutions.propertybricks.Model.AddTenantModel;
import com.openwebsolutions.propertybricks.Model.ComplexDelete.ComplexDelete;
import com.openwebsolutions.propertybricks.Model.DeleteModel.Delete;
import com.openwebsolutions.propertybricks.Model.DeleteTenantInProperty.DeleteTenantInProperty;
import com.openwebsolutions.propertybricks.Model.Forget_Password.ForgetPassword;
import com.openwebsolutions.propertybricks.Model.GetComplexModel.GetComplexModel;
import com.openwebsolutions.propertybricks.Model.GetComplex_ByID.GetComplexByID;
import com.openwebsolutions.propertybricks.Model.GetInvoiceModel.GetInvoiceModel;
import com.openwebsolutions.propertybricks.Model.GetProperty.GetPropertyModel;
import com.openwebsolutions.propertybricks.Model.GetTenantDetails.GetTenantModel;
import com.openwebsolutions.propertybricks.Model.GmailFacebook;
import com.openwebsolutions.propertybricks.Model.Login;
import com.openwebsolutions.propertybricks.Model.PropertyAddedINComplex.PropertyAddedINComplex;
import com.openwebsolutions.propertybricks.Model.PropertyByLocation.PropertyByLocation;
import com.openwebsolutions.propertybricks.Model.PropertyEditBy_Id.GetPropertyEdit;
import com.openwebsolutions.propertybricks.Model.Register;
import com.openwebsolutions.propertybricks.Model.RemovePropertyUnderComplex.RemovePropertyUnderComplex;
import com.openwebsolutions.propertybricks.Model.Search_Item.Searchitem;
import com.openwebsolutions.propertybricks.Model.Tenants_Details.TenantDetailsPerProductId;
import com.openwebsolutions.propertybricks.Model.UpdateInvoice.UpdateInvoiceModel;
import com.openwebsolutions.propertybricks.Model.UpdateUserDemo.UpdateUser;
import com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex.ViewPropertyUnderComplex;
import com.openwebsolutions.propertybricks.UpdateInvoice.UpdateInvoice;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IUsersApi {
    int id = AppData.tenant_id;
    String location = AppData.complex_location;

    @Multipart
    @POST("register")
    Call<Register> createUser(@Part("name") RequestBody name,
                              @Part("phone") RequestBody phone,
                              @Part("email") RequestBody email,
                              @Part("address") RequestBody address,
                              @Part("password") RequestBody password,
                              @Part("comp_gstin") RequestBody comp_gstin,
                              @Part("device_token") RequestBody device_token,
                              @Part MultipartBody.Part file);
    @Multipart
    @POST("register")
    Call<Register> createUserWithoutPath(
                              @Part("name") RequestBody name,
                              @Part("phone") RequestBody phone,
                              @Part("email") RequestBody email,
                              @Part("address") RequestBody address,
                              @Part("password") RequestBody password,
                              @Part("comp_gstin") RequestBody comp_gstin,
                              @Part("device_token") RequestBody device_token,
                              @Part ("image") RequestBody image
    );

    @Multipart
    @POST("update-user")
    Call<UpdateUser> update_user(@Header("Authorization") String authorization,

                                @Part("name") RequestBody name,
                                @Part("phone") RequestBody phone,
                                @Part("email") RequestBody email,
                                @Part("address") RequestBody address,
                                @Part("comp_gstin") RequestBody comp_gstin,
                                @Part("id") RequestBody id,



                                 @Part MultipartBody.Part file);
    @Multipart
    @POST("update-user")
    Call<UpdateUser> update_user_wtoutid(@Header("Authorization") String authorization,
                                 @Part("name") RequestBody name,
                                 @Part("phone") RequestBody phone,
                                 @Part("email") RequestBody email,
                                 @Part("address") RequestBody address,
                                 @Part("comp_gstin") RequestBody comp_gstin,
                                         @Part("id") RequestBody id,


                                         @Part ("image") RequestBody image);

    @FormUrlEncoded
    @POST("login")
    Call<Login> loginUser(@Field("email") String email,
                          @Field("password") String password,
                          @Field("device_token") String device_token);
    @FormUrlEncoded
    @POST("gmail-user-login")
    Call<GmailFacebook> loginSignup(
            @Field("gmail_id") String id,
//            @Part("phone") RequestBody phone,
            @Field("email") String email,
//            @F("password") RequestBody password,
            @Field("device_token") String device_token
//            @Part ("image") RequestBody image
    );


    @Multipart
@POST("add-property")
Call<AddPropertyModel> addpropertyUser(@Header("Authorization") String authorization,
                                       @Part MultipartBody.Part file,
                                       @Part("property_location") RequestBody property_location,
                                       @Part("property_name") RequestBody property_name,
                                       @Part("property_des") RequestBody property_des,
                                       @Part("property_price") RequestBody property_price,
                                       @Part("latitude") RequestBody latitude,
                                       @Part("longitude") RequestBody longitude);

    @Multipart
    @POST("add-property")
    Call<AddSubmitProperty> addsubmitpropertyUser(@Header("Authorization") String authorization,
                                            @Part MultipartBody.Part file,
                                            @Part("property_location") RequestBody property_location,
                                            @Part("property_name") RequestBody property_name,
                                            @Part("property_des") RequestBody property_des,
                                            @Part("property_price") RequestBody property_price);


@Multipart
@POST("add-complex")
Call<AddComplexModel> addcomplexUser(@Header("Authorization") String authorization,
                                          @Part MultipartBody.Part file,
                                          @Part("complex_name") RequestBody complex_name,
                                          @Part("complex_des") RequestBody complex_des,
                                          @Part("complex_location") RequestBody complex_location,
                                     @Part("latitude") RequestBody latitude,
                                     @Part("longitude") RequestBody longitude);
    @Multipart
    @POST("add-tenant")
    Call<AddTenantModel> addtenantUser(@Header("Authorization") String authorization,
                                       @Part MultipartBody.Part file,
                                       @Part("tenantOwner_name") RequestBody tenantOwner_name,
                                       @Part("tenantOwner_phone") RequestBody tenantOwner_phone,
                                       @Part("tenantOwner_email") RequestBody tenantOwner_email,
                                       @Part("tenantOwner_address") RequestBody tenantOwner_address,
                                       @Part("tenantOwner_entrydate") RequestBody tenantOwner_entrydate,
                                       @Part("tenantOwner_paymentcycle") RequestBody tenantOwner_paymentcycle,
                                       @Part("billing_address") RequestBody billing_address,
                                       @Part("comp_Gstin") RequestBody comp_Gstin,
                                        @Part("property_id") RequestBody property_id);
    @Multipart
    @POST("add-tenant/{id}")
    Call<AddTenantModel> addtenantUserByID_Path(@Path("id")String id,@Header("Authorization") String authorization,
                                       @Part MultipartBody.Part file,
                                       @Part("tenantOwner_name") RequestBody tenantOwner_name,
                                       @Part("tenantOwner_phone") RequestBody tenantOwner_phone,
                                       @Part("tenantOwner_email") RequestBody tenantOwner_email,
                                       @Part("tenantOwner_address") RequestBody tenantOwner_address,
                                       @Part("tenantOwner_entrydate") RequestBody tenantOwner_entrydate,
                                       @Part("tenantOwner_paymentcycle") RequestBody tenantOwner_paymentcycle,
                                       @Part("billing_address") RequestBody billing_address,
                                       @Part("comp_Gstin") RequestBody comp_Gstin,
                                       @Part("property_id") RequestBody property_id);

    @Multipart
    @POST("add-tenant/{id}")
    Call<AddTenantModel> addtenantUserByID(@Path("id")String id,@Header("Authorization") String authorization,
                                                @Part("tenantOwner_name") RequestBody tenantOwner_name,
                                                @Part("tenantOwner_phone") RequestBody tenantOwner_phone,
                                                @Part("tenantOwner_email") RequestBody tenantOwner_email,
                                                @Part("tenantOwner_address") RequestBody tenantOwner_address,
                                                @Part("tenantOwner_entrydate") RequestBody tenantOwner_entrydate,
                                                @Part("tenantOwner_paymentcycle") RequestBody tenantOwner_paymentcycle,
                                                @Part("billing_address") RequestBody billing_address,
                                                @Part("comp_Gstin") RequestBody comp_Gstin,
                                                @Part("property_id") RequestBody property_id);




    @Multipart
    @POST("add-property/{id}")
    Call<AddPropertyModel> addpropertyUserById_Path(@Path("id") String id,@Header("Authorization") String authorization,
                                           @Part MultipartBody.Part file,
                                           @Part("property_location") RequestBody property_location,
                                           @Part("property_name") RequestBody property_name,
                                           @Part("property_des") RequestBody property_des,
                                           @Part("property_price") RequestBody property_price,
                                                    @Part("latitude") RequestBody latitude,
                                                    @Part("longitude") RequestBody longitude);

    @Multipart
    @POST("add-complex/{id}")
    Call<AddComplexModel> addcomplexUserById_Path(@Path("id") String id,@Header("Authorization") String authorization,
                                                    @Part MultipartBody.Part file,
                                                    @Part("complex_location") RequestBody complex_location,
                                                    @Part("complex_name") RequestBody complex_name,
                                                    @Part("complex_des") RequestBody complex_des,
                                                    @Part("latitude") RequestBody latitude,
                                                    @Part("longitude") RequestBody longitude);
    @Multipart
    @POST("add-complex/{id}")
    Call<AddComplexModel> addcomplexUserById(@Path("id") String id,@Header("Authorization") String authorization,
                                                  @Part ("image") RequestBody image,
                                                  @Part("complex_location") RequestBody complex_location,
                                                  @Part("complex_name") RequestBody complex_name,
                                                  @Part("complex_des") RequestBody complex_des,
                                                  @Part("latitude") RequestBody latitude,
                                                  @Part("longitude") RequestBody longitude);

    @Multipart
    @POST("add-property/{id}")
    Call<AddPropertyModel> addpropertyUserById(@Path("id") String id,@Header("Authorization") String authorization,

                                                    @Part("property_location") RequestBody property_location,
                                                    @Part("property_name") RequestBody property_name,
                                                    @Part("property_des") RequestBody property_des,
                                                    @Part("property_price") RequestBody property_price,
                                               @Part("latitude") RequestBody latitude,
                                               @Part("longitude") RequestBody longitude);

    @Multipart
    @POST("delete-property/{id}")
    Call<Delete> deletepropertyUserById(@Path("id") String id, @Header("Authorization") String authorization,
                                        @Part("property_name") RequestBody property_name);
    @Multipart
    @POST("delete-tenant/{id}")
    Call<DeleteTenantInProperty> deletetenantByIdinProperty(@Path("id") String id, @Header("Authorization") String authorization,
                                                  @Part("property_name") RequestBody property_name);

@GET("get-property")
    Call<GetPropertyModel>getpropertyUser(@Header("Authorization")String authorization);

 @GET("get-tenant")
    Call<GetTenantModel>gettenantyUser(@Header("Authorization")String authorization);

    @GET("get-complex")
    Call<GetComplexModel>getcomplexyUser(@Header("Authorization")String authorization);

    @GET("get-data-for-edit/AddTenant/{id}")
    Call<TenantDetailsPerProductId>getTenantUser(@Path("id") String id, @Header("Authorization")String authorization);

    @GET("get-property-for-edit/{id}")
    Call<GetPropertyEdit>getPropertyByIdUser(@Path("id") String id, @Header("Authorization")String authorization);

    @GET("get-complex/{id}")
    Call<GetComplexByID>getComplexByIdUser(@Path("id") String id, @Header("Authorization")String authorization);

    @GET("delete-complex/{id}")
    Call<ComplexDelete>getComplexDeleteIdUser(@Path("id") String id, @Header("Authorization")String authorization);

    @GET("location-search/{id}")
    Call<PropertyByLocation>getlist_PropertyInComplexIdUser(@Path("id") String id, @Header("Authorization")String authorization);

    @GET("add-property-to-complex/{property_id}/{complex_id}")
    Call<PropertyAddedINComplex>add_PropertyInComplexUser(@Path("property_id") String property_id,@Path("complex_id") String complex_id, @Header("Authorization")String authorization);


    @GET("view-property/{id}")
    Call<ViewPropertyUnderComplex>getPropertylist_IN_Complex(@Path("id") String id, @Header("Authorization")String authorization);

    @GET("delete-property-from-complex/{id}")
    Call<RemovePropertyUnderComplex>deletePropertyfromComplex(@Path("id") String id, @Header("Authorization")String authorization);

    @GET("search-complex-property/{search}")
    Call<Searchitem>getSearchItem(@Path("search") String search, @Header("Authorization")String authorization);

    @GET("send-mail/{email}")
    Call<ForgetPassword>getforget_password(@Path("email") String email);

    @GET("get-invoice/{tenant_id}")
    Call<GetInvoiceModel>getinvoicedata(@Path("tenant_id") String tenant_id,@Header("Authorization")String authorization);
    @Multipart
    @POST("update-invoice/{id}")
    Call<UpdateInvoiceModel> update_invoice(@Path("id") String tenant_id, @Header("Authorization") String authorization,


                                            @Part MultipartBody.Part file
                                            );

}
