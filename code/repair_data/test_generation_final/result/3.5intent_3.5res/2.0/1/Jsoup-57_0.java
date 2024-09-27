public void removeIgnoreCase(String key) {
    Validate.notEmpty(key);
    if (attributes == null)
        return;
    List<String> keysToRemove = new ArrayList<>();
    for (Iterator<String> it = attributes.keySet().iterator(); it.hasNext(); ) {
        String attrKey = it.next();
        if (attrKey.equalsIgnoreCase(key))
            keysToRemove.add(attrKey);
    }
    for (String attrKey : keysToRemove) {
        attributes.remove(attrKey);
    }
}