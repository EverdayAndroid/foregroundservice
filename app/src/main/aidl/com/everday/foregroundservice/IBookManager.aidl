// IBookManager.aidl
package com.everday.foregroundservice;

// Declare any non-default types here with import statements
import com.everday.foregroundservice.entity.Book;
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

     List<Book> getListBook();

     void addBook(in Book book1);
}
