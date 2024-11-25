private void readTypeVariables() {
    List<Type> types = new ArrayList<>();
    for (Type type : typeVariable.getBounds()) {
        types.add(type);
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actualTypeArgumentForTypeVariable = getActualTypeArgumentFor(parameterizedType.getOwnerClass());
            if (actualTypeArgumentForTypeVariable != null && actualTypeArgumentForTypeVariable.getTypeName().equals(typeVariable.getTypeName())) {
                types.add(actualTypeArgumentForTypeVariable);
            }
        } else if (type instanceof WildcardType) {
            // Handle wildcard type
        }
    }
    registerTypeVariablesOn(types);
}