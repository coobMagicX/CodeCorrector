private void readTypeVariables() {
    if (typeVariable instanceof TypeVariable) {
        TypeVariable<?> tv = (TypeVariable<?>) typeVariable;
        for (Type bound : tv.getBounds()) {
            registerTypeVariablesOn(bound);
        }
        if (tv.getGenericDeclaration() instanceof Class) {
            Class<?> clazz = (Class<?>) tv.getGenericDeclaration();
            for (TypeVariable<?> typeVar : clazz.getTypeParameters()) {
                registerTypeVariablesOn(getActualTypeArgumentFor(typeVar));
            }
        }
    }
}