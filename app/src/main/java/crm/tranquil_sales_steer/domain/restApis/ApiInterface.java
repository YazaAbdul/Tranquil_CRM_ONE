package crm.tranquil_sales_steer.domain.restApis;

import android.graphics.Bitmap;

import java.util.ArrayList;

import crm.tranquil_sales_steer.domain.services.CallersDialogActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.DashBoardIncomingCallsActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.LeadEditActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.LeadHistoryActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.SearchViewActivity;
import crm.tranquil_sales_steer.ui.activities.start_ups.LoginActivity;
import crm.tranquil_sales_steer.ui.activities.tags.TagDetailsActivity;
import crm.tranquil_sales_steer.ui.activities.templates.EmailTemplatesActivity;
import crm.tranquil_sales_steer.ui.activities.templates.SendTemplatesActivity;
import crm.tranquil_sales_steer.ui.activities.templates.SmsTemplatesActivity;
import crm.tranquil_sales_steer.ui.fragments.ActivitiesFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadCommunicationFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadHistoryFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadJourneyFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadProfileFragment;
import crm.tranquil_sales_steer.ui.models.ActivestatusResponse;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;
import crm.tranquil_sales_steer.ui.models.AgentsMainResponse;
import crm.tranquil_sales_steer.ui.models.AllActivityResponse;
import crm.tranquil_sales_steer.ui.models.AllDataResponse;
import crm.tranquil_sales_steer.ui.models.AllStatusResoponse;
import crm.tranquil_sales_steer.ui.models.AllStatusResponse;
import crm.tranquil_sales_steer.ui.models.AllUploadFilesResponse;
import crm.tranquil_sales_steer.ui.models.AskMeResponse;
import crm.tranquil_sales_steer.ui.models.AttendancePunchOutResponse;
import crm.tranquil_sales_steer.ui.models.AutoFollowupResponse;
import crm.tranquil_sales_steer.ui.models.AvailableProjectsResponse;
import crm.tranquil_sales_steer.ui.models.CampaignCallResponse;
import crm.tranquil_sales_steer.ui.models.CardsResponse;
import crm.tranquil_sales_steer.ui.models.CollectionsResponse;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import crm.tranquil_sales_steer.ui.models.CreateDayreportResponse;
import crm.tranquil_sales_steer.ui.models.CreateLeadResponse;
import crm.tranquil_sales_steer.ui.models.CreativesResponse;
import crm.tranquil_sales_steer.ui.models.CreatreUserResponse;
import crm.tranquil_sales_steer.ui.models.CrmSearchNumberResponse;
import crm.tranquil_sales_steer.ui.models.DashBoardResponseNew;
import crm.tranquil_sales_steer.ui.models.DashboardProfileResponse;
import crm.tranquil_sales_steer.ui.models.DirectModelResponse;
import crm.tranquil_sales_steer.ui.models.DisableResponse;
import crm.tranquil_sales_steer.ui.models.DueCustomersListResponse;
import crm.tranquil_sales_steer.ui.models.DueCustomersResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeCallCreativesResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeTrackResponse;
import crm.tranquil_sales_steer.ui.models.EndPointResponse;
import crm.tranquil_sales_steer.ui.models.FetchItemResponse;
import crm.tranquil_sales_steer.ui.models.FoldersResponse;
import crm.tranquil_sales_steer.ui.models.GetBaseUrlResponse;
import crm.tranquil_sales_steer.ui.models.GetCompanyUserdlt;
import crm.tranquil_sales_steer.ui.models.GetModuleNameResponse;
import crm.tranquil_sales_steer.ui.models.GetlogoResponse;
import crm.tranquil_sales_steer.ui.models.GettopicnamesResponse;
import crm.tranquil_sales_steer.ui.models.ImagescreateiveResponse;
import crm.tranquil_sales_steer.ui.models.InsertAudioResponse;
import crm.tranquil_sales_steer.ui.models.InsertContactsResponse;
import crm.tranquil_sales_steer.ui.models.KnowlarityCallsResponse;
import crm.tranquil_sales_steer.ui.models.KnowlarityResponse;
import crm.tranquil_sales_steer.ui.models.MeetSubmitionRespons;
import crm.tranquil_sales_steer.ui.models.MeetingResponse;
import crm.tranquil_sales_steer.ui.models.MessageResponse;
import crm.tranquil_sales_steer.ui.models.NewLeadPlansResponse;
import crm.tranquil_sales_steer.ui.models.NextCallResponse;
import crm.tranquil_sales_steer.ui.models.NextReceivingCallResponse;
import crm.tranquil_sales_steer.ui.models.NextTeleCallersResponse;
import crm.tranquil_sales_steer.ui.models.NotesResponse;
import crm.tranquil_sales_steer.ui.models.PdfModel;
import crm.tranquil_sales_steer.ui.models.PunchInResponse;
import crm.tranquil_sales_steer.ui.models.PunchStatusResponse;
import crm.tranquil_sales_steer.ui.models.RecordingsList;
import crm.tranquil_sales_steer.ui.models.RequirementResponse;
import crm.tranquil_sales_steer.ui.models.RequrimentdropdownResponse;
import crm.tranquil_sales_steer.ui.models.SaleDoneResponse;
import crm.tranquil_sales_steer.ui.models.SearchResponse;
import crm.tranquil_sales_steer.ui.models.SendFileResponse;
import crm.tranquil_sales_steer.ui.models.SiteVisitStartResponse;
import crm.tranquil_sales_steer.ui.models.SourceResponse;
import crm.tranquil_sales_steer.ui.models.SourceTypeLeadsResponse;
import crm.tranquil_sales_steer.ui.models.SourceTypesResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import crm.tranquil_sales_steer.ui.models.TeleCallersDataResponse;
import crm.tranquil_sales_steer.ui.models.TeleResponse;
import crm.tranquil_sales_steer.ui.models.TeleSourceTypeResponse;
import crm.tranquil_sales_steer.ui.models.TelecallersResponse;
import crm.tranquil_sales_steer.ui.models.UserStatusResponse;
import crm.tranquil_sales_steer.ui.models.Versioncontrol;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

