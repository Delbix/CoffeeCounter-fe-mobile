package it.delbix.coffeecounter.utils


import android.content.Context



actual object ScreenUtils {
    private var context: Context? = null

    fun initialize(context: Context) {
        this.context = context
    }

    actual fun getScreenWidth(): Int {
        val context = this.context ?: throw IllegalStateException("Context has not been initialized")
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }
}
