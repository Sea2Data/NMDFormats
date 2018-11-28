/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import BioticTypes.v3.AgedeterminationType;
import BioticTypes.v3.CatchsampleType;
import BioticTypes.v3.FishstationType;
import BioticTypes.v3.IndividualType;
import BioticTypes.v3.MissionsType;
import HierarchicalData.HierarchicalData;
import HierarchicalData.RelationalConversion.DelimitedOutputWriter;
import HierarchicalData.RelationalConversion.NamingConventions.DoNothingNamingConvention;
import HierarchicalData.RelationalConversion.NamingConventions.ITableMakerNamingConvention;
import HierarchicalData.RelationalConversion.RelationalConvertionException;
import HierarchicalData.RelationalConversion.TableMaker;
import LandingsTypes.v1.LandingsdataType;
import XMLHandling.SchemaReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import partialRDBES.v1_16.BiologicalvariableType;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.FishingtripType;
import partialRDBES.v1_16.LandingeventType;
import partialRDBES.v1_16.ObjectFactory;
import partialRDBES.v1_16.OnshoreeventType;
import partialRDBES.v1_16.SampleType;
import partialRDBES.v1_16.SamplingdetailsType;
import partialRDBES.v1_16.SpecieslistdetailsType;
import partialRDBES.v1_16.SpeciesselectionType;
import partialRDBES.v1_16.VesseldetailsType;

