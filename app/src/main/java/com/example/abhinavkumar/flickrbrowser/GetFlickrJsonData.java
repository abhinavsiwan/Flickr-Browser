package com.example.abhinavkumar.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinavkumar on 7/18/16.
 */
public class GetFlickrJsonData extends GetRawData{
    private String LOG_TAG = GetFlickrJsonData.class.getSimpleName();
    private List<Photo> mPhotos;
    private Uri mDestinationUri;

    public GetFlickrJsonData(String searchCriteria, boolean matchAll) {
        super(null);
        mPhotos = new ArrayList<Photo>();
        createAndUpdateUri(searchCriteria, matchAll);
    }

    public List<Photo> getMPhotos() {
        return mPhotos;
    }

    public void execute() {
        //super.setmRawUrl(mDestinationUri.toString());
        Log.v(LOG_TAG, "Built Uri: " +mDestinationUri.toString());

        DownloadJsonData downloadJsonData = new DownloadJsonData();
        downloadJsonData.execute(mDestinationUri.toString());
    }
    //create the Uri component
    public boolean createAndUpdateUri(String searchCriteria, boolean matchAll) {
        final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAGMODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String NO_JSON_CALLBACK_PARAMA = "nojsoncallback";

        mDestinationUri = Uri.parse(FLICKR_API_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM, searchCriteria)
                .appendQueryParameter(TAGMODE_PARAM, matchAll ? "ALL" : "ANY")
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(NO_JSON_CALLBACK_PARAMA, "1")
                .build();

        return mDestinationUri != null;
    }

    public void processResult() {
        if(getmDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error downloading raw file");
            return;
        }

        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTO_URL = "m";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";
        final String FLICKR_LINK = "link";
        final String FLICKR_TAGS = "tags";

        //Parse JSON data
        try {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(FLICKR_ITEMS);

            //Extracting each photo data and binding it into the Photo Object
            for(int i=0;i<itemsArray.length();i++) {
                JSONObject jsonPhoto = itemsArray.getJSONObject(i);

                String title = jsonPhoto.getString(FLICKR_TITLE);
                String author = jsonPhoto.getString(FLICKR_AUTHOR);
                String authorId = jsonPhoto.getString(FLICKR_AUTHOR_ID);
                String link = jsonPhoto.getString(FLICKR_LINK);
                String tags = jsonPhoto.getString(FLICKR_TAGS);

                JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                String photoUrl = jsonMedia.getString(FLICKR_PHOTO_URL);

                //Creat a Photo object with these values
                Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);

                //Add the photObject to the mPhotos List
                this.mPhotos.add(photoObject);
            }
        } catch (JSONException jsone) {
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
        }

        for(Photo singlePhoto : mPhotos) {
            Log.v(LOG_TAG, singlePhoto.toString());
        }
    }

    public class DownloadJsonData extends DownloadRawData {
        @Override
        protected String doInBackground(String... params) {
            //String[] par = {mDestinationUri.toString()};
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }
    }
}
