  public void testNullSafeBugSerialize() throws Exception {
    Device device = new Device("ec57803e");
    gson.toJson(device);
  }