package metal.ezplay.player

sealed class MusicPlayerState {

    object Playing : MusicPlayerState()

    object Idle : MusicPlayerState()

    object Finished : MusicPlayerState()
}