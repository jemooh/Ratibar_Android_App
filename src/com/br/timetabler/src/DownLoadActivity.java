package com.br.timetabler.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.br.timetabler.R;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownLoadActivity  extends ActionBarActivity {
private ListView mListView;
private ArrayAdapter<Progress> mAdapter;
private boolean mReceiversRegistered;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.downloads_list);

    ListView listView = mListView = (ListView) findViewById(R.id.list);
    listView.setAdapter(mAdapter = new ArrayAdapter<Progress>(this,
            R.layout.single_row_downloads, R.id.textView, new Progress[] { new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress(),
                    new Progress(), new Progress(), new Progress() }) {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            upadteRow(getItem(position), v);
            return v;
        }
    });

    StringBuilder builder = new StringBuilder();
    
    HttpUriRequest request = new HttpGet("http://www.x12.se/sgu_metadata.xml");
    HttpClient httpclient = new DefaultHttpClient();
    
    try {
        HttpResponse response = httpclient.execute(request);
        
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        try {
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    } catch (ClientProtocolException e) {
        e.printStackTrace();
        
    } catch (IOException e) {
        e.printStackTrace();
        
    }
    
    
    if (savedInstanceState == null) {
        Intent intent = new Intent(this, DownloadingService.class);
        intent.putExtra("http://www.x12.se/sgu_metadata.xml", mAdapter.getCount());
        startService(intent);
    }

    registerReceiver();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver();
}

private void registerReceiver() {
    unregisterReceiver();
    IntentFilter intentToReceiveFilter = new IntentFilter();
    intentToReceiveFilter
            .addAction(DownloadingService.PROGRESS_UPDATE_ACTION);
    LocalBroadcastManager.getInstance(this).registerReceiver(
            mDownloadingProgressReceiver, intentToReceiveFilter);
    mReceiversRegistered = true;
}

private void unregisterReceiver() {
    if (mReceiversRegistered) {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mDownloadingProgressReceiver);
        mReceiversRegistered = false;
    }
}

private void upadteRow(Progress p, View v) {
    ProgressBar bar = (ProgressBar) v.findViewById(R.id.progressBar);
    bar.setProgress(p.progress);
    TextView tv = (TextView) v.findViewById(R.id.textView);
    tv.setText(p.toString());
}

// don't call notifyDatasetChanged() too frequently, have a look at
// following url http://stackoverflow.com/a/19090832/1112882
protected void onProgressUpdate(int position, int progress) {
    final ListView listView = mListView;
    int first = listView.getFirstVisiblePosition();
    int last = listView.getLastVisiblePosition();
    mAdapter.getItem(position).progress = progress > 100 ? 100 : progress;
    if (position < first || position > last) {
        // just update your DataSet he next time getView is called the ui is
        // updated automatically
        return;
    } else {
        View convertView = mListView.getChildAt(position - first);
        // this is the convertView that you previously returned in getView
        // just fix it (for example:)
        upadteRow(mAdapter.getItem(position), convertView);
    }
}

protected void onProgressUpdateOneShot(int[] positions, int[] progresses) {
    for (int i = 0; i < positions.length; i++) {
        int position = positions[i];
        int progress = progresses[i];
        onProgressUpdate(position, progress);
    }
}

private final BroadcastReceiver mDownloadingProgressReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                DownloadingService.PROGRESS_UPDATE_ACTION)) {
            final boolean oneShot = intent
                    .getBooleanExtra("oneshot", false);
            if (oneShot) {
                final int[] progresses = intent
                        .getIntArrayExtra("progress");
                final int[] positions = intent.getIntArrayExtra("position");
                onProgressUpdateOneShot(positions, progresses);
            } else {
                final int progress = intent.getIntExtra("progress", -1);
                final int position = intent.getIntExtra("position", -1);
                if (position == -1) {
                    return;
                }
                onProgressUpdate(position, progress);
            }
        }
    }
};

public static class DownloadingService extends IntentService {
    public static String PROGRESS_UPDATE_ACTION = DownloadingService.class
            .getName() + ".progress_update";
    private boolean mIsAlreadyRunning;

