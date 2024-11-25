if (options.checkGlobalThisLevel.isOn()) {
  options.setWarningLevel(
      DiagnosticGroups.GLOBAL_THIS,
      options.checkGlobalThisLevel);
}