//import crm.tranquil_sales_steer.ui.models._PaymentModesResponse;

/**
 * Created by venkei on 02-Apr-18.
 */

public interface ApiInterface {

    @GET("mobileapp/empwhatsappcretives")
    Call<EmployeeCallCreativesResponse>  getEmployeeCallCreatives  (@Query("lead_id") String lead_id,
                                                                    @Query("created_by") String created_by);
    @GET("mobileapp/dailynewsales")
    Call<ArrayList<SaleDoneResponse>> getSaleDone  (@Query("user_id") String user_id,
                                                    @Query("user_type") String user_type,
                                                    @Query("start") int start);
    @GET("mobileapp/dailycollection")
    Call<ArrayList<CollectionsResponse>> getDailyCollections  (@Query("user_id") String user_id,
                                                               @Query("user_type") String user_type,
                                                               @Query("start") int start);

    @GET("mobileapp/duecustomerslist")
    Call<ArrayList<DueCustomersListResponse>> getDueCustomersList (@Query("user_id") String user_id,
                                                                   @Query("user_type") String user_type);


    @GET("mobileapp/duecustomerssummary")
    Call<ArrayList<DueCustomersResponse>> getDueCustomers ();

    @GET("mobileapp/creativesnew")
    Call<ArrayList<CreativesResponse>> getCreatives (@Query("start") int start);

    @POST("mobileapp/setusercontactlist")
    Call<InsertContactsResponse> pushUserContactsToServer (@Body RequestBody body);

    /*@POST("setusercontactlist")
    Call<InsertContactsResponse> pushUserContactsToServer (@Body JsonArray contactsArray);*/

    @GET("mobileapp/getemployeetrack")
    Call<ArrayList<EmployeeTrackResponse>> getEmployeeTrack (@Query("user_id") String user_id,
                                                             @Query("start") int start);

    @GET("mobileapp/getknowlaritycalls")
    Call<ArrayList<KnowlarityCallsResponse>> getKnowlarityCalls(@Query("user_id") String user_id);



    @GET("mobileapp/nextReceivingCallDetails")
    Call<NextReceivingCallResponse> getNextReceivingCall (@Query("user_id") String user_id);

    @GET("mobileapp/getUserStatus")
    Call<UserStatusResponse> getUserStatus (@Query("user_id") String user_id);

    @GET("http://tranquilcrm.in/ss/Autocalling/runAutocaller")
    Call<CampaignCallResponse> getCampaignCall (@Query("user_id") String user_id);

    @GET("mobileapp/campaigncallstatusupdate")
    Call<CampaignCallResponse> updateCampaignCallStatus (@Query("mobile_number") String mobile_number,
                                                         @Query("status") String status,
                                                         @Query("call_id") String call_id);

    @GET("mobileapp/autofollowupcalls")
    Call<ArrayList<AutoFollowupResponse>> getAutoFollowups (@Query("user_id") String user_id);

    @GET("mobileapp/getnextautofollowupcall")
    Call<AutoFollowupResponse> getNextAutoFollowupCall (@Query("user_id") String user_id);

    @GET("mobileapp/login")
    Call<ArrayList<LoginActivity.LoginResponse>> getLoginResponse(@Query("email") String email,
                                                                  @Query("password") String password,
                                                                  @Query("clientid") String clientid);

    @GET("createuser")
    Call<CreatreUserResponse> getSingupResponse(
            @Query("user_name")String user_name,
            @Query("mobile_number")String mobile_number,
            @Query("email_id")String email_id,
            @Query("password")String password
    );


    @GET("mobileapp/updateUserStatus")
    Call<UserStatusResponse> getUpdateUserStatus (@Query("user_id") String user_id,
                                                  @Query("status") String status);



    @GET("mobileapp/getlogo")
    Call<ArrayList<GetlogoResponse>>  getlogo();


@GET("getbaseurl")
Call<ArrayList<GetBaseUrlResponse>> getBaseUrl(@Query("secret_key") String secret_key);



@GET("mobileapp/getcompanyuserdlt")
Call<ArrayList<GetCompanyUserdlt>> getCompanyUserdlt();




    @GET("mobileapp/Versioncontrol")
    Call<Versioncontrol> versionControl(@Query("v") String v);


    @GET("mobileapp/changepassword")
    Call<ArrayList<StatusResponse>> getChangePasswordResponse(@Query("user_id") String user_id,
                                                              @Query("presentpassword") String presentpassword,
                                                              @Query("newpassword") String newpassword);

    @GET("mobileapp/requirements")
    Call<ArrayList<RequirementResponse>> getRequirements(@Query("company_id") String company_id);


    @GET("mobileapp/allstatus")
    Call<ArrayList<AllStatusResoponse>> getAllStatus();




    @GET("mobileapp/mobilenocheck")
    Call<StatusResponse> checkCustomerNumber(@Query("mobile") String mobile);

    @GET("mobileapp/sourcetypes")
    Call<ArrayList<SourceResponse>> getSource(@Query("company_id") String company_id);

    @GET("mobileapp/sourcetypeleadcount")
    Call<ArrayList<SourceTypesResponse>> getSourceType(@Query("user_id") String user_id,
                                                       @Query("user_type") String user_type);

    @GET("mobileapp/sourcewiseleads")
    Call<ArrayList<SourceTypeLeadsResponse>> getSourceTypeLeads(@Query("user_id") String user_id,
                                                                @Query("user_type") String user_type,
                                                                @Query("src_id") String src_id,
                                                                @Query("start") int start);

    @GET("mobileapp/projecttypeleadcount")
    Call<ArrayList<SourceTypesResponse>> getProjectTypes(@Query("user_id") String user_id,
                                                         @Query("user_type") String user_type);

    @GET("mobileapp/projectwiseleads")
    Call<ArrayList<SourceTypeLeadsResponse>> getProjectTypeLeads(@Query("user_id") String user_id,
                                                                 @Query("user_type") String user_type,
                                                                 @Query("pjt_id") String pjt_id,
                                                                 @Query("start") int start);

