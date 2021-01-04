/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.room.*

// Define n interface VideoDao annotated with @Dao
@Dao
interface VideoDao {
    // Add getVideos as a @Query that selects all from datbasevideo
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>
    // Add insertAll as an @Insert that replaces on conflict(or upsert).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}
// Add VideosDatabase, an abstract class extending RoomDatabase
// Annotate VideosDatabase with @Database, including entities and version
@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {
    // Create abstract val videoDao
    abstract val videoDao: VideoDao
}
// Create an INSTANCE variable to store the VideosDatabase singleton
private lateinit var INSTANCE: VideosDatabase

// Define a function getDatabase() that returns the VideosDatabase INSTANCE
fun getDatabase(context: Context): VideosDatabase {
    // Use a synchronized block to check whether INSTANCE is initialized, and if it isn't,
    // use DatabaseBuilder to create it
    synchronized(VideosDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java,
                    "videos").build()
        }
        return INSTANCE
    }
}