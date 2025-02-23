// Just before the loop:
double ta = t0;
double ga = g0;
double tb = ta + h; // Start tb one step ahead

for (int i = 0; i < n; ++i) {
    interpolator.setInterpolatedTime(tb);
    final double gb = handler.g(tb, interpolator.getInterpolatedState());

    // Check for events occurrence based on sign change:
    if ((ga >= 0) != (gb >= 0)) { // Ensure exact opposite signs strictly
        // proceed with existing root-finding and processing logic here...
        
        // Brent solver is triggered correctly within this conditional block
        //...
    } else {
        // shift window forward without changing signs
        ta = tb;
        ga = gb;
        tb += h; // Move to the next interval by adding h
    }
    
    // Ensure the interpolator time is set at the end of the loop iteration
    interpolator.setInterpolatedTime(tb);
}

// After exiting loop:
pendingEvent = false;
pendingEventTime = Double.NaN;
return false;
