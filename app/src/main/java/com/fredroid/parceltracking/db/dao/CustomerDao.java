package com.fredroid.parceltracking.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fredroid.parceltracking.db.entity.Customer;

import java.util.List;

/**
 * Created by jackttc on 23/12/17.
 */

@Dao
public interface  CustomerDao {
    @Query("SELECT * FROM CustomerTable")
    List<Customer> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(Customer... chatEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Customer> chatEntityList);

    @Query("delete from CustomerTable where nId = :nId")
    void remove(int nId);

    @Query("SELECT COUNT(*) from CustomerTable")
    int countCustomer();


 /*   @Query("delete from CustomerTable where nChatId = :chatId")
    public  void  deleteUploadRecordId(int chatId);

    @Query("SELECT * FROM ChatTable where nChatId = :chatId")
    ChatEntity getChat(int chatId);



    @Query("SELECT COUNT(*) from ChatTable")
    int countChats();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(ChatEntity... chatEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ChatEntity> chatEntityList);

    @Query("delete from ChatTable")
    void deleteAllChats();
*/

}