    @GET("mobileapp/activities")
    Call<ArrayList<ActivityMainResponse>> getActivities(@Query("company_id") String company_id);
    @GET("mobileapp/requirement")
    Call<ArrayList<RequrimentdropdownResponse>> getRequirement();

    @GET("mobileapp/sourcetypestele")
    Call<ArrayList<TeleSourceTypeResponse>> getTeleSourceType(@Query("assigned_to") String assigned_to,
                                                              @Query("user_type") String user_type);

    @POST("mobileapp/tranquilai")
    Call<AskMeResponse> getAskMeResult   (@Body RequestBody result);

    @GET("mobileapp/tellecallerdata")
    Call<ArrayList<TeleCallersDataResponse>> getTeleDta(@Query("src_id") String src_id,
                                                        @Query("assigned_to") String assigned_to,
                                                        @Query("start") int start);

    @GET("mobileapp/updateleadprofile")
    Call<ArrayList<StatusResponse>> getUpdateLeadProfile(@Query("lead_id") String lead_id,
                                                         @Query("activity_id") String activity_id,
                                                         @Query("activity_date") String activity_date,
                                                         @Query("time") String time,
                                                         @Query("source_id") String source_id,
                                                         @Query("requirement_id") String requirement_id,
                                                         @Query("created_by") String created_by,
                                                         @Query("user_id") String user_id);

    @GET("mobileapp/notes")
    Call<StatusResponse> getNotes (@Query("status") String status,
                                   @Query("lead_id") String lead_id,
                                   @Query("user_id") String user_id);
    @GET("mobileapp/callcompleted")
    Call<StatusResponse> callCompleted (@Query("user_id") String user_id,
                                        @Query("next_act_id") String next_act_id,
                                        @Query("lead_id") String lead_id,
                                        @Query("note") String note,
                                        @Query("date") String date,
                                        @Query("time") String time,
                                        @Query("call_id") String call_id,
                                        @Query("campaign_call") String campaign_call,
                                        @Query("call_start") String call_start,
                                        @Query("call_completed") String call_completed);

    @GET("mobileapp/notesshow")
    Call<ArrayList<NotesResponse>> getNotesShow (@Query("lead_id") String lead_id,
                                                 @Query("user_id") String user_id);

    @GET("mobileapp/updatenextactivitythumb")
    Call<StatusResponse> updateNextActivity(@Query("lead_id") String lead_id,
                                                         @Query("activity_id") String activity_id,
                                                         @Query("user_id") String user_id,
                                                         @Query("company_id") String company_id);

    @GET("mobileapp/cmpact")
    Call<StatusResponse> completeActivity(@Query("next_activity") String next_activity,
                                          @Query("completd_act_date") String completd_act_date,
                                            @Query("msg") String msg,
                                            @Query("user_id") String user_id,
                                            @Query("lead_id") String lead_id,
                                            @Query("requirement_name") String requirement_name,
                                            @Query("next_act_time") String next_act_time,
                                            @Query("completed_activity") String completed_activity,
                                            @Query("next_act_date") String next_act_date,
                                            @Query("next_req") String next_req,
                                            @Query("lead_status") String lead_status


                                          );

    @GET("mobileapp/callcompletednextno")
    Call<ArrayList<NextCallResponse>> nextCall(@Query("user_id") String user_id,
                                               @Query("lead_id") String lead_id,
                                               @Query("activity_id") String activity_id,
                                               @Query("user_type") String user_type);

    @GET("mobileapp/getdirectmeeting")
    Call<ArrayList<StatusResponse>> getDirectMeeting (@Query("user_id") String user_id);

    @GET("mobileapp/updatesale")
    Call<StatusResponse> updateSale(@Query("lead_id") String lead_id,
                                               @Query("user_id") String user_id,
                                               @Query("company_id") String company_id,
                                               @Query("sale_value") String sale_value);

    @GET("mobileapp/alldata")
    Call<ArrayList<AllDataResponse>> getAllData(@Query("user_id") String user_id,
                                                @Query("user_type") String user_type,
                                                @Query("act") String act,
                                                @Query("activity_id") String activity_id,
                                                @Query("start") int start);

    @GET("mobileapp/allact")
    Call<ArrayList<AllActivityResponse>> getAllActivities(@Query("act") String act,
                                                          @Query("user_id") String user_id,
                                                          @Query("user_type") String user_type);

    @GET("mobileapp/overalact")
    Call<ArrayList<NewLeadPlansResponse>> getAllPlans(@Query("activity_id") String activity_id,
                                                      @Query("user_id") String user_id,
                                                      @Query("user_type") String user_type);


    @GET("mobileapp/alldreasions")
    Call<ArrayList<DisableResponse>> getAllDeadReasons();

    @GET("mobileapp/deadlead")
    Call<StatusResponse> getDeadLead(@Query("lead_id") String lead_id,
                                      @Query("user_id") String user_id,
                                      @Query("s_reasion") String s_reasion,
                                      @Query("other_reason") String other_reason,
                                      @Query("reason") String reason);

    @GET("mobileapp/overallactivities")
    Call<ArrayList<DashBoardResponseNew>> getOverallActivities(@Query("company_id") String company_id,
                                                    @Query("menu_id") String menu_id,
                                                    @Query("user_type") String user_type,
                                                    @Query("user_id") String user_id);


    @GET("mobileapp/getuseractivate")
    Call<ArrayList<ActivestatusResponse>> getactivestatus(@Query("user_id") String user_id);


    @GET("mobileapp/getmodulenames")
    Call<ArrayList<GetModuleNameResponse>>getModuleName(@Query("start") int start);




    @GET("mobileapp/employees")
    Call<ArrayList<EmployeeResponse>> getEmployees();



