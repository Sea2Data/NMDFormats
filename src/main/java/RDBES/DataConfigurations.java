/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
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
    
    /**
     * @param resourcefiles path to location for resource files
     */
    public DataConfigurations(File resourcefiles){
        this.resourcefilepath = resourcefiles;
    }
    
    /**
     * Loads the named resource file from the resourcefile location (set in constructor)
     * Resource file should be a tab delimited file with two columns, where the values of the first column is unique
     * @param filename 
     * @return map from values in first column of resource file to values in second column
     */
    protected Map<String,String> loadResourceFile(String filename) throws IOException{
        InputStream resourcefile = DataConfigurations.class.getClassLoader().getResourceAsStream(filename);
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
    public Map<String, String> getLandingsSiteLoCode() throws IOException{
        return loadResourceFile("landingssite.txt");
    }

    public Map <String, String> getMetaDataPb(int year) {
        
        // put in resource file
        Map <String, String> metadatapb = new HashMap<>();
        metadatapb.put("samplingScheme", "Port sampling North of 64");
        metadatapb.put("samplingFrame", "Fresh fish landings / N64 / COD-POL-HAD-GHB");
        metadatapb.put("samplingCountry", "NOR");
        metadatapb.put("samplingInstitution", "IMR");
        metadatapb.put("sampler", "IMR");
        metadatapb.put("portselectionmethod", "random");
        metadatapb.put("portlocationtype", "processor");
        return metadatapb;
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
    
    public TemporalStrata getPortStratificationPb(int year) throws StrataException {
        // put in resource file
        return getQstrat();
        
    }

    
}