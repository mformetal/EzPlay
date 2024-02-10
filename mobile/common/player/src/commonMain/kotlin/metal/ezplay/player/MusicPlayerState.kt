package metal.ezplay.player

sealed class MusicPlayerState {

    object Playing : MusicPlayerState()

    object Paused : MusicPlayerState()

    object Idle : MusicPlayerState()
}