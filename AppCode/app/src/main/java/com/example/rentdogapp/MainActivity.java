package com.example.rentdogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rentdogapp.http.HttpConnector;
import com.example.rentdogapp.listener.MyCallback;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static Application application;
    private TextView txtResult;
    private Button sendRequest;
    private MyCallback listener;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = this.getApplication();
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.txtDisplayContent);
        sendRequest = findViewById(R.id.btnSendRequest);

        listener = new MyCallback(new MyCallback.BackendResponse() {
            @Override
            public void callbackResult(String message) {
                txtResult.setText(message);
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new HttpConnector(listener);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}