package metal.ezplay.logging

import android.util.Log

actual object SystemOut {

    private const val TAG = "metal.ezplay.logging"

    actual fun debug(message: String?) {
        Log.d(TAG, message ?: "Original message was null.")
    }

    actual fun exception(throwable: Throwable) {
        Log.e(TAG, null, throwable)
    }
}