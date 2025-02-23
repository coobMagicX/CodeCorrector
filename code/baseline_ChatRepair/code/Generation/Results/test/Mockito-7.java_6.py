private void readTypeVariables() {
    // Ensure typeVariable.getBounds() is not null
    if (typeVariable.getBounds() != null) {
        for (Type type : typeVariable.getBounds()) {
            if (type != null) { // Ensure type is not null
                registerTypeVariablesOn(type);
            }
        }
    }

    Type actualTypeArgument = getActualType