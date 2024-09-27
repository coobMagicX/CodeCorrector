import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SourceCode {

    private static final Set<String> _cfgIllegalClassNames = new HashSet<>(Arrays.asList(
            "com.example.IllegalClass1",
            "com.example.IllegalClass2"
    ));

    private static final String PREFIX_STRING = "com.example.spring.";

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
                    // looking for "AbstractBeanFactoryPointcutAdvisor" but no point to allow any is there?
                    if ("AbstractPointcutAdvisor".equals(name)
                            // ditto  for "FileSystemXmlApplicationContext": block all ApplicationContexts
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

    public static void main(String[] args) {
        // Test case
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":30}";
        try {
            Authentication1872 authentication = objectMapper.readValue(json, Authentication1872.class);
            System.out.println(authentication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}