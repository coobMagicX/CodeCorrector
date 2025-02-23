protected CompilerOptions createOptions() {
  CompilerOptions options = new CompilerOptions();
  options.setCodingConvention(new ClosureCodingConvention());
  CompilationLevel level = flags.compilation_level;
  level.setOptionsForCompilationLevel(options);
  if (flags.debug) {
    level.setDebugOptionsForCompilationLevel(options);
  }

  WarningLevel wLevel = flags.warning_level;
  wLevel.setOptionsForWarningLevel(options);
  for (FormattingOption formattingOption : flags.formatting) {
    formattingOption.applyToOptions(options);
  }
  
  if (flags.process_closure_primitives) {
    options.closurePass = true;
    options.setCheckTypes(true); // May require type checking to be enabled for certain Closure processing.
    options.setRewritePolyfills(true); // Rewrite polyfills as needed.
  }

  initOptionsFromFlags(options);
  return options;
}
