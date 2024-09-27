public KeyDeserializer createKeyDeserializer(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
    final DeserializationConfig config = ctxt.getConfig();
    KeyDeserializer deser = null;
    if (_factoryConfig.hasKeyDeserializers()) {
        BeanDescription beanDesc = config.introspectClassAnnotations(type.getRawClass());
        for (KeyDeserializers d : _factoryConfig.keyDeserializers()) {
            deser = d.findKeyDeserializer(type, config, beanDesc);
            if (deser != null) {
                break;
            }
        }
    }
    // the only non-standard thing is this:
    if (deser == null) {
        if (type.isEnumType()) {
            return _createEnumKeyDeserializer(ctxt, type);
        }
        deser = StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
    }
    // and then post-processing
    if (deser != null) {
        if (_factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : _factoryConfig.deserializerModifiers()) {
                deser = mod.modifyKeyDeserializer(config, type, deser);
            }
        }
    }
    return deser;
}

private KeyDeserializer _createEnumKeyDeserializer(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
    final DeserializationConfig config = ctxt.getConfig();
    Class<?> enumClass = type.getRawClass();

    BeanDescription beanDesc = config.introspect(type);
    KeyDeserializer des = findKeyDeserializerFromAnnotation(ctxt, beanDesc.getClassInfo());
    if (des != null) {
        return des;
    } else {
        JsonDeserializer<?> custom = _findCustomEnumDeserializer(enumClass, config, beanDesc);
        if (custom != null) {
            return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, type, custom);
        }
        JsonDeserializer<?> valueDesForKey = findDeserializerFromAnnotation(ctxt, beanDesc.getClassInfo());
        if (valueDesForKey != null) {
            return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, type, valueDesForKey);
        }
    }
    EnumResolver enumRes = constructEnumResolver(enumClass, config, beanDesc.findJsonValueMethod());
    final AnnotationIntrospector ai = config.getAnnotationIntrospector();
    for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
        if (ai.hasCreatorAnnotation(factory)) {
            int argCount = factory.getParameterCount();
            if (argCount == 1) {
                Class<?> returnType = factory.getRawReturnType();
                if (returnType.isAssignableFrom(enumClass)) {
                    if (factory.getRawParameterType(0) != String.class) {
                        throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String");
                    }
                    if (config.canOverrideAccessModifiers()) {
                        ClassUtil.checkAndFixAccess(factory.getMember(), ctxt.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
                    }
                    return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes, factory);
                }
            }
            throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
        }
    }
    return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes);
}