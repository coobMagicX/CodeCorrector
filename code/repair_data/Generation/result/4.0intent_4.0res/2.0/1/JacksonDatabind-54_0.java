protected BeanPropertyWriter buildWriter(SerializerProvider prov,
    BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser,
    TypeSerializer typeSer, TypeSerializer contentTypeSer,
    AnnotatedMember am, boolean defaultUseStaticTyping)
throws JsonMappingException
{
    JavaType serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);

    if (contentTypeSer != null) {
        if (serializationType == null) {
            serializationType = declaredType;
        }
        JavaType ct = serializationType.getContentType();
        if (ct == null) {
            throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '"
                    + propDef.getName() + "' (of type " + _beanDesc.getType() + "); serialization type " + serializationType + " has no content");
        }
        serializationType = serializationType.withContentTypeHandler(contentTypeSer);
        ct = serializationType.getContentType();
    }

    Object valueToSuppress = null;
    boolean suppressNulls = false;

    JsonInclude.Value inclV = _defaultInclusion.withOverrides(propDef.findInclusion());
    JsonInclude.Include inclusion = inclV.getValueInclusion();
    if (inclusion == JsonInclude.Include.USE_DEFAULTS) {
        inclusion = JsonInclude.Include.ALWAYS;
    }

    JavaType actualType = (serializationType == null) ? declaredType : serializationType;

    switch (inclusion) {
    case NON_DEFAULT:
        if (_defaultInclusion.getValueInclusion() == JsonInclude.Include.NON_DEFAULT) {
            valueToSuppress = getPropertyDefaultValue(propDef.getName(), am, actualType);
        } else {
            valueToSuppress = getDefaultValue(actualType);
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
        if (declaredType.isReferenceType() || declaredType.isOptionalType()) {
            valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
        }
        break;
    case NON_EMPTY:
        suppressNulls = true;
        valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
        break;
    case NON_NULL:
        suppressNulls = true;
        break;
    case ALWAYS:
    default:
        if (declaredType.isContainerType() && !_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
            valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
        }
        break;
    }
    BeanPropertyWriter bpw = new BeanPropertyWriter(propDef, am, _beanDesc.getClassAnnotations(), declaredType,
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