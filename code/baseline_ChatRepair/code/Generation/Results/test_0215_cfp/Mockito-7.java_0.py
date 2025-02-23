private void readTypeVariables() {
    Type[] bounds = typeVariable.getBounds();
    if (bounds != null) {
        for (Type type : bounds) {
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
``