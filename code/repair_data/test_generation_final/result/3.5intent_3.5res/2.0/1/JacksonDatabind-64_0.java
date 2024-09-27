protected BeanPropertyWriter buildWriter(SerializerProvider prov,
        BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser,
        TypeSerializer typeSer, TypeSerializer contentTypeSer,
        AnnotatedMember am, boolean defaultUseStaticTyping)
    throws JsonMappingException {
    // do we have annotation that forces type to use (to declared type or its super type)?
    JavaType serializationType;
    try {
        serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);
    } catch (JsonMappingException e) {
        return prov.reportBadPropertyDefinition(_beanDesc, propDef, e.getMessage());
    }

    // Container types can have separate type serializers for content (value / element) type
    if (contentTypeSer != null) {
        if (serializationType == null) {
            serializationType = TypeFactory.type(am.getGenericType(), _beanDesc.getType());
        }
        JavaType ct = serializationType.getContentType();
        if (ct == null) {
            prov.reportBadPropertyDefinition(_beanDesc, propDef,
                    "serialization type " + serializationType + " has no content");
        }
        serializationType = serializationType.withContentTypeHandler(contentTypeSer);
        ct = serializationType.getContentType();
    }

    Object valueToSuppress = null;
    boolean suppressNulls = false;

    // 12-Jul-2016, tatu: [databind#1256] Need to make sure we consider type refinement
    JavaType actualType = (serializationType == null) ? declaredType : serializationType;

    // 17-Aug-2016, tatu: Default inclusion covers global default (for all types), as well
    //   as type-default for enclosing POJO. What we need, then, is per-type default (if any)
    //   for declared property type... and finally property annotation overrides
    JsonInclude.Value inclV = _config.getDefaultPropertyInclusion(actualType.getRawClass(),
            _defaultInclusion);

    // property annotation override

    inclV = inclV.withOverrides(propDef.findInclusion());
    JsonInclude.Include inclusion = inclV.getValueInclusion();

    if (inclusion == JsonInclude.Include.USE_DEFAULTS) {
        inclusion = JsonInclude.Include.ALWAYS;
    }

    switch (inclusion) {
        case NON_DEFAULT:
            if (_useRealPropertyDefaults) {
                if (prov.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
                    am.fixAccess(_config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
                }
                valueToSuppress = getPropertyDefaultValue(propDef.getName(), am, actualType);
            } else {
                valueToSuppress = getDefaultValue(actualType);
                suppressNulls = true;
            }
            if (valueToSuppress == null) {
                suppressNulls = true;
            } else {
                if (valueToSuppress.getClass().isArray()) {
                    valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
                }
            }
            break;
        case NON_ABSENT:
            suppressNulls = true;
            if (actualType.isReferenceType()) {
                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            }
            break;
        case NON_EMPTY:
            suppressNulls = true;
            valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            break;
        case NON_NULL:
            suppressNulls = true;
        case ALWAYS:
        default:
            if (actualType.isContainerType()
                    && !_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            }
            break;
    }
    BeanPropertyWriter bpw = new BeanPropertyWriter(propDef,
            am, _beanDesc.getClassAnnotations(), declaredType,
            ser, typeSer, serializationType, suppressNulls, valueToSuppress);

    Object serDef = _annotationIntrospector.findNullSerializer(am);
    if (serDef != null) {
        bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
    }

    NameTransformer unwrapper = _annotationIntrospector.findUnwrappingNameTransformer(am);
    if (unwrapper != null) {
        bpw = bpw.unwrappingWriter(unwrapper);
    }
    return bpw;
}