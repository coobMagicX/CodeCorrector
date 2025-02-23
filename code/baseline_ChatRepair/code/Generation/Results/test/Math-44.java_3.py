// prepare handling of the remaining part of the step
previousT = eventT; // this should be moved to after the event is handled fully (after the event handling blocks)
interpolator.setSoftPreviousTime(previousT);
interpolator.setSoftCurrentTime(currentT);

// Evaluate and possibly re-add the event in the new context after the above updates
if (currentEvent.evaluateStep(interpolator)) {
    // the event reoccurs during the remaining part of the step
    occuringEvents.add(currentEvent);
}
