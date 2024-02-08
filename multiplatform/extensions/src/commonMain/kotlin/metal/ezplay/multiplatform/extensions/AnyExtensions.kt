package metal.ezplay.multiplatform.extensions

@Suppress("UNCHECKED_CAST")
fun <T> Any.takeIfInstance(): T? = (this as? T)