  } else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
    // In SIMPLE or WHITESPACE mode, if the user hasn't specified a
    // translations file, they might reasonably try to write their own
    // implementation of goog.getMsg that makes the substitution at
    // run-time.
    //
    // In ADVANCED mode, goog.getMsg is going to be renamed anyway,
    // so we might as well inline it. But shut off the i18n warnings,
    // because the user didn't really ask for i18n.
    options.messageBundle = new EmptyMessageBundle();
    options.setWarningLevel(DiagnosticGroups.MSG_CONVENTIONS, CheckLevel.OFF);
  }
