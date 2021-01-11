package com.example.piandroid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.piandroid.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertOne(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table ORDER BY 'id' DESC LIMIT 200")
    List<User> getAll();

   // @Query("SELECT * FROM user_table  WHERE email = :email AND password = :password")
   // List<User> findUser(String email, String password);


  //  @Query("SELECT email FROM user_table  WHERE email = :email")
  //  String findUserEmail(String email);

   // @Update
   //  void updateUser(User user);

}
