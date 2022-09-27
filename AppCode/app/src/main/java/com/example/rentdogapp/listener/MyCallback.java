package com.example.rentdogapp.listener;

import android.util.Log;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MyCallback extends UrlRequest.Callback{

    private static final String TAG = "Piccolo";

    private BackendResponse listener;

    public interface BackendResponse{
        public void callbackResult(String message);
    }

    public MyCallback(BackendResponse listener){
        this.listener = listener;
    }

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        Log.i(TAG, "onRedirectReceived method called.");
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        Log.i(TAG, "onResponseStarted method called.");
        request.read(ByteBuffer.allocateDirect(102400));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        Log.i(TAG, "onReadCompleted method called.");
        byteBuffer.clear();
        request.read(byteBuffer);
        String result = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        this.listener.callbackResult(result);

    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        Log.i(TAG, "onSucceeded method called.");
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        Log.i(TAG, "onFailure method called.");
    }
}
