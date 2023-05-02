package zephyr.ALHR

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Lama(var properName: String?,
           var barnName:String?,
           var dateOfBirth:String?,
           var sex:String?,
           var species:String?) {

    @SerialName("Age")
    var age:String? = ""
    var ILRNum:String? = "123"
    var ARINum:String? = ""
    var microchipNum:String? = ""
    @SerialName("Lama Weights")
    private var weights: Array<Array<String>> = arrayOf(arrayOf())
    private var notes: Array<Array<String>> = arrayOf(arrayOf())

    fun addNote(note:String, date:String){
        notes = notes.plus(arrayOf(date, note))
    }

    fun addWeight(weight:String, date:String){
        weights = weights.plus(arrayOf(date, weight))
    }

    fun addChipNumsLlama(ILR:String, micro:String){
        ILRNum = ILR
        microchipNum = micro
    }

    fun addChipNumsAlpaca(ARI:String, micro:String){
        ARINum = ARI
        microchipNum = micro
    }
}