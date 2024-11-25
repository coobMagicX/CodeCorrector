private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    
    if (typeVariable.getActualTypeArguments().length > 0) {
        Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
        registerTypeVariablesOn(actualTypeArgument);
    } else {
        registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
    }
}