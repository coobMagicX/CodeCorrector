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

    // Check for standard deserializers only if no custom deserializer has been found
    if (deser == null) {
        if (type.isEnumType()) {
            deser = _createEnumKeyDeserializer(ctxt, type);
        } else {
            deser = StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
        }
    }

    // Apply deserializer modifiers after finding a suitable deserializer
    if (deser != null && _factoryConfig.hasDeserializerModifiers()) {
        for (BeanDeserializerModifier mod : _factoryConfig.deserializerModifiers()) {
            deser = mod.modifyKeyDeserializer(config, type, deser);
        }
    }

    return deser;
}