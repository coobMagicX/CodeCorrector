private void readTypeVariables() {
    Set<Type> boundsTypes = new HashSet<>();
    
    // Assuming typeVariable is an instance of some class that has getBounds method.
    // We need to ensure that we only register types once and handle any nested generics properly.
    for (Type type : typeVariable.getBounds()) {
        if (!boundsTypes.contains(type)) { // This check prevents duplicate registrations
            boundsTypes.add(type);
            registerTypeVariablesOn(type); // Register the outer type
        }
    }
    
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument != null) {
        registerTypeVariablesOn(actualTypeArgument); // Register the actual type argument
    }
}