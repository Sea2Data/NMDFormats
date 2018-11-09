/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.io.File;
import java.util.Map;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class CodeConversions {
    
    File resourcefilepath;
    
    /**
     * @param resourcefiles path to location for resource files
     */
    public CodeConversions(File resourcefiles){
        this.resourcefilepath = resourcefiles;
    }
    
    /**
     * Loads the named resource file from the resourcefile location (set in constructor)
     * Resource file should be a tab delimited file with two columns, where the values of the first column is unique
     * @param filename 
     * @return map from values in first column of resource file to values in second column
     */
    protected Map<String,String> loadResourceFile(String filename){
        throw new UnsupportedOperationException("Not implemented");
    }
    
    /**
     * Mapping from IMR gear to FAO
     * @return 
     */
    public Map<String, String> getGearIMR2FAO(){
        throw new UnsupportedOperationException("Not implemented");
    }
    
}
