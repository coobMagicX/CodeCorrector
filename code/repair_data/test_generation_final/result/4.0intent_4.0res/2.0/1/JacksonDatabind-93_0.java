public void validateSubType(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
    // There are certain nasty classes that could cause problems, mostly
    // via default typing -- catch them here.
    final Class<?> raw = type.getRawClass();
    String full = raw.getName();

    main_check:
    do {
        if (_cfgIllegalClassNames.contains(full)) {
            break;
        }

        // 18-Dec-2017, tatu: As per [databind#1855], need bit more sophisticated handling
        //    for some Spring framework types
        // 05-Jan-2017, tatu: ... also, only applies to classes, not interfaces
        if (full.startsWith(PREFIX_STRING)) {
            for (Class<?> cls = raw; cls != Object.class; cls = cls.getSuperclass()) {
                String name = cls.getSimpleName();
                // looking for "AbstractBeanFactoryPointcutAdvisor" and "AbstractApplicationContext"
                // but allow other subclasses that do not pose security risks
                if ("AbstractBeanFactoryPointcutAdvisor".equals(name)
                        || "FileSystemXmlApplicationContext".equals(name)
                        || "AbstractApplicationContext".equals(name)) {
                    break main_check;
                }
            }
        }
        return;
    } while (false);

    throw JsonMappingException.from(ctxt,
            String.format("Illegal type (%s) to deserialize: prevented for security reasons", full));
}