package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.utils.SharePreferences.isNonNull;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.adapter.WhatsappStoryAdapter;
import com.applock.fingerprint.passwordlock.databinding.ActivityStatusSaverBinding;
import com.applock.fingerprint.passwordlock.databinding.DialogWhatsAppPermissionBinding;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.Constants;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.iUtils;
import com.applock.fingerprint.passwordlock.model.WAStoryModel;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;
import com.applock.fingerprint.passwordlock.utils.Utility;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class StatusSaverActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private static final int OPEN_DOCOMENT_TREE_REQUEST_CODE = 1012;
    private static final int OPEN_DOCOMENT_TREE_REQUEST_CODE_BUSINESS = 1013;
    static DocumentFile[] documentFilesWhatsapp;
    static DocumentFile[] documentFilesWhatsappBusiness;
    ArrayList<Object> filesList = new ArrayList<>();
    ActivityStatusSaverBinding binding;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout recyclerLayout;
    private TextView grantpermissionand11;
    private TextView grantpermissionand11business;
    private String namedataprefs;
    private String namedataprefs_business;
    private Activity myselectedActivity = null;
    private boolean isWhatsAppBusinessAvaliable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_status_saver);

        myselectedActivity = this;

        recyclerView = binding.recyclerView;
        recyclerLayout = binding.swipeRecyclerViewlayout;
        grantpermissionand11 = binding.grantpermissionand11;
        grantpermissionand11business = binding.grantpermissionand11business;

