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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class DataConfigurations {
    
    File resourcefilepath;
    Map<String, String> landingssites = null;
    Map<Integer, Map<String,String>> metaDataPb;
    Map<String, String> homrices3 = null;
    Map<String, String> gearfao = null;
    Map<String, String> gearTargetSpecies = null;
    Map<String, String> gearMeshSize = null;
    Map<String, String> gearSelDev = null;
    Map<String, String> gearSelDevMeshSize = null;
    Map<String, String> presentation = null;
    Map<String, String> scalingfactors = null;
    Map<String, String> maturity = null;
    Map<String, Map<String,String>> otolithtype = null;
    /**
     * @param resourcefiles path to location for resource files
     */
    public DataConfigurations(File resourcefiles){
        this.metaDataPb = new HashMap<>();
        this.resourcefilepath = resourcefiles;
    }
    
    /**
     * Loads the named resource file from the resourcefile location (set in constructor)
     * Resource file should be a tab delimited file with two columns, where the values of the first column is unique
     * @param filename 
     * @return map from values in first column of resource file to values in second column
     */
    protected Map<String,String> loadResourceFile(String filename) throws IOException{
        File infile = new File (this.resourcefilepath + File.pathSeparator + filename);
        InputStream resourcefile = new FileInputStream(infile);
        BufferedReader br = new BufferedReader(new InputStreamReader(resourcefile));
        Map<String, String> resources = new HashMap<>();
        
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("#")){
                continue;
            }
            else if (line.length()==0){
                continue;
            }
            else{
                String[] map = line.split("\t");
                if (map.length!=2){
                    throw new IOException("Format error in resource file");
                }
                resources.put(map[0], map[1]);
            }
        }        
        return resources;
    }
    
    /**
     * Mapping from IMR gear to FAO
     * @return 
     */
    public Map<String, String> getGearIMR2FAO(){
        throw new UnsupportedOperationException("Not implemented");
    }
    
    /**
     * Mapping IMR location code for landing site to LOCODE
     * @return 
     */
    public String getLandingsSiteLoCode(String imrCode) throws IOException, RDBESConversionException{
        if (this.landingssites==null){
            this.landingssites = loadResourceFile("landingssite.txt");
        }
        String locode = this.landingssites.get(imrCode);
        if (locode==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return locode;
    }

    public String getMetaDataPb(int year, String field) throws IOException, RDBESConversionException {
        if (this.metaDataPb.get(year)==null){
            this.metaDataPb.put(year, loadResourceFile(year + File.pathSeparator + "provebat_metadata.txt"));
        }
        
        String value = this.metaDataPb.get(year).get(field);
        if (value==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return value;
    }

    private TemporalStrata getQstrat() throws StrataException{
        List<TemporalStratum> s = new ArrayList<>();
        TemporalStratum q1 = new TemporalStratum("Q1");
        q1.addRange(1, 91);
        TemporalStratum q2 = new TemporalStratum("Q2");
        q2.addRange(92, 182);
        TemporalStratum q3 = new TemporalStratum("Q3");
        q3.addRange(183, 274);
        TemporalStratum q4 = new TemporalStratum("Q4");
        q4.addRange(275,366);
        
        s.add(q1);
        s.add(q2);
        s.add(q3);
        s.add(q4);
        
        return new TemporalStrata(s);
    }
    
    public TemporalStrata getPortStratificationPb(int year) throws StrataException, IOException, RDBESConversionException {
        
        if (this.getMetaDataPb(year,"portstrata").trim().equals("quarter")){
            return getQstrat();
        }
        else{
            throw new UnsupportedOperationException("Stratasystem " + this.getMetaDataPb(year,"portstrata") + "not supported.");
        }
        
    }

    public GearStrata getLandingStratificationPb(int year) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getHomrICES3(String homr) throws IOException, RDBESConversionException {
         if (this.homrices3==null){
            this.homrices3 = loadResourceFile("homr_ices3.txt");
        }
        String ices3 = this.homrices3.get(homr);
        if (homr==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return homr;
    }

    /**
     * Maps 4 digit imr gear code to FAO 1980 code
     * @param imrGear
     * @return
     * @throws IOException 
     */
    public String getImrGearFAO(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearfao==null){
            this.gearfao = loadResourceFile("imrgear_FAO.txt");
        }
        String fao = this.gearfao.get(imrGear);
        if (fao==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return fao;
    }
    
    public String getImrGearTargetSpecies(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearTargetSpecies==null){
            this.gearTargetSpecies = loadResourceFile("imrgear_target.txt");
        }
        String ts = this.gearTargetSpecies.get(imrGear);
        if (ts==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return ts;
    }

    public int getImrGearMeshSize(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearMeshSize==null){
            this.gearMeshSize = loadResourceFile("imrgear_meshSize.txt");
        }
        String ms = this.gearMeshSize.get(imrGear);
        if (ms==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return Integer.parseInt(ms);
    }

    public int getImrGearSelDev(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearSelDev==null){
            this.gearSelDev = loadResourceFile("imrgear_seldev.txt");
        }
        String gsd = this.gearSelDev.get(imrGear);
        if (gsd==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return Integer.parseInt(gsd);
    }

    public int getImrGearSelDevMeshSize(String imrGear) throws IOException, RDBESConversionException {
        if (this.gearSelDevMeshSize==null){
            this.gearSelDevMeshSize = loadResourceFile("imrgear_SelDevMeshSize.txt");
        }
        String gsd = this.gearSelDevMeshSize.get(imrGear);
        if (gsd==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return Integer.parseInt(gsd);
    }

    public String getPresentation(String sampleproducttype) throws IOException, RDBESConversionException {
        if (this.presentation==null){
            this.presentation = loadResourceFile("imrProductype_presentation.txt");
        }
        String pres = this.presentation.get(sampleproducttype);
        if (pres==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return pres;
    }
    
    public double getScalingFactor(String fromCode, String toCode) throws IOException, RDBESConversionException{
         if (this.scalingfactors==null){
            this.scalingfactors = loadResourceFile("imrProductype_scalingfactors.txt");
        }
        String scaling = this.scalingfactors.get(fromCode+","+toCode);
        if (scaling==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return Double.parseDouble(scaling);
    }

    /**
     * Factor for converting lengths to the unit returned by getLengthUnit(lengthresolution)
     * @param lengthresolution
     * @return 
     */
    public double getLengthFactor(String lengthresolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getLengthUnit(String lengthresolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getMaturity(String imrMaturity) throws IOException, RDBESConversionException{
        if (this.maturity==null){
            this.maturity = loadResourceFile("homr_ices3.txt");
        }
        String mat = this.maturity.get(imrMaturity);
        if (mat==null){
            throw new RDBESConversionException("No mapping found for code");
        }
        return mat;
    }
    
    public Map<String, String> getOtolithType(String aphia){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
