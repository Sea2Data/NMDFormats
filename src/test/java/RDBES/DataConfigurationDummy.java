/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DataConfigurationDummy extends DataConfigurations {

    @Override
    public String getLengthMeasurement(String lengthmeasurement) throws IOException, RDBESConversionException {
        return "ForkLength";
    }

    @Override
    public int getVesselSize(String catchplatform) {
        return 1000;
    }

    @Override
    public int getVesselPower(String catchplatform) {
        return 100;
    }

    @Override
    public int getVesselLength(String catchplatform) {
        return 10;
    }

    @Override
    public String getVesselFlag(String catchplatform) {
        return "NOR";
    }

    public DataConfigurationDummy(File resourcefiles) {
        super(resourcefiles);
    }

    @Override
    public Map<String, String> getOtolithType(String aphia) {
        Map<String, String> m = new HashMap<>();
        m.put("1", "1");
        m.put("2", "2");
        m.put("3", "3");
        m.put("4", "4");
        m.put("5", "5");
        m.put("6", "6");
        m.put("7", "7");
        m.put("8", "8");
        return m;
    }


    @Override
    public String getMaturity(String imrMaturity) throws IOException, RDBESConversionException {
        return imrMaturity;
    }

    @Override
    public String getLengthUnit(String lengthresolution) {
        return "1cm";
    }

    @Override
    public double getLengthFactor(String lengthresolution) {
        return 1.0;
    }

    @Override
    public double getScalingFactor(String fromCode, String toCode) throws IOException, RDBESConversionException {
        return 1.0;
    }

    @Override
    public String getPresentation(String sampleproducttype) throws IOException, RDBESConversionException {
        return sampleproducttype;
    }

    @Override
    public Integer getImrGearSelDevMeshSize(String imrGear) throws IOException, RDBESConversionException {
        return 10;
    }

    @Override
    public int getImrGearSelDev(String imrGear) throws IOException, RDBESConversionException {
        return 2;
    }

    @Override
    public Integer getImrGearMeshSize(String imrGear) throws IOException, RDBESConversionException {
        return 10;
    }


    @Override
    public String getImrGearFAO(String imrGear) throws IOException, RDBESConversionException {
        return imrGear;
    }

    @Override
    public String getHomrICES3(String homr) throws IOException, RDBESConversionException {
        return homr;
    }

    @Override
    public GearStrata getLandingStratificationPb(int year) throws StrataException {
        GearStrata g = new DummyGearStrata(new LinkedList<>());
        
        return g;
    }

    @Override
    public TemporalStrata getPortStratificationPb(int year) throws StrataException, IOException, RDBESConversionException {
        return getQstrat();
    }

    @Override
    public String getMetaDataPb(int year, String field) throws IOException, RDBESConversionException {
        return field;
    }

    @Override
    public String getLandingsSiteLoCode(String imrCode) throws IOException, RDBESConversionException {
        return imrCode;
    }
    
}
