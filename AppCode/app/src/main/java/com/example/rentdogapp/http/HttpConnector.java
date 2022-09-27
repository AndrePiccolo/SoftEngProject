package com.example.rentdogapp.http;

import com.example.rentdogapp.MainActivity;
import com.example.rentdogapp.listener.MyCallback;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpConnector{

    public HttpConnector(MyCallback callback) throws IOException {
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(MainActivity.getContext());
        CronetEngine cronetEngine = myBuilder.build();

        Executor executor = Executors.newSingleThreadExecutor();

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "http://10.0.2.2:8080/rentdog/v1/sayhello", callback, executor);

        requestBuilder.addHeader("Authorization", "123");

        UrlRequest request = requestBuilder.build();
        request.start();
    }
}
