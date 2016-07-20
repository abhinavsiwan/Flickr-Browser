package com.example.abhinavkumar.flickrbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    //Part 2
    private List<Photo> mPhotosList = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Part 2
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProcessPhotos processPhotos = new ProcessPhotos("android", true);
        processPhotos.execute();

        //GetRawData getRawData = new GetRawData("https://api.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1");
        //getRawData.execute();

        /*GetFlickrJsonData jsonData = new GetFlickrJsonData("android", true);
        jsonData.execute();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Part 2
    public class ProcessPhotos extends GetFlickrJsonData {

        public ProcessPhotos(String searchCriteria, boolean matchAll) {
            super(searchCriteria, matchAll);
        }

        public void execute() {
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            @Override
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this, getMPhotos());
                mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
            }
        }
    }
}
