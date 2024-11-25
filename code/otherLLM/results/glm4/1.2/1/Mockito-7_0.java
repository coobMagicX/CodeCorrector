private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        // Assuming the method 'registerTypeVariablesOn' exists and can take 'type' as an argument.
        registerTypeVariablesOn(type);
    }
    // We need to ensure that 'getActualTypeArgumentFor' is mocked properly in tests if it's supposed to return a nested generic value.
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    // Assuming the method 'registerTypeVariablesOn' can also handle 'Type' as an argument.
    registerTypeVariablesOn(actualTypeArgument);
}