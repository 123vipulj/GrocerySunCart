package com.suncart.grocerysuncart.config;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.suncart.grocerysuncart.MainActivity;
import com.suncart.grocerysuncart.R;
import com.suncart.grocerysuncart.model.fcm.NotificationData;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class GroceryFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = GroceryFirebaseMessagingService.class.getCanonicalName();



    private EventBus eventBus = EventBus.getDefault();

    public GroceryFirebaseMessagingService() {

        //eventBus.register(this);
        //this.userService = new UserService(ENSApplication.getAppContext());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "### Firebase Token: " + token);
        //  Toast.makeText(ENSFirebaseMessagingService.this, token, Toast.LENGTH_SHORT).show();
        //   userService.updateUserFCMKey(ENSApplication.getLoggedInUserId(), token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "### Firebase Message Received: " + remoteMessage);

        final NotificationData notificationData = new NotificationData(remoteMessage);

        Handler uiHandler = new Handler(Looper.getMainLooper());

        uiHandler.post(() -> {

            Glide.with(this).load(notificationData.getImageUrl()).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    sendNotificationImages(drawableToBitmap(resource), notificationData);
                    return true;
                }
            }).submit();

        });

    }


    private void sendNotificationImages(Bitmap bitmap, final NotificationData notificationData) {

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setSummaryText("");
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "SunCart Deal";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel
                    = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "ENSnews", NotificationManager.IMPORTANCE_DEFAULT);

            //Configure Notification Channel
            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
        String formattedDate = sdf.format(date);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(notificationData.getTitle())
                //.setContentText(notificationData.())
                .setContentIntent(pendingIntent)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setStyle(style);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
