package metal.ezplay.multiplatform.extensions

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> Any.takeIfInstance(): T? =
    if (this is T) {
        this
    } else {
        null
    }
