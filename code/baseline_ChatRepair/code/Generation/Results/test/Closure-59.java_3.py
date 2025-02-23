if (options.checkGlobalThisLevel.isOn()) {
  options.setWarningLevel(
      DiagnosticGroups.GLOBAL_THIS,
      options.checkGlobalThisLevel);
} else {
  // Explicitly set the warning level to off if checkGlobalThisLevel is not on
  options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, CheckLevel.OFF);
}
