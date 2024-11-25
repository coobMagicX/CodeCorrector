private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
        Type actualType = getActualTypeArgumentFor(type);
        // Ensure that the actualType is not null and register it if necessary
        if (actualType != null) {
            registerTypeVariablesOn(actualType);
        }
    }
    // If typeVariable itself is a generic type, we need to handle its actual type argument as well
    Type actualTypeOfVariable = getActualTypeArgumentFor(typeVariable);
    if (actualTypeOfVariable != null) {
        readTypeParameters(); // Use the provided method to handle nested type parameters
    }
}