    @Multipart
    @POST("mobileapp/createlead")
    Call<ArrayList<CreateLeadResponse>> getCreateLead(@Part MultipartBody.Part image,
                                                      @Query("first_name") String first_name,
                                                      @Query("last_name") String last_name,
                                                      @Query("mobile_number") String mobile_number,
                                                      @Query("alternate_number") String alternate_number,
                                                      @Query("email_id") String email_id,
                                                      @Query("website") String website,
                                                      @Query("location") String location,
                                                      @Query("city") String city,
                                                      @Query("company_name") String company_name,
                                                      @Query("gst_number") String gst_number,
                                                      @Query("created_by") String created_by,
                                                      @Query("created_date") String created_date,
                                                      @Query("requirement_id") String requirement_id,
                                                      @Query("requirement_other") String requirement_other,
                                                      @Query("source_id") String source_id,
                                                      @Query("source_other") String source_other,
                                                      @Query("activity_id") String activity_id,
                                                      @Query("activity_date") String activity_date,
                                                      @Query("user_company_id") String user_company_id,
                                                      @Query("activity_other") String activity_other,
                                                      @Query("communication") String communication,
                                                      @Query("note") String note

    );




    @Multipart
    @POST("mobileapp/editlead")
    Call<ArrayList<AllStatusResponse>> getEditLead(@Part MultipartBody.Part image,
                                                   @Query("lead_id") String lead_id,
                                                   @Query("first_name") String first_name,
                                                   @Query("last_name") String last_name,
                                                   @Query("mobile_number") String mobile_number,
                                                   @Query("email_id") String email_id,
                                                   @Query("website") String website,
                                                   @Query("location") String location,
                                                   @Query("city") String city,
                                                   @Query("company_name") String company_name,
                                                   @Query("gst_number") String gst_number,
                                                   @Query("requirement_id") String requirement_id,
                                                   @Query("requirement_other") String requirement_other,
                                                   @Query("user_company_id") String user_company_id);

    @GET("mobileapp/leadprofile")
    Call<ArrayList<LeadProfileFragment.LeadProfileResponse>> getLeadProfileDetails(@Query("lead_id") String lead_id);

    @GET("mobileapp/leadprofile")
    Call<ArrayList<LeadEditActivity.LeadProfileResponse>> getEditLeadProfileDetails(@Query("lead_id") String lead_id);

    @GET("mobileapp/presentactivity")
    Call<ArrayList<LeadHistoryActivity.PresentActivityResponse>> getPresentActivityResponse(@Query("lead_id") String lead_id);

    @GET("mobileapp/activityhistory")
    Call<ArrayList<LeadHistoryFragment.LeadHistoryResponse>> getLeadHistory(@Query("lead_id") String lead_id);

    @GET("mobileapp/leadjourney")
    Call<ArrayList<LeadJourneyFragment.LeadHistoryResponse>> getLeadJourney(@Query("lead_id") String lead_id);

    @GET("mobileapp/completeactivity")
    Call<ArrayList<AllStatusResponse>> getUpdateCompleteResponse(@Query("lead_id") String lead_id,
                                                                 @Query("act_no") String act_no,
                                                                 @Query("notes") String notes,
                                                                 @Query("next_act_id") String next_act_id,
                                                                 @Query("next_act_date") String next_act_date,
                                                                 @Query("company_id") String company_id,
                                                                 @Query("user_id") String user_id,
                                                                 @Query("voice_file") String voice_file,
                                                                 @Query("activity_other") String activity_other);

    @GET("mobileapp/cancelactivity")
    Call<ArrayList<AllStatusResponse>> getUpdateCancelResponse(@Query("lead_id") String lead_id,
                                                               @Query("act_no") String act_no,
                                                               @Query("notes") String notes,
                                                               @Query("next_act_id") String next_act_id,
                                                               @Query("next_act_date") String next_act_date,
                                                               @Query("company_id") String company_id,
                                                               @Query("user_id") String user_id,
                                                               @Query("voice_file") String voice_file,
                                                               @Query("activity_other") String activity_other);

    @GET("mobileapp/addtags")
    Call<ArrayList<StatusResponse>> addTagResponse(@Query("lead_id") String lead_id,
                                                   @Query("user_id") String user_id,
                                                   @Query("company_id") String company_id,
                                                   @Query("tag_name") String tag_name,
                                                   @Query("color_id") String color_id);

    @GET("mobileapp/leadtags")
    Call<ArrayList<TagDetailsActivity.TagsMainList>> getMainTags(@Query("company_id") String company_id,
                                                                 @Query("lead_id") String lead_id);

    @GET("mobileapp/inserttagstolead")
    Call<ArrayList<StatusResponse>> InsertTagsToLead(@Query("lead_id") String lead_id,
                                                     @Query("tag_id") String tag_id,
                                                     @Query("company_id") String company_id,
                                                     @Query("user_id") String user_id);

    @GET("mobileapp/smstemplate")
    Call<ArrayList<SmsTemplatesActivity.SmsTemplatesResponse>> getSmsTemplates(@Query("company_id") String company_id);

    @GET("mobileapp/createsmstemplate")
    Call<ArrayList<StatusResponse>> getCreateSms(@Query("sms_title") String sms_title,
                                                 @Query("sms_message") String sms_message,
                                                 @Query("company_id") String company_id,
                                                 @Query("user_id") String user_id);

    @GET("mobileapp/updatesmstemplate")
    Call<ArrayList<StatusResponse>> getEditSms(@Query("sms_title") String sms_title,
                                               @Query("sms_message") String sms_message,
                                               @Query("company_id") String company_id,
                                               @Query("user_id") String user_id,
                                               @Query("sms_id") String sms_id);

    @GET("mobileapp/emailtemplate")
    Call<ArrayList<EmailTemplatesActivity.EmailTemplatesResponse>> getEmailTemplates(@Query("company_id") String company_id);

    @Multipart
    @POST("mobileapp/createemailtemplate")
    Call<ArrayList<StatusResponse>> getCreateEmail(@Part MultipartBody.Part image,
                                                   @Query("email_title") String sms_title,
                                                   @Query("email_subject") String email_subject,
                                                   @Query("email_message") String email_message,
                                                   @Query("company_id") String company_id,
                                                   @Query("user_id") String user_id);

