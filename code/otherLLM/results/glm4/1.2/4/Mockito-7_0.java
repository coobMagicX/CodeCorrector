private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        // Assuming registerTypeVariablesOn is a method that should be called with the actual generic type
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();

            // Register the raw type and any arguments for stubbing purposes
            registerTypeVariablesOn(rawType);
            registerTypeVariablesOn(parameterizedType.getActualTypeArguments());
        } else {
            // If it's not a ParameterizedType, just register it
            registerTypeVariablesOn(type);
        }
    }

    // Check if the typeVariable itself is a generic type and needs to be stubbed as well
    if (typeVariable instanceof GenericArrayType) {
        GenericArrayType genericArrayType = (GenericArrayType) typeVariable;
        Type componentType = genericArrayType.getGenericComponentType();
        registerTypeVariablesOn(componentType);
    } else if (typeVariable instanceof ParameterizedType) {
        // If typeVariable is a ParameterizedType, it needs special handling to stub the nested generics
        ParameterizedType parameterizedType = (ParameterizedType) typeVariable;
        Type rawType = parameterizedType.getRawType();
        registerTypeVariablesOn(rawType);
        
        // Deeply stub its nested generics
        if (parameterizedType.getActualTypeArguments().length > 0) {
            for (Type nestedType : parameterizedType.getActualTypeArguments()) {
                registerTypeVariablesOn(nestedType);
            }
        }
    } else {
        // If it's not a ParameterizedType, just register it
        registerTypeVariablesOn(typeVariable);
    }
}