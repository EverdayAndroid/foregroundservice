package com.everday.foregroundservice.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.everday.foregroundservice.IBookManager;
import com.everday.foregroundservice.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class RemoteService extends ClientService {
    private List<Book> book1List;
    private BookManager bookManager;
    private IBinder iBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getListBook() throws RemoteException {
            return book1List;
        }

        @Override
        public void addBook(Book book1) throws RemoteException {
            book1List.add(book1);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        book1List = new ArrayList<>();
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("BookManager","onStartCommand");
        bindService(new Intent(RemoteService.this,LocalService.class),connection,Service.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("TAG","onServiceConnected");
            bookManager = BookManager.Stub.asInterface(service);
            try {
                //设置死亡代理
                bookManager.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("TAG","onServiceDisconnected");
            startService(new Intent(RemoteService.this,LocalService.class));
//            bindService(new Intent(RemoteService.this,LocalService.class),connection,Service.BIND_IMPORTANT);
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e("BookManager","binderDied");
            if(null == bookManager){
                return;
            }

            bookManager.asBinder().unlinkToDeath(deathRecipient,0);
            bookManager = null;
            Log.e("BookManager","准备绑定服务");
            bindService(new Intent(RemoteService.this,RemoteService.class),connection,Service.BIND_IMPORTANT);

        }
    };

}
