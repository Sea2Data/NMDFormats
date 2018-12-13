/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import Landings.SeddellinjerFilter;
import Landings.LandingsHandler;
import Landings.NoFilter;
import LandingsTypes.v2.LandingsdataType;
import LandingsTypes.v2.ObjectFactory;
import LandingsTypes.v2.SeddellinjeType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Extracts data from landings after applying registered filters
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class LandingsCompiler {
    
    File landingsfile;
    SeddellinjerFilter filter;
    SeddellinjerFilter rawiterator;
    
    public LandingsCompiler(File landings){
        this.landingsfile = landings;
        this.filter = new NoFilter();
        this.rawiterator = this.filter;
    }
    
    public LandingsCompiler(LandingsCompiler landingscompiler){
        this.landingsfile = landingscompiler.landingsfile;
        this.filter = landingscompiler.filter;
    }
    
    public LandingsCompiler makeFilteredCompiler(SeddellinjerFilter filter){
        LandingsCompiler filteredcompiler = new LandingsCompiler(this);
        if (this.filter==null){
            filteredcompiler.filter=filter;
            filter.setBaseIterator(rawiterator);
        }
        else{
            filteredcompiler.filter.setBaseIterator(filter);
        }
        return(filteredcompiler);
    }
    
    /**
     * Get landings from underlying file filtered by the set filter.
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public LandingsdataType getFilteredLandings() throws FileNotFoundException, IOException{
        ObjectFactory landingsfac = new ObjectFactory();
        LandingsdataType filteredlandings = landingsfac.createLandingsdataType();
        
        LandingsHandler h = new LandingsHandler();
        
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(this.landingsfile));
        this.rawiterator.setBaseIterator(h.getPSViterator(reader));
        
        while(this.filter.hasNext()){
            filteredlandings.getSeddellinje().add(this.filter.next());
        }
        reader.close();
        return filteredlandings;
        
    }
    
    public int getNumberOfSiteDaysInStratum(TemporalStrata strata, TemporalStratum stratum) throws StrataException, FileNotFoundException, IOException{
        Set<String> sitedays = new HashSet<>();
        
        BufferedReader reader = this.initreading();
        while(this.filter.hasNext()){
            SeddellinjeType s = this.filter.next();
            if(strata.getStratum(s.getSisteFangstdato())==stratum){
                sitedays.add(s.getMottaker().getMottaksstasjon()+"/"+s.getSisteFangstdato());
            }
        }
        reader.close();
        
        return sitedays.size();
    }
    
    private BufferedReader initreading() throws FileNotFoundException, IOException{
        LandingsHandler h = new LandingsHandler();
        
        BufferedReader reader = new BufferedReader(new FileReader(this.landingsfile));
        this.rawiterator.setBaseIterator(h.getPSViterator(reader));
        return reader;
    }
    
    
}
