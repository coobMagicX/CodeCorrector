private void readTypeVariables() {
    // First, let's ensure that typeVariable itself is not null before calling getBounds
    if (typeVariable != null) {
        Type[] bounds = typeVariable.getBounds();
        // Checking if bounds is not null and also non-empty.
        if (bounds != null && bounds.length > 0) {
            for (Type