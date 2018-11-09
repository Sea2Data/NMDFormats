/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import BioticTypes.v3.MissionsType;
import LandingsTypes.v1.LandingsdataType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.ObjectFactory;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class CompileRDBES {
    
    protected ObjectFactory rdbesFactory;
    protected List<DesignType> rdbes;
    protected MissionsType biotic;
    protected LandingsdataType landings;
    protected CodeConversions conversions;
    
    /**
     * Compiles tables in the RDBES exchange format
     * @param biotic
     * @param landings
     * @param conversions 
     */
    public CompileRDBES(MissionsType biotic, LandingsdataType landings, CodeConversions conversions){
        this.rdbesFactory = new ObjectFactory();
        this.rdbes = new ArrayList<>();
        this.biotic = biotic;
        this.landings = landings;
        this.conversions = conversions;
    }
    
    /**
     * Generates table in the RDBES exchange format at in the directory specified as outputpath
     * @param outputpath 
     */
    public void writeTables(File outputpath){
        
        //skip CL etc if landings is null
        
        // use generic XML to relational conversion
        
        throw new UnsupportedOperationException("Not implemented");
    }
    
    /**
     * Creates CL table for RDBES
     */
    protected void createCL(){
        throw new UnsupportedOperationException("Not implemented");
    }
    
    /**
     * Adds Port sampling program "Provebat" to RDBES
     * @param strict if true exception is thrown when mandatory fields are missing, else lines are skipped with warning printed to stderr
     */
    protected void addProveBat(boolean strict){
        
        //filtrer på oppdragstype
        
        throw new UnsupportedOperationException("Not implemented");
    }
}