    @Multipart
    @POST("mobileapp/updateemailtemplate")
    Call<ArrayList<StatusResponse>> getEditEmail(@Part MultipartBody.Part image,
                                                 @Query("email_title") String sms_title,
                                                 @Query("email_subject") String email_subject,
                                                 @Query("email_message") String email_message,
                                                 @Query("company_id") String company_id,
                                                 @Query("user_id") String user_id,
                                                 @Query("emai_template_id") String emai_template_id);

    @GET("mobileapp/smstemplate")
    Call<ArrayList<SendTemplatesActivity.SmsTemplatesResponse>> getUpdateSmsTemplates(@Query("company_id") String company_id);

    @GET("mobileapp/emailtemplate")
    Call<ArrayList<SendTemplatesActivity.SendEmailResponse>> getUpdateEmailTemplates(@Query("company_id") String company_id);

    @GET("mobileapp/tagsforleadview")
    Call<ArrayList<LeadHistoryActivity.TagsDisplayResponse>> getLeadTags(@Query("lead_id") String company_id);

    //Quotes
    @GET("mobileapp/getproductsforquote")
    Call<ArrayList<StatusResponse>> addQuotationResponse(@Query("user_id") String user_id,
                                                         @Query("company_id") String company_id,
                                                         @Query("other_product") String other_product,
                                                         @Query("project_id") String project_id,
                                                         @Query("uom") String uom,
                                                         @Query("description") String description,
                                                         @Query("rate") String rate,
                                                         @Query("quantity") String quantity,
                                                         @Query("gst_per") String gst_per,
                                                         @Query("disc_amt") String disc_amt,
                                                         @Query("hsn_sac") String hsn_sac,
                                                         @Query("lead_id") String lead_id);


    @GET("mobileapp/getproductsforbills")
    Call<ArrayList<StatusResponse>> addbillsResponse(@Query("lead_id") String lead_id,
                                                         @Query("product_other") String product_name,
                                                         @Query("qty") String qty,
                                                         @Query("price") String price,
                                                         @Query("user_id") String user_id,
                                                         @Query("company_id") String company_id,
                                                         @Query("product_id") String product_id,
                                                         @Query("bill_no") String bill_no,
                                                         @Query("gst_per") String gst_per);


    @GET("mobileapp/createquote")
    Call<ArrayList<StatusResponse>> addQuotationCreateResponse(@Query("lead_id") String lead_id,
                                                               @Query("company_id") String company_id,
                                                               @Query("user_id") String user_id,
                                                               @Query("sub_total_amount") String sub_total_amount,
                                                               @Query("discount") String discount,
                                                               @Query("total_amount") String total_amount,
                                                               @Query("tax") String tax,
                                                               @Query("total_with_tax") String total_with_tax,
                                                               @Query("discount_per") String discount_per);
@Multipart
    @POST("mobileapp/createbills")
    Call<ArrayList<StatusResponse>> addbillsCreateResponse(@Query("lead_id") String lead_id,
                                                           @Query("company_id") String company_id,
                                                           @Query("user_id") String user_id,
                                                           @Query("amount") String total_amount,
                                                           @Query("tax_amount") String tax,
                                                           @Query("total_inclusive_tax") String total_with_tax,
                                                           @Query("bill_no") String bill_no,
                                                           @Part MultipartBody.Part upload_file);

    @GET("mobileapp/updateproduct")
    Call<ArrayList<StatusResponse>> addQuotationEditResponse(@Query("q_id") String q_id,
                                                             @Query("product_other") String product_name,
                                                             @Query("product_id") String product_id,
                                                             @Query("qty") String qty,
                                                             @Query("price") String price,
                                                             @Query("user_id") String user_id,
                                                             @Query("company_id") String company_id);





    @GET("mobileapp/deleteproduct")
    Call<ArrayList<StatusResponse>> getQuotationDeleteResponse(@Query("q_id") String q_id);


    //Perform Invoices
    @GET("mobileapp/getproductsforinvoice")
    Call<ArrayList<StatusResponse>> addProformaInvoiceResponse(@Query("lead_id") String lead_id,
                                                               @Query("product_other") String product_name,
                                                               @Query("qty") String qty,
                                                               @Query("price") String price,
                                                               @Query("user_id") String user_id,
                                                               @Query("company_id") String company_id,
                                                               @Query("product_id") String product_id,
                                                               @Query("gst_per") String gst_per,
                                                               @Query("description") String description);



    @GET("mobileapp/createinvoice")
    Call<ArrayList<StatusResponse>> addProformaCreateResponse(@Query("lead_id") String lead_id,
                                                              @Query("company_id") String company_id,
                                                              @Query("user_id") String user_id,
                                                              @Query("sub_total_amount") String sub_total_amount,
                                                              @Query("discount") String discount,
                                                              @Query("total_amount") String total_amount,
                                                              @Query("tax") String tax,
                                                              @Query("total_with_tax") String total_with_tax);

    @GET("mobileapp/updateproductinv")
    Call<ArrayList<StatusResponse>> addProformaEditResponse(@Query("inv_id") String q_id,
                                                            @Query("product_other") String product_name,
                                                            @Query("product_id") String product_id,
                                                            @Query("qty") String qty,
                                                            @Query("price") String price,
                                                            @Query("user_id") String user_id,
                                                            @Query("company_id") String company_id);




    @GET("mobileapp/deleteproductinvoice")
    Call<ArrayList<StatusResponse>> getProformaDeleteResponse(@Query("inv_id") String q_id);

    //Invoices

    @GET("mobileapp/getproductsforinv")
    Call<ArrayList<StatusResponse>> addInvoiceResponse(@Query("lead_id") String lead_id,
                                                       @Query("product_other") String product_name,
                                                       @Query("qty") String qty,
                                                       @Query("price") String price,
                                                       @Query("user_id") String user_id,
                                                       @Query("company_id") String company_id,
                                                       @Query("product_id") String product_id,
                                                       @Query("gst_per") String gst_per,
                                                       @Query("description") String description);



