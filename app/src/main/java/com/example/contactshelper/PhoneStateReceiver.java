package com.example.contactshelper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class PhoneStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Call";
                String description = "On incoming call";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("Call", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) && incomingNumber != null) {
                Log.d("Prasad_test", "Ringing State Number is - " + incomingNumber);
                Contact contact = ContactsHandler.getContact(context, Long.parseLong(incomingNumber.substring(incomingNumber.length() - 10)));

                if (contact == null) {
                    return;
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Call")
                        .setContentTitle(contact.name)
                        .setContentText(contact.number + "")
                        .setSmallIcon(R.drawable.detective)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
