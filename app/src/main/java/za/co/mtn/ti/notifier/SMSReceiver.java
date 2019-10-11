/**
 * 
 */
package za.co.mtn.ti.notifier;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.smslistener.R;

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
	

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		        
        
		Log.i(TAG, "Intent recieved: " + intent.getAction());
        if (intent.getAction().equals(DATA_SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {
                    Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
                    Toast.makeText(context, messages[0].getMessageBody(), Toast.LENGTH_SHORT).show();
                    
                    try {
                    	 SMSNotification smsnotification = new SMSNotification( messages[0].getMessageBody());
                         notify(context,smsnotification);
					} catch (Exception e) {
						Log.e(TAG,"SMS received cannot be parsed correctly!");
						e.printStackTrace();
					}
                   
                }
            }
        }
	}
	
	public void notify(Context context, SMSNotification sms){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.mtn_logo)
		        .setContentTitle(sms.getHeading())
		        .setContentText(sms.getMessage());

		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		int mId = sms.getId();
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mId, mBuilder.build());
		
	}
}
