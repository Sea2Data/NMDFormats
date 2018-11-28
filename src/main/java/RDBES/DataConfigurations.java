/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class DataConfigurations {

    File resourcefilepath;
    Map<String, String> landingssites = null;
    Map<Integer, Map<String, String>> metaDataPb;
    Map<String, String> homrices3 = null;
    Map<String, String> gearfao = null;
    Map<String, String> gearTargetSpecies = null;
    Map<String, String> gearMeshSize = null;
    Map<String, String> gearSelDev = null;
    Map<String, String> gearSelDevMeshSize = null;
    Map<String, String> presentation = null;
    Map<String, String> scalingfactors = null;
    Map<String, String> maturity = null;
    Map<String, Map<String, String>> otolithtype = null;
    Map<String, Map<String, String>> scalingfactor = null;
    Map<String, String> gearImrToFao = null;
    Map<Integer, GearStrata> landingsstratificationpb = null;
    Map<String, String> vesselflag = null;
    Map<String, String> vessellength = null;
    Map<String, String> vesselpower = null;
    Map<String, String> vesselsize = null;
    Map<String, String> lengthmeasurement = null;
    Map<String, String> lengthunit = null;

    /**
     * @param resourcefiles path to location for resource files
     */
    public DataConfigurations(File resourcefiles) {
        this.metaDataPb = new HashMap<>();
        this.landingsstratificationpb = new HashMap<>();
        this.otolithtype = new HashMap<>();
        this.scalingfactor = new HashMap<>();
        this.resourcefilepath = resourcefiles;
    }

    /**
     * Loads the named resource file from the resourcefile location (set in
     * constructor) Resource file should be a tab delimited file with two
     * columns, where the values of the first column is unique
     *
     * @param filename
     * @return map from values in first column of resource file to values in
     * second column
     */
    protected Map<String, String> loadResourceFile(String filename) throws IOException {
        File infile = new File(this.resourcefilepath.toString() + File.separator + filename);
        InputStream resourcefile = new FileInputStream(infile);
        BufferedReader br = new BufferedReader(new InputStreamReader(resourcefile));
        Map<String, String> resources = new HashMap<>();

        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            } else if (line.length() == 0) {
                continue;
            } else {
                String[] map = line.split("\t");
                if (map.length != 2) {
                    throw new IOException("Format error in resource file:" + line);
                }
                resources.put(map[0].trim(), map[1].trim());
            }
        }
        return resources;
    }

    /**
     * Mapping IMR location code for landing site to LOCODE
     *
     * @return
     */
    public String getLandingsSiteLoCode(String imrCode) throws IOException, RDBESConversionException {
        if (this.landingssites == null) {
            this.landingssites = loadResourceFile("landingssite.csv");
        }
        String locode = this.landingssites.get(imrCode);
        if (locode == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return locode;
    }

    public String getMetaDataPb(int year, String field) throws IOException, RDBESConversionException {
        if (this.metaDataPb.get(year) == null) {
            this.metaDataPb.put(year, loadResourceFile(year + File.separator + "provebat_metadata.txt"));
        }

        String value = this.metaDataPb.get(year).get(field);
        if (value == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return value;
    }

    protected TemporalStrata getQstrat() throws StrataException {
        List<TemporalStratum> s = new ArrayList<>();
        TemporalStratum q1 = new TemporalStratum("Q1");
        q1.addRange(1, 91);
        TemporalStratum q2 = new TemporalStratum("Q2");
        q2.addRange(92, 182);
        TemporalStratum q3 = new TemporalStratum("Q3");
        q3.addRange(183, 274);
        TemporalStratum q4 = new TemporalStratum("Q4");
        q4.addRange(275, 366);

        s.add(q1);
        s.add(q2);
        s.add(q3);
        s.add(q4);

        return new TemporalStrata(s);
    }

    public TemporalStrata getPortStratificationPb(int year) throws StrataException, IOException, RDBESConversionException {

        if (this.getMetaDataPb(year, "portstrata").trim().equals("quarter")) {
            return getQstrat();
        } else {
            throw new UnsupportedOperationException("Stratasystem " + this.getMetaDataPb(year, "portstrata") + "not supported.");
        }

    }

    public GearStrata getLandingStratificationPb(int year) throws IOException, StrataException {
        if (this.landingsstratificationpb.get(year) == null) {
            this.landingsstratificationpb.put(year, makeGearStrata(this.loadResourceFile(year + File.separator + "gearstrata_pb.csv")));
        }
        return this.landingsstratificationpb.get(year);
    }

    protected GearStrata makeGearStrata(Map<String, String> gearstrata) throws StrataException {
        Map<String, Set<String>> ss = new HashMap<>();
        for (String imrgear : gearstrata.keySet()) {
            if (!ss.containsKey(gearstrata.get(imrgear))) {
                ss.put(gearstrata.get(imrgear), new HashSet<>());
            }

            ss.get(gearstrata.get(imrgear)).add(imrgear);
        }
        List<GearStratum> gss = new LinkedList<>();
        for (String strataname : ss.keySet()) {
            gss.add(new GearStratum(strataname, new HashSet<>(), ss.get(strataname)));
        }
        return new GearStrata(gss);
    }

    public String getHomrICES3(String homr) throws IOException, RDBESConversionException {
        if (this.homrices3 == null) {
            this.homrices3 = loadResourceFile("homr_ices3.csv");
        }
        String ices3 = this.homrices3.get(String.format("%02d", Integer.parseInt(homr)));
        if (ices3 == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return ices3;
    }
    
        /**
     * Looks up ICES level 3 rectangles from position
     * @param latitudestart
     * @param longitudestart
     * @return 
     */
    String getICES3(BigDecimal latitudestart, BigDecimal longitudestart) throws RDBESConversionException {
        throw new RDBESConversionException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Maps 4 digit imr gear code to FAO 1980 code
     *
     * @param imrGear
     * @return
     * @throws IOException
     */
    public String getImrGearFAO(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearfao == null) {
            this.gearfao = loadResourceFile("imrgear_FAO.csv");
        }
        String fao = this.gearfao.get(imrGear);
        if (fao == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return fao;
    }

    /**
     * Reads mesh size for gear
     * return null if mesh size is not relevant, and throws exception if no mapping is found. This means  that all gears must be added to resource file, and left with a blank mesh-size if mesh-size is not relevant
     * @param imrGear
     * @return
     * @throws IOException
     * @throws RDBESConversionException if no mapping is found for gear
     */
    public Integer getImrGearMeshSize(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearMeshSize == null) {
            this.gearMeshSize = loadResourceFile("imrgear_meshsize.csv");
        }
        String ms = this.gearMeshSize.get(imrGear);
        if (ms == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        if (ms.equals("")){
            return null;
        }
        return Integer.parseInt(ms);
    }

    /**
     * Maps gear to selection device mounted.
     * 
     * @param imrGear
     * @return
     * @throws IOException
     * @throws RDBESConversionException 
     */
    public int getImrGearSelDev(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearSelDev == null) {
            this.gearSelDev = loadResourceFile("imrgear_seldev.csv");
        }
        String gsd = this.gearSelDev.get(imrGear);
        if (gsd == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return Integer.parseInt(gsd);
    }

    /**
     * Returns Mesh size for selectivity device
     * throws exception if mapping not found
     * returns null if no selectivitydevice is mapped for this gear
     * @param imrGear
     * @return
     * @throws IOException
     * @throws RDBESConversionException 
     */
    public Integer getImrGearSelDevMeshSize(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearSelDevMeshSize == null) {
            this.gearSelDevMeshSize = loadResourceFile("imrgear_seldevmeshsize.csv");
        }
        String gsd = this.gearSelDevMeshSize.get(imrGear);
        if (gsd == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        if (gsd.equals("")){
            return null;
        }
        return Integer.parseInt(gsd);
    }

    /**
     * Constructs metier level 6 code. 
     * Assumes information is complete. E.g. null for mesh size, implies that mesh size is not relevant for gear.
     * @param gear FAO code
     * @param target 
     * @param mesh_size 
     * @param seldev
     * @param seldelvmeshsize
     * @return 
     */
    public String getImrGearMetier6(String gear, String target, Integer mesh_size, Integer seldev, Integer seldelvmeshsize) throws RDBESConversionException{
        
        if (gear==null){
            throw new RDBESConversionException("No gear provided");
        }
        if (target==null){
            throw new RDBESConversionException("No target species provided");
        }
        
        if (seldev==null){
            seldev = 0;
        }
        if (seldelvmeshsize==null){
            seldelvmeshsize=0;
        }
        if (mesh_size != null){
            return gear + "_" + target + "_>=" + mesh_size + "_" + seldev + "_" + seldelvmeshsize;
        }
        else{
            return gear + "_" + target + "_" + seldev + "_" + seldelvmeshsize;
        }

    }

    public String getPresentation(String sampleproducttype) throws IOException, RDBESConversionException {
        if (this.presentation == null) {
            this.presentation = loadResourceFile("imrProductype_presentation.csv");
        }
        String pres = this.presentation.get(sampleproducttype);
        if (pres == null) {
            throw new RDBESConversionException("No mapping found for code: " + sampleproducttype);
        }
        return pres;
    }

    public double getScalingFactor(String aphia, String fromCode, String toCode) throws IOException, RDBESConversionException {
        if (this.scalingfactor.get(aphia)==null){
            this.scalingfactor.put(aphia, loadResourceFile("presentationfactors" + File.separator + aphia+ ".csv"));
        }
        String sf = this.scalingfactor.get(aphia).get(fromCode+"-"+toCode);
        if (sf==null){
            throw new RDBESConversionException("No mapping found for code: " + fromCode+"-"+toCode + ", and species:" + aphia);
        }
        
        return Double.parseDouble(sf);
    }

    /**
     * Factor for converting lengths to the unit returned by
     * getLengthUnit(lengthresolution)
     *
     * @param lengthresolution
     * @return
     */
    public double getLengthFactor(String lengthresolution) throws RDBESConversionException, IOException {
        String unit = getLengthUnit(lengthresolution);
        
        if (unit.equals("1cm")){
            return 1e2;
        }
        else if (unit.equals("1mm")){
            return 1e3;
        }
        else{
            throw new RDBESConversionException("No conversion factor defined for unit:" + unit);
        }
    }

    public String getLengthUnit(String lengthresolution) throws IOException, RDBESConversionException {
        if (this.lengthunit == null) {
            this.lengthunit = loadResourceFile("lengthunit.csv");
        }
        String lm = this.lengthunit.get(lengthresolution);
        if (lm == null) {
            throw new RDBESConversionException("No mapping found for code: " + lengthresolution);
        }
        return lm;
    }
    
    public String getLengthMeasurement(String lengthmeasurement) throws IOException, RDBESConversionException {
        if (this.lengthmeasurement == null) {
            this.lengthmeasurement = loadResourceFile("lengthmeasurement.csv");
        }
        String lm = this.lengthmeasurement.get(lengthmeasurement);
        if (lm == null) {
            throw new RDBESConversionException("No mapping found for code: " + lengthmeasurement);
        }
        return lm;
    }


    public String getMaturity(String imrMaturity) throws IOException, RDBESConversionException {
        if (this.maturity == null) {
            this.maturity = loadResourceFile("maturity.csv");
        }
        String mat = this.maturity.get(imrMaturity);
        if (mat == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return mat;
    }

    public String getOtolithType(String aphia, String type) throws IOException, RDBESConversionException {
        if (this.otolithtype.get(aphia)==null){
            this.otolithtype.put(aphia, loadResourceFile("otolithtypes" + File.separator + aphia+ ".csv"));
        }
        String ot = this.otolithtype.get(aphia).get(type);
        if (ot==null){
            throw new RDBESConversionException("No mapping found for code: " + type + ", and species:" + aphia);
        }
        
        return ot;
    }

    public String getVesselFlag(String catchplatform) throws IOException, RDBESConversionException {
        if (this.vesselflag == null) {
            this.vesselflag = loadResourceFile("platform_flag.csv");
        }
        String flag = this.vesselflag.get(catchplatform);
        if (flag == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return flag;
    }

    public int getVesselLength(String catchplatform) throws IOException, RDBESConversionException {
        if (this.vessellength == null) {
            this.vessellength = loadResourceFile("platform_length.csv");
        }
        String length = this.vessellength.get(catchplatform);
        if (length == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return Integer.parseInt(length);
    }

    public int getVesselPower(String catchplatform) throws IOException, RDBESConversionException {
        if (this.vesselpower == null) {
            this.vesselpower = loadResourceFile("platform_power.csv");
        }
        String power = this.vesselpower.get(catchplatform);
        if (power == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return Integer.parseInt(power);
    }

    public int getVesselSize(String catchplatform) throws IOException, RDBESConversionException {
        if (this.vesselsize == null) {
            this.vesselsize = loadResourceFile("platform_size.csv");
        }
        String power = this.vesselsize.get(catchplatform);
        if (power == null) {
            throw new RDBESConversionException("No mapping found for code");
        }
        return Integer.parseInt(power);
    }

}
