public String generateToolTipFragment(String toolTipText) {
    String escapedToolTipText = toolTipText.replace("\"", "&quot;");
    return " title=\"" + escapedToolTipText + "\" alt=\"\"";
}
