package de.ivleafcloverapps.pillreminder.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.activities.MainActivity;

/**
 * Created by Christian on 26.05.2017.
 *
 * the service that sends one notification
 */

public class NotificationService extends Service {

    private static final int NOTIFICATION_ID = 91;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // set Intent Activity to open, when on notification is clicked
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // set Notifocation here
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.notication)).setWhen(System.currentTimeMillis());
        builder.setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);

        // stop service hear
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // we do not need this method
        return null;
    }
}
