package org.robotv.recordings.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;

import com.google.android.exoplayer2.Format;

import org.robotv.client.MovieController;
import org.robotv.player.Player;
import org.robotv.player.StreamBundle;
import org.robotv.recordings.fragment.PlaybackOverlayFragment;
import org.robotv.recordings.fragment.VideoDetailsFragment;
import org.robotv.client.model.Movie;
import org.robotv.robotv.R;
import org.robotv.dataservice.DataService;
import org.robotv.dataservice.NotificationHandler;
import org.robotv.setup.SetupUtils;
import org.robotv.ui.DataServiceActivity;

public class PlayerActivity extends DataServiceActivity implements Player.Listener, DataService.Listener {

    public static final String TAG = "PlayerActivity";

    private Player mPlayer;
    private PlaybackOverlayFragment mControls;
    private Movie mSelectedMovie;
    private NotificationHandler notificationHandler;
    private long lastUpdateTimeStamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        notificationHandler = new NotificationHandler(this);
        lastUpdateTimeStamp = 0;

        mControls = (PlaybackOverlayFragment) getSupportFragmentManager().findFragmentById(R.id.playback);

        mPlayer = new Player(
            this,
            SetupUtils.getServer(this),                       // Server
            SetupUtils.getLanguageISO3(this),                 // Language
            this,                                      // Listener
            SetupUtils.getPassthrough(this),                  // AC3 passthrough
            SetupUtils.getTunneledVideoPlaybackEnabled(this)
        );

        mControls.setPlayer(mPlayer);
        setServiceListener(this);
    }

   private void startPlayback() {
        if(mSelectedMovie == null) {
            return;
        }

        //DataService service = getService();
        //MovieController controller = service.getMovieController();
        String recid = mSelectedMovie.getRecordingIdString();

        long position = 0; //controller.getPlaybackPosition(recid);

        mPlayer.open(Player.createRecordingUri(recid, position));
        mControls.togglePlayback(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopPlayback();

        if(mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playWhenReady && (playbackState == com.google.android.exoplayer2.Player.STATE_READY)) {
            mControls.startProgressAutomation();
        }
    }

    public void updatePlaybackPosition(boolean force) {
        long now = System.currentTimeMillis();

        if(now - lastUpdateTimeStamp < 5000 && !force) {
            return;
        }

        DataService service = getService();
        long lastPosition = mPlayer.getDurationSinceStart(); // duration since start in ms

        if(service != null) {
            service.getMovieController().setPlaybackPosition(mSelectedMovie, lastPosition);
        }

        lastUpdateTimeStamp = now;
    }

    protected void stopPlayback() {
        if(mPlayer == null) {
            return;
        }

        mControls.stopProgressAutomation();

        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;

        finishAndRemoveTask();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mPlayer == null || keyCode != KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
            return super.onKeyDown(keyCode, event);
        }

        mControls.togglePlayback(mControls.isPlaying());
        return true;
    }

    @Override
    public void onPlayerError(Exception e) {
    }

    @Override
    public void onDisconnect() {
        mPlayer.pause();
    }

    @Override
    public void onTracksChanged(final StreamBundle bundle) {
        runOnUiThread(() -> mControls.updateAudioTracks(bundle));
    }

    @Override
    public void onAudioTrackChanged(final Format format) {
        if(format == null) {
            return;
        }

        runOnUiThread(() -> mControls.updateAudioTrackSelection(Long.parseLong(format.id)));
    }

    @Override
    public void onVideoTrackChanged(Format format) {
    }

    @Override
    public void onRenderedFirstFrame() {
    }

    @Override
    public void onStreamError(int status) {
        notificationHandler.error(getString(R.string.error_open_recording));
    }

    @Override
    public void onConnected(DataService service) {
        // check if thats a service reconnect
        if(mPlayer != null && mPlayer.getPlaybackState() > com.google.android.exoplayer2.Player.STATE_IDLE) {
            return;
        }

        String recid  = (String) getIntent().getSerializableExtra(VideoDetailsFragment.EXTRA_RECID);

        Log.d(TAG, "recid: " + recid);
        mSelectedMovie = service.getMovieController().getMovie(recid);

        SurfaceView mVideoView = findViewById(R.id.videoView);
        mPlayer.setSurface(mVideoView.getHolder().getSurface());

        mControls.setMovie(mSelectedMovie);
        mControls.setUpRows();

        startPlayback();
    }

    @Override
    public void onConnectionError(DataService service) {

    }

    @Override
    public void onMovieUpdate(DataService service) {
    }

    @Override
    public void onTimersUpdated(DataService service) {
    }
}
