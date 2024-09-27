if (currentEvent.reset(eventT, eventY)) {
    // some event handler has triggered changes that
    // invalidate the derivatives, we need to recompute them
    System.arraycopy(eventY, 0, y, 0, y.length);
    computeDerivatives(eventT, y, yDot);
    resetOccurred = true;
    break; // Add break statement to exit the loop after resetting
}