/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import BioticTypes.v3.FishstationType;
import BioticTypes.v3.MissionsType;
import LandingsTypes.v1.LandingsdataType;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.LandingeventType;
import partialRDBES.v1_16.ObjectFactory;
import partialRDBES.v1_16.OnshoreeventType;
import partialRDBES.v1_16.SamplingdetailsType;
import partialRDBES.v1_16.VesseldetailsType;

/**
 * Base class for converting NMD / IMR data to to RDBES
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESCompiler {

    protected ObjectFactory rdbesFactory;
    protected List<DesignType> rdbes;
    protected MissionsType biotic;
    protected LandingsdataType landings;
    protected DataConfigurations dataconfigurations;
    protected int year;
    protected boolean strict;
    protected PrintStream log;

    /**
     * Compiles tables in the RDBES exchange format
     *
     * @param biotic NMD biotic data / samples to be used for data set
     * compilation
     * @param landings landings data to be used for data set compilation
     * @param conversions data configurations and conversion tables
     * @param year year to compile data for
     * @param strict if Exceptions will be thrown for inconvertable data.
     * Otherwise inconvertable sampling units will be skipped with a warning.
     */
    public RDBESCompiler(MissionsType biotic, LandingsdataType landings, DataConfigurations conversions, int year, boolean strict) {
        this.rdbesFactory = new ObjectFactory();
        this.rdbes = new ArrayList<>();
        this.biotic = biotic;
        this.landings = landings;
        this.dataconfigurations = conversions;
        this.year = year;
        this.strict = strict;
        this.log = System.err;
    }

    /**
     * Construct a new RDBES compiler initialized with same source files and
     * configurations as another.
     *
     * @param rdbesCompiler
     */
    public RDBESCompiler(RDBESCompiler rdbesCompiler) {
        this.rdbesFactory = rdbesCompiler.rdbesFactory;
        this.rdbes = rdbesCompiler.rdbes;
        this.biotic = rdbesCompiler.biotic;
        this.landings = rdbesCompiler.landings;
        this.dataconfigurations = rdbesCompiler.dataconfigurations;
        this.year = rdbesCompiler.year;
        this.strict = rdbesCompiler.strict;
        this.log = rdbesCompiler.log;
    }

    /**
     * Generates table in the RDBES exchange format at in the directory
     * specified as outputpath
     *
     * @param outputpath
     */
    public void writeTables(File outputpath) {

        //skip CL etc if landings is null
        // use generic XML to relational conversion
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Creates CL table for RDBES
     */
    public void createCL() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * get unused designid
     *
     * @return
     */
    private int getDesingId() {
        int m = 0;
        for (DesignType d : this.rdbes) {
            if (d.getDEid() > m) {
                m = d.getDEid();
            }
        }
        return (m + 1);
    }

    /**
     * Get unused samplingDetailsID
     *
     * @return
     */
    private int getSamplingDetailsId() {
        int m = 0;
        for (DesignType d : this.rdbes) {
            if (d.getDEid() > m) {
                m = d.getSamplingdetails().getSDid();
            }
        }
        return (m + 1);
    }

    private int getOnshoreEventId() {
        int m = 0;
        for (DesignType d : this.rdbes) {
            for (OnshoreeventType os : d.getSamplingdetails().getOnshoreevent()) {
                if (os.getOSid() > m) {
                    m = os.getOSid();
                }
            }
        }
        return (m + 1);
    }

    private int getLandingEventId() {
        int m = 0;
        for (DesignType d : this.rdbes) {
            for (OnshoreeventType os : d.getSamplingdetails().getOnshoreevent()) {
                for (LandingeventType lt : os.getLandingevent()) {
                    if (lt.getLEid() > m) {
                        m = lt.getLEid();
                    }
                }
            }
        }
        return (m + 1);
    }

    protected void setCommonSamplingDetails(SamplingdetailsType samplingdetails){
        samplingdetails.setSDid(getSamplingDetailsId());
        samplingdetails.setSDrecordType("SD");
    }
    
    protected void setCommonDesign(DesignType design) {
        design.setDEid(getDesingId());
        design.setDErecordType("DE");
    }

    protected void setCommonOnshoreEvent(OnshoreeventType os, FishstationType fs) throws IOException {
        os.setOSid(getOnshoreEventId());
        os.setOSrecordType("OS");
        os.setOSnationalLocationName(fs.getLandingsite());
        os.setOSlocation(this.dataconfigurations.getLandingsSiteLoCode().get(fs.getLandingsite()));
        os.setOSsamplingDate(fs.getStationstartdate());
    }

    protected void setCommonLandingEvent(LandingeventType le, FishstationType fs) {
        le.setLEid(getLandingEventId());
        le.setLErecordType("LE");
        
    }

    protected VesseldetailsType getVesselDetails(String catchplatform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected String getImrGearMetier6(String gear){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
