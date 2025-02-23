private void readTypeVariables() {
    // Get bounds and safely check they are not null before iterating.
    Type[] bounds = typeVariable.getBounds();
    if (bounds != null) {
        for (Type type : bounds) {
            if (type != null) { // Additionally ensure each type in bounds is not null.
                registerTypeVariablesOn(type);
            }
        }
    }
    
    // Safely process getActualTypeArgumentFor with null check
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument !=