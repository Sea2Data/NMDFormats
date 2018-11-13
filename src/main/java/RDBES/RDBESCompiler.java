/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import Biotic.Biotic3.Biotic3Handler;
import BioticTypes.v3.MissionsType;
import LandingsTypes.v1.LandingsdataType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.ObjectFactory;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESCompiler {
    
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
    public RDBESCompiler(MissionsType biotic, LandingsdataType landings, CodeConversions conversions){
        this.rdbesFactory = new ObjectFactory();
        this.rdbes = new ArrayList<>();
        this.biotic = biotic;
        this.landings = landings;
        this.conversions = conversions;
    }
    
    public static void main(String[] args) throws JAXBException, XMLStreamException, ParserConfigurationException, ParserConfigurationException, SAXException, SAXException, IOException, FileNotFoundException{
        
        // make command line interface for conversion options for each source file so that e.g. CL can be created separately from samples and vice versa
        
        String pbpath = "/Users/a5362/bioticsets/filtered/pb_2016.xml";
        Biotic3Handler handler = new Biotic3Handler();
        MissionsType biotic = handler.read(new File(pbpath));
        
        RDBESCompiler compiler = new RDBESCompiler(biotic, null, null);
        
        compiler.addProveBat(false);
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
        System.out.println(this.biotic);
        //filtrer på oppdragstype
        
        //lag hvert object
        
        // kjør fyll inn standard metode
        
        // overskriv pb spesifikke ting
        
        throw new UnsupportedOperationException("Not implemented");
    }
}