/**
 * Base class for converting NMD / IMR data to to RDBES
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESCompiler {

    protected ObjectFactory rdbesFactory;
    protected List<HierarchicalData> rdbes;
    protected MissionsType biotic;
    protected LandingsdataType landings;
    protected DataConfigurations dataconfigurations;
    protected int year;
    protected boolean strict;
    protected PrintStream log;
    private Map<String, Integer> ids;

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
        this.ids = new HashMap<>();
        initids();
    }

    private void initids() {
        this.ids.put("DE", 1);
        this.ids.put("SD", 1);
        this.ids.put("OS", 1);
        this.ids.put("LE", 1);
        this.ids.put("SE", 1);
        this.ids.put("SS", 1);
        this.ids.put("SL", 1);
        this.ids.put("SA", 1);
        this.ids.put("BV", 1);
        this.ids.put("FS", 1);
        this.ids.put("VD", 1);
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
        this.ids = rdbesCompiler.ids;
    }

    /**
     * Generates table in the RDBES exchange format at in the directory
     * specified as outputpath
     *
     * @param outputpath
     */
    public void writeTables(File outputpath) throws JAXBException, ParserConfigurationException, ITableMakerNamingConvention.NamingException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, RelationalConvertionException, IOException {
        DelimitedOutputWriter writer = new DelimitedOutputWriter("\t", "\\", "#", ".csv", "");

        TableMaker tablemaker = new TableMaker(new SchemaReader(RDBESCompiler.class.getClassLoader().getResourceAsStream("partialRDBESv1_16.xsd")), new RDBESLeafNodeHandler());
        tablemaker.setNamingConvention(new DoNothingNamingConvention());

        Map<String, List<List<String>>> tables = tablemaker.getAllTables(new HierarchicalDataList(this.rdbes));
        tables.remove("HierarchicalDataList");
        writer.writeDelimitedFiles(tables, outputpath, tablemaker.getNamingConvention().getDescription());
        //skip CL etc if landings is null
        // use generic XML to relational conversion
        if (this.landings != null) {
            throw new UnsupportedOperationException("writing of CL not supported yet");
        }

    }

    /**
     * Creates CL table for RDBES
     */
    public void createCL() {
        throw new UnsupportedOperationException("Not implemented");
    }

    private int getID(String fieldcode) {
        int m = this.ids.get(fieldcode) + 1;
        this.ids.put(fieldcode, m);
        return m;
    }

    /**
     * get unused designid
     *
     * @return
     */
    private int getDesingId() {
        return getID("DE");
    }

    private int getSamplingDetailsId() {
        return getID("SD");
    }

    private int getOnshoreEventId() {
        return getID("OS");
    }

    private int getLandingEventId() {
        return getID("LE");
    }

    private int getSpeciesSelectionId() {
        return getID("SS");
    }

    private int getSpeciesListDetailsId() {
        return getID("SL");
    }

    private int getSampleId() {
        return getID("SA");
    }

    private int getBiologicalVariableId() {
        return getID("BV");
    }

    private int getFishingTripId() {
        return getID("FS");
    }

    private int getVEsselDetailsId() {
        return getID("VD");
    }

    protected SamplingdetailsType getSamplingDetails() throws RDBESConversionException, StrataException, IOException {
        SamplingdetailsType samplingdetails = this.rdbesFactory.createSamplingdetailsType();
        samplingdetails.setSDid(getSamplingDetailsId());
        samplingdetails.setSDrecordType("SD");
        return samplingdetails;
    }

    protected DesignType getDesign() {
        DesignType design = this.rdbesFactory.createDesignType();
        design.setDEid(getDesingId());
        design.setDErecordType("DE");
        return design;
    }

    protected OnshoreeventType getOnshoreEvent() throws IOException, RDBESConversionException {
        OnshoreeventType os = this.rdbesFactory.createOnshoreeventType();
        os.setOSid(getOnshoreEventId());
        os.setOSrecordType("OS");
        return os;
    }

    protected LandingeventType getLandingEvent() throws IOException, RDBESConversionException {
        LandingeventType le = this.rdbesFactory.createLandingeventType();
        le.setLEid(getLandingEventId());
        le.setLErecordType("LE");
        return le;
    }

    protected SpeciesselectionType getSpeciesSelection() {
        SpeciesselectionType speciesSelection = this.rdbesFactory.createSpeciesselectionType();
        speciesSelection.setSSid(getSpeciesSelectionId());
        speciesSelection.setSSrecordType("SS");
        return speciesSelection;
    }

    protected SpecieslistdetailsType getSpeciesSelectionDetails() throws IOException, RDBESConversionException {
        SpecieslistdetailsType specieslistdetails = this.rdbesFactory.createSpecieslistdetailsType();
        specieslistdetails.setSLid(getSpeciesListDetailsId());
        specieslistdetails.setSLrecordType("SL");
        specieslistdetails.setSlyear(this.year);

        return specieslistdetails;

    }

    protected SampleType getSample() {
        SampleType sample = this.rdbesFactory.createSampleType();
        sample.setSAid(getSampleId());
        sample.setSArecordType("SA");
        return sample;
    }

    protected BiologicalvariableType getBiologicalVariable() throws RDBESConversionException, IOException {
        BiologicalvariableType biovar = this.rdbesFactory.createBiologicalvariableType();
        biovar.setBVid(getBiologicalVariableId());
        biovar.setBVrecordType("BV");
        return biovar;
    }

    protected FishingtripType getFishingTrip() {
        FishingtripType ft = this.rdbesFactory.createFishingtripType();
        ft.setFTid(getFishingTripId());
        ft.setFTrecordType("FT");
        return ft;
    }

    protected VesseldetailsType getVesselDetails(String catchplatform) throws IOException, RDBESConversionException {
        VesseldetailsType vd = this.rdbesFactory.createVesseldetailsType();
        vd.setVDid(this.getVEsselDetailsId());
        vd.setVDrecordType("VD");
        vd.setVDencryptedCode(this.scramble_vessel(catchplatform));
        try{
            vd.setVDflagCountry(this.dataconfigurations.getVesselFlag(catchplatform));
        } catch (RDBESConversionException e){
            if (this.strict){
                throw new MandatoryFieldMissing("Missing mandatory vessel parameter VDflagCountry.");
            }
            else{
                this.log.println("Missing mandatory vessel parameter VDflagCountry. Skipping field."); 
            }
        }

        try {
            vd.setVDlength(this.dataconfigurations.getVesselLength(catchplatform));
            vd.setVDpower(this.dataconfigurations.getVesselPower(catchplatform));
            //vd.setVDsize(this.dataconfigurations.getVesselSize(catchplatform));
            //vd.setVDsizeUnit();
        } catch (RDBESConversionException e) {
            this.log.println("Missing non-mandatory vessel parameters. Skipping fields.");
        }

        if (vd.getVDlength()==null){
            if (strict) {
                throw new MandatoryFieldMissing("Missing mandatory filed VDlengthCategory.");
            } else {
                this.log.println("Missing mandatory vessel parameter VDlengthCategory. Skipping field.");
            }
            
 
        } else {
                       vd.setVDlengthCategory(getLengthCategory(vd.getVDlength()));
        }

        this.log.println("Fix size parameters");
        return vd;
    }

    protected void addLength(BiologicalvariableType biovar, IndividualType i, CatchsampleType cs) throws IOException, RDBESConversionException {
        biovar.setBVtype(dataconfigurations.getLengthMeasurement(cs.getLengthmeasurement()));
        double factor = dataconfigurations.getLengthFactor(i.getLengthresolution());
        biovar.setBVvalue("" + Math.round(i.getLength().doubleValue() * factor));
        biovar.setBVunitValue(this.dataconfigurations.getLengthUnit(i.getLengthresolution()));
        biovar.setBVsampled(countLengths((CatchsampleType) i.getParent()));

    }

    protected void addWeight(BiologicalvariableType biovar, IndividualType i) {
        biovar.setBVtype("Weight");
        biovar.setBVvalue(i.getIndividualweight().toString());
        biovar.setBVunitValue("kg");
        biovar.setBVsampled(countWeights((CatchsampleType) i.getParent()));
    }

    protected void addMaturation(BiologicalvariableType biovar, IndividualType i) {
        biovar.setBVtype("Maturation");
        biovar.setBVvalue(i.getMaturationstage());
        biovar.setBVunitValue("maturity scale");
        biovar.setBVunitScaleList("IMR code");
        biovar.setBVsampled(countMaturity((CatchsampleType) i.getParent()));
    }

    protected void addOtolithType(BiologicalvariableType biovar, IndividualType i, String sAspeciesCode) {
        AgedeterminationType age = getPrefferedAgeReading(i);
        biovar.setBVtype("Stock");
        biovar.setBVvalue(this.dataconfigurations.getOtolithType(sAspeciesCode).get(age.getOtolithtype()));
        biovar.setBVunitValue("stock list");
        biovar.setBVunitScaleList("ICES stock list");
        biovar.setBVsampled(countOtolithTypes((CatchsampleType) i.getParent()));
    }

    protected void addAge(BiologicalvariableType biovar, IndividualType i) {
        AgedeterminationType age = getPrefferedAgeReading(i);
        biovar.setBVtype("Age");
        biovar.setBVvalue(age.getAge().toString());
        biovar.setBVunitValue("years");
        biovar.setBVsampled(countAges((CatchsampleType) i.getParent()));
    }

    private int countLengths(CatchsampleType catchsampleType) {
        int lengths = 0;
        for (IndividualType i : catchsampleType.getIndividual()) {
            if (i.getLength() != null) {
                lengths++;
            }
        }
        return lengths;
    }

    private int countWeights(CatchsampleType catchsampleType) {
        int weights = 0;
        for (IndividualType i : catchsampleType.getIndividual()) {
            if (i.getIndividualweight() != null) {
                weights++;
            }
        }
        return weights;
    }

    private int countMaturity(CatchsampleType catchsampleType) {
        int mat = 0;
        for (IndividualType i : catchsampleType.getIndividual()) {
            if (i.getMaturationstage() != null) {
                mat++;
            }
        }
        return mat;
    }

    protected AgedeterminationType getPrefferedAgeReading(IndividualType i) {
        if (i.getAgedetermination().size() == 1) {
            return i.getAgedetermination().get(0);
        }
        AgedeterminationType pref = null;
        for (AgedeterminationType a : i.getAgedetermination()) {
            if (a.getAgedeterminationid().equals(i.getPreferredagereading())) {
                pref = a;
            }

        }
        return pref;
    }

    private int countOtolithTypes(CatchsampleType catchsampleType) {
        int ot = 0;
        for (IndividualType i : catchsampleType.getIndividual()) {
            if (getPrefferedAgeReading(i) != null && getPrefferedAgeReading(i).getOtolithtype() != null) {
                ot++;
            }
        }
        return ot;
    }

    private int countAges(CatchsampleType catchsampleType) {
        int ot = 0;
        for (IndividualType i : catchsampleType.getIndividual()) {
            if (getPrefferedAgeReading(i) != null && getPrefferedAgeReading(i).getAge() != null) {
                ot++;
            }
        }
        return ot;
    }

    /**
     * Creates consistent scrambling of vessel identifier, so that vessels can
     * be ided in database, but not connected to other sources
     *
     * @param id
     * @return
     */
    protected String scramble_vessel(String id) {
        this.log.println("Implement vessel scrambler");
        return id;
    }

    private String getLengthCategory(int vDlength) {
        this.log.println("Implement length classes");
        if (vDlength > 0 && vDlength < 12) {
            return "0-12";
        }
        throw new UnsupportedOperationException("Length class not defined");
    }
    
    /**
     * Returns code for target species for generating metier codes
     * @param fs
     * @return 
     */
    protected String getTargetSpecies(FishstationType fs) throws RDBESConversionException{
        throw new UnsupportedOperationException("Not supported. Override");
    }

}
