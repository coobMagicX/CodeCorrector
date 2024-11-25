private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            registerTypeVariablesOn(actualTypeArguments);
        } else {
            registerTypeVariablesOn(type);
        }
    }
}