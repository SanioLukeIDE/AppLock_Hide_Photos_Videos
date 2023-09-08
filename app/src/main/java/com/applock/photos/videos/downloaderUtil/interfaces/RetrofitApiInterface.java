package com.applock.photos.videos.downloaderUtil.interfaces;

import androidx.annotation.Keep;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

@Keep
public interface RetrofitApiInterface {
    @GET
    Call<JsonObject> getsnackvideoresult(@Url String url);


    @GET
    Call<ResponseBody> getMainResponse(@Url String str);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> getTikResponse(@Url String str, @Field("tiktokurl") String str2);

    @Multipart
    @POST
    @Headers({"accept: application/json, text/plain, /", "x-req: 1", "client: snaptik", "z: snaptik.tiktokvideodownloader.savetiktokvideo.nowatermark", "Host: api.downloadtiktokvideos.com", "randomid: 28"})
    Call<ResponseBody> getTikVideoApi(@Url String str, @Header("user-agent") String str2, @PartMap Map<String, RequestBody> map);

    @GET
    Call<ResponseBody> getTikVideoResponse(@Url String str, @Header("User-Agent") String str2, @Header("Cookie") String str3);

    @GET
    Call<ResponseBody> getTikVideoobj(@Url String str, @Header("User-Agent") String str2);

    @GET
    Call<ResponseBody> getFullDetailInfoOfProfile(@Url String url, @Header("Cookie") String cookies);


    @GET
    Call<JsonObject> getInstagramDataNoCookie(@Url String Value, @Header("Cookie") String cookie);

