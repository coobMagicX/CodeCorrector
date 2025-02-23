private void readTypeVariables() {
    Type[] bounds = typeVariable.getBounds();
    if (bounds != null) {
        for (Type type : bounds) {
            registerTypeVariablesOn(type);
        }
    }

    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    registerTypeVariablesOn(actualTypeArgument);
}
