package com.learning.lenovo.grpc_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rpc.mail.SendMailRequest;
import com.rpc.mail.SendMailResponse;
import com.rpc.mail.SendMailServiceGrpc;

import java.util.concurrent.TimeUnit;
;import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b=(Button)findViewById(R.id.ok);
        final EditText name=(EditText)findViewById(R.id.name);
        final EditText pwd=(EditText)findViewById(R.id.pwd);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.client(name.getText().toString(),pwd.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void client(String name,String pwd) throws InterruptedException {
        ManagedChannel channel = OkHttpChannelBuilder.forAddress("192.168.49.10", 8080).usePlaintext(true).build();
        //同步调用: SendMailServiceGrpc.newBlockingStub(channel)
        //异步调用的话，就是：SendMailServiceGrpc.newFutureStub(channel))
        SendMailServiceGrpc.SendMailServiceBlockingStub stub = SendMailServiceGrpc.newBlockingStub(channel);

        //设置请求参数
        SendMailRequest param = SendMailRequest.newBuilder().setName(name).setPwd(pwd).build();
        SendMailResponse resp = stub.sendMail(param);
        System.out.println(resp.getMsg() + "\t" + resp.getCode());

        //close
        channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
    }

}
