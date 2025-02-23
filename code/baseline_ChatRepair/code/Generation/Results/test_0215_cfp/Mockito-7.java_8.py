private void readTypeVariables() {
    if (typeVariable != null && typeVariable.getBounds() != null) {
        for (Type type : typeVariable.getBounds()) {
            if (type != null) {
                registerTypeVariablesOn(type);
            }
        }
    }

    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument != null) {
        registerTypeVariablesOn(actualTypeArgument);
    }
}