    @GET("mobileapp/createinv")
    Call<ArrayList<StatusResponse>> getInvoiceProducts(@Query("lead_id") String lead_id,
                                                       @Query("company_id") String company_id,
                                                       @Query("user_id") String user_id,
                                                       @Query("sub_total_amount") String sub_total_amount,
                                                       @Query("discount") String discount,
                                                       @Query("total_amount") String total_amount,
                                                       @Query("tax") String tax,
                                                       @Query("total_with_tax") String total_with_tax);

    @GET("mobileapp/updateproductinv")
    Call<ArrayList<StatusResponse>> addInvoiceEditResponse(@Query("inv_id") String q_id,
                                                           @Query("product_other") String product_name,
                                                           @Query("product_id") String product_id,
                                                           @Query("qty") String qty,
                                                           @Query("price") String price,
                                                           @Query("user_id") String user_id,
                                                           @Query("company_id") String company_id);


    @GET("mobileapp/deleteproductinv")
    Call<ArrayList<StatusResponse>> getInvoiceResponse(@Query("inv_id") String q_id);

    //others

    @GET("mobileapp/disablelist")
    Call<ArrayList<LeadProfileFragment.ReasonsResponse>> getDisableReasons(@Query("company_id") String company_id);

    @GET("mobileapp/disabled")
    Call<ArrayList<StatusResponse>> getDisableLeadReasons(@Query("lead_id") String lead_id,
                                                          @Query("company_id") String company_id,
                                                          @Query("reason_id") String reason_id,
                                                          @Query("reason_other") String reason_other,
                                                          @Query("user_id") String user_id);

    @GET("mobileapp/communication")
    Call<ArrayList<CommunicationsResponse>> getInsertCommunicationResponse(@Query("lead_id") String lead_id,
                                                                           @Query("message") String message,
                                                                           @Query("subject") String subject,
                                                                           @Query("activity_id") String activity_id,
                                                                           @Query("user_id") String user_id,
                                                                           @Query("skip") Boolean skip);

    @GET("mobileapp/commphonecalls")
    Call<ArrayList<LeadCommunicationFragment.CommunicationResponse>> getAllCalls(@Query("lead_id") String lead_id);


    @GET("mobileapp/commsms")
    Call<ArrayList<LeadCommunicationFragment.CommunicationResponse>> getAllSms(@Query("lead_id") String lead_id);

    @GET("mobileapp/commwhatsapp")
    Call<ArrayList<LeadCommunicationFragment.CommunicationResponse>> getAllWhatsApp(@Query("lead_id") String lead_id);





    @GET("mobileapp/commemail")
    Call<ArrayList<LeadCommunicationFragment.CommunicationResponse>> getAllEmail(@Query("lead_id") String lead_id);


    @Multipart
    @POST("mobileapp/addbill")
    Call<ArrayList<AllStatusResponse>> getAddBillResponse(@Part MultipartBody.Part image,
                                                          @Query("lead_id") String lead_id,
                                                          @Query("amount") String amount,
                                                          @Query("user_id") String user_id,
                                                          @Query("type_of_bill") String type_of_bill,
                                                          @Query("tax") String tax,
                                                          @Query("company_id") String company_id);

    @Multipart
    @POST("mobileapp/addreceipts")
    Call<ArrayList<AllStatusResponse>> getAddReceiptResponse(@Part MultipartBody.Part image,
                                                             @Query("lead_id") String lead_id,
                                                             @Query("amount") String amount,
                                                             @Query("user_id") String user_id,
                                                             @Query("payment_type") String payment_type,
                                                             @Query("reference_no") String reference_no);

    @Multipart
    @POST("mobileapp/makepayment")
    Call<ArrayList<AllStatusResponse>> getMakePaymentResponse(@Part MultipartBody.Part image,
                                                              @Query("lead_id") String lead_id,
                                                              @Query("amount") String amount,
                                                              @Query("user_id") String user_id,
                                                              @Query("payment_type") String payment_type,
                                                              @Query("reference_no") String reference_no);


    @GET("mobileapp/closedbussiness")
    Call<ArrayList<AllStatusResponse>> getSingleClosed(@Query("lead_id") String lead_id,
                                                       @Query("company_id") String company_id,
                                                       @Query("type_of_payment") String type_of_payment,
                                                       @Query("sch_amount") String sch_amount,
                                                       @Query("sch_date") String sch_date,
                                                       @Query("product_id") String product_id,
                                                       @Query("user_id") String user_id);


    @GET("mobileapp/search")
    Call<ArrayList<SearchViewActivity.SearchResponse>> getSearchResponse(@Query("keyword") String keyword,
                                                                         @Query("user_id") String user_id,
                                                                         @Query("user_type") String user_type);


    @GET("mobileapp/updatequotestatus")
    Call<ArrayList<AllStatusResponse>> getUpdateQuote(@Query("status") String status,
                                                      @Query("quote_id") String quote_id);



    @GET("mobileapp/updateprofarmastatus")
    Call<ArrayList<AllStatusResponse>> getUpdateProforma(@Query("status") String status,
                                                         @Query("proforma_invoice_id") String quote_id);


    @GET("mobileapp/convertquote")
    Call<ArrayList<AllStatusResponse>> getConvertQuoteResponse(@Query("quote_id") String quote_id,
                                                               @Query("convert_id") String convert_id,
                                                               @Query("user_id") String user_id,
                                                               @Query("inv_id") String inv_id);

    @GET("mobileapp/convertproformainvoicetoinvoice")
    Call<ArrayList<AllStatusResponse>> getConvertProformaResponse(@Query("proforma_invoice_id") String quote_id,
                                                                  @Query("user_id") String user_id);


    @GET("mobileapp/search")
    Call<ArrayList<CrmSearchNumberResponse>> getCrmNumberSearchResponse(@Query("keyword") String keyword,
                                                                        @Query("company_id") String company_id);

    @GET("mobileapp/leadprofile")
    Call<ArrayList<CallersDialogActivity.LeadInformationResponse>> getCrmNumberDetails(@Query("lead_id") String lead_id);

