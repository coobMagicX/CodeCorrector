public String getValue() {
    // Check if the value is null and if the attribute is a boolean attribute
    // Assuming there's a way to identify boolean attributes, for example, using a method isBooleanAttribute()
    if (val == null && isBooleanAttribute()) {
        return "";
    }
    return val;
}