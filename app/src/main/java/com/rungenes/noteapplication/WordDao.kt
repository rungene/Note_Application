package com.rungenes.noteapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {

    @Query("SELECT * from word_table ORDER BY word ASC")// Returns a list of words sorted in ascending order.
    fun getAllWords(): List<Word>//A method to get all the words and have it return a List of Words

    @Insert(onConflict = OnConflictStrategy.IGNORE) //The conflict strategy ignores a new word if it's exactly the same as one already in the list
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

}