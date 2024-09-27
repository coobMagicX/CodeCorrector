protected void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
    final boolean isConcrete = !beanDesc.getType().isAbstract();
    final SettableBeanProperty[] creatorProps = isConcrete ? builder.getValueInstantiator().getFromObjectArguments(ctxt.getConfig()) : null;
    final boolean hasCreatorProps = (creatorProps != null);

    JsonIgnoreProperties.Value ignorals = ctxt.getConfig().getDefaultPropertyIgnorals(beanDesc.getBeanClass(), beanDesc.getClassInfo());
    Set<String> ignored;

    if (ignorals != null) {
        boolean ignoreAny = ignorals.getIgnoreUnknown();
        builder.setIgnoreUnknownProperties(ignoreAny);
        // Or explicit/implicit definitions?
        ignored = ignorals.getIgnored();
        for (String propName : ignored) {
            builder.addIgnorable(propName);
        }
        // Add the "name" property to the list of ignored properties
        builder.addIgnorable("name");
        
        // Check for ignored properties using getter methods
        List<BeanPropertyDefinition> propDefs = beanDesc.findProperties();
        for (BeanPropertyDefinition propDef : propDefs) {
            if (propDef.hasGetter() && ignored.contains(propDef.getInternalName())) {
                builder.addIgnorable(propDef.getInternalName());
            }
        }
        
    } else {
        ignored = Collections.emptySet();
    }

    // Rest of the code remains the same
    // ...
}