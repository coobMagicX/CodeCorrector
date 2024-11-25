private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    // Fix: Extract actual type argument from the TypeVariable itself, 
    //      because getActualTypeArgumentFor() might not be needed here.
    if (typeVariable instanceof ParameterizedType) {
        registerTypeVariablesOn(((ParameterizedType) typeVariable).getActualTypeArguments()[0]);
    } else {
        registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
    }
}