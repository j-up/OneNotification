package com.jup.oneNotification.db.room

import android.content.Context
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import androidx.test.core.app.ApplicationProvider
import com.jup.oneNotification.utils.JLog
import kotlinx.coroutines.*

@RunWith(AndroidJUnit4::class)
class RoomUnitTest {
    private var oneNotifyDatabase: OneNotifyDatabase? = null
    lateinit var context: Context

    @Before
    fun dbCreateTest() {
        context = ApplicationProvider.getApplicationContext<Context>()
    }

    @After
    fun dbCloseTest() {
        OneNotifyDatabase.close()
    }

    @Test
    fun dbTest() {
        // 이 객체가 널이 아닌지 판단 (it != null) 널이 아니면 Pass
        dbCreate()
        Assert.assertNotNull("DB 생성실패", oneNotifyDatabase)

        dbInsert(
            // null을 파라미터로 넣을경우 실패
            Notify(0, 12, 20, 1, 1)
        ).let {
            Assert.assertNotNull("DB 삽입실패", it)
            JLog.d(RoomUnitTest::class.java,"insert key: $it")
        }

        when(val dbSelectData = dbSelect(0)) {
            null -> JLog.d(RoomUnitTest::class.java,"DB 조회 값 없음")

            else -> {
                dbSelectData.apply {
                    Assert.assertEquals("id 불일치", id, 0)
                    Assert.assertEquals("hour 불일치", hour, 12)
                    Assert.assertEquals("minute 불일치", minute, 20)
                    Assert.assertEquals("조선일보 구독 불일치", cho_subscribe_yn, 1)
                    Assert.assertEquals("경향신문 구독 불일치", gyung_subscribe_yn, 1)
                }
            }
        }
    }

    private fun dbCreate() {
        oneNotifyDatabase =
            OneNotifyDatabase.getInstance(
                context
            )
    }
    /*
    param: 기본 키 id
    return: 성공시 기본 키 값 반환
     */
    private fun dbInsert(insertData: Notify): Long? {
        var insertResult: Long? = null

        runBlocking {
            insertResult = oneNotifyDatabase?.notifyDao()?.insert(insertData)
        }.run {
            return insertResult
        }
    }
    // null을 파라미터로 넣을경우 실패
    private fun dbSelect(id: Int): Notify? = oneNotifyDatabase?.notifyDao()?.getNotify(id)

}

@Dao
interface NotifyDao {
    @Query("Select * from notify where id=:id")
    fun getNotify(id: Int): Notify
    @Query("Delete from notify")
    fun delete()
    @Insert(onConflict = REPLACE)
    fun insert(notify : Notify): Long
}

@Database(entities = [Notify::class], version = 1)
abstract class OneNotifyDatabase:RoomDatabase() {
    abstract fun notifyDao(): NotifyDao

    companion object {
        private var INSTANCE: OneNotifyDatabase? = null

        fun getInstance(context: Context): OneNotifyDatabase? {
            when(INSTANCE) {
                null -> {
                    synchronized(OneNotifyDatabase::class) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            OneNotifyDatabase::class.java, "oneNotifyDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return INSTANCE
        }

        fun close() {
            INSTANCE = null
        }
    }
}

@Entity(tableName = "notify")
data class Notify (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val hour: Int?,
    val minute: Int?,
    val cho_subscribe_yn: Int?,
    val gyung_subscribe_yn: Int?
)