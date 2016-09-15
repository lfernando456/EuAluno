package com.andreoid.EuAluno;

/**
 * Created by André on 22/05/2016.
 */
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;

import com.andreoid.EuAluno.models.Download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DownloadService extends IntentService {

    private RequestInterface requestInterface;
    private long total;
    private File outputFile;
    private int MAXTRIES = 3;
    private Response<ResponseBody> request;
    private String mimetype;

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private double totalFileSize;
    private Intent intent = new Intent();
    private String filename;
    @Override
    protected void onHandleIntent(Intent intent) {
        this.intent=intent;
        filename = intent.getExtras().getString("FILENAME");
        requestInterface = RetroClient.getApiService(1);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download_white_24dp)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_download_grey600_48dp))
                .setContentTitle("Download")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText("Baixando " + filename)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        notificationManager.notify(filename, 0, notificationBuilder.build());

        initDownload(1);

    }

    private void initDownload(int tries){
        try {
            request = requestInterface.download(filename).execute();
            mimetype = request.headers().get("Content-Type");
            //System.out.println(mimetype);
            downloadFile(request.body());
        } catch (IOException e) {
            e.printStackTrace();
            if(tries<=MAXTRIES)initDownload(tries+1);
            else sendNotificationError();
        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        outputFile = getFile(filename);
        OutputStream output = new FileOutputStream(outputFile);
        total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = Math.round((fileSize / (Math.pow(1024, 1))));
            double current = Math.round(total / (Math.pow(1024, 1)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize(current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private static File getFile(String filename) {
        String fileNameWithOutExt = removeExtension(filename);
        String extension = getExtension(filename);
        int count=1;
        while(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename).exists()) {
            filename = fileNameWithOutExt + "-" + count+extension;
            ++count;
        }return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
    }

    private void sendNotification(Download download){

        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText("Baixando " + outputFile.getName() + ": " + download.getCurrentFileSize() + "/" + totalFileSize + " KB");
        notificationBuilder.setPriority(Notification.PRIORITY_DEFAULT);

        notificationManager.notify(filename,0, notificationBuilder.build());
    }
    private void sendNotificationError(){

        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_error_black_48dp));
        notificationBuilder.setContentText("Falha no Download");
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

        outputFile.delete();
        notificationManager.notify(filename,0, notificationBuilder.build());
    }


    private void onDownloadComplete(){


        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("Arquivo Baixado: " + outputFile.getName());


        Intent i2 = new Intent();
        i2.setAction(android.content.Intent.ACTION_VIEW);
        i2.setDataAndType(Uri.fromFile(outputFile), mimetype);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i2,0);

        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

        notificationManager.notify(filename,0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    private static final char EXTENSION_SEPARATOR = '.';
    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);

        if (index == -1) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }


    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }

        int index = indexOfExtension(filename);

        if (index == -1) {
            return filename;
        } else {
            return filename.substring(index);
        }
    }



    public static int indexOfExtension(String filename) {

        if (filename == null) {
            return -1;
        }

        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);

        /*int lastDirSeparator = this.filename.lastIndexOf(EXISTS_SEPARATOR);

        if (lastDirSeparator!=-1&&lastDirSeparator < extensionPos&&a) {
            return lastDirSeparator;
        }*/

        return extensionPos;
    }

}