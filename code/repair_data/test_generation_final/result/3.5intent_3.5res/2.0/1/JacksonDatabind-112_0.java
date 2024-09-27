public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
    // May need to resolve types for delegate-based creators:
    JsonDeserializer<Object> delegate = null;
    if (_valueInstantiator != null) {
        // [databind#2324]: check both array-delegating and delegating
        AnnotatedWithParams delegateCreator = _valueInstantiator.getDelegateCreator();
        if (delegateCreator != null) {
            JavaType delegateType = _valueInstantiator.getDelegateType(ctxt.getConfig());
            delegate = findDeserializer(ctxt, delegateType, property);
        }
    }
    JsonDeserializer<?> valueDeser = _valueDeserializer;
    final JavaType valueType = _containerType.getContentType();
    if (valueDeser == null) {
        // [databind#125]: May have a content converter
        valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
        if (valueDeser == null) {
            // And we may also need to get deserializer for String
            valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
        }
    } else { // if directly assigned, probably not yet contextual, so:
        valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, valueType);
    }
    // 11-Dec-2015, tatu: Should we pass basic `Collection.class`, or more refined? Mostly
    //   comes down to "List vs Collection" I suppose... for now, pass Collection
    Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    NullValueProvider nuller = findContentNullProvider(ctxt, property, valueDeser);
    if (isDefaultDeserializer(valueDeser)) {
        valueDeser = null;
    }
    
    // Here, we need to ensure that the deserialized object is of type WithBagOfStrings
    if (WithBagOfStrings.class.isAssignableFrom(_containerType.getRawClass())) {
        valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
    }

    return withResolved(delegate, valueDeser, nuller, unwrapSingle);
}