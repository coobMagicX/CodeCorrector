    // After handling an event, prepare for next part of the step
    previousT = eventT;  // Capture the time after the event
    interpolator.setSoftPreviousTime(eventT);  // Set the new start time
    interpolator.setSoftCurrentTime(currentT); // Restore the original end time

    // Before evaluating and adding the re-triggered event, ensure no reversal of time
    if (currentEvent.evaluateStep(interpolator)) {  // Re-checking if the event will occur again
        double reTriggerTime = currentEvent.getEventTime();
        if (reTriggerTime <= eventT && interpolator.isForward()) {
            throw new MathIllegalStateException(LocalizedFormats.INTEGRATION_GONE_BACKWARD,
                                                reTriggerTime, eventT);
        }
        occuringEvents.add(currentEvent);
    }
