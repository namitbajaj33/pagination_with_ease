package com.example.mypagination.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface InboxMsgDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(posts: List<InboxMsg>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(posts: InboxMsg)

    @Update
    fun updateAll(posts: List<InboxMsg>)

    @Query("DELETE FROM inbox_msg")
    fun deleteAll()

    @Query("SELECT * FROM inbox_msg where enquiryGroupId = :id order by timestamp desc")
    fun getList(id: Int?): DataSource.Factory<Int, InboxMsg>

    @Query("SELECT MAX(timestamp) as timestamp FROM inbox_msg where enquiryGroupId = :idGroup and id>0")
    fun getTimestamp(idGroup: Int?): Long

    @Query("SELECT * FROM inbox_msg where id =-1 and msgId = 0 ")
    fun getSyncList(): LiveData<List<InboxMsg>>

    @Delete
    fun deleteList(list: ArrayList<InboxMsg>)


    //Messaging for projects
    @Query("SELECT MAX(timestamp) as timestamp FROM inbox_msg where projectId = :pId and  spaceId = :sId and istickerId = :stickerId and msgId>0")
    fun getTimestamp(pId: Int?, sId: String?, stickerId: Int?): Long

    @Query("SELECT * FROM inbox_msg  order by timestamp desc")
    fun getAll(): DataSource.Factory<Int, InboxMsg>

    @Query("SELECT * FROM inbox_msg where projectId = :pId and spaceId = :sId and istickerId = :stickerId  and isDeleted = 0 and isLocalDeleted = 0 order by timestamp desc")
    fun getList(pId: Int, sId: String, stickerId: Int): DataSource.Factory<Int, InboxMsg>

    @Query("SELECT * FROM inbox_msg where id = 0 and msgId = -1 and isLocalDeleted = 0 ")
    fun getProjectSyncList(): LiveData<List<InboxMsg>>

    @Query("SELECT * FROM inbox_msg where isLocalDeleted = 1 and msgId > 0 ")
    fun getDeletedProjectSyncList(): LiveData<List<InboxMsg>>

    @Query("UPDATE inbox_msg set isLocalDeleted = 1 where primaryId = :id")
    fun updateDelete(id: Long)

    @Query("UPDATE inbox_msg set isLocalDeleted = 0 , isDeleted = 1 where messageInfoId = :id")
    fun updateLocalDelete(id: Int)

    @Query("UPDATE inbox_msg set isLocalDeleted = 0 , isDeleted = 1,timestamp = :time where messageInfoId = :id")
    fun updateLocalDelete(id: Int,time:Long)

    @Query("DELETE FROM inbox_msg where msgId = '-1' and isLocalDeleted= '1' ")
    fun deleteLocalMsg()

}