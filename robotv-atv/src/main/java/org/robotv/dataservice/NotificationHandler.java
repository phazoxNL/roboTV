package org.robotv.dataservice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.robotv.robotv.R;
import org.robotv.ui.GlideApp;

public class NotificationHandler {
    private final static String TAG = "NotificationHandler";

    private final Context mContext;
    private final Handler mHandler;

    public NotificationHandler(Context context) {
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void notify(String message) {
        notify(
            message,
            mContext.getResources().getString(R.string.toast_information),
            R.drawable.ic_info_outline_white_48dp);
    }

    public void error(String message) {
        notify(
            message,
            mContext.getResources().getString(R.string.toast_error),
            R.drawable.ic_error_outline_white_48dp);
    }


    public void notify(final String message, final String title, final String imageUrl) {
        if(TextUtils.isEmpty(imageUrl)) {
            NotificationHandler.this.notify(message, title, R.drawable.ic_info_outline_white_48dp);
            return;
        }

        mHandler.post(() -> GlideApp.with(mContext)
            .load(imageUrl)
            .error(R.drawable.ic_info_outline_white_48dp)
            .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    NotificationHandler.this.notify(message, title, resource);
                }
            }));
    }

    private void notify(final String message, final String title, final Drawable d) {
        if(d == null) {
            notify(message, title, R.drawable.ic_info_outline_white_48dp);
            return;
        }

        mHandler.post(() -> {
            Log.d(TAG, "notify: " + message + " / " + title);
            final Toast toast = new Toast(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(inflater == null) {
                return;
            }

            View view = inflater.inflate(R.layout.layout_toast, null);

            TextView titleView = view.findViewById(R.id.title);
            titleView.setText(title);

            TextView messageView = view.findViewById(R.id.message);
            messageView.setText(message);

            ImageView imageView = view.findViewById(R.id.icon);
            imageView.setImageDrawable(d);

            toast.setView(view);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.END | Gravity.BOTTOM, 0, 0);

            toast.show();
        });
    }

    void notify(String message, String title, final int icon) {
        notify(message, title, mContext.getDrawable(icon));
    }

}
