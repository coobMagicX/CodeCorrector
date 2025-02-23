private void readTypeVariables() {
    // Check if typeVariable.getBounds() is not null
    if (typeVariable.getBounds() != null) {
        for (Type type : typeVariable.getBounds()) {
            // Additional check to ensure type is not null before processing it
            if (type != null) {
                registerTypeVariablesOn(type);
            }
        }
    }

    // Obtain the actual type argument and check it before processing
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument != null