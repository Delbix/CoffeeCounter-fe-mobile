package it.delbix.coffeecounter.RichiesteServer


interface RichiestaDatiService {

    suspend fun fetchPersonas(): List<Persona>
    suspend fun getDataUltimaTransazione(): String
    suspend fun getCaffeBevuti(): CaffeBevuti
}