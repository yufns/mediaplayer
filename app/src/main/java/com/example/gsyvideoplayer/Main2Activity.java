package com.example.gsyvideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.gsyvideoplayer.model.SwitchVideoModel;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = Main2Activity.class.getSimpleName();
    public static final String URL_VIDEO = "urls";
    private Unbinder mBind;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        try {
            createHttpsServer();
            createHttpServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBind = ButterKnife.bind(this);
    }

    @OnClick({ R.id.textView, R.id.textView2, R.id.textView3, R.id.textView4 })
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(Main2Activity.this, PlayActivity.class);
        String source1;
        String name;
        SwitchVideoModel switchVideoModel;
        String source2;
        String name2;
        SwitchVideoModel switchVideoModel2;
        ArrayList<SwitchVideoModel> list;
        switch (view.getId()) {
            case R.id.textView:
                //http离线
                source1 = "http://localhost:8888/test2/outputR.m3u8";
                name = "普通";
                switchVideoModel = new SwitchVideoModel(name, source1);
                source2 = "http://localhost:8888/test2/outputR.m3u8";
                name2 = "清晰";
                switchVideoModel2 = new SwitchVideoModel(name2, source2);
                list = new ArrayList<>();
                list.add(switchVideoModel);
                list.add(switchVideoModel2);
                intent.putParcelableArrayListExtra(URL_VIDEO, list);
                break;
            case R.id.textView2:
                //http在线
                source1 = "http://domain.wooba.cn/static/yc/output/outputR.m3u8";
                name = "普通";
                switchVideoModel = new SwitchVideoModel(name, source1);
                source2 = "http://domain.wooba.cn/static/yc/output/output240R.m3u8";
                name2 = "清晰";
                switchVideoModel2 = new SwitchVideoModel(name2, source2);
                list = new ArrayList<>();
                list.add(switchVideoModel);
                list.add(switchVideoModel2);
                intent.putParcelableArrayListExtra(URL_VIDEO, list);
                break;
            case R.id.textView3:
                //https离线
                source1 = "https://localhost:8080/test5/outputR.m3u8";
                name = "普通";
                switchVideoModel = new SwitchVideoModel(name, source1);
                source2 = "https://localhost:8080/test5/outputR.m3u8";
                name2 = "清晰";
                switchVideoModel2 = new SwitchVideoModel(name2, source2);
                list = new ArrayList<>();
                list.add(switchVideoModel);
                list.add(switchVideoModel2);
                intent.putParcelableArrayListExtra(URL_VIDEO, list);
                break;
            case R.id.textView4:
                //https在线
                source1 = "https://jth.tyread.com/static/yc/output/outputRS.m3u8";
                name = "普通";
                switchVideoModel = new SwitchVideoModel(name, source1);
                source2 = "https://jth.tyread.com/static/yc/output/output240RS.m3u8";
                name2 = "清晰";
                switchVideoModel2 = new SwitchVideoModel(name2, source2);
                list = new ArrayList<>();
                list.add(switchVideoModel);
                list.add(switchVideoModel2);
                intent.putParcelableArrayListExtra(URL_VIDEO, list);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    /**
     * 创建本地的http server
     * 建议开service启动
     */
    private void createHttpServer() {
        AsyncHttpServer server = new AsyncHttpServer();
        server.get("[\\s\\S]*", new HttpServerRequestCallback() {
            @Override public void onRequest(AsyncHttpServerRequest request,
                AsyncHttpServerResponse response) {
                switch (request.getPath()) {
                    case "/test2/outputR.m3u8":
                        Log.d(TAG, "/test2/outputR.m3u8");
                        try {
                            InputStream open = getAssets().open("outputR.m3u8");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test2/outputA0.ts":
                        Log.d(TAG, "/test2/outputA0.ts");
                        try {
                            InputStream open = getAssets().open("outputA0.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test2/outputA1.ts":
                        Log.d(TAG, "/test2/outputA1.ts");
                        try {
                            InputStream open = getAssets().open("outputA1.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test2/outputA2.ts":
                        Log.d(TAG, "/test2/outputA2.ts");
                        try {
                            InputStream open = getAssets().open("outputA2.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test2/outputA3.ts":
                        Log.d(TAG, "/test2/outputA3.ts");
                        try {
                            InputStream open = getAssets().open("outputA3.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test2/outputA4.ts":
                        Log.d(TAG, "/test2/outputA4.ts");
                        try {
                            InputStream open = getAssets().open("outputA4.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        Log.d(TAG, request.getPath());
                        break;
                }
            }
        });
        server.listen(8888);
    }

    /**
     * 创建本地 https服务
     * 证书需要自己生产,Android支持bks格式的证书
     * 建议放在service中启动
     */
    private void createHttpsServer() throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(getResources().openRawResource(R.raw.keystore), "storepass".toCharArray());
        kmf.init(ks, "storepass".toCharArray());
        TrustManagerFactory tmf =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
        ts.load(getResources().openRawResource(R.raw.keystore), "storepass".toCharArray());
        tmf.init(ts);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        AsyncHttpServer server = new AsyncHttpServer();
        server.listenSecure(8080, sslContext);
        server.get("[\\s\\S]*", new HttpServerRequestCallback() {
            @Override public void onRequest(AsyncHttpServerRequest request,
                AsyncHttpServerResponse response) {
                switch (request.getPath()) {
                    case "/test5/outputR.m3u8":
                        Log.d(TAG, "/test5/outputR.m3u8");
                        try {
                            InputStream open = getAssets().open("outputR.m3u8");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test5/outputA0.ts":
                        Log.d(TAG, "/test5/outputA0.ts");
                        try {
                            InputStream open = getAssets().open("outputA0.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test5/outputA1.ts":
                        Log.d(TAG, "/test5/outputA1.ts");
                        try {
                            InputStream open = getAssets().open("outputA1.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test5/outputA2.ts":
                        Log.d(TAG, "/test5/outputA2.ts");
                        try {
                            InputStream open = getAssets().open("outputA2.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test5/outputA3.ts":
                        Log.d(TAG, "/test5/outputA3.ts");
                        try {
                            InputStream open = getAssets().open("outputA3.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "/test5/outputA4.ts":
                        Log.d(TAG, "/test5/outputA4.ts");
                        try {
                            InputStream open = getAssets().open("outputA4.ts");
                            response.sendStream(open, open.available());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        Log.d(TAG, request.getPath());
                        break;
                }
            }
        });
        AsyncHttpClient
            .getDefaultInstance()
            .getSSLSocketMiddleware()
            .setSSLContext(sslContext);
        AsyncHttpClient
            .getDefaultInstance()
            .getSSLSocketMiddleware()
            .setTrustManagers(tmf.getTrustManagers());
    }
}
