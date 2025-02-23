boolean needReset = currentEvent.reset(eventT, eventY);
if (needReset) {
    // some event handler has triggered changes that
    // invalidate the derivatives, we need to recompute them
    System.arraycopy(eventY, 0, y, 0, y.length);
    computeDerivatives(eventT, y, yDot);
    resetOccurred = true;
    // Reinitialize all events as the states have changed
    for (final EventState state : eventsStates) {
        state.reinitializeBegin(interpolator);
        if (state.evaluateStep(interpolator)) {
            occuringEvents.add(state);
        }
    }
    return eventT;
}
