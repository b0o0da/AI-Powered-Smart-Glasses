package avn.app.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

object SettingKeys {
    const val BACKEND_URL = "backend_url"

    // ponytail: per-install key kept in app-private storage (allowBackup=false), not the Keystore.
    // Move to a Keystore-wrapped value if the threat model grows beyond a research prototype.
    const val API_KEY = "api_key"
    const val EMERGENCY_CONTACTS = "emergency_contacts"
    const val SPEECH_RATE = "speech_rate"
    const val MOCK_SCENARIO = "mock_scenario"
}

@Entity(tableName = "settings")
data class SettingEntity(@PrimaryKey val name: String, val value: String)

/** One row per engine decision, spoken or suppressed. Labels only, never screen or message content. */
@Entity(tableName = "alert_log")
data class AlertLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val eventId: String,
    val source: String,
    val type: String,
    val label: String,
    val level: String,
    val decision: String,
    val reason: String?
)

/** Embeddings only. There is deliberately no image column. */
@Entity(tableName = "known_people_cache")
class KnownPersonEntity(
    @PrimaryKey val personId: String,
    val displayName: String,
    val embedding: ByteArray,
    val modelVersion: String,
    val consentAt: Long
)

@Dao
interface SettingsDao {
    @Query("SELECT value FROM settings WHERE name = :name")
    fun observe(name: String): Flow<String?>

    @Query("SELECT value FROM settings WHERE name = :name")
    suspend fun get(name: String): String?

    @Upsert
    suspend fun put(setting: SettingEntity)
}

@Dao
interface AlertLogDao {
    @Insert
    suspend fun insert(entry: AlertLogEntity): Long

    @Query("SELECT * FROM alert_log ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<AlertLogEntity>>
}

@Dao
interface KnownPeopleDao {
    @Upsert
    suspend fun upsert(person: KnownPersonEntity)

    @Query("SELECT * FROM known_people_cache")
    suspend fun all(): List<KnownPersonEntity>

    @Query("DELETE FROM known_people_cache WHERE personId = :personId")
    suspend fun delete(personId: String)
}

@Database(
    entities = [SettingEntity::class, AlertLogEntity::class, KnownPersonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AvnDatabase : RoomDatabase() {
    abstract fun settings(): SettingsDao

    abstract fun alertLog(): AlertLogDao

    abstract fun knownPeople(): KnownPeopleDao
}
