protected SettableBeanProperty constructSettableProperty(DeserializationContext ctxt,
        BeanDescription beanDesc, BeanPropertyDefinition propDef,
        JavaType propType0)
    throws JsonMappingException
{
    // need to ensure method is callable (for non-public)
    AnnotatedMember mutator = propDef.getNonConstructorMutator();

    if (ctxt.canOverrideAccessModifiers()) {
        // [databind#877]: explicitly prevent forced access to `cause` of `Throwable`;
        // never needed and attempts may cause problems on some platforms.
        // !!! NOTE: should be handled better for 2.8 and later
        if (!Throwable.class.isAssignableFrom(mutator.getRawType())) {
            mutator.fixAccess(ctxt.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
        }
    }
    // note: this works since we know there's exactly one argument for methods
    BeanProperty.Std property = new BeanProperty.Std(propDef.getFullName(),
            propType0, propDef.getWrapperName(),
            beanDesc.getClassAnnotations(), mutator, propDef.getMetadata());
    JavaType type = resolveType(ctxt, beanDesc, propType0, mutator);
    // did type change?
    if (type != propType0) {
        property = property.withType(type);
    }

    // First: does the Method specify the deserializer to use? If so, let's use it.
    JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(ctxt, mutator);
    type = modifyTypeByAnnotation(ctxt, mutator, type);
    TypeDeserializer typeDeser = type.getTypeHandler();
    SettableBeanProperty prop;
    if (mutator instanceof AnnotatedMethod) {
        prop = new MethodProperty(propDef, type, typeDeser,
                beanDesc.getClassAnnotations(), (AnnotatedMethod) mutator);
    } else {
        prop = new FieldProperty(propDef, type, typeDeser,
                beanDesc.getClassAnnotations(), (AnnotatedField) mutator);
    }
    if (propDeser != null) {
        prop = prop.withValueDeserializer(propDeser);
    }
    // need to retain name of managed forward references:
    AnnotationIntrospector.ReferenceProperty ref = propDef.findReferenceType();
    if (ref != null && ref.isManagedReference()) {
        prop.setManagedReferenceName(ref.getName());
    }
    ObjectIdInfo objectIdInfo = propDef.findObjectIdInfo();
    if(objectIdInfo != null){
        prop.setObjectIdInfo(objectIdInfo);
    }
    return prop;
}