protected void addBeanProps(DeserializationContext ctxt,
        BeanDescription beanDesc, BeanDeserializerBuilder builder)
    throws JsonMappingException
{
    final boolean isConcrete = !beanDesc.getType().isAbstract();
    final SettableBeanProperty[] creatorProps = isConcrete
            ? builder.getValueInstantiator().getFromObjectArguments(ctxt.getConfig())
            : null;
    final boolean hasCreatorProps = (creatorProps != null);
    
    JsonIgnoreProperties.Value ignorals = ctxt.getConfig()
            .getDefaultPropertyIgnorals(beanDesc.getBeanClass(),
                    beanDesc.getClassInfo());
    Set<String> ignored;

    if (ignorals != null) {
        boolean ignoreAny = ignorals.getIgnoreUnknown();
        builder.setIgnoreUnknownProperties(ignoreAny);
        ignored = ignorals.getIgnored();
        for (String propName : ignored) {
            builder.addIgnorable(propName);
        }
    } else {
        ignored = Collections.emptySet();
    }

    AnnotatedMethod anySetterMethod = beanDesc.findAnySetter();
    AnnotatedMember anySetterField = null;
    if (anySetterMethod != null) {
        builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetterMethod));
    }
    else {
        anySetterField = beanDesc.findAnySetterField();
        if(anySetterField != null) {
            builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetterField));
        }
    }

    if (anySetterMethod == null && anySetterField == null) {
        Collection<String> ignored2 = beanDesc.getIgnoredPropertyNames();
        if (ignored2 != null) {
            ignored.addAll(ignored2);
        }
    }

    final boolean useGettersAsSetters = ctxt.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS)
            && ctxt.isEnabled(MapperFeature.AUTO_DETECT_GETTERS);

    List<BeanPropertyDefinition> propDefs = filterBeanProps(ctxt,
            beanDesc, builder, beanDesc.findProperties(), ignored);

    if (_factoryConfig.hasDeserializerModifiers()) {
        for (BeanDeserializerModifier mod : _factoryConfig.deserializerModifiers()) {
            propDefs = mod.updateProperties(ctxt.getConfig(), beanDesc, propDefs);
        }
    }
    
    for (BeanPropertyDefinition propDef : propDefs) {
        SettableBeanProperty prop = null;
        if (propDef.hasSetter()) {
            JavaType propertyType = propDef.getSetter().getParameterType(0);
            prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
        } else if (propDef.hasField()) {
            JavaType propertyType = propDef.getField().getType();
            prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
        } else if (useGettersAsSetters && propDef.hasGetter()) {
            AnnotatedMethod getter = propDef.getGetter();
            Class<?> rawPropertyType = getter.getRawType();
            if (Collection.class.isAssignableFrom(rawPropertyType)
                    || Map.class.isAssignableFrom(rawPropertyType)) {
                prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
                if (!ignored.contains(propDef.getName())) {
                    builder.addProperty(prop);
                }
            }
        }

        if (hasCreatorProps && propDef.hasConstructorParameter()) {
            final String name = propDef.getName();
            CreatorProperty cprop = null;
            if (creatorProps != null) {
                for (SettableBeanProperty cp : creatorProps) {
                    if (name.equals(cp.getName()) && (cp instanceof CreatorProperty)) {
                        cprop = (CreatorProperty) cp;
                        break;
                    }
                }
            }
            if (cprop == null) {
                List<String> n = new ArrayList<>();
                for (SettableBeanProperty cp : creatorProps) {
                    n.add(cp.getName());
                }
                ctxt.reportBadPropertyDefinition(beanDesc, propDef,
                        "Could not find creator property with name '%s' (known Creator properties: %s)",
                        name, n);
                continue;
            }
            if (prop != null) {
                cprop.setFallbackSetter(prop);
            }
            prop = cprop;
            builder.addCreatorProperty(cprop);
            continue;
        }

        if (prop != null && !ignored.contains(propDef.getName())) {
            Class<?>[] views = propDef.findViews();
            if (views == null) {
                if (!ctxt.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
                    views = NO_VIEWS;
                }
            }
            prop.setViews(views);
            builder.addProperty(prop);
        }
    }
}