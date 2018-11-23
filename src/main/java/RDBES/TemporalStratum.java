/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class TemporalStratum {
    
    private final String name;
    protected List<Integer> startdates;
    protected List<Integer> enddates;
    
    public TemporalStratum(String name){
        this.name = name;
        this.startdates = new LinkedList<>();
        this.enddates = new LinkedList<>();
    }
    
    /**
     * 
     * @param startdate day in year
     * @param enddate day in year
     * @throws StrataException 
     */
    public void addRange(int startdate, int enddate) throws StrataException{
        if (this.inStratum(startdate) || this.inStratum(enddate)){
            throw new StrataException("Overlapping ranges specified");
        }
        startdates.add(startdate);
        enddates.add(enddate);
    }
    
    public boolean inStratum(int day){
        for (int i=0; i<startdates.size(); i++){
            if (day >= startdates.get(i) && day <= enddates.get(i)){
                return true;
            } 
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    boolean overlap(TemporalStratum d) {
        for (int i=0; i<startdates.size(); i++){
            if (d.inStratum(startdates.get(i)) || d.inStratum(enddates.get(i))){
                return true;
            }
        }

        return (false);
    }
}