//        setGradientShaderToTextView(binding.tvTitle, getColor(R.color.primary), getColor(R.color.secondary));

        isWhatsAppBusinessAvaliable = iUtils.isMyPackageInstalled("com.whatsapp.w4b", getPackageManager());

        namedataprefs = new SharePreferences(getApplicationContext()).getPREFERENCE_whatsapp();
        namedataprefs_business = new SharePreferences(getApplicationContext()).getPREFERENCE_whatsappbusiness();


        grantpermissionand11.setOnClickListener(v -> showAccessDialog(0));


        grantpermissionand11business.setOnClickListener(v -> showAccessDialog(1));

        binding.btnBack.setOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }


    private void showAccessDialog(int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(myselectedActivity);
        DialogWhatsAppPermissionBinding permissionBinding = DialogWhatsAppPermissionBinding.inflate(LayoutInflater.from(myselectedActivity));
        builder.setView(permissionBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        permissionBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        permissionBinding.btnAllow.setOnClickListener(v -> {
            if (type == 0){
                grantAndroid11StorageAccessPermission();
            } else grantAndroid11StorageAccessPermissionBusiness();
            dialog.dismiss();
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        binding.btnBack.setOnClickListener(view -> onBackPressed());

        if (!isNonNull(namedataprefs)) {
            grantpermissionand11.setVisibility(View.GONE);
            runMyTask();
        }
        if (!isNonNull(namedataprefs_business)) {
            grantpermissionand11business.setVisibility(View.GONE);
            runMyTask();
        }


        boolean result = checkPermission();
        if (result) {
            if (isNonNull(namedataprefs)) {

                grantpermissionand11.setVisibility(View.VISIBLE);
            } else {
                if (isWhatsAppBusinessAvaliable) {
                    if (getDataFromWhatsAppBusinessFolder() != null && !isNonNull(namedataprefs_business)) {

                        grantpermissionand11business.setVisibility(View.GONE);
                    } else {
                        grantpermissionand11business.setVisibility(View.VISIBLE);

                    }
                } else {
                    if (getDataFromWhatsAppFolder() != null) {

                        grantpermissionand11.setVisibility(View.GONE);
                        runMyTask();
                    } else {
                        grantpermissionand11.setVisibility(View.VISIBLE);

                    }
                }

            }
        }

        recyclerLayout.setOnRefreshListener(() -> {
            try {
                if (result) {
                    documentFilesWhatsapp = null;
                    documentFilesWhatsappBusiness = null;
                    if (!isNonNull(namedataprefs) && getDataFromWhatsAppFolder() != null) {

                        if (isWhatsAppBusinessAvaliable) {
                            if (getDataFromWhatsAppBusinessFolder() != null) {

                                grantpermissionand11business.setVisibility(View.GONE);
                            } else {

                                grantpermissionand11business.setVisibility(View.VISIBLE);
                            }
                        } else {
                            grantpermissionand11business.setVisibility(View.GONE);

                        }

                        grantpermissionand11.setVisibility(View.VISIBLE);

                        recyclerLayout.setRefreshing(true);
                        runMyTask();


                        (new Handler()).postDelayed(() -> {
                            try {
                                recyclerLayout.setRefreshing(false);
                                iUtils.ShowToast(myselectedActivity, getString(R.string.refre));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(myselectedActivity, R.string.refre, Toast.LENGTH_SHORT).show();
                        }, 2000);


                    }
                }


            } catch (Exception e) {
                iUtils.ShowToastError(myselectedActivity, "Error in swipe refresh " + e.getMessage());
            }
        });


    }

    @SuppressLint("WrongConstant")
    public void grantAndroid11StorageAccessPermission() {
        System.out.println("mydhfhsdkfsd 00");

        if (iUtils.isMyPackageInstalled("com.whatsapp", myselectedActivity.getPackageManager())) {


            Intent intent;
            String whatsappfolderdir;
            StorageManager storageManager = (StorageManager) myselectedActivity.getSystemService(STORAGE_SERVICE);

            if (new File(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses").isDirectory()) {
                whatsappfolderdir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
            } else {
                whatsappfolderdir = "WhatsApp%2FMedia%2F.Statuses";
            }

            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String scheme = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString().replace("/root/", "/document/");
                String stringBuilder = scheme + "%3A" + whatsappfolderdir;
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(stringBuilder));

                System.out.println("mydhfhsdkfsd " + stringBuilder);
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(whatsappfolderdir));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            startActivityForResult(intent, OPEN_DOCOMENT_TREE_REQUEST_CODE);
        } else {
            iUtils.ShowToastError(myselectedActivity, myselectedActivity.getString(R.string.appnotinstalled));
        }
    }

    @SuppressLint("WrongConstant")
    public void grantAndroid11StorageAccessPermissionBusiness() {
        System.out.println("mydhfhsdkfsd 00");

        if (iUtils.isMyPackageInstalled("com.whatsapp.w4b", myselectedActivity.getPackageManager())) {


            Intent intent;
            String whatsappfolderdir;
            StorageManager storageManager = (StorageManager) myselectedActivity.getSystemService(STORAGE_SERVICE);

            if (new File(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses").isDirectory()) {

                whatsappfolderdir = "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses";
            } else if (new File(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/.Statuses").isDirectory()) {
                whatsappfolderdir = "WhatsApp%20Business%2FMedia%2F.Statuses";
            } else {
                return;
            }

            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String scheme = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString().replace("/root/", "/document/");
                String stringBuilder = scheme + "%3A" + whatsappfolderdir;
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(stringBuilder));

                System.out.println("mydhfhsdkfsd " + stringBuilder);
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(whatsappfolderdir));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            startActivityForResult(intent, OPEN_DOCOMENT_TREE_REQUEST_CODE_BUSINESS);
            return;
        } else {
            iUtils.ShowToastError(myselectedActivity, myselectedActivity.getString(R.string.appnotinstalled));
        }
    }


    private DocumentFile[] getDataFromWhatsAppFolder() {
        try {
            if (documentFilesWhatsapp != null) {
                Log.e("StatusSaverMainFragment", "data not empty");

                return documentFilesWhatsapp;
            } else {
                Log.e("StatusSaverMainFragment", "empty");

                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(myselectedActivity.getApplicationContext(), Uri.parse(namedataprefs));
                if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory() && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
                    documentFilesWhatsapp = fromTreeUri.listFiles();
                    return documentFilesWhatsapp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private DocumentFile[] getDataFromWhatsAppBusinessFolder() {
        try {
            if (documentFilesWhatsappBusiness != null) {
                Log.e("StatusSaverMainFragment", "business data not empty");
                return documentFilesWhatsappBusiness;
            } else {
                Log.e("StatusSaverMainFragment", "business empty");
                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(myselectedActivity.getApplicationContext(), Uri.parse(namedataprefs_business));
                if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory() && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
                    documentFilesWhatsappBusiness = fromTreeUri.listFiles();
                    return documentFilesWhatsappBusiness;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(myselectedActivity);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.pernecessory);
                alertBuilder.setMessage(R.string.write_neesory);
                alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE));
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
            return false;
        } else {
            return true;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(myselectedActivity);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle(R.string.pernecessory);
            alertBuilder.setMessage(R.string.write_neesory);
            alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE));
            AlertDialog alert = alertBuilder.create();
            alert.show();
        }
    }

    private void runMyTask() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

        new MyDataLoadTask(StatusSaverActivity.this).execute();

//            }
//        }, 100);
    }

    private ArrayList<Object> getData() {


        if (Build.VERSION.SDK_INT >= 30) {
            if (filesList != null) {
                filesList = new ArrayList<>();
            }
            WAStoryModel f;
            String targetPath = "";
            DocumentFile[] allFiles;
            try {
                DocumentFile[] allFilesWhatsapp = (documentFilesWhatsapp != null) ? documentFilesWhatsapp : getDataFromWhatsAppFolder();
                if (isWhatsAppBusinessAvaliable) {
                    DocumentFile[] allFilesBusiness = (documentFilesWhatsappBusiness != null) ? documentFilesWhatsappBusiness : getDataFromWhatsAppBusinessFolder();
                    allFiles = ArrayUtils.addAll(allFilesBusiness, allFilesWhatsapp);

                } else {
                    allFiles = allFilesWhatsapp;

                }

                for (DocumentFile allFile : Objects.requireNonNull(allFiles)) {
                    Uri file = allFile.getUri();
                    f = new WAStoryModel();
                    f.setName(getString(R.string.stor_saver));
                    f.setUri(file);
                    f.setPath(allFile.getUri().toString());
                    f.setFilename(allFile.getUri().getLastPathSegment());


                    if (!allFile.getUri().toString().contains(".nomedia") && !allFile.getUri().toString().equals("")) {
                        filesList.add(f);
                    }
                    runOnUiThread(() -> {
                        grantpermissionand11.setVisibility(View.GONE);
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            if (filesList != null) {
                filesList = new ArrayList<>();
            }
            WAStoryModel f;
            String targetPath = "";

            SharedPreferences prefs = myselectedActivity.getSharedPreferences("whatsapp_pref", MODE_PRIVATE);
            String name = prefs.getString("whatsapp", "main");//"No name defined" is the default value.


            String mainWP = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME + "Media/.Statuses";

            String mainWP_11 = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME_Whatsapp_and11 + "Media/.Statuses";

            String mainWP_B = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME_Whatsappbusiness + "Media/.Statuses";

            String mainWP_B_11 = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME_Whatsapp_and11_B + "Media/.Statuses";

            String mainFM = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FMWhatsApp/Media/.Statuses";

            String mainGB = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBWhatsApp/Media/.Statuses";


            File targetDirector1 = new File(mainWP);
            File targetDirector2 = new File(mainWP_11);

            File targetDirector3 = new File(mainWP_B);

            File targetDirector4 = new File(mainWP_B_11);

            File targetDirector5 = new File(mainFM);

            File targetDirector6 = new File(mainGB);


            ArrayList<File> aList = new ArrayList<>(Arrays.asList(targetDirector1.listFiles() != null ? Objects.requireNonNull(targetDirector1.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector2.listFiles() != null ? Objects.requireNonNull(targetDirector2.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector3.listFiles() != null ? Objects.requireNonNull(targetDirector3.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector4.listFiles() != null ? Objects.requireNonNull(targetDirector4.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector5.listFiles() != null ? Objects.requireNonNull(targetDirector5.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector6.listFiles() != null ? Objects.requireNonNull(targetDirector6.listFiles()) : new File[]{new File("")}));


            File[] n = new File[aList.size()];
            File[] files;
            Arrays.setAll(n, aList::get);
            files = n;


            try {
                Arrays.sort(Objects.requireNonNull(files), (Comparator) (o1, o2) -> Long.compare(((File) o2).lastModified(), ((File) o1).lastModified()));

                for (File file : files) {
                    f = new WAStoryModel();
                    f.setName(getString(R.string.stor_saver));
                    f.setUri(Uri.fromFile(file));
                    f.setPath(file.getAbsolutePath());
                    f.setFilename(file.getName());

                    if (!file.getName().equals(".nomedia") && !file.getPath().equals("")) {
                        filesList.add(f);
                    }
                }

                runOnUiThread(() -> {
                    grantpermissionand11.setVisibility(View.GONE);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return filesList;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1239) {
            runMyTask();
            grantpermissionand11.setVisibility(View.GONE);
            grantpermissionand11business.setVisibility(View.GONE);
        } else if (requestCode == OPEN_DOCOMENT_TREE_REQUEST_CODE && resultCode == -1) {

            Uri uri = Objects.requireNonNull(data).getData();

            try {
                myselectedActivity.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            namedataprefs = uri.toString();

            if (isWhatsAppBusinessAvaliable && namedataprefs_business.equals("")) {
                grantpermissionand11.setVisibility(View.GONE);
                grantpermissionand11business.setVisibility(View.VISIBLE);
            } else {
                new SharePreferences(getApplicationContext()).setPREFERENCE_whatsapp(uri.toString());
                new Handler(Looper.getMainLooper()).postDelayed(() -> new MyDataLoadTask(StatusSaverActivity.this).execute(), 2000);
            }


        } else if (requestCode == OPEN_DOCOMENT_TREE_REQUEST_CODE_BUSINESS && resultCode == -1) {

            Uri uri = Objects.requireNonNull(data).getData();


            try {
                myselectedActivity.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            namedataprefs_business = uri.toString();
            new SharePreferences(getApplicationContext()).setPREFERENCE_whatsappbusiness(uri.toString());

            grantpermissionand11business.setVisibility(View.GONE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> new MyDataLoadTask(StatusSaverActivity.this).execute(), 2000);

        }

    }

    private class MyDataLoadTask extends AsyncTask<Void, Void, Void> {

        private Activity activity;

        public MyDataLoadTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                WhatsappStoryAdapter recyclerViewAdapter = new WhatsappStoryAdapter(this.activity, getData());
                runOnUiThread(() -> {
                    recyclerView.setLayoutManager(new GridLayoutManager(this.activity, 2));
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();
                    binding.swipeRecyclerViewlayout.setVisibility(View.VISIBLE);
                    binding.grantlayout.setVisibility(View.GONE);
                });

            } catch (Throwable e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    iUtils.ShowToastError(this.activity, "Error " + e.getMessage());
                });

            }

            return null;
        }
    }


}