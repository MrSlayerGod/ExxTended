package nb.util.serdes

sealed interface DefaultState {
    object NotSet: DefaultState
    object Found: DefaultState
    object IsSet: DefaultState
}

class DefaultStatus {

    var state: DefaultState = DefaultState.NotSet

    val notSet: Boolean get() = state == DefaultState.NotSet

    val found: Boolean get() = state == DefaultState.Found

    val isSet: Boolean get() = state == DefaultState.IsSet

    fun promote() = when(state) {
        DefaultState.NotSet -> state = DefaultState.Found
        DefaultState.Found -> state = DefaultState.IsSet
        DefaultState.IsSet -> { }
    }

    fun demote() = when(state) {
        DefaultState.NotSet -> { }
        DefaultState.Found -> state = DefaultState.NotSet
        DefaultState.IsSet -> state = DefaultState.Found
    }
}
