package com.applock.photos.videos.ui.activity;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.applock.photos.videos.utils.Const.NO_CONNECTION;
import static com.applock.photos.videos.utils.Const.SESSIONID;
import static com.applock.photos.videos.utils.Const.USERID;
import static com.applock.photos.videos.utils.MyApp.getLockInfoManager;
import static com.applock.photos.videos.utils.Utility.OpenApp;
import static com.applock.photos.videos.utils.Utility.setToast;
import static com.applock.photos.videos.utils.Utils.RootDirectoryInsta;
import static com.applock.photos.videos.utils.Utils.RootDirectoryWhatsappShow;
import static com.applock.photos.videos.utils.Utils.isNullOrEmpty;
import static com.applock.photos.videos.utils.Utils.startDownload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import com.applock.photos.videos.R;
import com.applock.photos.videos.api.CommonClassForAPI;
import com.applock.photos.videos.databinding.ActivityStatusSaverElementBinding;
import com.applock.photos.videos.model.DataModel;
import com.applock.photos.videos.model.Edge;
import com.applock.photos.videos.model.EdgeSidecarToChildren;
import com.applock.photos.videos.model.ResponseModel;
import com.applock.photos.videos.utils.MyApp;
import com.applock.photos.videos.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class StatusSaverElementActivity extends AppCompatActivity {

    ActivityStatusSaverElementBinding binding;
    DataModel model;
    private ClipboardManager clipBoard;
    CommonClassForAPI commonClassForAPI;
    private String PhotoUrl;
    private String VideoUrl;
    StatusSaverElementActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_status_saver_element);

        model = (DataModel) getIntent().getSerializableExtra("model");
        clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();

        binding.title.setText(model.getName());
        Glide.with(getApplicationContext()).load(model.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image);

    }

    @Override
    protected void onResume() {
        super.onResume();

        activity = this;
        getLockInfoManager().lockCommApplication(model.getValue());

        clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        pasteText();

        binding.btnBack.setOnClickListener(view -> finish());

        binding.image.setOnClickListener(view -> {
            getLockInfoManager().unlockCommApplication(model.getValue());
            OpenApp(getApplicationContext(), model.getValue());
        });

        binding.btnPaste.setOnClickListener(view -> {
            pasteText();
        });

        binding.btnDownload.setOnClickListener(v -> {
            String link = binding.etLink.getText().toString();
            if (link.equals("")) {
                binding.etLink.setError("Enter URL");
                binding.etLink.requestFocus();
                return;
            }
            if (!Patterns.WEB_URL.matcher(link).matches()) {
                binding.etLink.setError(getString(R.string.enter_valid_url));
                binding.etLink.requestFocus();
                return;
            }
            getInstagramData();

        });

    }

    private void getInstagramData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etLink.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);
            if (host.equals("www.instagram.com")) {
                callDownload(binding.etLink.getText().toString());

            } else {
                setToast(getApplicationContext(), getString(R.string.enter_valid_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void pasteText() {
        try {
            binding.etLink.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
            if (isNullOrEmpty(copyIntent)) {
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("instagram.com")) {
                        binding.etLink.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("instagram.com")) {
                        binding.etLink.setText(item.getText().toString());
                    }
                }
            } else {
                if (copyIntent.contains("instagram.com")) {
                    binding.etLink.setText(copyIntent);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callDownload(String Url) {
        String UrlWithoutQP = getUrlWithoutParameters(Url);
        UrlWithoutQP = UrlWithoutQP + "?__a=1";
        try {
            Utils utils = new Utils(getApplicationContext());
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(this);
                    commonClassForAPI.callResult(instaObserver, UrlWithoutQP,
                            "ds_user_id=" + MyApp.getPreferences().getString(USERID)
                                    + "; sessionid=" + MyApp.getPreferences().getString(SESSIONID));
                }
            } else {
                setToast(getApplicationContext(), NO_CONNECTION);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private DisposableObserver<JsonObject> instaObserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject versionList) {
            Utils.hideProgressDialog(activity);
            try {
                Log.e("onNext: ", versionList.toString());
                Type listType = new TypeToken<ResponseModel>() {
                }.getType();
                ResponseModel responseModel = new Gson().fromJson(versionList.toString(), listType);
                EdgeSidecarToChildren edgeSidecarToChildren = responseModel.getGraphql().getShortcode_media().getEdge_sidecar_to_children();
                if (edgeSidecarToChildren != null) {
                    List<Edge> edgeArrayList = edgeSidecarToChildren.getEdges();
                    for (int i = 0; i < edgeArrayList.size(); i++) {
                        if (edgeArrayList.get(i).getNode().isIs_video()) {
                            VideoUrl = edgeArrayList.get(i).getNode().getVideo_url();
                            startDownload(VideoUrl, RootDirectoryInsta, activity, getVideoFilenameFromURL(VideoUrl));
                            binding.etLink.setText("");
                            VideoUrl = "";

                        } else {
                            PhotoUrl = edgeArrayList.get(i).getNode().getDisplay_resources().get(edgeArrayList.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            startDownload(PhotoUrl, RootDirectoryInsta, activity, getImageFilenameFromURL(PhotoUrl));
                            PhotoUrl = "";
                            binding.etLink.setText("");
                        }
                    }
                } else {
                    boolean isVideo = responseModel.getGraphql().getShortcode_media().isIs_video();
                    if (isVideo) {
                        VideoUrl = responseModel.getGraphql().getShortcode_media().getVideo_url();
                        //new DownloadFileFromURL().execute(VideoUrl,getFilenameFromURL(VideoUrl));
                        startDownload(VideoUrl, RootDirectoryInsta, activity, getVideoFilenameFromURL(VideoUrl));
                        VideoUrl = "";
                        binding.etLink.setText("");
                    } else {
                        PhotoUrl = responseModel.getGraphql().getShortcode_media().getDisplay_resources()
                                .get(responseModel.getGraphql().getShortcode_media().getDisplay_resources().size() - 1).getSrc();

                        startDownload(PhotoUrl, RootDirectoryInsta, activity, getImageFilenameFromURL(PhotoUrl));
                        PhotoUrl = "";
                        binding.etLink.setText("");
                        // new DownloadFileFromURL().execute(PhotoUrl,getFilenameFromURL(PhotoUrl));
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };

    public String getImageFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    private String getUrlWithoutParameters(String url) {
        try {
            URI uri = new URI(url);
            return new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null, // Ignore the query part of the input url
                    uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            setToast(getApplicationContext(), getResources().getString(R.string.enter_valid_url));
            return "";
        }
    }
    public static void createFileFolder() {
        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
    }

}