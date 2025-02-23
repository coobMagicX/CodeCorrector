if (!options.checkSymbols &&
      !composedGuards.enables(DiagnosticGroups.CHECK_VARIABLES)) {
  composedGuards.addGuard(new DiagnosticGroupWarningsGuard(
      DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF));
}

// Correctly handling the setting of GLOBAL_THIS based on checkGlobalThisLevel
if (options.checkGlobalThisLevel != CheckLevel.OFF) {
  options.setWarningLevel(
      DiagnosticGroups.GLOBAL_THIS,
      options.checkGlobalThisLevel);
}

this.warningsGuard = composedGuards;
