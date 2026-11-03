package com.example.arch_izmir.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import com.example.arch_izmir.data.model.RouteStop
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Query("SELECT COUNT(*) FROM route_stops")
    suspend fun getRouteCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStop(stop: RouteStop)

    @Query("SELECT * FROM route_stops ORDER BY orderIndex ASC")
    fun getMyRouteStops() : Flow<List<RouteStop>>

    // ID'sine göre silmek için
    @Query("DELETE FROM route_stops WHERE id = :stopId")
    suspend fun deleteStopById(stopId: Int)

    // Tüm rotayı  temizlemek için
    @Query("DELETE FROM route_stops")
    suspend fun deleteAllStops()

    @Query("UPDATE route_stops SET orderIndex = :newOrderIndex WHERE id = :stopId")
    suspend fun updateOrderIndex(stopId: Int, newOrderIndex: Int)

    @Transaction
    suspend fun saveOrder(stops : List<RouteStop>){
        stops.forEachIndexed { index, stop ->
            updateOrderIndex(stop.id,index)
        }
    }
    @Query("SELECT COALESCE(MAX(orderIndex), -1) FROM route_stops")
    suspend fun getMaxOrderIndex(): Int
}