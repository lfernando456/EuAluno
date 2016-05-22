package com.andreoid.EuAluno;

/**
 * Created by André on 22/05/2016.
 */
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
    int MAXTRIES = 3;
    private Response<ResponseBody> request;
    private String mimetype;

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private double totalFileSize;
    Intent intent = new Intent();
    String filename;
    @Override
    protected void onHandleIntent(Intent intent) {
        this.intent=intent;
        filename = intent.getExtras().getString("FILENAME");
        requestInterface = RetroClient.getApiService();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download_white_18dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_download_grey600_48dp))
                .setContentTitle("Download")
                .setContentText("Baixando Arquivo")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initDownload(0);

    }

    private void initDownload(int tries){


        try {
            request = requestInterface.download(filename).execute();
            mimetype = request.headers().get("Content-Type");
            System.out.println(mimetype);
            downloadFile(request.body());
        } catch (IOException e) {
            e.printStackTrace();
            if(tries<=MAXTRIES)initDownload(tries+1);
            else sendNotificationError();
        }

        //downloadFile(request.execute().body());
        //request
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
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

    private void sendNotification(Download download){

        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText("Baixando "+filename+": " + download.getCurrentFileSize() + "/" + totalFileSize + " KB");
        notificationManager.notify(0, notificationBuilder.build());
    }
    private void sendNotificationError(){

        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_alert));
        notificationBuilder.setContentText("Falha no Download");
        outputFile.delete();
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void onDownloadComplete(){


        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("Arquivo Baixado: " + filename);


        Intent i2 = new Intent();
        i2.setAction(android.content.Intent.ACTION_VIEW);
        i2.setDataAndType(Uri.fromFile(outputFile), mimetype);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i2,0);

        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

}