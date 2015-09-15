package com.br.timetabler.src;
 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import com.br.timetabler.R;
import com.br.timetabler.adapter.DownAdapter;
import com.br.timetabler.model.Rowdownload;
import com.br.timetabler.util.FileUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
 
public class Download_List_Activity extends Activity {
 
    ProgressDialog progressDialog;
    DownAdapter listViewAdapter;
    ListView listView;
     
    public static final String URL =
        "http://coderzheaven.com/sample_folder/sample_file.png";
    public static final String URL1 =
        "http://coderzheaven.com/sample_folder/sample_file.png";
    public static final String URL2 =
        "http://coderzheaven.com/sample_folder/sample_file.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.downloads_list);
            listView = (ListView) findViewById(R.id.list);
             
        /*Creating and executing background task*/
        GetXMLTask task = new GetXMLTask(this);
        task.execute(new String[] { URL, URL1, URL2 });
         
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("In progress...");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);  
        progressDialog.setMax(100);
        progressDialog.setIcon(R.drawable.ic_launcher);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
     
    private class GetXMLTask extends AsyncTask<String, Integer, List<Rowdownload>> {
        private Activity context;
        List<Rowdownload> rowItems;
        int noOfURLs;
        public GetXMLTask(Activity context) {
            this.context = context;
        }
         
        @Override
        protected List<Rowdownload> doInBackground(String... urls) {
            noOfURLs = urls.length;
            rowItems = new ArrayList<Rowdownload>();
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
                rowItems.add(new Rowdownload(map));
            }
            return rowItems;           
        }
         
        private Bitmap downloadImage(String urlString) {
             
            int count = 0;
            Bitmap bitmap = null;
 
            URL url;
            InputStream inputStream = null;
            BufferedOutputStream outputStream = null;
             
            try {
                url = new URL(urlString);
                URLConnection connection = url.openConnection();
                int lenghtOfFile = connection.getContentLength();
                
                inputStream = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
               
                outputStream = new BufferedOutputStream(dataStream);
                 
                byte data[] = new byte[512];       
                long total = 0;
 
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    /*publishing progress update on UI thread.
                    Invokes onProgressUpdate()*/
                    publishProgress((int)((total*100)/lenghtOfFile));
 
                    // writing data to byte array stream
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();
                 
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;
                 
                byte[] bytes = dataStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,bmOptions);
 
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.close(inputStream);
                FileUtils.close(outputStream);
            }
            return bitmap;
        }
         
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
            if(rowItems != null) {
                progressDialog.setMessage("Loading " + (rowItems.size()+1) + "/" + noOfURLs);
            }
       }
  //changed  DownAdapter       
       @Override
       protected void onPostExecute(List<Rowdownload> rowItems) {
      //  listViewAdapter = new DownAdapter(context, rowItems);
      //  listView.setAdapter(listViewAdapter);
        progressDialog.dismiss();
       }   
      
    }
}