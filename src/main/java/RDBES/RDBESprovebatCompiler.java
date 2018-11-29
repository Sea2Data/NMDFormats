/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import Biotic.Biotic3.Biotic3Handler;
import BioticTypes.v3.CatchsampleType;
import BioticTypes.v3.FishstationType;
import BioticTypes.v3.IndividualType;
import BioticTypes.v3.MissionType;
import BioticTypes.v3.MissionsType;
import HierarchicalData.RelationalConversion.NamingConventions.ITableMakerNamingConvention;
import HierarchicalData.RelationalConversion.RelationalConvertionException;
import LandingsTypes.v1.LandingsdataType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import partialRDBES.v1_16.BiologicalvariableType;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.FishingtripType;
import partialRDBES.v1_16.OnshoreeventType;
import partialRDBES.v1_16.SamplingdetailsType;
import partialRDBES.v1_16.LandingeventType;
import partialRDBES.v1_16.SampleType;
import partialRDBES.v1_16.SpecieslistdetailsType;
import partialRDBES.v1_16.SpeciesselectionType;

/**
 * 2d0:
 * - handle specieslist configured by trip (identify bl.kveite-turer)
 * - handle unkown-codes in platforms
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESprovebatCompiler extends RDBESCompiler {

    protected SpecieslistdetailsType speciesselectiondetails;

    public static void main(String[] args) throws JAXBException, XMLStreamException, ParserConfigurationException, ParserConfigurationException, SAXException, SAXException, IOException, FileNotFoundException, RDBESConversionException, StrataException, ITableMakerNamingConvention.NamingException, NoSuchMethodException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, IllegalArgumentException, InvocationTargetException, RelationalConvertionException {

        // make command line interface for conversion options for each source file so that e.g. CL can be created separately from samples and vice versa
        String pbpath = "/Users/a5362/bioticsets/filtered/pb_2016.xml";
        String resourcepath = "/Users/a5362/code/github/NMDFormats/RDBES_resources";
        String outpath = "/Users/a5362/code/github/NMDFormats/RDBES_output";
        Biotic3Handler handler = new Biotic3Handler();
        MissionsType biotic = handler.read(new File(pbpath));

        RDBESprovebatCompiler compiler = new RDBESprovebatCompiler(biotic, null, new DataConfigurations(new File(resourcepath)), 2016, false);

        compiler.addProveBatAsH5();
        compiler.writeTables(new File(outpath));
    }

    public RDBESprovebatCompiler(MissionsType biotic, LandingsdataType landings, DataConfigurations conversions, int year, boolean strict) throws IOException, RDBESConversionException {
        super(biotic, landings, conversions, year, strict);
        this.speciesselectiondetails = getSpeciesSelectionDetails();
    }

    public RDBESprovebatCompiler(RDBESCompiler compiler) throws IOException, RDBESConversionException {
        super(compiler);
        if (this.speciesselectiondetails == null) {
            this.speciesselectiondetails = getSpeciesSelectionDetails();
        }
    }

    @Override
    protected SamplingdetailsType getSamplingDetails() throws RDBESConversionException, StrataException, IOException {
        SamplingdetailsType samplingdetails = super.getSamplingDetails();
        samplingdetails.setSDcountry(this.dataconfigurations.getMetaDataPb(this.year, "samplingFrame"));
        samplingdetails.setSDinstitution(this.dataconfigurations.getMetaDataPb(this.year, "samplingInstitution"));
        addChild(samplingdetails, this.speciesselectiondetails);
        addProvebatOnshorevents(samplingdetails);

        return samplingdetails;

    }

    @Override
    protected OnshoreeventType getOnshoreEvent() throws IOException, RDBESConversionException {
        OnshoreeventType os = super.getOnshoreEvent();

        os.setOSclustering("No");
        os.setOSsampler(this.dataconfigurations.getMetaDataPb(this.year, "sampler"));
        os.setOSselectionMethod(this.dataconfigurations.getMetaDataPb(this.year, "portselectionmethod"));
        os.setOSlocationType(this.dataconfigurations.getMetaDataPb(this.year, "portlocationtype"));
        return os;
    }

    @Override
    protected SpecieslistdetailsType getSpeciesSelectionDetails() throws IOException, RDBESConversionException {
        SpecieslistdetailsType specieslistdetails = super.getSpeciesSelectionDetails();
        specieslistdetails.setSLlistName("Port sampling species list (provebat)" + this.year);
        specieslistdetails.setSLspeciesCode(this.dataconfigurations.getMetaDataPb(this.year, "species"));
        specieslistdetails.setSLcatchFraction("Lan");
        return specieslistdetails;
    }

    @Override
    protected LandingeventType getLandingEvent() throws IOException, RDBESConversionException {
        LandingeventType landing = super.getLandingEvent();
        landing.setLEclustering("No");
        landing.setLEsampler(this.dataconfigurations.getMetaDataPb(year, "sampler"));
        landing.setLEmixedTrip(0);
        landing.setLEcatchReg("Lan");
        landing.setLEcountry("NOR");
        landing.setLEselectionMethod(this.dataconfigurations.getMetaDataPb(this.year, "landingselectionmethod"));
        landing.setLEclustering("No");
        landing.setLEfullTripAvailable("Yes");
        return landing;
    }

    @Override
    protected SpeciesselectionType getSpeciesSelection() {
        SpeciesselectionType speciesSelection = super.getSpeciesSelection();
        speciesSelection.setSLid(this.speciesselectiondetails.getSLid());
        speciesSelection.setSSstratification(false);
        speciesSelection.setSScatchCategory("Lan");
        speciesSelection.setSSclustering("No");
        speciesSelection.setSSsampled(this.speciesselectiondetails.getSLspeciesCode().split(",").length);
        speciesSelection.setSStotal(this.speciesselectiondetails.getSLspeciesCode().split(",").length);
        speciesSelection.setSSselectionMethod("census");

        return speciesSelection;
    }

    @Override
    protected FishingtripType getFishingTrip() {
        FishingtripType ft = super.getFishingTrip();
        return ft;
    }

    @Override
    protected SampleType getSample() {
        SampleType sample = super.getSample();
        return sample;
    }

    @Override
    protected BiologicalvariableType getBiologicalVariable() throws RDBESConversionException, IOException {
        BiologicalvariableType biovar = super.getBiologicalVariable();
        biovar.setBVsampler(this.dataconfigurations.getMetaDataPb(year, "sampler"));
        biovar.setBVstratification(false);
        biovar.setBVselectionMethod("census");
        return biovar;
    }

    @Override
    protected String getTargetSpecies(FishstationType fs) throws RDBESConversionException {
        return "DEM"; // move to config ?
    }

    /**
     * Adds Port sampling program "Provebat" to RDBES
     */
    protected void addProveBatAsH5() throws RDBESConversionException, StrataException, IOException {

        DesignType pb = super.getDesign();

        //make configureable by year
        pb.setDEhierarchy("5");
        pb.setDEhierarchyCorrect("No");
        pb.setDEsamplingScheme(this.dataconfigurations.getMetaDataPb(this.year, "samplingScheme"));
        pb.setDEstratum(this.dataconfigurations.getMetaDataPb(this.year, "samplingFrame"));
        pb.setDEyear("" + this.year);
        SamplingdetailsType sd = this.getSamplingDetails();
        addChild(pb, sd);
        addChild(this.rdbes, pb);
    }

    private void addProvebatOnshorevents(SamplingdetailsType samplingdetails) throws RDBESConversionException, StrataException, IOException {
        TemporalStrata strata = this.dataconfigurations.getPortStratificationPb(this.year);
        List<FishstationType> pbstations = getProveBatStations();
        Map<String, List<FishstationType>> stationsToAdd = new HashMap<>();
        Map<String, OnshoreeventType> onshoreadded = new HashMap<>();
        for (FishstationType f : pbstations) {
            if (f.getLandingsite() == null || "".equals(f.getLandingsite())) {
                if (this.strict) {
                    throw new MissingKeyException("Landing site id missing for hierarchy 5");
                } else {
                    this.log.println("Landing site id missing for hierarchy 5. Station skipped.");
                }
            } else {
                String portdayid = f.getLandingsite() + "/" + f.getStationstartdate().toString();
                if (!stationsToAdd.containsKey(portdayid)) {
                    stationsToAdd.put(portdayid, new LinkedList<>());
                    OnshoreeventType os = getOnshoreEvent();
                    os.setOSstratification(true);
                    os.setOSstratum(strata.getStratum(f.getStationstartdate()).getName());
                    os.setOSnationalLocationName(f.getLandingsite());
                    try {
                        os.setOSlocation(this.dataconfigurations.getLandingsSiteLoCode(f.getLandingsite()));
                    } catch (RDBESConversionException e) {
                        if (this.strict) {
                            throw new MandatoryFieldMissing("No to locode found for landingssite: " + f.getLandingsite());
                        } else {
                            this.log.println("No to locode found for landingssite: " + f.getLandingsite() + "skipping field");
                        }
                    }
                    os.setOSsamplingDate(f.getStationstartdate());

                    addChild(samplingdetails, os);
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
        for (String portday : onshoreadded.keySet()) {
            addProveBatLandingEvents(onshoreadded.get(portday), stationsToAdd.get(portday));
        }
    }

    private void addProveBatLandingEvents(OnshoreeventType os, List<FishstationType> stations) throws StrataException, IOException, RDBESConversionException {
        GearStrata gearstrata = this.dataconfigurations.getLandingStratificationPb(this.year);
        for (FishstationType fs : stations) {

            if (fs.getCatchplatform() == null) {
                missingMandatoryField("LandingEvent", "Catchplatform");

            } else {

                LandingeventType landing = getLandingEvent();
                try {
                    landing.setLElocation(this.dataconfigurations.getLandingsSiteLoCode(fs.getLandingsite()));
                } catch (RDBESConversionException e) {
                    this.log.println("Missing field LElocation (landingsite: " + fs.getLandingsite() + "). Skipping field.");
                }

                landing.setVesseldetails(getVesselDetails(fs.getCatchplatform()));
                landing.setVDid(landing.getVesseldetails().getVDid());
                landing.setLEstratification(true);
                landing.setLEsequenceNumber(fs.getSerialnumber().intValue());
                landing.setLEstratum(gearstrata.getStratum(fs.getGear()).getName());
                landing.setLElocation(os.getOSlocation());
                landing.setLElocationType(os.getOSlocationType());
                landing.setLEdate(os.getOSsamplingDate());
                try {
                    String ices3 = this.dataconfigurations.getICES3(fs.getLatitudestart(), fs.getLongitudestart());
                    landing.setLEarea(ices3);
                } catch (RDBESConversionException e) {
                    try {
                        if (fs.getSystem() == null || !"2".equals(fs.getSystem()) || fs.getArea() == null) {
                            missingMandatoryField("LEarea", "System");
                        } else {
                            String ices3 = this.dataconfigurations.getHomrICES3(fs.getArea());
                            landing.setLEarea(ices3);
                        }
                    } catch (RDBESConversionException e2) {
                        if (this.strict) {
                            throw new MandatoryFieldMissing("Missing mandatory field LEarea.");
                        } else {
                            this.log.println("Missing mandatory field LEarea (area: " + fs.getArea() + "). skipping field.");
                        }
                    }
                }

                try {
                    landing.setLEgear(this.dataconfigurations.getImrGearFAO(fs.getGear()));
                } catch (RDBESConversionException e) {
                    missingMandatoryField("LEgear", "Gear");
                }
                try {
                    landing.setLEmeshSize(this.dataconfigurations.getImrGearMeshSize(fs.getGear()));
                } catch (RDBESConversionException e) {

                }
                try {
                    landing.setLEselectionDevice(this.dataconfigurations.getImrGearSelDev(fs.getGear()));
                } catch (RDBESConversionException e) {

                }
                try {
                    if (landing.getLEselectionDevice() != 0) {
                        landing.setLEselectionDeviceMeshSize(this.dataconfigurations.getImrGearSelDevMeshSize(fs.getGear()));
                    }

                } catch (RDBESConversionException | NullPointerException e) {

                }
                try {
                    landing.setLEtargetSpecies(this.getTargetSpecies(fs));
                } catch (RDBESConversionException e) {

                }

                try {
                    landing.setLEmetier6(this.dataconfigurations.getImrGearMetier6(this.dataconfigurations.getImrGearFAO(fs.getGear()), landing.getLEtargetSpecies(), this.dataconfigurations.getImrGearMeshSize(fs.getGear()), this.dataconfigurations.getImrGearSelDev(fs.getGear()), this.dataconfigurations.getImrGearSelDevMeshSize(fs.getGear())));
                } catch (RDBESConversionException e) {
                    missingMandatoryField("LEgear", "gear parameters");
                }
                addFishingTrip(landing, fs);
                addSpeciesSelection(landing, fs);

                addChild(os, landing);
            }
        }

        Map<String, Integer> sampled = new HashMap<>();
        for (LandingeventType e : os.getLandingevent()) {
            if (!sampled.containsKey(e.getLEstratum())) {
                sampled.put(e.getLEstratum(), 1);
            } else {
                sampled.put(e.getLEstratum(), sampled.get(e.getLEstratum()) + 1);
            }
        }
        for (LandingeventType e : os.getLandingevent()) {
            e.setLEsampled(sampled.get(e.getLEstratum()));
        }

        // add total in strata from landings ?
    }

    private void addSpeciesSelection(LandingeventType landing, FishstationType fs) throws IOException, RDBESConversionException {
        SpeciesselectionType speciesSelection = getSpeciesSelection();

        Map<String, List<CatchsampleType>> samples_by_species = new HashMap<>();

        //gather catchsamples by species, exclude those not in specieslist
        for (CatchsampleType cs : fs.getCatchsample()) {
            if (this.speciesselectiondetails.getSLspeciesCode().contains(cs.getAphia())) {
                if (!samples_by_species.containsKey(cs.getAphia())) {
                    samples_by_species.put(cs.getAphia(), new LinkedList<>());
                }
                samples_by_species.get(cs.getAphia()).add(cs);

            }
        }

        for (String species : samples_by_species.keySet()) {
            List<CatchsampleType> samples = samples_by_species.get(species);
            addSample(speciesSelection, samples);
        }

        addChild(landing, speciesSelection);

    }

    private void addSample(SpeciesselectionType speciesSelection, List<CatchsampleType> samples) throws IOException, RDBESConversionException {

        if (samples.size() == 1) {
            addLeafSample(speciesSelection, samples.get(0));
        } else {
            throw new UnsupportedOperationException("Delpr not implemented");
        }

    }

    private void addLeafSample(SpeciesselectionType speciesSelection, CatchsampleType catchsample) throws IOException, RDBESConversionException {
        SampleType sample = this.getSample();
        if (((catchsample.getLengthsamplecount() == null || catchsample.getLengthsamplecount().intValue() == 0) && !catchsample.getIndividual().isEmpty()) || (catchsample.getLengthsamplecount() != null && catchsample.getLengthsamplecount().intValue() != catchsample.getIndividual().size())) {
            error("Mismatch between registered individuals and noted sample size (" + ((FishstationType) catchsample.getParent()).getSerialnumber() + "/" + catchsample.getCatchsampleid() + ").");
            sample.setSAreasonNotSampledBV("Incomplete registration");
            addChild(speciesSelection, sample);
            return;

        }
        if ("1".equals(catchsample.getCatchproducttype())) {
            // do not set factor for type 1, as weight is taken from sales nots which often contain converted weights.
            sample.setSAtotalWeightLive(Math.round(1000 * catchsample.getCatchweight().floatValue()));
        } else {
            try {
                double factor = this.dataconfigurations.getScalingFactor(catchsample.getAphia(), catchsample.getCatchproducttype(), "1");
                if (catchsample.getCatchweight() != null) {
                    sample.setSAconversionFactorMesLive(factor);
                    sample.setSAtotalWeightMeasured((int) Math.round(1000 * catchsample.getCatchweight().floatValue()));
                    sample.setSAtotalWeightLive((int) Math.round(1000 * catchsample.getCatchweight().floatValue() * factor));
                } else {
                    missingField("SAtotalWeightLive", "Catchweight");
                }

            } catch (RDBESConversionException e) {
                missingField("SAtotalWeightLive", "Catchproducttype");
            }
        }

        if (catchsample.getCatchproducttype() != null && catchsample.getCatchproducttype().equals(catchsample.getSampleproducttype())) {
            if ("1".equals(catchsample.getSampleproducttype())) {
                if (catchsample.getLengthsampleweight() != null) {
                    sample.setSAsampleWeightMeasured(Math.round(1000 * catchsample.getLengthsampleweight().floatValue()));
                    sample.setSAsampleWeightLive(Math.round(1000 * catchsample.getLengthsampleweight().floatValue()));
                } else {
                    missingField("SAsampleWeightLive", "Lengthsampleweight");
                }
            } else {
                try {
                    double factor = this.dataconfigurations.getScalingFactor(catchsample.getAphia(), catchsample.getSampleproducttype(), "1");
                    if (catchsample.getLengthsampleweight() != null) {
                        sample.setSAsampleWeightMeasured(Math.round(1000 * catchsample.getLengthsampleweight().floatValue()));
                        sample.setSAsampleWeightLive((int) Math.round(1000 * catchsample.getLengthsampleweight().floatValue() * factor));
                    } else {
                        missingField("SAsampledWeightLive", "Lengthsampleweight");
                    }

                } catch (RDBESConversionException e) {
                    missingField("SAsampledWeightLive", "Sampleproducttype");
                }
            }
        } else {
            error("Could not set SAsampleWeightLive because Sampleproducttype is different from Catchproducttype");
        }

        if (catchsample.getSampleproducttype() == null) {
            missingMandatoryField("SApresentation", "Sampleproducttype");
        } else {
            sample.setSApresentation(this.dataconfigurations.getPresentation(catchsample.getSampleproducttype()));
        }

        sample.setSAstratification(false);
        sample.setSAspeciesCode(catchsample.getAphia());

        sample.setSAcatchCategory("Lan");
        sample.setSAsex("U");
        sample.setSAunitType("number");

        sample.setSAselectionMethod(this.dataconfigurations.getMetaDataPb(year, "fishselectionmethod"));
        sample.setSAsampler(this.dataconfigurations.getMetaDataPb(year, "sampler"));
        if (catchsample.getIndividual().size() > 0 && catchsample.getLengthsampleweight() != null) {
            double totals = catchsample.getCatchweight().doubleValue() * catchsample.getLengthsampleweight().doubleValue() / catchsample.getIndividual().size();
            sample.setSAtotal(totals);
            sample.setSAsampled(catchsample.getLengthsamplecount().doubleValue());

        } else {
            if (strict) {
                throw new RDBESConversionException("Could not set design parameters for sample");
            } else {
                this.log.println("Could not set design parameters for sample. Skipping SAtotal and SAsampled");
            }
        }
        if (catchsample.getIndividual().size() == 0) {
            sample.setSAreasonNotSampledBV("Access");
        } else {
            addBiologicalVariables(sample, catchsample);
        }
        sample.setSAlowerHierarchy("C");

        addChild(speciesSelection, sample);
    }

    private void addBiologicalVariables(SampleType sample, CatchsampleType cs) throws RDBESConversionException, IOException {
        List<IndividualType> individuals = cs.getIndividual();
        for (IndividualType i : individuals) {

            if (i.getLength() != null) {
                BiologicalvariableType biovar = this.getBiologicalVariable();
                biovar.setBVfishID(i.getSpecimenid().intValue());
                biovar.setBVtotal(individuals.size());
                addLength(biovar, i, cs);
                addChild(sample, biovar);
            } else {
                throw new RDBESConversionException("Individual without length");
            }
            if (i.getIndividualweight() != null) {
                BiologicalvariableType biovar = this.getBiologicalVariable();
                biovar.setBVfishID(i.getSpecimenid().intValue());
                biovar.setBVtotal(individuals.size());
                addWeight(biovar, i);
                addChild(sample, biovar);
            }
            if (i.getMaturationstage() != null) {
                BiologicalvariableType biovar = this.getBiologicalVariable();
                biovar.setBVfishID(i.getSpecimenid().intValue());
                biovar.setBVtotal(individuals.size());
                addMaturation(biovar, i);
                addChild(sample, biovar);
            }
            if (getPrefferedAgeReading(i) != null && getPrefferedAgeReading(i).getOtolithtype() != null) {
                BiologicalvariableType biovar = this.getBiologicalVariable();
                biovar.setBVfishID(i.getSpecimenid().intValue());
                biovar.setBVtotal(individuals.size());
                addOtolithType(biovar, i, sample.getSAspeciesCode());
                addChild(sample, biovar);
            }
            if (getPrefferedAgeReading(i) != null && getPrefferedAgeReading(i).getAge() != null) {
                BiologicalvariableType biovar = this.getBiologicalVariable();
                biovar.setBVfishID(i.getSpecimenid().intValue());
                biovar.setBVtotal(individuals.size());
                addAge(biovar, i);
                addChild(sample, biovar);
            }

        }
    }

    protected List<FishstationType> getProveBatStations() throws RDBESConversionException {
        List<FishstationType> pbstations = new ArrayList<>();
        for (MissionType m : this.biotic.getMission()) {
            if ("11".equals(m.getMissiontype())) {
                if (m.getMissionstartdate().getYear() == this.year) {
                    if (m.getMissionstopdate().getYear() != this.year) {
                        if (this.strict) {
                            throw new RDBESConversionException("Sampling trip crossing years");
                        } else {
                            this.log.println("Sampling trip crossing years. Mission skipped.");
                        }
                    }
                    for (FishstationType s : m.getFishstation()) {
                        //skip samples where sample boat is fishing platform
                        if ((s.getCatchplatform() != null && m.getPlatform() != null && s.getCatchplatform().equals(m.getPlatform()))) {
                            this.log.println("Station vessel same as mission vessel for sampling boat. Skipping station.");
                        } else {
                            pbstations.add(s);
                        }
                    }
                }
            }
        }
        return pbstations;
    }

    private void addFishingTrip(LandingeventType landing, FishstationType fs) {

        FishingtripType ft = getFishingTrip();
        ft.setFTnationalCode(scramble_vessel(fs.getCatchplatform()) + "/" + fs.getStationstartdate().toString());
        ft.setFTstratification(false);
        ft.setFTclustering("No");
        ft.setFTarrivalLocation(landing.getLElocation());
        ft.setFTarrivalDate(landing.getLEdate());

        addChild(landing, ft);
    }

    private void missingField(String rdbesfield, String sourcefield) throws RDBESConversionException {
        this.log.println("Skippping field: " + rdbesfield + ", because " + sourcefield + "is missing.");
    }

    private void missingMandatoryField(String rdbesfield, String sourcefield) throws MandatoryFieldMissing {
        if (this.strict) {
            throw new MandatoryFieldMissing("Could not fill: " + rdbesfield + ", because " + sourcefield + " is missing or has incompatible value.");
        } else {
            this.log.println("Skipping: " + rdbesfield + ", because " + sourcefield + " is missing or has incompatible value.");
        }
    }

    private void error(String message) throws MandatoryFieldMissing {
        if (this.strict) {
            throw new MandatoryFieldMissing(message);
        } else {
            this.log.println(message);
        }
    }

}
