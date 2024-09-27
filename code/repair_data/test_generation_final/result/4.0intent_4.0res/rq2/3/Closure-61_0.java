public void testCall1() {
  assertEquals(false, functionCallHasSideEffects(parse("Math.sin(0)"), null));
}