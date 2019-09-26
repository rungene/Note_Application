package com.rungenes.noteapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Word::class), version = 1)
public abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.wordDao()

                    // Delete all content here.
                    wordDao.deleteAll()

                    // Add sample words.
                    var word = Word("Hello")
                    wordDao.insert(word)
                    word = Word("World!")
                    wordDao.insert(word)

                    // TODO: Add your own words!
                    word = Word("TODO!")
                    wordDao.insert(word)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile//writes to this field are immediately made visible to other threads.
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context, scope:CoroutineScope): WordRoomDatabase {


       /*     val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }*/

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

           return INSTANCE ?:synchronized(this) {
                /*synchronized meaning that the method will be protected from concurrent execution by multiple
                threads by the monitor of the instance
                (or, for static methods, the class) on which the method is defined.
                * */
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                 instance
            }
        }
    }
}