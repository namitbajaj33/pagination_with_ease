package com.example.mypagination.db

import android.content.Context
import androidx.room.*
import com.google.gson.Gson

@Database(
    entities = [InboxMsg::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    Converters::class,
    ToInfoConverters::class,
    ShareInfoConverters::class)

abstract class AppDatabase : RoomDatabase() {
    companion object {
        fun create(context: Context): AppDatabase {
            val databaseBuilder =
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)

            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun personsDao(): InboxMsgDao
}


class Converters {

    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return if (value == null)
            "[]"
        else
            Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String?): List<String>? {
        return if (value == null || value == "") {
            ArrayList()
        } else {
            val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
            val list = objects.toList()
            list
        }
    }
}

class ToInfoConverters {

    @TypeConverter
    fun toInfoToJson(value: List<InboxMsg.ToInfo>?): String {
        return if (value == null)
            "[]"
        else
            Gson().toJson(value)
    }

    @TypeConverter
    fun toInfoToList(value: String?): List<InboxMsg.ToInfo>? {
        return if (value == null || value == "") {
            ArrayList()
        } else {
            val objects = Gson().fromJson(value, Array<InboxMsg.ToInfo>::class.java) as Array<InboxMsg.ToInfo>
            val list = objects.toList()
            list
        }
    }
}

class ShareInfoConverters {

    @TypeConverter
    fun toInfoToJson(value: List<InboxMsg.ShareInfo>?): String {
        return if (value == null)
            "[]"
        else
            Gson().toJson(value)
    }

    @TypeConverter
    fun toInfoToList(value: String?): List<InboxMsg.ShareInfo>? {
        return if (value == null || value == "") {
            ArrayList()
        } else {
            val objects = Gson().fromJson(value, Array<InboxMsg.ShareInfo>::class.java) as Array<InboxMsg.ShareInfo>
            val list = objects.toList()
            list
        }
    }
}


