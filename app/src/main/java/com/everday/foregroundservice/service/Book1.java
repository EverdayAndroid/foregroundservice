package com.everday.foregroundservice.service;

import android.os.Parcel;
import android.os.Parcelable;

public class Book1 implements Parcelable {
    private int bookId;
    private String bookName;

    protected Book1(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public Book1(int bookId, String bookName){
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public static final Creator<Book1> CREATOR = new Creator<Book1>() {
        @Override
        public Book1 createFromParcel(Parcel in) {
            return new Book1(in);
        }

        @Override
        public Book1[] newArray(int size) {
            return new Book1[size];
        }
    };

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName == null ? "" : bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }
}