    private ExecutorService mExec;
    private CompletionService<NoResultType> mEcs;
    private LocalBroadcastManager mBroadcastManager;
    private List<DownloadTask> mTasks;

    private static final long INTERVAL_BROADCAST = 800;
    private long mLastUpdate = 0;

    public DownloadingService() {
        super("DownloadingService");
        mExec = Executors.newFixedThreadPool( /* only 5 at a time */5);
        mEcs = new ExecutorCompletionService<NoResultType>(mExec);
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mTasks = new ArrayList<DownLoadActivity.DownloadingService.DownloadTask>();
    }
/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mIsAlreadyRunning) {
            publishCurrentProgressOneShot(true);
        }
        return super.onStartCommand(intent, flags, startId);
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if (mIsAlreadyRunning) {
            return;
        }
        mIsAlreadyRunning = true;

        int noOfFilesToDownalod = intent.getIntExtra("http://www.x12.se/sgu_metadata.xml", 0);
        final Collection<DownloadTask> tasks = mTasks;

        for (int i = 0; i < noOfFilesToDownalod; i++) {
            DownloadTask yt1 = new DownloadTask(i);
            tasks.add(yt1);
        }

        for (DownloadTask t : tasks) {
            mEcs.submit(t);
        }
        // wait for finish
        int n = tasks.size();
        for (int i = 0; i < n; ++i) {
            NoResultType r;
            try {
                r = mEcs.take().get();
                // if (r != null) {
                //
                // }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        // send a last broadcast
        publishCurrentProgressOneShot(true);
        mExec.shutdown();
    }

    private void publishCurrentProgressOneShot(boolean forced) {
        if (forced
                || System.currentTimeMillis() - mLastUpdate > INTERVAL_BROADCAST) {
            mLastUpdate = System.currentTimeMillis();
            final List<DownloadTask> tasks = mTasks;
            int[] positions = new int[tasks.size()];
            int[] progresses = new int[tasks.size()];
            for (int i = 0; i < tasks.size(); i++) {
                DownloadTask t = tasks.get(i);
                positions[i] = t.mPosition;
                progresses[i] = t.mProgress;
            }
            publishProgress(positions, progresses);
        }
    }

    private void publishCurrentProgressOneShot() {
        publishCurrentProgressOneShot(false);
    }

    private synchronized void publishProgress(int[] positions,
            int[] progresses) {
        Intent i = new Intent();
        i.setAction(PROGRESS_UPDATE_ACTION);
        i.putExtra("position", positions);
        i.putExtra("progress", progresses);
        i.putExtra("oneshot", true);
        mBroadcastManager.sendBroadcast(i);
    }

    // following methods can also be used but will cause lots of broadcasts
    private void publishCurrentProgress() {
        final Collection<DownloadTask> tasks = mTasks;
        for (DownloadTask t : tasks) {
            publishProgress(t.mPosition, t.mProgress);
        }
    }

    private synchronized void publishProgress(int position, int progress) {
        Intent i = new Intent();
        i.setAction(PROGRESS_UPDATE_ACTION);
        i.putExtra("progress", progress);
        i.putExtra("position", position);
        mBroadcastManager.sendBroadcast(i);
    }

    class DownloadTask implements Callable<NoResultType> {
        private int mPosition;
        private int mProgress;
        private Random mRand = new Random();

        public DownloadTask(int position) {
            mPosition = position;
        }

        @Override
        public NoResultType call() throws Exception {
            while (mProgress < 100) {
                mProgress += mRand.nextInt(5);
                Thread.sleep(mRand.nextInt(500));

                // publish progress 
                publishCurrentProgressOneShot();

                // we can also call publishProgress(int position, int
                // progress) instead, which will work fine but avoid broadcasts by
                // aggregating them

                // publishProgress(mPosition,mProgress);
            }
            return new NoResultType();
        }

        public int getProgress() {
            return mProgress;
        }

        public int getPosition() {
            return mPosition;
        }
    }

    class NoResultType {
    }
}

public static class Progress {
    int progress;

    @Override
    public String toString() {
        return progress + " %";
    }
}
}