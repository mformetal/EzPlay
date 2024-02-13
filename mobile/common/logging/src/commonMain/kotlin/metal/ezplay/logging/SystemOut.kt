package metal.ezplay.logging

expect object SystemOut {

    fun debug(message: String?)

    fun exception(throwable: Throwable)
}
