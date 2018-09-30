package com.example.jungeb.seoulapp.PushAlarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.example.jungeb.seoulapp.R;

import java.util.List;

public class PushAlarm {
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;

    public PushAlarm(Context context){
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("notification", "sdk.26"); //API 26버전부터는 NotificationManager 사용시 NotificationChannel 생성 필요
            notificationChannel = new NotificationChannel("Main_Notifi", "Main_channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }else{
            Log.d("notification", "sdk.25");
        }
    }
    public void VisitMessages(Context context, String language, List resultlist){
        int resultcnt;
        String tourName;
        resultcnt = (int)resultlist.get(0);
        tourName = resultlist.get(1).toString();

        if (language == "ko"){
            if (resultcnt > 1){
                showMessages(context, tourName + "와 주변 지역에 처음 방문하셨습니다.", "앱이름");
            }else {
                showMessages(context, tourName + "에 처음 방문하셨습니다.", "앱이름");
            }
        }else{
            if (resultcnt > 1){
                showMessages(context, "first arrived at the "+ tourName + "and surrounding area.", "appname");
            }else {
                showMessages(context, "first arrived at the" + tourName + ".", "appname");
            }
        }

}
    public void showMessages(Context context, String messages, String title){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(context, context.getClass());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context, "Main_Notifi")
                    .setContentTitle(title)
                    .setContentText(messages)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.back) //아이콘 교체 필요
                    .setAutoCancel(true);
            notificationManager.notify(0, builder.build());
        }
    }
}