    @GET
    Call<JsonObject> getInstagramData(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @GET
    Call<JsonObject> getInstagramProfileDataBulk(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @GET
    Call<JsonObject> getInstagramProfileDataAllImagesAndVideosBulk(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @GET
    Call<JsonObject> getInstagramSearchResults(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @POST
    Call<JsonObject> getInstagramSearchResultsPost(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);


//    @FormUrlEncoded
//    @POST("1/fetch")
//    //@Headers("Content-Type: application/json")
//    Call<Html>getVideo_Info_tiktok(@FieldMap Map<String,String> params);


//    @GET("users/blocked_list/")
//    Call<BlockedListResponse> blockedList(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3);
//
//    @GET("accounts/current_user/?edit=true")
//    Call<CurrentUserResponse> currentUser(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3);
//
//    @GET("feed/user/{userid}/?")
//    Call<Feed> feed(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3, @Path("userid") long j, @Query("max_id") String str4);
//
//    @GET("friendships/{userid}/followers/")
//    Call<UserListResponse> followers(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3, @Path("userid") long j);
//
//    @GET("friendships/{userid}/followers/?")
//    Call<UserListResponse> followersNextPage(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3, @Path("userid") long j, @Query("max_id") String str4);
//
//    @GET("friendships/{userid}/following/")
//    Call<UserListResponse> followings(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3, @Path("userid") long j);
//
//    @GET("friendships/{userid}/following/?")
//    Call<UserListResponse> followingsNextPage(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3, @Path("userid") long j, @Query("max_id") String str4);
//
//    @GET("feed/reels_tray")
//    Call<ReelsTrayResponse> getReelsTray(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3);
//
//    @GET("feed/user/{userid}/reel_media")
//    Call<ReelsTray> getUserReels(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3, @Path("userid") long j);
//
//    @GET("direct_v2/inbox/")
//    Call<InboxResponse> inbox(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3);
//
//    @GET("users/{userid}/info/")
//    Call<CurrentUserResponse> userInfo(@Header("User-Agent") String str, @Header("Host") String str2, @Header("Cookie") String str3, @Path("userid") long j);





 /*



   static final boolean $assertionsDisabled = false;
    public static final String BASE_URL = "https://i.instagram.com/api/v1/";
    private static final String DEVICE_ANDROID_RELEASE = "6.0.1";
    private static final String DEVICE_ANDROID_VERSION = "23";
    private static final String DEVICE_MANUFACTURER = "samsung";
    private static final String DEVICE_MODEL = "SM-G935F";
    private static final String EXPERIMENTS = "ig_android_progressive_jpeg,ig_creation_growth_holdout,ig_android_report_and_hide,ig_android_new_browser,ig_android_enable_share_to_whatsapp,ig_android_direct_drawing_in_quick_cam_universe,ig_android_huawei_app_badging,ig_android_universe_video_production,ig_android_asus_app_badging,ig_android_direct_plus_button,ig_android_ads_heatmap_overlay_universe,ig_android_http_stack_experiment_2016,ig_android_infinite_scrolling,ig_fbns_blocked,ig_android_white_out_universe,ig_android_full_people_card_in_user_list,ig_android_post_auto_retry_v7_21,ig_fbns_push,ig_android_feed_pill,ig_android_profile_link_iab,ig_explore_v3_us_holdout,ig_android_histogram_reporter,ig_android_anrwatchdog,ig_android_search_client_matching,ig_android_high_res_upload_2,ig_android_new_browser_pre_kitkat,ig_android_2fac,ig_android_grid_video_icon,ig_android_white_camera_universe,ig_android_disable_chroma_subsampling,ig_android_share_spinner,ig_android_explore_people_feed_icon,ig_explore_v3_android_universe,ig_android_media_favorites,ig_android_nux_holdout,ig_android_search_null_state,ig_android_react_native_notification_setting,ig_android_ads_indicator_change_universe,ig_android_video_loading_behavior,ig_android_black_camera_tab,liger_instagram_android_univ,ig_explore_v3_internal,ig_android_direct_emoji_picker,ig_android_prefetch_explore_delay_time,ig_android_business_insights_qe,ig_android_direct_media_size,ig_android_enable_client_share,ig_android_promoted_posts,ig_android_app_badging_holdout,ig_android_ads_cta_universe,ig_android_mini_inbox_2,ig_android_feed_reshare_button_nux,ig_android_boomerang_feed_attribution,ig_android_fbinvite_qe,ig_fbns_shared,ig_android_direct_full_width_media,ig_android_hscroll_profile_chaining,ig_android_feed_unit_footer,ig_android_media_tighten_space,ig_android_private_follow_request,ig_android_inline_gallery_backoff_hours_universe,ig_android_direct_thread_ui_rewrite,ig_android_rendering_controls,ig_android_ads_full_width_cta_universe,ig_video_max_duration_qe_preuniverse,ig_android_prefetch_explore_expire_time,ig_timestamp_public_test,ig_android_profile,ig_android_dv2_consistent_http_realtime_response,ig_android_enable_share_to_messenger,ig_explore_v3,ig_ranking_following,ig_android_pending_request_search_bar,ig_android_feed_ufi_redesign,ig_android_video_pause_logging_fix,ig_android_default_folder_to_camera,ig_android_video_stitching_7_23,ig_android_profanity_filter,ig_android_business_profile_qe,ig_android_search,ig_android_boomerang_entry,ig_android_inline_gallery_universe,ig_android_ads_overlay_design_universe,ig_android_options_app_invite,ig_android_view_count_decouple_likes_universe,ig_android_periodic_analytics_upload_v2,ig_android_feed_unit_hscroll_auto_advance,ig_peek_profile_photo_universe,ig_android_ads_holdout_universe,ig_android_prefetch_explore,ig_android_direct_bubble_icon,ig_video_use_sve_universe,ig_android_inline_gallery_no_backoff_on_launch_universe,ig_android_image_cache_multi_queue,ig_android_camera_nux,ig_android_immersive_viewer,ig_android_dense_feed_unit_cards,ig_android_sqlite_dev,ig_android_exoplayer,ig_android_add_to_last_post,ig_android_direct_public_threads,ig_android_prefetch_venue_in_composer,ig_android_bigger_share_button,ig_android_dv2_realtime_private_share,ig_android_non_square_first,ig_android_video_interleaved_v2,ig_android_follow_search_bar,ig_android_last_edits,ig_android_video_download_logging,ig_android_ads_loop_count_universe,ig_android_swipeable_filters_blacklist,ig_android_boomerang_layout_white_out_universe,ig_android_ads_carousel_multi_row_universe,ig_android_mentions_invite_v2,ig_android_direct_mention_qe,ig_android_following_follower_social_context";
    private static final String IG_SIG_KEY = "4f8732eb9ba7d1c8e8897a75d6474d4eb3f5279137431b2aafb71fafe2abe178";
    private static final String KEY_VERSION = "4";
    public static final String USER_AGENT = String.format("Instagram 10.26.0 Android (%s/%s; 640dpi; 1440x2560; %s; %s; hero2lte; samsungexynos8890; en_US)", new Object[]{DEVICE_ANDROID_VERSION, DEVICE_ANDROID_RELEASE, DEVICE_MANUFACTURER, DEVICE_MODEL});
    private static Connection _instance;
    private AsyncHttpClient httpClient;
    private final UserAuthentication userAuthentication;
    private String uuid;

    public interface ResponseHandler {
        void OnFailure(int i, Throwable th, JSONObject jSONObject);

        void OnSuccess(JSONObject jSONObject);
    }

    public static Connection getInstance() {
        if (_instance == null) {
            _instance = new Connection();
        }
        return _instance;
    }

    private Connection() {
        UserAuthentication FromFile = UserAuthentication.FromFile();
        this.userAuthentication = FromFile;
        if (FromFile.uuid != null) {
            this.uuid = FromFile.uuid;
        } else {
            GenerateUUID(true);
        }
    }

    public boolean IsLoggedIn() {
        return this.userAuthentication.IsLoggedIn();
    }

    public void Login(final String str, final String str2, final ResponseHandler responseHandler) {
        BuildHttpClient();
        AsyncHttpClient asyncHttpClient = this.httpClient;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://i.instagram.com/api/v1/si/fetch_headers/?challenge_type=signup&guid=");
        stringBuilder.append(GenerateUUID($assertionsDisabled));
        asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
            public void onSuccess(int i, Header[] headerArr, final JSONObject jSONObject) {
                super.onSuccess(i, headerArr, jSONObject);
                Connection.this.userAuthentication.username = str;
                Connection.this.userAuthentication.userId = Seossions.getInstance(App.context).getPrefString(Preferences.PREF_USERID);
                Connection.this.userAuthentication.SetToken(str2);
                Connection.this.userAuthentication.uuid = Connection.this.uuid;
                Connection.this.userAuthentication.SaveToFile();
                i = ProfileData.getInstance().getSelf_user();
                i.setUserName(str);
                i.setUserId(Connection.this.userAuthentication.userId);
                i.setToken(str);
                ProfileData.getInstance().setSelf_user(i);
                Connection.this.SyncFeatures(new com.inplus.app.officialapi.SecureHttpApi.ResponseHandler() {
                    public void OnSuccess(JSONObject jSONObject) {
                        responseHandler.OnSuccess(jSONObject);
                    }

                    public void OnFailure(int i, Throwable th, JSONObject jSONObject) {
                        responseHandler.OnFailure(i, th, jSONObject);
                    }
                });
            }

            public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                if (jSONObject != null) {
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            }
        });
    }

    private void GetUsernameInfo(String str, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            AsyncHttpClient asyncHttpClient = this.httpClient;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(BASE_URL);
            stringBuilder.append(String.format("users/%s/info/", new Object[]{str}));
            asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONArray jSONArray) {
                    responseHandler.OnFailure(i, th, null);
                }

                public void onFailure(int i, Header[] headerArr, String str, Throwable th) {
                    headerArr = new JSONObject();
                    if (str != null) {
                        try {
                            headerArr.put("message", str);
                        } catch (String str2) {
                            str2.printStackTrace();
                        }
                    }
                    try {
                        headerArr.put("skip", true);
                    } catch (String str22) {
                        str22.printStackTrace();
                    }
                    responseHandler.OnFailure(i, th, headerArr);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    public void GetSelfUsernameInfo(ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUsernameInfo(this.userAuthentication.userId, responseHandler);
    }

    public void GetSelfUsernameInfo2(String str, ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUsernameInfo(str, responseHandler);
    }

    private void GetUserFeed(String str, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            AsyncHttpClient asyncHttpClient = this.httpClient;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(BASE_URL);
            stringBuilder.append(String.format("feed/user/%s/?rank_token=%s&ranked_content=true&", new Object[]{str, this.userAuthentication.GetRankToken(this.uuid)}));
            asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    private void GetUserFeed(String str, String str2, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            AsyncHttpClient asyncHttpClient = this.httpClient;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(BASE_URL);
            stringBuilder.append(String.format("feed/user/%s/?max_id=%s&rank_token=%s&ranked_content=true&", new Object[]{str, str2, this.userAuthentication.GetRankToken(this.uuid)}));
            asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    public void GetUsernameSearch(String str, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            AsyncHttpClient asyncHttpClient = this.httpClient;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://i.instagram.com/api/v1/users/search/?ig_sig_key_version=4f8732eb9ba7d1c8e8897a75d6474d4eb3f5279137431b2aafb71fafe2abe178&is_typeahead=true&query=");
            stringBuilder.append(str);
            stringBuilder.append("&rank_token=");
            stringBuilder.append(this.userAuthentication.GetRankToken(this.uuid));
            asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    public void GetStorieViewers(String str, String str2, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            boolean isEmpty = str2.isEmpty();
            String str3 = BASE_URL;
            if (isEmpty) {
                str2 = new StringBuilder();
                str2.append(str3);
                str2.append(String.format("media/%s/list_reel_media_viewer/", new Object[]{str}));
                str = str2.toString();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(String.format("media/%s/list_reel_media_viewer/?max_id=%s&ig_sig_key_version=%s&rank_token=%s", new Object[]{str, str2, IG_SIG_KEY, this.userAuthentication.GetRankToken(this.uuid)}));
                str = stringBuilder.toString();
            }
            System.out.println(str);
            this.httpClient.get(str, new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    public void GetMediaSelfPicture(String str, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            AsyncHttpClient asyncHttpClient = this.httpClient;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.instagram.com/oembed/?url=");
            stringBuilder.append(str);
            asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    public void GetProfileSelfPicture(String str, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            AsyncHttpClient asyncHttpClient = this.httpClient;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://www.instagram.com/");
            stringBuilder.append(str);
            stringBuilder.append("/?__a=1&__d=dis");
            asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    public void GetMediaLikers(String str, final ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        if (IsLoggedIn()) {
            AsyncHttpClient asyncHttpClient = this.httpClient;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://i.instagram.com/api/v1/media/");
            stringBuilder.append(str);
            stringBuilder.append("/likers/");
            asyncHttpClient.get(stringBuilder.toString(), new JsonHttpResponseHandler() {
                public void onSuccess(int i, Header[] headerArr, JSONObject jSONObject) {
                    responseHandler.OnSuccess(jSONObject);
                }

                public void onFailure(int i, Header[] headerArr, Throwable th, JSONObject jSONObject) {
                    Connection.this.ResetLoginIfRequired(i, jSONObject);
                    responseHandler.OnFailure(i, th, jSONObject);
                }
            });
            return;
        }
        throw new CNException("Not Logged In");
    }

    public void GetSelfFeed(ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUserFeed(this.userAuthentication.userId, responseHandler);
    }

    public void GetSelfFeed(String str, ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUserFeed(this.userAuthentication.userId, str, responseHandler);
    }

    public void GetSelfUserFollowers(String str, ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUserFollowers(this.userAuthentication.userId, str, responseHandler);
    }

    public void GetSelfUserFollowers(ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUserFollowers(this.userAuthentication.userId, responseHandler);
    }

    public void GetSelfUserFollowings(String str, ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUserFollowings(this.userAuthentication.userId, str, responseHandler);
    }

    public void GetSelfUserFollowings(ResponseHandler responseHandler) throws CNException {
        BuildHttpClient();
        GetUserFollowings(this.userAuthentication.userId, responseHandler);
    }

    private void GetUserFollowers(String str, String str2, final ResponseHandler responseHandler) throws CNException {
        Bu



  */


}
