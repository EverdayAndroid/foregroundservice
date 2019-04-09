package com.everday.foregroundservice.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
/**
* @author Everday
* @emil wangtaohandsome@gmail.com
* create at 2019/3/16
* description: 手写系统生成AIDL
*/

public interface BookManager extends IInterface {
    public String TAG = "BookManager";
    abstract class Stub extends Binder implements BookManager{
        //唯一标识
        private static final String DESCRIPTOR = "com.everday.foregroundservice.service.BookManager";

        public Stub(){
            //设置唯一标识
            this.attachInterface(this,DESCRIPTOR);
        }

        /**
         * 区分当前主进程，还是其它进程
         * @param obj
         * @return
         */
        public static BookManager asInterface(IBinder obj){
            if(obj == null){
                return null;
            }
            //在当前进程中查询，如果有则返回，否则返回Proxy
            IInterface anInterface = obj.queryLocalInterface(DESCRIPTOR);
            Log.e(TAG,android.os.Process.myPid()+" "+Thread.currentThread().getName());
            if(anInterface !=null && anInterface instanceof  BookManager){
                return (BookManager) anInterface;
            }

            return new BookManager.Stub.Proxy(obj);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }


        //这个方法运行服务器端中的Binder线程池中，当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法来处理
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code){
                case TRANSACTION_getListBook1:
                    Log.e(TAG,"client====TRANSACTION_getListBook1");
                    //校验标识
                    data.enforceInterface(descriptor);
                    List<Book1> _result = getListBook();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case TRANSACTION_addBook1:
                    Log.e(TAG,"client====TRANSACTION_addBook1");
                    data.enforceInterface(descriptor);
                    Book1 book1 = null;
                    if(data.readInt()!=0){
                        book1 = Book1.CREATOR.createFromParcel(data);
                    }
                    this.addBook(book1);
                    reply.writeNoException();
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }

        /**
         * 服务端Proxy
         */
        private static class Proxy implements BookManager{
            private IBinder mRemote;

            public Proxy(IBinder mRemote) {
                this.mRemote = mRemote;
            }

            public String getInterfaceDescriptor(){ return DESCRIPTOR;}

            @Override
            public IBinder asBinder() {
                return mRemote;
            }
            @Override
            public List<Book1> getListBook() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                List<Book1> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    Log.e(TAG,"service====TRANSACTION_getListBook1");
                    mRemote.transact(TRANSACTION_getListBook1,_data,_reply,0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(Book1.CREATOR);
                }finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }


            @Override
            public void addBook(Book1 book1) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                try{
                    if(null!= book1){
                        _data.writeInt(1);
                        book1.writeToParcel(_data,0);
                    }else{
                        _data.writeInt(0);
                    }
                    Log.e(TAG,"service====TRANSACTION_addBook1");
                    mRemote.transact(TRANSACTION_addBook1,_data,_reply,0);
                    _reply.readException();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    _reply.recycle();
                    _data.recycle();

                }

            }
        }

        static final int TRANSACTION_getListBook1 = IBinder.FIRST_CALL_TRANSACTION+0;
        static final int TRANSACTION_addBook1 = IBinder.FIRST_CALL_TRANSACTION+1;
    }


    List<Book1> getListBook() throws RemoteException;

    void addBook(Book1 book1) throws RemoteException;

}
