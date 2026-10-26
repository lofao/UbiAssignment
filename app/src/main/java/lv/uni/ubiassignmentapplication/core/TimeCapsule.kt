package lv.uni.ubiassignmentapplication.core

interface TimeCapsule<State: Reducer.ViewState> {
    fun addState(state: State)
    fun selectState(position: Int)
    fun getStates(): List<State>
}

class TimeTravelCapsule<State: Reducer.ViewState>(private val onStateSelected: (State) -> Unit): TimeCapsule<State> {
    private val states: MutableList<State> = mutableListOf()

    override fun addState(state: State) {
        states.add(state)
    }

    override fun getStates(): List<State> {
        return states
    }

    override fun selectState(position: Int) {
        onStateSelected(states[position])
    }

}