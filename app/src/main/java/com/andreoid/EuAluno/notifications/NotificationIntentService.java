package com.andreoid.EuAluno.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.andreoid.EuAluno.Constants;
import com.andreoid.EuAluno.MainActivity;
import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.RequestInterface;
import com.andreoid.EuAluno.RetroClient;
import com.andreoid.EuAluno.broadcast_receivers.NotificationEventReceiver;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    private static RequestInterface requestInterface;
    private static ServerRequest request;
    private SharedPreferences pref;

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }
    boolean notificado = false;
    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        requestInterface = RetroClient.getApiService(0);
        request = new ServerRequest();
        //notificado=true;
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        //notificado=false;
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {

                processStartNotification();
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?
        pref = getSharedPreferences("EuAluno", Context.MODE_PRIVATE);

        request.setOperation("getTopicosIndex");

        request.setUnique_id(pref.getString(Constants.UNIQUE_ID, ""));
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                ServerResponse resp = response.body();
//
           //     Toast.makeText(getApplicationContext(),resp.getMessage(),Toast.LENGTH_SHORT).show();
                String content="";

                if(response.body().getListaDeTopicos()!=null) {
                    for (int i = 0; i < response.body().getListaDeTopicos().size(); i++) {
                        if (response.body().getListaDeTopicos().get(i).getTopic_viewed() == 0) {

                            content += response.body().getListaDeTopicos().get(i).getNomeDisciplina() + ": " + response.body().getListaDeTopicos().get(i).getTopic_subject() + "\n";
                        }
                    }
                    if (content != "")
                        showNotification("Atualizações de conteúdo", content.substring(0, content.lastIndexOf('\n')));
                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                //System.out.println(call.request().body());

                Log.d(Constants.TAG, "falha notification");


            }
        });

    }
    private void showNotification(String title, String content){
        System.out.println("adsadsada222");
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title)
                .setAutoCancel(true)
                //.setColor(getResources().getColor(R.color.colorAccent))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher)  ;

        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
