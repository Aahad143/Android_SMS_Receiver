/**
 *
 */
package za.co.kaka.yusuf.smsnotify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.smslistener.R;

import java.util.Objects;

/**
 * @author yusuf
 *
 */
public class SMSReceiver extends BroadcastReceiver {

    /**
     *
     */
    private static final String DATA_SMS_RECEIVED = "android.intent.action.DATA_SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
    private NotificationChannel nChannel;

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Intent received: " + intent.getAction());
        this.nChannel = initChannels(context);
        if (Objects.equals(intent.getAction(), DATA_SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                assert pdus != null;
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    String format = bundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i],format);
                    Log.d(TAG, "Message received: " + messages[i].getMessageBody());
                    Toast.makeText(context, messages[i].getMessageBody(), Toast.LENGTH_SHORT).show();
                }
                try {
                    SMSNotification smsnotification = new SMSNotification( messages[0].getMessageBody());
                    notify(context,smsnotification, nChannel);
                } catch (Exception e) {
                    Log.e(TAG,"SMS received cannot be parsed correctly!");
                    //e.printStackTrace();
                }
            }
        }
    }

    public NotificationChannel initChannels(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
        return channel;
    }

    public void notify(Context context, SMSNotification sms, NotificationChannel channel){
        Notification.Builder mBuilder = new Notification.Builder(context , nChannel.getId())
                .setSmallIcon(R.drawable.ic_sms_notifier)
                .setContentTitle(sms.getHeading())
                .setContentText(sms.getMessage())
                //.setPriority(Notification.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int mId = sms.getId();
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

    }
}
