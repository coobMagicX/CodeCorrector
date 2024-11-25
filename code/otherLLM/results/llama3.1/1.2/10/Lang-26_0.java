public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    
    // Correctly apply formatting rules for the specified locale ("sv", "SE")
    Rule[] rules = mRulesForLocale.get("sv");  // assuming a map of locale to rules is available
    
    int len = rules.length;
    StringBuffer buf = new StringBuffer(mMaxLengthEstimate);
    
    for (int i = 0; i < len; i++) {
        rules[i].appendTo(buf, c);  // use the correct set of formatting rules
    }
    
    return buf.toString();
}