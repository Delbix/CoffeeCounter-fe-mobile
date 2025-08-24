package it.delbix.coffeecounter.Settings

interface CriterioAutomaticoManagerType {
    fun salvaCriterioAutomatico(valore: Int)
    fun recuperaCriterioAutomatico(): Int
}

object CriterioAutomaticoManager: CriterioAutomaticoManagerType {

    private val settings = getSettings()
    private const val KEY_CRITERIO_AUTOMATICO = "criterio_automatico"

    override fun salvaCriterioAutomatico(valore: Int) {
        settings.putInt(KEY_CRITERIO_AUTOMATICO, valore)
    }

    override fun recuperaCriterioAutomatico(): Int {
        return settings.getInt(KEY_CRITERIO_AUTOMATICO, defaultValue = 1)
    }

}
