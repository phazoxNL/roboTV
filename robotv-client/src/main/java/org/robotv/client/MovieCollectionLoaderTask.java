package org.robotv.client;

import android.os.AsyncTask;
import android.util.Log;

import org.robotv.msgexchange.Packet;
import org.robotv.client.model.Movie;

import java.util.ArrayList;
import java.util.Collection;

public class MovieCollectionLoaderTask extends AsyncTask<Connection, Void, Collection<Movie>> {

    public interface Listener {
        void onCompleted(Collection<Movie> adapter);
    }

    private final static String TAG = "MovieCollectionTask";

    private final Connection connection;
    private Listener listener;

    MovieCollectionLoaderTask(Connection connection) {
        this.connection = connection;
    }

    public void load(Listener listener) {
        this.listener = listener;
        execute(connection);
    }

    @Override
    protected Collection<Movie> doInBackground(Connection... params) {

        // get movies
        Packet request = connection.CreatePacket(Connection.XVDR_RECORDINGS_GETLIST);
        Packet response = connection.transmitMessage(request);

        if(response == null) {
            Log.e(TAG, "no response for request");
            return null;
        }

        ArrayList<Movie> collection = new ArrayList<>(500);

        // uncompress response
        response.uncompress();

        while(!response.eop()) {
            final Movie movie = PacketAdapter.toMovie(response);
            collection.add(movie);
        }

        return collection;
    }

    @Override
    protected void onPostExecute(Collection<Movie> result) {
        if(listener != null) {
            listener.onCompleted(result);
        }
    }

    @Override
    protected void onCancelled(Collection<Movie> result) {
        if(listener != null) {
            listener.onCompleted(result);
        }
    }

}
