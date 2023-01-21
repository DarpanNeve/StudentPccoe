package com.Pccoe.Student;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notification extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        checkfirebase(context);
    }

    private void checkfirebase(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messageRef = database.getReference().child("chats");

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sender = dataSnapshot.child("name").getValue(String.class);
                String text = dataSnapshot.child("message").getValue(String.class);
                // Create a new Message object with the data
                sendNotification(context, sender, text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    private void sendNotification(Context context, String name, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "n")
                .setSmallIcon(R.drawable.pccoelogo)
                .setContentTitle(name)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(999, builder.build());
    }
}

