package com.example.abhinavkumar.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhinavkumar on 7/18/16.
 */

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset() {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawUrl = null;
        this.mData = null;
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public void setmRawUrl(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }

    public void execute() {
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadrawData = new DownloadRawData();
        downloadrawData.execute(mRawUrl);
    }

    public class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            if(params == null)
                return null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream is = connection.getInputStream();
                if(is == null)
                    return null;

                InputStreamReader isr = new InputStreamReader(is);
                reader = new BufferedReader(isr);

                String line;
                StringBuffer buffer = new StringBuffer();
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                return buffer.toString();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error: ", e);
                return null;
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream: ", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            mData = webData;
            Log.v(LOG_TAG, "Data returned was: " + mData);
            if(mData == null) {
                if(mRawUrl == null) {
                    mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                //success
                mDownloadStatus = DownloadStatus.OK;
            }
        }
    }
}
