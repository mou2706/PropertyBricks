package com.openwebsolutions.propertybricks.Api;

import android.util.Log;

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
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static IUsersApi service;
    private static ApiManager apiManager;

    private ApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.199:8001/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IUsersApi.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    //signup
    public void createUser(RequestBody name,RequestBody phone,RequestBody email,RequestBody address,RequestBody password,RequestBody comp_gstin,RequestBody device_token,MultipartBody.Part image, Callback<Register> callback) {
        Call<Register> userCall = service.createUser(name,phone,email,address,password,comp_gstin,device_token,image);
        userCall.enqueue(callback);
    }
    //signup without Image
    public void createUserWithoutPath(RequestBody name,RequestBody phone,RequestBody email,RequestBody address,RequestBody password,RequestBody comp_gstin,RequestBody device_token,RequestBody image, Callback<Register> callback) {
        Call<Register> userCall = service.createUserWithoutPath(name,phone,email,address,password,comp_gstin,device_token,image);
        userCall.enqueue(callback);
    }

    //update user
    public void update_user(String authorization,RequestBody name,RequestBody phone,RequestBody email,RequestBody address,RequestBody comp_gstin,RequestBody id,MultipartBody.Part image, Callback<UpdateUser> callback) {
        Call<UpdateUser> userCall = service.update_user(authorization,name,phone,email,address,comp_gstin,id,image);
        userCall.enqueue(callback);
    }
    //update invoice
