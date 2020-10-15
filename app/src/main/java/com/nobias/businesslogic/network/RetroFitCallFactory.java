package com.nobias.businesslogic.network;

import com.nobias.BuildConfig;
import com.nobias.utils.constants.ConstantCodes;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class RetroFitCallFactory {
    public static RetroFitInterface create() {
        return getRetrofit().create(RetroFitInterface.class);
    }

    private static Retrofit getRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //OkHttpClient.Builder builder = getUnsafeOkHttpClient().newBuilder();
        builder.readTimeout(ConstantCodes.TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.connectTimeout(ConstantCodes.TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.followRedirects(false);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }


        builder.addInterceptor(chain -> {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("X-APP-Version", BuildConfig.VERSION_NAME)
                    .addHeader("X-APP-VersionCode", String.valueOf(BuildConfig.VERSION_CODE))
                    .addHeader("X-APP-Platform", "android")
                    // .addHeader("Authorization", "Bearer " + BuildConfig.APP_TOKEN)
                    .build();
            return chain.proceed(request);
        });

        return new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build()).build();
    }


    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
