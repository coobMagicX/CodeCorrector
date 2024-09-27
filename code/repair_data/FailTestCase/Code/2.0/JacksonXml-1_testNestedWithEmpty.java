    public void testNestedWithEmpty() throws Exception
    {
        final String XML =
"<ServiceDelivery>\n"
+"  <ResponseTimestamp>2012-09-12T09:28:17.213-04:00</ResponseTimestamp>\n"
+"  <VehicleMonitoringDelivery>\n"
+"  </VehicleMonitoringDelivery>\n"
+"</ServiceDelivery>\n"
                ;
        
        ServiceDelivery svc = _xmlMapper.readValue(XML, ServiceDelivery.class);
        assertNotNull(svc);
        assertNotNull(svc.vehicleMonitoringDelivery);
        // 06-Jan-2015, tatu: Really should have one empty entry, not 0
        assertEquals(1, svc.vehicleMonitoringDelivery.size());
    }