    @GET("mobileapp/tagsforleadview")
    Call<ArrayList<CallersDialogActivity.TagsDisplayResponse>> getCrmNumberTags(@Query("lead_id") String company_id);


    @Multipart
    @POST("mobileapp/taxpayment")
    Call<ArrayList<AllStatusResponse>> getTaxMakePaymentResponse(@Part MultipartBody.Part image,
                                                                 @Query("company_id") String company_id,
                                                                 @Query("user_id") String user_id,
                                                                 @Query("last_months") String last_months,
                                                                 @Query("tax_amount") String tax_amount,
                                                                 @Query("payment_type") String payment_type,
                                                                 @Query("ref_no") String ref_no);



    @Multipart
    @POST("mobileapp/companysettings")
    Call<ArrayList<AllStatusResponse>> getChangeCompanySettingsResponse(@Part MultipartBody.Part image,
                                                                        @Query("company_id") String company_id,
                                                                        @Query("company_name") String company_name,
                                                                        @Query("address") String address,
                                                                        @Query("gst_number") String gst_number,
                                                                        @Query("contact_number") String contact_number,
                                                                        @Query("website") String website);



    @GET("mobileapp/deleteclosedbusiness")
    Call<ArrayList<AllStatusResponse>> getClosedBusinessDeleteResponse(@Query("sch_id") String sch_id);


    @GET("mobileapp/incomingcalls")
    Call<ArrayList<StatusResponse>> storeIncomingCallsInDataBase(@Query("mobile_number") String mobile_number,
                                                                    @Query("user_id") String user_id);

    @GET("mobileapp/viewincomingcalls")
    Call<ArrayList<DashBoardIncomingCallsActivity.IncomingResponse>> getIncomingDisplayResponse(@Query("user_id") String user_id,
                                                                                                @Query("start") int start);

    //payment
    @GET("mobileapp/subscription")
    Call<ArrayList<AllStatusResponse>> getPaymentSuccessResponse(@Query("user_id") String user_id,
                                                                 @Query("plan_days") String plan_days,
                                                                 @Query("plan_amount") String plan_amount);


    @Multipart
    @POST("mobileapp/uploadprofilepicture")
    Call<ArrayList<AllStatusResponse>> updateUserProfile(@Part MultipartBody.Part image,
                                                         @Query("user_id") String user_id,
                                                         @Query("company_id")String company_id,
                                                         @Query("company_name")String company_name,
                                                         @Query("user_name")String user_name);

    @Multipart
    @POST("mobileapp/punchinstore")
    Call<PunchInResponse> getPunchRequestResponse(@Part MultipartBody.Part image,
                                                         @Query("punchin_area") String punchin_area,
                                                         @Query("user_id")String user_id);

    @Multipart
    @POST("mobileapp/punchoutstore")
    Call<PunchInResponse> getPunchOut(@Part MultipartBody.Part image,
                                                  @Query("punchout_area") String punchin_area,
                                                  @Query("user_id")String user_id);


    @GET("mobileapp/punchinstore")
    Call<ArrayList<PunchInResponse>> getPunchRequestResponse(@Query("punchin_area") String area,
                                                             @Query("punchin_pic") Bitmap punchin_pic,
                                                             @Query("user_id") String user_id);

    @GET("mobileapp/punchoutstore")
    Call<ArrayList<AttendancePunchOutResponse>> getPunchOutRequestResponse(
                                                                           @Query("user_id") String user_id,
                                                                           @Query("punchout_area") String area,
                                                                           @Query("punchout_pic") Bitmap pic);

    @GET("mobileapp/punchstatus")
    Call<ArrayList<PunchStatusResponse>> getPunchStatusData(@Query("user_id") String user_id);

    @GET("mobileapp/overallactivities")
    Call<ArrayList<ActivitiesFragment.MainDisplayResponse>> getOverallactivities(@Query("company_id") String company_id,
                                                                                 @Query("menu_id") String menu_id,
                                                                                 @Query("user_type") String user_type,
                                                                                 @Query("user_id") String user_id);
    @GET("mobileapp/skycards")
    Call<ArrayList<CardsResponse>> getSkycards(@Query("user_id") String user_id,
                                               @Query("user_type") String user_type);

    @GET("mobileapp/createdayreportnew")
    Call<ArrayList<CreateDayreportResponse>> getCreateDayReport(@Query("user_id") String user_id);


    @GET("mobileapp/fetchitemvalue")
    Call<ArrayList<FetchItemResponse>> getFetchitemvalue(@Query("project_id") String project_id);


    @Multipart
    @POST("mobileapp/directmeeting")
    Call<DirectModelResponse> directMeeting(@Part MultipartBody.Part upload_pic,
                                            @Query("customer_name") String customer_name,
                                            @Query("company_name") String company_name,
                                            @Query("location") String location,
                                            @Query("contact_number") String contact_number,
                                            @Query("user_id") String user_id,
                                            @Query("lead_source_type") String lead_source_type);

    @GET("mobileapp/downloadpdf")
    Call<ArrayList<PdfModel>> getDownloadPdf(@Query("quote_id") String quote_id,
                                 @Query("type") String type);

    @GET("mobileapp/sendmessage")
    Call<ArrayList<StatusResponse>> getSendMessage(@Query("user_id") String user_id,
                                                   @Query("lead_id") String lead_id,
                                                   @Query("message") String message,
                                                   @Query("type") String type);

    @POST("mobileapp/updatefcm")
    Call<MessageResponse> updateFCMID(@Query("user_id") String user_id,
                                      @Query("device_id") String device_id,
                                      @Query("fcm_id") String fcm_id,
                                      @Query("device_type") String device_type);

    @GET("mobileapp/availableprojects")
    Call<ArrayList<AvailableProjectsResponse>> getAvailableProjects ();

    @GET("mobileapp/telecallerskip")
    Call<TeleResponse> getTeleCallerSkip (@Query("tele_cust_id") String tele_cust_id);

    @GET("mobileapp/telecallerconvert")
    Call<TeleResponse> getTeleCallerConvert (@Query("tele_cust_id") String tele_cust_id);

