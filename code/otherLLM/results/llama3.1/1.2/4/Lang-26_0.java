public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    
    // Apply the "EEEE', week 'ww" rules to the calendar instance
    Rule[] rules = getRulesForLocale("sv", "SE");
    int len = rules.length;
    for (int i = 0; i < len; i++) {
        rules[i].appendTo(c, new StringBuffer(mMaxLengthEstimate));
    }
    
    // Return the formatted string
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}

// New method to get the rules for a specific locale
protected Rule[] getRulesForLocale(String lang, String country) {
    // Assuming there's an array of locale-specific rules
    Rule[] localeRules = mRulesByLocale.get(lang + "_" + country);
    return localeRules != null ? localeRules : mDefaultRules;
}