package it.delbix.coffeecounter.utils

import android.content.ClipData
import android.content.Context
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

/**
 * Classe per copiare una stringa nella clipboard
 * (al momento utilizzata solo per copiare il token di firebase a scopo di sviluppo.. ma in futuro potrebbe essere utile)
 */

class AndroidClipboardManager(private val context: Context) : ClipboardManager {
    fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }

    override fun getText(): AnnotatedString? {
        TODO("Not yet implemented")
    }

    override fun setText(annotatedString: AnnotatedString) {
        TODO("Not yet implemented")
    }
}
