protected void _addExplicitAnyCreator(DeserializationContext ctxt,
        BeanDescription beanDesc, CreatorCollector creators,
        CreatorCandidate candidate)
    throws JsonMappingException {
    if (1 != candidate.paramCount()) {
        int oneNotInjected = candidate.findOnlyParamWithoutInjection();
        if (oneNotInjected >= 0) {
            if (candidate.paramName(oneNotInjected) == null) {
                _addExplicitDelegatingCreator(ctxt, beanDesc, creators, candidate);
                return;
            }
        }
        _addExplicitPropertyCreator(ctxt, beanDesc, creators, candidate);
        return;
    }
    AnnotatedParameter param = candidate.parameter(0);
    JacksonInject.Value injectId = candidate.injection(0);
    PropertyName paramName = candidate.explicitParamName(0);
    BeanPropertyDefinition paramDef = candidate.propertyDef(0);

    boolean useProps = (paramName != null) || (injectId != null);
    if (!useProps && (paramDef != null)) {
        paramName = candidate.findImplicitParamName(0);
        paramName = candidate.findImplicitParamName(0);
        useProps = (paramName != null) && paramDef.couldSerialize();
    }
    if (useProps) {
        SettableBeanProperty[] properties = new SettableBeanProperty[] {
                constructCreatorProperty(ctxt, beanDesc, paramName, 0, param, injectId)
        };
        creators.addPropertyCreator(candidate.creator(), true, properties);
        return;
    }

    // Handle snake_case property names during deserialization
    PropertyNamingStrategy.PropertyNamingStrategyBase namingStrategy = (PropertyNamingStrategy.PropertyNamingStrategyBase) ctxt.getConfig().getPropertyNamingStrategy();
    if (namingStrategy != null) {
        String snakeCaseName = namingStrategy.translate(param.getName());
        if (snakeCaseName != null) {
            paramName = PropertyName.construct(snakeCaseName);
        }
    }

    _handleSingleArgumentCreator(creators, candidate.creator(), true, true);

    if (paramDef != null) {
        ((POJOPropertyBuilder) paramDef).removeConstructors();
    }
}