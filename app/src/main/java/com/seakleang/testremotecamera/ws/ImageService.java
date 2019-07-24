package com.seakleang.testremotecamera.ws;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageService {

        @GET("/ISAPI/Streaming/channels/{channel}01/picture")
        Call<ResponseBody> getImage(@Path("channel") String channel);

}
