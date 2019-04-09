package com.everday.foregroundservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.everday.foregroundservice.entity.Book;
import com.everday.foregroundservice.service.Book1;
import com.everday.foregroundservice.service.BookManager;
import com.everday.foregroundservice.service.LocalService;
import com.everday.foregroundservice.service.LocalService1;
import com.everday.foregroundservice.service.RemoteService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IBookManager ibookManager;
    private BookManager bookManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //通过1像素activity 进行提权
//        KeepManager.getInstance().registerKeep(this);
        //保活进程  ---- 前台服务
//        startService(new Intent(this,ClientService.class));
        //双进程守护
//        Log.e("BookManager",android.os.Process.myPid()+"   ==  "+ Thread.currentThread().getName());

        startService(new Intent(this,LocalService.class));
        bindService(new Intent(this,LocalService.class),connection,Service.BIND_IMPORTANT);



        startService(new Intent(this,LocalService1.class));
        bindService(new Intent(this,LocalService1.class),connection1,Service.BIND_IMPORTANT);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ibookManager.addBook(new Book(100,"Android"));
                    List<Book> listBook = ibookManager.getListBook();
                    Toast.makeText(MainActivity.this, listBook.size()+"  ", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bookManager.addBook(new Book1(100,"Android"));
                    List<Book1> listBook = bookManager.getListBook();
                    Toast.makeText(MainActivity.this, listBook.size()+"  ", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ibookManager = IBookManager.Stub.asInterface(service);
            try {
                //设置死亡代理
                ibookManager.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ServiceConnection connection1 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = BookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e("BookManager","binderDied");
            if(null == bookManager){
                return;
            }

            ibookManager.asBinder().unlinkToDeath(deathRecipient,0);
            ibookManager = null;
            Log.e("BookManager","准备绑定服务");
            bindService(new Intent(MainActivity.this,RemoteService.class),connection,Service.BIND_IMPORTANT);

        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        KeepManager.getInstance().unRegisterKeep(this);
    }
}
