  } else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
    // In SIMPLE or WHITESPACE mode, if the user hasn't specified a
    // translations file, they might reasonably try to write their own
    // implementation of goog.getMsg that makes the substitution at
    // run-time.
    //
    // In ADVANCED mode, goog.getMsg will be renamed anyway,
    // so we should stub it out without i18n warnings, as the user didn't ask for i18n.
    options.messageBundle = new EmptyMessageBundle();

    // Assume options has a method to dynamically manage warnings for specific diagnostic groups:
    options.setWarningLevel(DiagnosticGroups.MSG_CONVENTIONS, CheckLevel.OFF);
}
