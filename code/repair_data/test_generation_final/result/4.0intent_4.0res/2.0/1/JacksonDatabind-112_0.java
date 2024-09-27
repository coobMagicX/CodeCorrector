public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
        BeanProperty property) throws JsonMappingException
{
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
            // We need to get deserializer for the content type of the collection, i.e., String in this case
            valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
        }
    } else { // if directly assigned, it is probably not yet contextual, so:
        valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, valueType);
    }
    // Determine if single values should be accepted as arrays; this is crucial for correct JSON array processing
    Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class,
            JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    NullValueProvider nuller = findContentNullProvider(ctxt, property, valueDeser);
    if (isDefaultDeserializer(valueDeser)) {
        valueDeser = null;
    }
    return withResolved(delegate, valueDeser, nuller, unwrapSingle);
}