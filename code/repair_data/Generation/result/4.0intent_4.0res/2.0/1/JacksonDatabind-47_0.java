public JavaType refineSerializationType(final MapperConfig<?> config,
        final Annotated a, final JavaType baseType) throws JsonMappingException {
    JavaType type = baseType;
    final TypeFactory tf = config.getTypeFactory();

    // Refine the main type
    Class<?> serClass = findSerializationType(a);
    if (serClass != null) {
        if (type.hasRawClass(serClass)) {
            type = type.withStaticTyping();
        } else {
            try {
                // Correcting the type refining logic by checking subclass relationship properly
                if (serClass.isAssignableFrom(type.getRawClass())) {
                    type = tf.constructSpecializedType(type, serClass);
                } else if (type.getRawClass().isAssignableFrom(serClass)) {
                    type = tf.constructGeneralizedType(type, serClass);
                } else {
                    throw new JsonMappingException(null,
                            String.format("Cannot refine type %s to %s; types not related",
                                    type.getRawClass().getName(), serClass.getName()));
                }
            } catch (IllegalArgumentException iae) {
                throw new JsonMappingException(null,
                        String.format("Failed to refine type %s with annotation (value %s), from '%s': %s",
                                type, serClass.getName(), a.getName(), iae.getMessage()),
                                iae);
            }
        }
    }

    // Process key type for Map-like types
    if (type.isMapLikeType()) {
        JavaType keyType = type.getKeyType();
        Class<?> keyClass = findSerializationKeyType(a, keyType);
        if (keyClass != null && !keyType.hasRawClass(keyClass)) {
            try {
                if (keyClass.isAssignableFrom(keyType.getRawClass())) {
                    keyType = tf.constructSpecializedType(keyType, keyClass);
                } else if (keyType.getRawClass().isAssignableFrom(keyClass)) {
                    keyType = tf.constructGeneralizedType(keyType, keyClass);
                } else {
                    throw new JsonMappingException(null,
                            String.format("Cannot refine key type %s to %s; types not related",
                                    keyType.getRawClass().getName(), keyClass.getName()));
                }
            } catch (IllegalArgumentException iae) {
                throw new JsonMappingException(null,
                        String.format("Failed to refine key type %s with concrete-type annotation (value %s), from '%s': %s",
                                keyType, keyClass.getName(), a.getName(), iae.getMessage()),
                                iae);
            }
            type = ((MapLikeType) type).withKeyType(keyType);
        }
    }

    // Process content type for collection-like or array-like types
    JavaType contentType = type.getContentType();
    if (contentType != null) {
        Class<?> contentClass = findSerializationContentType(a, contentType);
        if (contentClass != null && !contentType.hasRawClass(contentClass)) {
            try {
                if (contentClass.isAssignableFrom(contentType.getRawClass())) {
                    contentType = tf.constructSpecializedType(contentType, contentClass);
                } else if (contentType.getRawClass().isAssignableFrom(contentClass)) {
                    contentType = tf.constructGeneralizedType(contentType, contentClass);
                } else {
                    throw new JsonMappingException(null,
                            String.format("Cannot refine content type %s to %s; types not related",
                                    contentType.getRawClass().getName(), contentClass.getName()));
                }
            } catch (IllegalArgumentException iae) {
                throw new JsonMappingException(null,
                        String.format("Failed to refine content type %s with concrete-type annotation (value %s), from '%s': %s",
                                contentType, contentClass.getName(), a.getName(), iae.getMessage()),
                                iae);
            }
            type = type.withContentType(contentType);
        }
    }

    return type;
}