package org.xvdr.player;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelections;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;

class RoboTvLoadControl  implements LoadControl {

    private final DefaultAllocator allocator;

    RoboTvLoadControl() {
        allocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);
    }

    @Override
    public void onPrepared() {
    }

    @Override
    public void onTracksSelected(Renderer[] renderers, TrackGroupArray trackGroups, TrackSelections<?> trackSelections) {
    }

    @Override
    public void onStopped() {
        allocator.reset();
    }

    @Override
    public void onReleased() {
        allocator.reset();
    }

    @Override
    public Allocator getAllocator() {
        return allocator;
    }

    @Override
    public boolean shouldStartPlayback(long bufferedDurationUs, boolean rebuffering) {
        return true;
    }

    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs) {
        // we buffer up to 5 seconds
        return (bufferedDurationUs < 5000000);
    }
}