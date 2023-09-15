package com.applock.fingerprint.passwordlock.downloaderUtil.utils;

import static com.applock.fingerprint.passwordlock.downloaderUtil.utils.DownloadVideosMain.fromService;
import static com.applock.fingerprint.passwordlock.downloaderUtil.utils.DownloadVideosMain.pd;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.applock.fingerprint.passwordlock.downloaderUtil.models.VideoModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DailyMotionDownloader {

    private final Context context;
    private String Quality;
    private String FinalURL;
    private final String VideoURL;
    private long DownLoadID;

    public DailyMotionDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    public void DownloadVideo() {
        new Data().execute(getVideoId(VideoURL));
    }

    private String createDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator);

        File subFolder = null;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        } else {
            boolean success1 = true;
            subFolder = new File(folder.getPath() + File.separator + "Dailymotion Videos");
            if (!subFolder.exists()) {
                success1 = subFolder.mkdirs();
            }
        }
        return subFolder.getPath();
    }

    public static String getVideoId(String link) {
        String videoId;
        if (link.contains("?")) {
            videoId = link.substring(link.indexOf("video/") + 1, link.indexOf("?"));
        } else {
            videoId = link.substring(link.indexOf("video/") + 1);
        }
        videoId = videoId.substring(videoId.lastIndexOf("/") + 1);
        return videoId;
    }


    private class Data extends AsyncTask<Object, Integer, ArrayList<VideoModel>> {
        ArrayList<VideoModel> videoModels = new ArrayList();
        private String videoModelsstr;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public ArrayList<VideoModel> doInBackground(Object... parm) {

            BufferedReader reader = null;
            try {


                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .addInterceptor(logging)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url("https://www.dailymotion.com/player/metadata/video/" + parm[0] + "?embedder=https%3A%2F%2Fwww.dailymotion.com%2Fvideo%2F" + parm[0] + "&referer=&dmTs=453325&section_type=player&component_style=_&is_native_app=0&dmV1st=C5B9DB63B03B4E5D25084AF736BB9520&app=com.dailymotion.neon&client_type=website")
                        .addHeader("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                        .build();
                Response response = client.newCall(request).execute();


                if (response.code() == 200) {
                    String manifest_M3u8url = new JSONObject(response.body().string()).getJSONObject("qualities").getJSONArray("auto").getJSONObject(0).getString("url");

                    System.out.println("myfinalurlis 1=" + manifest_M3u8url);


                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(manifest_M3u8url).openConnection();
                        httpURLConnection.setConnectTimeout(60000);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                        HashMap hashMap = null;
                        Pattern compile = Pattern.compile("\\d+");
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                bufferedReader.close();
                                //  HomeActivity.this.customProgress.hideProgress();
                                return this.videoModels;
                            } else if (readLine.equals("#EXTM3U")) {
                                hashMap = new HashMap();
                            } else if (readLine.contains("#EXT-X-STREAM-INF")) {
                                compile.matcher(readLine).find();
                                if (hashMap != null) {
                                    VideoModel videoModel = new VideoModel();
                                    Log.e("TAG", "doInBackground: " + readLine);
                                    if (readLine.contains("PROGRESSIVE-URI")) {
                                        String[] split3 = readLine.split(",");
                                        String[] split4 = split3[3].split("=");
                                        split3[0].split("=");
                                        String[] split5 = split3[5].split("=");
                                        //   videoModel.setName(Uri.parse(split5[1].substring(1)).getLastPathSegment());
                                        videoModel.setQuality(split4[1].replace("\"", ""));
                                        videoModel.setUrl(split5[1].substring(1));
                                        this.videoModels.add(videoModel);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("MyTag", e.toString());
                    }


                    return this.videoModels;

                } else {
                    return null;
                }

            } catch (Exception e) {

                System.out.println("error is " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }


        public ArrayList<VideoModel> removeDuplicates(ArrayList<VideoModel> arrayList) {
            TreeSet treeSet = new TreeSet(new Comparator<VideoModel>() {
                public int compare(VideoModel videoModel, VideoModel videoModel2) {
                    return videoModel.getQuality().equalsIgnoreCase(videoModel2.getQuality()) ? 0 : 1;
                }
            });
            treeSet.addAll(arrayList);
            return new ArrayList<>(treeSet);
        }


        protected void DownloadFiles(String decryptedurl) {
            try {
                URL u = new URL(decryptedurl);
                InputStream is = u.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/master.m3u8");

                FileOutputStream fos = new FileOutputStream(file);
                while ((length = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }


                List<String> lines = Collections.emptyList();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lines = Files.readAllLines(Paths.get(file.toString()), StandardCharsets.UTF_8);
                }

                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("http")) {
                        System.out.println("wojfdjhfdjh qqqq master data = " + lines.get(i));
                    } else {
                        lines.remove(i);
                    }
                }


                URL u2 = new URL(lines.get(1));
                System.out.println("wojfdjhfdjh qqqq master data lone0=" + lines.get(0));

                InputStream is2 = u2.openStream();

                DataInputStream dis2 = new DataInputStream(is2);

                byte[] buffer2 = new byte[1024];
                int length2;
                File fileindex0 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/index_0_a_1.m3u8");

                FileOutputStream fos2 = new FileOutputStream(fileindex0);
                while ((length2 = dis2.read(buffer2)) > 0) {
                    fos2.write(buffer, 0, length2);
                }


                List<String> lines2 = Collections.emptyList();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lines2 = Files.readAllLines(Paths.get(fileindex0.toString()), StandardCharsets.UTF_8);
                }


                for (int i = 0; i < lines2.size(); i++) {
                    if (lines2.get(i).contains("http")) {
                        System.out.println("wojfdjhfdjh qqqq datanew indexread= " + lines2.get(i));
                    } else {
                        lines2.remove(i);
                    }
                }


                URL u3 = new URL(lines.get(1));
                System.out.println("wojfdjhfdjh qqqq master data lone0=" + lines.get(0));

                InputStream is3 = u3.openStream();

                DataInputStream dis3 = new DataInputStream(is3);

                byte[] buffer3 = new byte[1024];
                int length3;

                File fileindex1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/index_0_a_2.m3u8");

                FileOutputStream fos3 = new FileOutputStream(fileindex1);
                while ((length3 = dis3.read(buffer3)) > 0) {
                    fos3.write(buffer, 0, length3);
                }


                List<String> lines3 = Collections.emptyList();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lines3 = Files.readAllLines(Paths.get(fileindex1.toString()), StandardCharsets.UTF_8);
                }


                for (int i = 0; i < lines3.size(); i++) {
                    if (lines3.get(i).contains("http")) {
                        System.out.println("wojfdjhfdjh qqqq datanew indexread2= " + lines3.get(i));
                    } else {
                        lines3.remove(i);
                    }
                }


            } catch (Exception mue) {
                mue.printStackTrace();
                Log.e("SYNC getUpdate", mue.getMessage());
            }
        }


        public void onPostExecute(ArrayList<VideoModel> arrayList) {
            super.onPostExecute(arrayList);

            try {
                ArrayList<VideoModel> removeDuplicates = this.removeDuplicates(arrayList);
                this.videoModels = removeDuplicates;
                if (removeDuplicates != null && removeDuplicates.size() > 0) {


                    ArrayList<VideoModel> removeDuplicatesvalues = removeDuplicates(this.videoModels);


                    CharSequence[] charSequenceArr = new CharSequence[removeDuplicatesvalues.size()];

                    System.out.println("fjsdjkhfsdjfsd =" + removeDuplicatesvalues.size());

                    for (int i = 0; i < removeDuplicatesvalues.size(); i++) {
                        System.out.println("mybsdfghsdgfhjsd =" + removeDuplicatesvalues.get(i).getQuality());
                        charSequenceArr[i] = removeDuplicatesvalues.get(i).getQuality();
                    }

                    new AlertDialog.Builder(context).setTitle("Quality!").setItems(charSequenceArr, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DownloadFileMain.startDownloading(context, removeDuplicatesvalues.get(i).getUrl(), "Dailymotion_" + System.currentTimeMillis(), ".mp4");

                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!fromService) {

                                pd.dismiss();
                            }
                            dialogInterface.dismiss();
                        }
                    }).setCancelable(false).show();


                    if (!fromService) {

                        pd.dismiss();
                    }


                }


            } catch (Exception e) {

                System.out.println("sdfsfsfsdfsdfs =" + e.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
            }
        }
    }
}
