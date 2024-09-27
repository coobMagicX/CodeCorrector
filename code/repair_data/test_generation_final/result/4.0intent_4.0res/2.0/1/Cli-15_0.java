import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandLineParser {
    private Map<Option, List> values;
    private Map<Option, List> defaultValues;

    public CommandLineParser(Map<Option, List> values, Map<Option, List> defaultValues) {
        this.values = values;
        this.defaultValues = defaultValues;
    }

    public List getValues(final Option option, List defaultValues) {
        // initialize the return list
        List valueList = values.get(option); // Fetch actual values associated with the option

        // If no values are found or list is empty, use provided default values
        if (valueList == null || valueList.isEmpty()) {
            valueList = new ArrayList<>(defaultValues);
        } else {
            // If values are found, augment them with the default values if they are not already included
            for (Object defaultValue : defaultValues) {
                if (!valueList.contains(defaultValue)) {
                    valueList.add(defaultValue);
                }
            }
        }

        // Return the list of values, or an empty list if null
        return valueList == null ? Collections.EMPTY_LIST : valueList;
    }
}