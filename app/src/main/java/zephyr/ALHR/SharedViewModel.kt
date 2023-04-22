package zephyr.ALHR


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class SharedViewModel : ViewModel() {
    var lamaList = mutableListOf<Lama>()
    val liveLamaList = MutableLiveData<List<Lama>>()

    init {
        liveLamaList.value = lamaList
    }

    fun addLama(newLama:Lama){
        lamaList.add(newLama)
    }

    private suspend fun writeToFile(data: String, file: File) = withContext(Dispatchers.IO) {
        try {
            if(!file.exists()) file.createNewFile()
            file.bufferedWriter().use { it.write(data) }
        }
        catch (e: Exception){
            Log.e(TAG, "File write failed: $e")
        }
    }

    private suspend fun retrieveAnimalData(context: Context):MutableList<Lama> = withContext(Dispatchers.IO){
        val file = File(context.getExternalFilesDir(null), SAVED_LAMA_DATA)
        try {
            //File is read only for some reason...
            if(!file.exists()) file.createNewFile()
            val data = file.bufferedReader().use { it.readText() }
            if (data == "") throw Exception()
            Json.decodeFromString(data)
        } catch (e: Exception) {
            Log.e(TAG, "File read failed: $e")
            return@withContext mutableListOf()
        }
    }


    fun updateAnimalList(context: Context){
        viewModelScope.launch(Dispatchers.IO) { lamaList = retrieveAnimalData(context) }
    }

    fun saveAnimalDataToFile(context: Context) {
        val dataJson = Json.encodeToString(lamaList)
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(context.getExternalFilesDir(null), SAVED_LAMA_DATA)
            writeToFile(dataJson, file)
        }
    }

    fun removeAnimal(pos:Int){
        lamaList.removeAt(pos)
    }

    fun sortBySpecies(){
        lamaList.sortBy { it.species }
    }

    fun sortByName(){
        lamaList.sortBy { it.properName }
    }

    companion object{
        const val SAVED_LAMA_DATA = "LamaData.txt"
        const val SAVED_SETTINGS = "SaveData.txt"
    }
}