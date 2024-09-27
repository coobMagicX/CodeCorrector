    public void testHWRuleEx1() {
        // From
        // http://www.archives.gov/research_room/genealogy/census/soundex.html:
        // Ashcraft is coded A-261 (A, 2 for the S, C ignored, 6 for the R, 1
        // for the F). It is not coded A-226.
        Assert.assertEquals("A261", this.getStringEncoder().encode("Ashcraft"));
        Assert.assertEquals("A261", this.getStringEncoder().encode("Ashcroft"));
        Assert.assertEquals("Y330", this.getStringEncoder().encode("yehudit"));
        Assert.assertEquals("Y330", this.getStringEncoder().encode("yhwdyt"));
    }