package com.example.appbandienthoai.Class;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API {

    private final OkHttpClient client;

    public API() {
        // Initialize OkHttp client
        this.client = new OkHttpClient();
    }

    public interface ApiResponseCallback {
        void onSuccess(String response);

        void onError(String error);
    }

    public void fetchApiData(final String endpoint, final ApiResponseCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("https://do-an-cellphonex.vercel.app" + endpoint)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    // On success: pass the result back via callback on main thread.
                    String responseData = response.body().string();
                    if (callback != null) {
                        callback.onSuccess(responseData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    // On Error: pass the error message back via callback on main thread.
                    if (callback != null) {
                        callback.onError(e.getMessage());
                    }
                }
            }
        }).start();
    }
}