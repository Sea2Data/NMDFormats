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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.FishingtripType;
import partialRDBES.v1_16.OnshoreeventType;
import partialRDBES.v1_16.SamplingdetailsType;
import partialRDBES.v1_16.LandingeventType;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESprovebatCompiler extends RDBESCompiler {

    public static void main(String[] args) throws JAXBException, XMLStreamException, ParserConfigurationException, ParserConfigurationException, SAXException, SAXException, IOException, FileNotFoundException, RDBESConversionException, StrataException {

        // make command line interface for conversion options for each source file so that e.g. CL can be created separately from samples and vice versa
        String pbpath = "/Users/a5362/bioticsets/filtered/pb_2016.xml";
        String resourcepath = "/Users/a5362/code/github/NMDFormats/RDBES_resources";
        Biotic3Handler handler = new Biotic3Handler();
        MissionsType biotic = handler.read(new File(pbpath));

        RDBESprovebatCompiler compiler = new RDBESprovebatCompiler(biotic, null, new DataConfigurations(new File(resourcepath)), 2016, false);

        compiler.addProveBatAsH5();
    }

    public RDBESprovebatCompiler(MissionsType biotic, LandingsdataType landings, DataConfigurations conversions, int year, boolean strict) {
        super(biotic, landings, conversions, year, strict);
    }

    public RDBESprovebatCompiler(RDBESCompiler compiler) {
        super(compiler);
    }

    //
    // Sasmpling prorgam specific methods
    //
    /**
     * Adds Port sampling program "Provebat" to RDBES
     */
    protected void addProveBatAsH5() throws RDBESConversionException, StrataException, IOException {

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

    protected void addProvebatSamplingDetails(DesignType design) throws RDBESConversionException, StrataException, IOException {
        SamplingdetailsType samplingdetails = this.rdbesFactory.createSamplingdetailsType();
        setCommonSamplingDetails(samplingdetails);
        samplingdetails.setSDcountry(this.dataconfigurations.getMetaDataPb(this.year).get("samplingFrame"));
        samplingdetails.setSDinstitution(this.dataconfigurations.getMetaDataPb(this.year).get("samplingInstitution"));
        samplingdetails.setDEid(design.getDEid());
        addProvebatOnshorevents(samplingdetails);
        design.setSamplingdetails(samplingdetails);
    }

    protected void addProvebatOnshorevents(SamplingdetailsType samplingdetails) throws RDBESConversionException, StrataException, IOException {
        TemporalStrata strata = this.dataconfigurations.getPortStratificationPb(this.year);
        List<FishstationType> pbstations = getProveBatStations();
        Map<String, List<FishstationType>> stationsToAdd = new HashMap<>();
        Map<String, OnshoreeventType> onshoreadded = new HashMap<>();
        for (FishstationType f : pbstations) {
            if (f.getLandingsite() == null || "".equals(f.getLandingsite())) {
                if (this.strict) {
                    throw new RDBESConversionException("Landing site id missing for hierarchy 5");
                } else {
                    this.log.print("Landing site id missing for hierarchy 5. Station skipped");
                }
            } else {
                String portdayid = f.getLandingsite() + "/" + f.getStationstartdate().toString();
                if (!stationsToAdd.containsKey(portdayid)) {
                    stationsToAdd.put(portdayid, new LinkedList<>());
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
                    onshoreadded.put(portdayid, os);
                }
                stationsToAdd.get(portdayid).add(f);
            }
        }
        
        // count sampled port-days in strata
        Map<String, Set<String>> ports = new HashMap<>();
        for (OnshoreeventType os : samplingdetails.getOnshoreevent()) {
            if (!ports.containsKey(os.getOSstratum())) {
                ports.put(os.getOSstratum(), new HashSet<>());
            }
            ports.get(os.getOSstratum()).add(os.getOSnationalLocationName() + "/" + os.getOSsamplingDate().toString());
        }
        // set number of sampeld port-days in strata
        for (OnshoreeventType os : samplingdetails.getOnshoreevent()) {
            os.setOSsampled(ports.get(os.getOSstratum()).size());
        }
        
        // add landings
        for (String portday : onshoreadded.keySet()){
            addProveBatLandingEvents(onshoreadded.get(portday), stationsToAdd.get(portday));
        }
    }

    protected List<FishstationType> getProveBatStations() throws RDBESConversionException {
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
                    //skip samples where sample boat is fishing platform
                    if (s.getStationstartdate().getYear() == this.year && (s.getCatchplatform() == null ? ((MissionType)s.getParent()).getPlatform() != null : !s.getCatchplatform().equals(((MissionType)s.getParent()).getPlatform()))) {
                        pbstations.add(s);
                    }
                }
            }
        }
        return pbstations;
    }

    private void addProveBatLandingEvents(OnshoreeventType os, List<FishstationType> stations) throws StrataException, IOException, RDBESConversionException {
        GearStrata gearstrata = this.dataconfigurations.getLandingStratificationPb(this.year);
        for (FishstationType fs: stations){
            
            if (fs.getCatchplatform()==null){
                if (this.strict){
                    throw new RDBESConversionException("Vessel missing for landing event");
                }
                else{
                    this.log.print("Vessel missing for landing event. Station skipped");
                }
            }
            else{
                
                            
            LandingeventType landing = this.rdbesFactory.createLandingeventType();
            
            landing.setFishingtrip(getProveBatFishingTrip());
            landing.setFTid(landing.getFishingtrip().getFTid());
            
            landing.setVesseldetails(getVesselDetails(fs.getCatchplatform()));
            landing.setVDid(landing.getVesseldetails().getVDid());
            
            landing.setOSid(os.getOSid());
            os.getLandingevent().add(landing);
            
            landing.setLEstratification(true);
            landing.setLEsequenceNumber(fs.getSerialnumber().intValue());
            landing.setLEstratum(gearstrata.getStratum(fs.getGear()).getName());
            landing.setLEclustering("No");
            landing.setLEsampler(this.dataconfigurations.getMetaDataPb(year).get("sampler"));
            landing.setLEmixedTrip(0);
            landing.setLEcatchReg("Lan");
            landing.setLElocation(os.getOSlocation());
            landing.setLElocationType(os.getOSlocationType());
            landing.setLEcountry("NOR");
            landing.setLEdate(os.getOSsamplingDate());
            
            //continue with LEeconomicalZone
                
            }
        }
    }

    private FishingtripType getProveBatFishingTrip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
