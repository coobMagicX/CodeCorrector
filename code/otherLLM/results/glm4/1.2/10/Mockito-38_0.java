private boolean safelyMatches(Matcher m, Object arg) {
    try {
        return m.matches(arg);
    } catch (Throwable t) {
        // Since we want to handle null values and avoid NullPointerException,
        // ensure that the argument is not null before calling matches on Matcher.
        if (arg == null && !m.canMatchNull()) {
            return false;
        }
        return true; // As per the original code, a match was attempted but we catch exceptions.
    }
}

public Integer[] getSuspiciouslyNotMatchingArgsIndexes(List<Matcher> matchers, Object[] arguments) {
    if (matchers == null || arguments == null || matchers.size() != arguments.length) {
        return new Integer[0];
    }
    
    List<Integer> suspicious = new LinkedList<>();
    int i = 0;
    for (Matcher m : matchers) {
        // Check for null values and avoid NullPointerException
        if (arguments[i] == null && !safelyMatches(m, arguments[i])) {
            suspicious.add(i);
            continue; // Skip further checks as we already have a non-matching case
        }
        
        if (!safelyMatches(m, arguments[i]) 
                && toStringEquals(m, arguments[i])
                && !((ContainsExtraTypeInformation) m).typeMatches(arguments[i])) {
            suspicious.add(i);
        }
        i++;
    }
    return suspicious.toArray(new Integer[0]);
}