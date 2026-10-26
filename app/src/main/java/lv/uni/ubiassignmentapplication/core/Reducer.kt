package lv.uni.ubiassignmentapplication.core

interface Reducer<State: Reducer.ViewState, Event: Reducer.ViewEvent, Effect: Reducer.ViewEffect> {


    //Representation of UI. Used by Compose
    interface ViewState
    // User interactions
    interface ViewEvent
    // Side effects like navigation
    interface ViewEffect

    fun reduce(previousState: State, event: Event): Pair<State, Effect?>

}