//    public void update_invoice(String authorization,RequestBody month Callback<UpdateInvoice> callback) {
//        Call<UpdateInvoice> userCall = service.update_invoice(authorization,month);
//        userCall.enqueue(callback);
//    }

    //update user without image
    public void update_user_wtoutid(String authorization, RequestBody name, RequestBody phone, RequestBody email, RequestBody address, RequestBody comp_gstin,RequestBody id, RequestBody image, Callback<UpdateUser> callback) {
        Call<UpdateUser> userCall = service.update_user_wtoutid(authorization,name,phone,email,address,comp_gstin,id,image);
        userCall.enqueue(callback);
    }

    //login
    public void loginUser(String email, String password,String device_token, Callback<Login> callback){
        Call<Login> userCall = service.loginUser(email,password,device_token);
        userCall.enqueue(callback);
    }

    //logingmailfacebook
    public void loginSignup(String id,String email, String device_token , Callback<GmailFacebook> callback){
        Call<GmailFacebook> userCall =  service.loginSignup(id,email,device_token);
        userCall.enqueue(callback);
    }

    //addproperty
    public void addpropertyUser(String authorization,MultipartBody.Part image, RequestBody location, RequestBody name, RequestBody description, RequestBody price,RequestBody latitude,RequestBody longitude, Callback<AddPropertyModel> callback){

        Call<AddPropertyModel> userCall = service.addpropertyUser(authorization,image,location,name,description,price,latitude,longitude);
        userCall.enqueue(callback);
    }

    //addpropertyById_Path
    public void addpropertyUserById_Path(Integer _id,String authorization,MultipartBody.Part image, RequestBody location, RequestBody name, RequestBody description, RequestBody price,RequestBody latitude,RequestBody longitude, Callback<AddPropertyModel> callback){

        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<AddPropertyModel> userCall = service.addpropertyUserById_Path(id,authorization,image,location,name,description,price,latitude,longitude);
        userCall.enqueue(callback);
    }

    public void addpropertysbmitUser(String authorization,MultipartBody.Part image, RequestBody location, RequestBody name, RequestBody description, RequestBody price, Callback<AddSubmitProperty> callback){

        Call<AddSubmitProperty> userCall = service.addsubmitpropertyUser(authorization,image,location,name,description,price);
        userCall.enqueue(callback);
    }



    //addcomplexById_Path
    public void addcomplexUserById_Path(Integer _id,String authorization,MultipartBody.Part image, RequestBody location, RequestBody name, RequestBody description,RequestBody latitude,RequestBody longitude, Callback<AddComplexModel> callback){

        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<AddComplexModel> userCall = service.addcomplexUserById_Path(id,authorization,image,location,name,description,latitude,longitude);
        userCall.enqueue(callback);
    }
    //addcomplexById
    public void addcomplexUserById(Integer _id,String authorization,RequestBody image, RequestBody location, RequestBody name, RequestBody description,RequestBody latitude,RequestBody longitude, Callback<AddComplexModel> callback){

        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<AddComplexModel> userCall = service.addcomplexUserById(id,authorization,image,location,name,description,latitude,longitude);
        userCall.enqueue(callback);
    }

    //addpropertyById
    public void addpropertyUserById(Integer _id,String authorization, RequestBody location, RequestBody name, RequestBody description, RequestBody price,RequestBody latitude,RequestBody longitude, Callback<AddPropertyModel> callback){

        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<AddPropertyModel> userCall = service.addpropertyUserById(id,authorization,location,name,description,price,latitude,longitude);
        userCall.enqueue(callback);
    }

    //addcomplex
    public void addcomplexUser(String authorization,MultipartBody.Part image, RequestBody complex_name, RequestBody complex_des, RequestBody complex_location,RequestBody latitude,RequestBody longitude, Callback<AddComplexModel> callback){

        Call<AddComplexModel> userCall = service.addcomplexUser(authorization,image,complex_name,complex_des,complex_location,latitude,longitude);
        userCall.enqueue(callback);
    }

    //addTenant
    public void addtenantUser(String authorization,MultipartBody.Part image, RequestBody tenantOwner_name, RequestBody tenantOwner_phone, RequestBody tenantOwner_email,RequestBody tenantOwner_address,RequestBody tenantOwner_entrydate,RequestBody tenantOwner_paymentcycle,RequestBody billing_address,RequestBody comp_Gstin,RequestBody property_id, Callback<AddTenantModel> callback){

        Call<AddTenantModel> userCall = service.addtenantUser(authorization,image,tenantOwner_name,tenantOwner_phone,tenantOwner_email,tenantOwner_address,tenantOwner_entrydate,tenantOwner_paymentcycle,billing_address,comp_Gstin,property_id);
        userCall.enqueue(callback);
    }

    //addTenantBy_Id_Path
    public void addtenantUserByID_Path(Integer _id,String authorization,MultipartBody.Part image, RequestBody tenantOwner_name, RequestBody tenantOwner_phone, RequestBody tenantOwner_email,RequestBody tenantOwner_address,RequestBody tenantOwner_entrydate,RequestBody tenantOwner_paymentcycle,RequestBody billing_address,RequestBody comp_Gstin,RequestBody property_id, Callback<AddTenantModel> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<AddTenantModel> userCall = service.addtenantUserByID_Path(id,authorization,image,tenantOwner_name,tenantOwner_phone,tenantOwner_email,tenantOwner_address,tenantOwner_entrydate,tenantOwner_paymentcycle,billing_address,comp_Gstin,property_id);
        userCall.enqueue(callback);
    }
    //addTenantBy_Id
    public void addtenantUserByID(Integer _id,String authorization, RequestBody tenantOwner_name, RequestBody tenantOwner_phone, RequestBody tenantOwner_email,RequestBody tenantOwner_address,RequestBody tenantOwner_entrydate,RequestBody tenantOwner_paymentcycle,RequestBody billing_address,RequestBody comp_Gstin,RequestBody property_id, Callback<AddTenantModel> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<AddTenantModel> userCall = service.addtenantUserByID(id,authorization,tenantOwner_name,tenantOwner_phone,tenantOwner_email,tenantOwner_address,tenantOwner_entrydate,tenantOwner_paymentcycle,billing_address,comp_Gstin,property_id);
        userCall.enqueue(callback);
    }


    //getproperty
    public  void getPropertyUser(String authorization, Callback<GetPropertyModel> callback){
        Call<GetPropertyModel> userCall = service.getpropertyUser(authorization);
        userCall.enqueue(callback);
    }


    //gettenant
    public  void gettenantyUser(String authorization, Callback<GetTenantModel> callback){
        Call<GetTenantModel> userCall = service.gettenantyUser(authorization);
        userCall.enqueue(callback);
    }

    //getinvoicemonth
    public  void getinvoicedata(Integer tenant_id,String authorization, Callback<GetInvoiceModel> callback){
        Log.d("error",""+getInstance());
        Log.d("tenant_id",""+tenant_id);
        String id=String.valueOf(tenant_id);
        Call<GetInvoiceModel> userCall = service.getinvoicedata(id,authorization);
        userCall.enqueue(callback);
    }
    public  void update_invoice(Integer tenant_id, String authorization, MultipartBody.Part image , Callback<UpdateInvoiceModel> callback){
        Log.d("error",""+getInstance());
        Log.d("tenant_id",""+tenant_id);
        String id=String.valueOf(tenant_id);
        Call<UpdateInvoiceModel> userCall =  service.update_invoice(id,authorization,image);
        userCall.enqueue(callback);
    }

    //getcomplex
    public  void getcomplexyUser(String authorization, Callback<GetComplexModel> callback){
        Call<GetComplexModel> userCall = service.getcomplexyUser(authorization);
        userCall.enqueue(callback);
    }

    //getTenantUser
    public  void getTenantUser_id(Integer _id,String authorization, Callback<TenantDetailsPerProductId> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<TenantDetailsPerProductId> userCall = service.getTenantUser(id,authorization);
        userCall.enqueue(callback);

    }

