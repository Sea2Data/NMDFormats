/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import Biotic.Biotic3.Biotic3Handler;
import BioticTypes.v3.CatchsampleType;
import BioticTypes.v3.FishstationType;
import BioticTypes.v3.MissionsType;
import java.io.File;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import partialRDBES.v1_16.BiologicalvariableType;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.FishingtripType;
import partialRDBES.v1_16.LandingeventType;
import partialRDBES.v1_16.OnshoreeventType;
import partialRDBES.v1_16.SampleType;
import partialRDBES.v1_16.SamplingdetailsType;
import partialRDBES.v1_16.SpecieslistdetailsType;
import partialRDBES.v1_16.SpeciesselectionType;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESprovebatCompilerTest {
    
    public RDBESprovebatCompilerTest() {
    }

    private RDBESprovebatCompiler mock(){
        String pbpath = this.getClass().getClassLoader().getResource("pb_t2_test.xml").getFile();
        String resourcepath = RDBESprovebatCompilerTest.class.getClassLoader().getResource("rdbes_test_resources").getFile();
        
        MissionsType biotic = null;
        RDBESprovebatCompiler compiler = null;
        
        try{
        Biotic3Handler handler = new Biotic3Handler();
        biotic = handler.read(new File(pbpath));

        compiler = new RDBESprovebatCompiler(biotic, null, new DataConfigurations(new File(resourcepath)), 2016, false);
        }
        catch (Exception e){
            assert false;
        }
        return compiler;
    } 
    
        private RDBESprovebatCompiler mockWDummyConfig() throws Exception{
        String pbpath = this.getClass().getClassLoader().getResource("pb_t2_test.xml").getFile();
        String resourcepath = RDBESprovebatCompilerTest.class.getClassLoader().getResource("rdbes_test_resources").getFile();
        
        MissionsType biotic = null;
        RDBESprovebatCompiler compiler = null;
        
        //try{
        Biotic3Handler handler = new Biotic3Handler();
        biotic = handler.read(new File(pbpath));

        compiler = new RDBESprovebatCompiler(biotic, null, new DataConfigurationDummy(null), 2016, false);
        //}
        //catch (Exception e){
        //    assert false;
        //}
        return compiler;
    } 

    /**
     * Test of getSamplingDetails method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSamplingDetails() throws Exception {
        System.out.println("getSamplingDetails");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        SamplingdetailsType expResult = null;
        SamplingdetailsType result = instance.getSamplingDetails();
        assertEquals("SD", result.getSDrecordType());
        assertTrue(result.getSDid()>0);
    }

    /**
     * Test of getOnshoreEvent method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetOnshoreEvent() throws Exception {
        System.out.println("getOnshoreEvent");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        OnshoreeventType expResult = null;
        OnshoreeventType result = instance.getOnshoreEvent();
        assertEquals("OS", result.getOSrecordType());
        assertTrue(result.getOSid()>0);
    }

    /**
     * Test of getSpeciesSelectionDetails method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSpeciesSelectionDetails() throws Exception {
        System.out.println("getSpeciesSelectionDetails");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        SpecieslistdetailsType expResult = null;
        SpecieslistdetailsType result = instance.getSpeciesSelectionDetails();
        assertEquals("SL", result.getSLrecordType());
        assertTrue(result.getSLid()>0);
    }

    /**
     * Test of getLandingEvent method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetLandingEvent() throws Exception {
        System.out.println("getLandingEvent");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        LandingeventType expResult = null;
        LandingeventType result = instance.getLandingEvent();
        assertEquals("LE", result.getLErecordType());
        assertTrue(result.getLEid()>0);
    }

    /**
     * Test of getSpeciesSelection method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSpeciesSelection() throws Exception {
        System.out.println("getSpeciesSelection");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        SpeciesselectionType expResult = null;
        SpeciesselectionType result = instance.getSpeciesSelection();
        assertEquals("SS", result.getSSrecordType());
        assertTrue(result.getSSid()>0);
    }

    /**
     * Test of getFishingTrip method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetFishingTrip() throws Exception {
        System.out.println("getFishingTrip");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        FishingtripType expResult = null;
        FishingtripType result = instance.getFishingTrip();
        assertEquals(result.getFTrecordType(), "FT");
        assertTrue(result.getFTid()>0);
    }

    /**
     * Test of getSample method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSample() throws Exception {
        System.out.println("getSample");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        SampleType expResult = null;
        SampleType result = instance.getSample();
        assertEquals(result.getSArecordType(), "SA");
        assertTrue(result.getSAid()>0);
    }

    /**
     * Test of getBiologicalVariable method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetBiologicalVariable() throws Exception {
        System.out.println("getBiologicalVariable");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        BiologicalvariableType expResult = null;
        BiologicalvariableType result = instance.getBiologicalVariable();
        assertEquals("BV", result.getBVrecordType());
        assertTrue(result.getBVid()>0);
    }

    /**
     * Test of addProveBatAsH5 method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testAddProveBatAsH5DummyConfigs() throws Exception {
        System.out.println("addProveBatAsH5");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        instance.addProveBatAsH5();
        assertTrue(instance.rdbes.size()==1);
        DesignType d = (DesignType) instance.rdbes.get(0);
        assertTrue(d.getDEhierarchy()!=null);
        assertTrue(d.getSamplingdetails().getOnshoreevent().size()>5);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().size()>1);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().size()==1);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().get(0).getSample().size()>0);
        
        SpeciesselectionType ss = d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().get(0);
        boolean bv_checked = false;
        boolean agefound = false;
        boolean lengthfound = false;
        boolean weightfound = false;
        System.out.println(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().size());
        for (SampleType sa : ss.getSample()){
            System.out.println(sa.getSAspeciesCode());
            if (sa.getBiologicalvariable().size()>0){
                bv_checked = true;
                for (BiologicalvariableType bv: sa.getBiologicalvariable()){
                    if (bv.getBVtype().equals("Age")){
                        agefound=true;
                    }
                    if (bv.getBVtype().equals("Length")){
                        lengthfound=true;
                    }
                    if (bv.getBVtype().equals("Weight")){
                        weightfound=true;
                    }

                }
            }
        }
        assertTrue(bv_checked);
        assertTrue(agefound);
        assertTrue(lengthfound);
        assertTrue(weightfound);
    }

        /**
     * Test of addProveBatAsH5 method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testAddProveBatAsH5WConfigs() throws Exception {
        System.out.println("addProveBatAsH5");
        RDBESprovebatCompiler instance = mock();
        instance.addProveBatAsH5();
        assertTrue(instance.rdbes.size()==1);
                DesignType d = (DesignType) instance.rdbes.get(0);
        assertTrue(d.getDEhierarchy()!=null);
        assertTrue(d.getSamplingdetails().getOnshoreevent().size()>5);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().size()>1);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().size()==1);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().get(0).getSample().size()>0);
        
        SpeciesselectionType ss = d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().get(0);
        boolean bv_checked = false;
        boolean agefound = false;
        boolean lengthfound = false;
        boolean weightfound = false;
        for (SampleType sa : ss.getSample()){
            if (sa.getBiologicalvariable().size()>0){
                bv_checked = true;
                for (BiologicalvariableType bv: sa.getBiologicalvariable()){
                    if (bv.getBVtype().equals("Age")){
                        agefound=true;
                    }
                    if (bv.getBVtype().equals("Length")){
                        lengthfound=true;
                    }
                    if (bv.getBVtype().equals("Weight")){
                        weightfound=true;
                    }

                }
            }
        }
        assertTrue(bv_checked);
        assertTrue(agefound);
        assertTrue(lengthfound);
        assertTrue(weightfound);
    }


    /**
     * Test of getProveBatStations method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetProveBatStations() throws Exception {
        System.out.println("getProveBatStations");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        List<FishstationType> expResult = null;
        List<FishstationType> result = instance.getProveBatStations();
        assertTrue(result.size()>0);
    }
    
}
