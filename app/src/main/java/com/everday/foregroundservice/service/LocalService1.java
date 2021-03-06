package com.everday.foregroundservice.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LocalService1 extends ClientService {
    private List<Book1> book1List;
    private BookManager bookManager;
    private IBinder iBinder = new BookManager.Stub() {
        @Override
        public List<Book1> getListBook() throws RemoteException {
            return book1List;
        }

        @Override
        public void addBook(Book1 book1) throws RemoteException {
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
        bindService(new Intent(LocalService1.this,RemoteService.class),connection,Service.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = BookManager.Stub.asInterface(service);
            try {
                //死亡代理
                bookManager.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("TAG","onServiceDisconnected");
            startService(new Intent(LocalService1.this,RemoteService.class));
//            bindService(new Intent(LocalService.this,RemoteService.class),connection,Service.BIND_IMPORTANT);
        }
    };


    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(null!= bookManager){return;}

            bookManager.asBinder().unlinkToDeath(deathRecipient,0);
            bookManager = null;
            bindService(new Intent(LocalService1.this,RemoteService.class),connection,Service.BIND_IMPORTANT);
        }
    };

}
