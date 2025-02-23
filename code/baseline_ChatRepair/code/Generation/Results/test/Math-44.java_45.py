// in the while loop after processing an event
previousT = eventT; // update the time up to which we have processed
interpolator.setSoftPreviousTime(eventT);
interpolator.setSoftCurrentTime(currentT);

// Recheck all events including the current one to see if they occur again in the remaining interval
occuringEvents.clear();
for (final EventState state : eventsStates) {
    if (state.evaluateStep(interpolator) && state.getEventTime() > previousT) {
        // only consider the event if it occurs after the last event time
        occuringEvents.add(state);
    }
}

// After re-evaluating, continue with the previousT modified only if no events are left in the set
// Otherwise, process the subsequent events in the set as per the earlier logic
