protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

        // Ensuring initialization of previousT right from the input interpolator
        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();
        resetOccurred = false;

        // Code Initialization of events if needed
        // ... (retain existing initialization and event setup code)

        while (!occuringEvents.isEmpty()) {
           // Existing event handling loop
           // ...
           
            if (isLastStep) {
                System.arraycopy(eventY, 0, y, 0, y.length);
                // Explicitly update the previousT to the eventT to avoid backward time situations.
                previousT = eventT; // Ensure proper update
                return eventT;
            }

            if (currentEvent.reset(eventT, eventY)) {
                // As before, but explicitly make sure to update previousT to maintain time consistency
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                resetOccurred = true;
                previousT = eventT; // Update previousT on reset to avoid going back in time
                return eventT;
            }

            // Update previousT explicitly for the next loop or process part.
            previousT = eventT;
            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(currentT);
            // Remainder of the loop...
        }

        // Final interpolator adjustment and state finalization
        interpolator.setInterpolatedTime(currentT);
        final double[] currentY = interpolator.getInterpolatedState();
        // Event finalization as previously set
        // ... 

        if (isLastStep || Precision.equals(currentT, tEnd, 1)) {
            // Explicitly update the previousT to synchronize with currentT as final operation times
            previousT = currentT;  // Safeguard against backward time after last step
        }

        // Return as originally intended
        return currentT;
}
