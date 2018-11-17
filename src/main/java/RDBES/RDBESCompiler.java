/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import Biotic.Biotic3.Biotic3Handler;
import BioticTypes.v3.FishstationType;
import BioticTypes.v3.MissionType;
import BioticTypes.v3.MissionsType;
import LandingsTypes.v1.LandingsdataType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.ObjectFactory;
import partialRDBES.v1_16.OnshoreeventType;
import partialRDBES.v1_16.SamplingdetailsType;

/**
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
     * @param biotic
     * @param landings
     * @param conversions
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

    public static void main(String[] args) throws JAXBException, XMLStreamException, ParserConfigurationException, ParserConfigurationException, SAXException, SAXException, IOException, FileNotFoundException, RDBESConversionException, StrataException {

        // make command line interface for conversion options for each source file so that e.g. CL can be created separately from samples and vice versa
        String pbpath = "/Users/a5362/bioticsets/filtered/pb_2016.xml";
        Biotic3Handler handler = new Biotic3Handler();
        MissionsType biotic = handler.read(new File(pbpath));

        RDBESCompiler compiler = new RDBESCompiler(biotic, null, null, 2016, false);

        compiler.addProveBat();
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
    protected void createCL() {
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
            for (OnshoreeventType os: d.getSamplingdetails().getOnshoreevent()){
                if (os.getOSid()>m){
                    m = os.getOSid();
                }
            }
        }
        return (m + 1);
    }

    private void setCommonDesign(DesignType design) {
        design.setDEid(getDesingId());
        design.setDErecordType("DE");
    }

    private void setCommonOnshoreEvent(OnshoreeventType os, FishstationType fs) {
        os.setOSid(getOnshoreEventId());
        os.setOSrecordType("OS");
        os.setOSnationalLocationName(fs.getLandingsite());
        os.setOSlocation(this.dataconfigurations.getLandingsSiteLoCode().get(fs.getLandingsite()));
        os.setOSsamplingDate(fs.getStationstartdate());
        os.setOSsamplingTime(fs.getStationstarttime());
    }

    //
    // Sasmpling prorgam specific methods
    //
    /**
     * Adds Port sampling program "Provebat" to RDBES
     */
    protected void addProveBat() throws RDBESConversionException, StrataException {

        DesignType pb = this.rdbesFactory.createDesignType();
        setCommonDesign(pb);
        this.rdbes.add(pb);

        //make configureable by year
        pb.setDEhierarchy("5");
        pb.setDEhierarchyCorrect("No");
        pb.setDEsamplingScheme(this.dataconfigurations.getMetaDataPb(this.year).get("samplingScheme"));
        pb.setDEstratum(this.dataconfigurations.getMetaDataPb(this.year).get("samplingFrame"));
        pb.setDEyear("" + this.year);
        addProvebatSamplingDetails(pb);

    }

    private void addProvebatSamplingDetails(DesignType design) throws RDBESConversionException, StrataException {
        SamplingdetailsType samplingdetails = this.rdbesFactory.createSamplingdetailsType();
        samplingdetails.setSDid(getSamplingDetailsId());
        samplingdetails.setSDcountry(this.dataconfigurations.getMetaDataPb(this.year).get("samplingFrame"));
        samplingdetails.setSDinstitution(this.dataconfigurations.getMetaDataPb(this.year).get("samplingInstitution"));
        samplingdetails.setSDrecordType("SD");
        samplingdetails.setDEid(design.getDEid());
        addProvebatOnshorevents(samplingdetails);
        design.setSamplingdetails(samplingdetails);
    }

    private void addProvebatOnshorevents(SamplingdetailsType samplingdetails) throws RDBESConversionException, StrataException {
        TemporalStrata strata = this.dataconfigurations.getPortStratificationPb(this.year);
        List<FishstationType> pbstations = getProveBatStations();
        Set<String> portdaysadded = new HashSet<>();
        for (FishstationType f : pbstations) {
            if (f.getLandingsite() == null || "".equals(f.getLandingsite())) {
                if (this.strict) {
                    throw new RDBESConversionException("Landing site id missing for hierarchy 5");
                } else {
                    this.log.print("Landing site id missing for hierarchy 5. Station skipped");
                }
            } else {
                OnshoreeventType os = this.rdbesFactory.createOnshoreeventType();
                setCommonOnshoreEvent(os, f);
                os.setSDid(samplingdetails.getSDid());
                os.setOSstratification(true);
                os.setOSstratum(strata.getStratum(f.getStationstartdate()).getName());
                os.setOSclustering("No");
                os.setOSsampler(this.dataconfigurations.getMetaDataPb(this.year).get("sampler"));
                os.setOSselectionMethod(this.dataconfigurations.getMetaDataPb(this.year).get("portselectionmethod"));
                os.setOSlocationType(this.dataconfigurations.getMetaDataPb(this.year).get("portlocationtype"));
                samplingdetails.getOnshoreevent().add(os);
            }
        }
        assert false: "set totals";
        // set totals for all onshorevents (count pr strata), merge from landings ?
    }

    private List<FishstationType> getProveBatStations() throws RDBESConversionException {
        List<FishstationType> pbstations = new ArrayList<>();
        for (MissionType m : this.biotic.getMission()) {
            if ("11".equals(m.getMissiontype())) {
                for (FishstationType s : m.getFishstation()) {
                    if (s.getStationstartdate() != null) {
                        if (this.strict) {
                            throw new RDBESConversionException("Startdate missing");
                        } else {
                            this.log.print("Startdate missing. Station skipped");
                        }
                    }
                    if (s.getStationstartdate().getYear() == this.year) {
                        pbstations.add(s);
                    }
                }
            }
        }
        return pbstations;
    }
}
