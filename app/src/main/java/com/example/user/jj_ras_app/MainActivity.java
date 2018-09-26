package com.example.user.jj_ras_app;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.jj_ras_app.Network.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    BackgroundTask task;
    String address, result;
    private TextView textView;
    private String url = "http://220.67.124.128:8080";
    private String res = "";
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new BackgroundTask(MainActivity.this);
                try {
                    String text = task.execute().get();
                    Log.e("Detail2", text );
                    textView.setText(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class BackgroundTask extends AsyncTask<Map<String, String>, Integer, String> {
        String body = "";
        Context context;
        public BackgroundTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", url+"/product/get_product");
            //Http 요청 전송


            HttpClient post = http.create();
            post.request();

// 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

// 응답 본문 가져오기
            body = post.getBody();

            return body;
        }

        protected void onPreExecute() {
            Log.e("Detail",body);
        }


    }

    private String request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                    conn.disconnect();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

}