//    //getProperty_by_location
//    public  void getPropertyByLocationUser(String _location,String authorization, Callback<PropertyByLocation> callback){
//        Log.d("error",""+getInstance());
//        Log.d("location",""+_location);
//
//        Call<PropertyByLocation> userCall = service.getPropertyByLocationUser(_location,authorization);
//        userCall.enqueue(callback);
//
//    }

    //getProperty_by_id
    public  void getPropertyByIdUser(Integer _id,String authorization, Callback<GetPropertyEdit> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<GetPropertyEdit> userCall = service.getPropertyByIdUser(id,authorization);
        userCall.enqueue(callback);
    }

    //getComplex_by_id
    public  void getComplexByIdUser(Integer _id,String authorization, Callback<GetComplexByID> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<GetComplexByID> userCall = service.getComplexByIdUser(id,authorization);
        userCall.enqueue(callback);
    }
    //getComplexDelete_by_id
    public  void getComplexDeleteIdUser(Integer _id,String authorization, Callback<ComplexDelete> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<ComplexDelete> userCall = service.getComplexDeleteIdUser(id,authorization);
        userCall.enqueue(callback);
    }

    //getlist_PropertyInComplex
    public  void getlist_PropertyInComplexIdUser(Integer _id,String authorization, Callback<PropertyByLocation> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<PropertyByLocation> userCall = service.getlist_PropertyInComplexIdUser(id,authorization);
        userCall.enqueue(callback);
    }

    //getPropertylist_IN_Complex
    public  void getPropertylist_IN_Complex(Integer _id,String authorization, Callback<ViewPropertyUnderComplex> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<ViewPropertyUnderComplex> userCall = service.getPropertylist_IN_Complex(id,authorization);
        userCall.enqueue(callback);
    }

    //get_add_PropertyInComplex
    public  void add_PropertyInComplexUser(Integer _property_id,Integer _complex_id,String authorization, Callback<PropertyAddedINComplex> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_property_id);
        String property_id=String.valueOf(_property_id);
        Log.d("id1",""+_complex_id);
        String complex_id=String.valueOf(_complex_id);
        Call<PropertyAddedINComplex> userCall = service.add_PropertyInComplexUser(property_id,complex_id,authorization);
        userCall.enqueue(callback);
    }

    //delete property
    public  void deletepropertyUserById(Integer _id,String authorization,RequestBody name, Callback<Delete> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<Delete> userCall = service.deletepropertyUserById(id,authorization,name);
        userCall.enqueue(callback);
    }

    //delete tenant in property
    public  void deletetenantByIdinProperty(Integer _id,String authorization,RequestBody name, Callback<DeleteTenantInProperty> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<DeleteTenantInProperty> userCall = service.deletetenantByIdinProperty(id,authorization,name);
        userCall.enqueue(callback);
    }

    //delete property from complex
    public  void deletePropertyfromComplex(Integer _id,String authorization, Callback<RemovePropertyUnderComplex> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_id);
        String id=String.valueOf(_id);
        Call<RemovePropertyUnderComplex> userCall = service.deletePropertyfromComplex(id,authorization);
        userCall.enqueue(callback);
    }


    //search property and complex
    public  void getSearchItem(String _search,String authorization, Callback<Searchitem> callback){
        Log.d("error",""+getInstance());
        Log.d("id",""+_search);
        String search=_search;
        Call<Searchitem> userCall = service.getSearchItem(search,authorization);
        userCall.enqueue(callback);
    }

    //get Forget Password
    public  void getforget_password(String _email, Callback<ForgetPassword> callback){
        Log.d("error",""+getInstance());
        Log.d("email",""+_email);
        String email=_email;
        Call<ForgetPassword> userCall = service.getforget_password(email);
        userCall.enqueue(callback);
    }


}