    @GET("mobileapp/telecallerdisable")
    Call<TeleResponse> getTeleCallerDelete (@Query("tele_cust_id") String tele_cust_id);

    @GET("mobileapp/agentslist")
    Call<ArrayList<AgentsMainResponse>> getAgentsList ();

    @GET("mobileapp/clicktocall")
    Call<KnowlarityResponse> clickToKnowlarityCall (@Query("user_id") String user_id,
                                                    @Query("customer_number") String customer_number);

    /*Site visit*/

    @GET("mobileapp/sitevisitsearch")
    Call<ArrayList<SearchResponse>> updateSiteVisit(@Query("mobileorname") String mobileorname,
                                                    @Query("user_id") String user_id);


    @GET("mobileapp/startsitevisit")
    Call<ArrayList<SiteVisitStartResponse>> startPointLocationDetails(@Query("user_id") String user_id,
                                                                      @Query("sitelocation") String sitelocation,
                                                                      @Query("sitearea") String sitearea,
                                                                      @Query("vehicle") String vehicle,
                                                                      @Query("startlat") String startlat,
                                                                      @Query("startlong") String startlong);


    @GET("mobileapp/meetingstart")
    Call<ArrayList<MeetingResponse>> meetPointLocationDetails(@Query("updateid") String updateid,
                                                              @Query("meetpoint") String meetpoint,
                                                              @Query("meetlat") String meetlat,
                                                              @Query("meetlong") String meetlong);


    @GET("mobileapp/meetingsubmit")
    Call<ArrayList<MeetSubmitionRespons>> meetSubmittionLocationDetails(@Query("updateid") String updateid,
                                                                        @Query("customer_id") String customer_id,
                                                                        @Query("customername") String customername,
                                                                        @Query("mobilenumber") String mobilenumber,
                                                                        @Query("notes") String notes,
                                                                        @Query("meetsubmitpoint") String meetsubmitpoint,
                                                                        @Query("meetsublat") String meetsublat,
                                                                        @Query("meetsublong") String meetsublong);

    @GET("mobileapp/endpoint")
    Call<ArrayList<EndPointResponse>> endPointLocationDetails(@Query("updateid") String updateid,
                                                              @Query("endpoint") String endpoint,
                                                              @Query("endlat") String endlat,
                                                              @Query("endlong") String endlong);

    @GET("mobileapp/foldertypes")
    Call<ArrayList<FoldersResponse>> getFoldersTypes();

    @GET("mobileapp/alluploadfiles")
    Call<ArrayList<AllUploadFilesResponse>> getAllUploadedFiles();

    @GET("mobileapp/selectfoldertype")
    Call<ArrayList<AllUploadFilesResponse>> getSelectFolderType(@Query("folder_type") String folder_type);

    @GET("mobileapp/deletefile")
    Call<StatusResponse> getDeleteFile(@Query("id") String id);

    @GET("mobileapp/search")
    Call<ArrayList<SearchViewActivity.SearchResponse>> getSearchForFileSend(@Query("keyword") String keyword,
                                                      @Query("company_id") String company_id);

    @GET("mobileapp/sendfile")
    Call<SendFileResponse> getSendFile(@Query("user_id") String user_id,
                                       @Query("lead_id") String lead_id,
                                       @Query("id") String id);



    @POST("mobileapp/telinextno")
    Call<ArrayList<NextTeleCallersResponse>> getNextCallersData(@Query("src_id") String src_id,
                                                                @Query("assigned_to") String assigned_to,
                                                                @Query("caller_id") String caller_id);

    @Multipart
    @POST("mobileapp/telinextnotest")
    Call<ArrayList<NextTeleCallersResponse>> getNextCallersDataTest(@Part MultipartBody.Part src_id,
                                                                    @Part MultipartBody.Part assigned_to,
                                                                    @Part MultipartBody.Part caller_id);

    @GET("mobileapp/addteleenquiry")
    Call<ArrayList<TelecallersResponse>> addTeleCallerData(@Query("user_id") String user_id,
                                                           @Query("source_type") String source_type,
                                                           @Query("lead_name") String lead_name,
                                                           @Query("lead_mobile") String lead_mobile,
                                                           @Query("lead_email") String lead_email,
                                                           @Query("company_name") String company_name);

    @GET("mobileapp/profile")
    Call<DashboardProfileResponse> getProfile  (@Query("user_id") String user_id);

    @GET("mobileapp/telicallcompleted")
    Call<StatusResponse> getTeleCallCompleted (@Query("call_id") String call_id,
                                               @Query("call_start") String call_start,
                                               @Query("call_end") String call_end,
                                               @Query("user_id") String user_id,
                                               @Query("user_type") String user_type);

    @GET("mobileapp/callrecdisplay")
    Call<ArrayList<RecordingsList>> getRecordings (@Query("user_id") String user_id);

    @GET("mobileapp/allivrrecords")
    Call<ArrayList<RecordingsList>> getRecordingsList (@Query("user_id") String user_id,
                                                       @Query("user_type") String user_type,
                                                       @Query("mobile_number") String mobile_number);

    @POST("mobileapp/callrec")
    Call<InsertAudioResponse> getAudioRecord(@Body RequestBody file);

    @GET("mobileapp/allrecordings")
    Call<ArrayList<RecordingsList>> getAllRecordings (@Query("start") int start);

    //location

    /*https://tranquilcrmone.com/mobileapp/employeetrack?user_id=1
    &area=test&location=fdscf&longitude=5645456&latitude=654564*/
    @GET("mobileapp/employeetrack")
    Call<ArrayList<MessageResponse>> insertLocation (@Query("user_id") String user_id,
                                                     @Query("area") String area,
                                                     @Query("location") String location,
                                                     @Query("longitude") String longitude,
                                                     @Query("latitude") String latitude);



    @GET("mobileapp/gettopicnames")
    Call<ArrayList<GettopicnamesResponse>> gettopicname(@Query("status") int status);


    @GET("mobileapp/getcreativiesimages")
    Call<ArrayList<ImagescreateiveResponse>> getimagescreative(@Query("status") int status);



}