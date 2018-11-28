/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.io.File;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DataConfigurationsTest {
    
    public DataConfigurationsTest() {
    }
    
    private DataConfigurations mock(){
        String resourcepath = DataConfigurationsTest.class.getClassLoader().getResource("rdbes_test_resources").getFile();
        return new DataConfigurations(new File(resourcepath));
    }

    /**
     * Test of loadResourceFile method, of class DataConfigurations.
     */
    @Test
    public void testLoadResourceFile() throws Exception {
        System.out.println("loadResourceFile");
        String filename = "landingssite.csv";
        DataConfigurations instance = mock();
        Map<String, String> expResult = null;
        Map<String, String> result = instance.loadResourceFile(filename);
        assertTrue(result.size()>0);
        assertEquals(result.get("854"), "NOABE");
        assertNull(result.get("Code")); // check that headers are not read
    }

    /**
     * Test of getLandingsSiteLoCode method, of class DataConfigurations.
     */
    @Test
    public void testGetLandingsSiteLoCode() throws Exception {
        System.out.println("getLandingsSiteLoCode");
        String imrCode = "30201";
        DataConfigurations instance = mock();
        String expResult = "NOADN";
        String result = instance.getLandingsSiteLoCode(imrCode);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMetaDataPb method, of class DataConfigurations.
     */
    @Test
    public void testGetMetaDataPb() throws Exception {
        System.out.println("getMetaDataPb");
        int year = 0;
        String field = "";
        DataConfigurations instance = null;
        String expResult = "";
        String result = instance.getMetaDataPb(year, field);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getQstrat method, of class DataConfigurations.
     */
    @Test
    public void testGetQstrat() throws Exception {
        System.out.println("getQstrat");
        DataConfigurations instance = mock();
        TemporalStrata result = instance.getQstrat();
        assertEquals("Q4", result.getStratum(360).getName());
        
    }

    /**
     * Test of getPortStratificationPb method, of class DataConfigurations.
     */
    @Test
    public void testGetPortStratificationPb() throws Exception {
        System.out.println("getPortStratificationPb");
        int year = 2016;
        DataConfigurations instance = mock();
        TemporalStrata expResult = null;
        TemporalStrata result = instance.getPortStratificationPb(year);
        assertEquals("Q2", result.getStratum(92).getName());
    }

    /**
     * Test of getLandingStratificationPb method, of class DataConfigurations.
     */
    @Test
    public void testGetLandingStratificationPb() throws Exception{
        System.out.println("getLandingStratificationPb");
        int year = 2016;
        DataConfigurations instance = mock();
        GearStrata result = instance.getLandingStratificationPb(year);
        assertEquals(4, result.strata.size());
        assertEquals("Gillnet", result.getStratum("4144").getName());
    }

    /**
     * Test of getHomrICES3 method, of class DataConfigurations.
     */
    @Test
    public void testGetHomrICES3() throws Exception {
        System.out.println("getHomrICES3");
        String homr = "";
        DataConfigurations instance = mock();
        String expResult = "27.2.a";
        String result = instance.getHomrICES3("04");
        assertEquals(expResult, result);
    }

    /**
     * Test of getImrGearFAO method, of class DataConfigurations.
     */
    @Test
    public void testGetImrGearFAO() throws Exception {
        System.out.println("getImrGearFAO");
        String imrGear = "";
        DataConfigurations instance = null;
        String expResult = "";
        String result = instance.getImrGearFAO(imrGear);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImrGearTargetSpecies method, of class DataConfigurations.
     */
    @Test
    public void testGetImrGearTargetSpecies() throws Exception {
        System.out.println("getImrGearTargetSpecies");
        String imrGear = "";
        DataConfigurations instance = null;
        String expResult = "";
        String result = instance.getImrGearTargetSpecies(imrGear);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImrGearMeshSize method, of class DataConfigurations.
     */
    @Test
    public void testGetImrGearMeshSize() throws Exception {
        System.out.println("getImrGearMeshSize");
        String imrGear = "";
        DataConfigurations instance = null;
        int expResult = 0;
        int result = instance.getImrGearMeshSize(imrGear);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImrGearSelDev method, of class DataConfigurations.
     */
    @Test
    public void testGetImrGearSelDev() throws Exception {
        System.out.println("getImrGearSelDev");
        String imrGear = "";
        DataConfigurations instance = null;
        int expResult = 0;
        int result = instance.getImrGearSelDev(imrGear);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImrGearSelDevMeshSize method, of class DataConfigurations.
     */
    @Test
    public void testGetImrGearSelDevMeshSize() throws Exception {
        System.out.println("getImrGearSelDevMeshSize");
        String imrGear = "";
        DataConfigurations instance = null;
        int expResult = 0;
        int result = instance.getImrGearSelDevMeshSize(imrGear);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImrGearMetier6 method, of class DataConfigurations.
     */
    @Test
    public void testGetImrGearMetier6() {
        System.out.println("getImrGearMetier6");
        String gear = "";
        DataConfigurations instance = null;
        String expResult = "";
        String result = instance.getImrGearMetier6(gear);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPresentation method, of class DataConfigurations.
     */
    @Test
    public void testGetPresentation() throws Exception {
        System.out.println("getPresentation");
        String sampleproducttype = "";
        DataConfigurations instance = null;
        String expResult = "";
        String result = instance.getPresentation(sampleproducttype);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getScalingFactor method, of class DataConfigurations.
     */
    @Test
    public void testGetScalingFactor() throws Exception {
        System.out.println("getScalingFactor");
        String fromCode = "";
        String toCode = "";
        DataConfigurations instance = null;
        double expResult = 0.0;
        double result = instance.getScalingFactor(fromCode, toCode);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLengthFactor method, of class DataConfigurations.
     */
    @Test
    public void testGetLengthFactor() {
        System.out.println("getLengthFactor");
        String lengthresolution = "";
        DataConfigurations instance = null;
        double expResult = 0.0;
        double result = instance.getLengthFactor(lengthresolution);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLengthUnit method, of class DataConfigurations.
     */
    @Test
    public void testGetLengthUnit() {
        System.out.println("getLengthUnit");
        String lengthresolution = "";
        DataConfigurations instance = null;
        String expResult = "";
        String result = instance.getLengthUnit(lengthresolution);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaturity method, of class DataConfigurations.
     */
    @Test
    public void testGetMaturity() throws Exception {
        System.out.println("getMaturity");
        String imrMaturity = "";
        DataConfigurations instance = null;
        String expResult = "";
        String result = instance.getMaturity(imrMaturity);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOtolithType method, of class DataConfigurations.
     */
    @Test
    public void testGetOtolithType() {
        System.out.println("getOtolithType");
        String aphia = "";
        DataConfigurations instance = null;
        Map<String, String> expResult = null;
        Map<String, String> result = instance.getOtolithType(aphia);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVesselFlag method, of class DataConfigurations.
     */
    @Test
    public void testGetVesselFlag() throws Exception{
        System.out.println("getVesselFlag");
        String catchplatform = "";
        DataConfigurations instance = mock();
        String expResult = "58";
        String result = instance.getVesselFlag("3936");
        assertEquals(expResult, result);
    }

    /**
     * Test of getVesselLength method, of class DataConfigurations.
     */
    @Test
    public void testGetVesselLength() throws Exception{
        System.out.println("getVesselLength");
        String catchplatform = "";
        DataConfigurations instance = null;
        int expResult = 0;
        int result = instance.getVesselLength(catchplatform);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVesselPower method, of class DataConfigurations.
     */
    @Test
    public void testGetVesselPower() throws Exception {
        System.out.println("getVesselPower");
        String catchplatform = "";
        DataConfigurations instance = null;
        int expResult = 0;
        int result = instance.getVesselPower(catchplatform);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVesselSize method, of class DataConfigurations.
     */
    @Test
    public void testGetVesselSize() throws Exception{
        System.out.println("getVesselSize");
        String catchplatform = "";
        DataConfigurations instance = null;
        int expResult = 0;
        int result = instance.getVesselSize(catchplatform);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
