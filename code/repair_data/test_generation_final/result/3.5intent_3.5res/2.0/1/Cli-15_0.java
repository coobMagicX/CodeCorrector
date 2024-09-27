public List getValues(final Option option, List defaultValues) {
    // initialize the return list
    List valueList = (List) values.get(option);

    // grab the correct default values
    if ((valueList == null) || valueList.isEmpty()) {
        valueList = defaultValues;
    }

    // augment the list with the default values
    if ((valueList == null) || valueList.isEmpty()) {
        valueList = (List) this.defaultValues.get(option);
    }

    // if there are more default values as specified, add them to the list
    if (valueList != null && valueList.size() < defaultValues.size()) {
        List additionalValues = defaultValues.subList(valueList.size(), defaultValues.size());
        valueList.addAll(additionalValues);
    }

    // if the list is still empty, return the default values
    if (valueList.isEmpty()) {
        valueList = defaultValues;
    }

    return valueList == null ? Collections.EMPTY_LIST : valueList;
}