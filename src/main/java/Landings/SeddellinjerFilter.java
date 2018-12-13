/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.SeddellinjeType;
import java.util.Iterator;

/**
 * Iterator that consumes another iterator and selectively yields those items unchanged;
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public abstract class SeddellinjerFilter implements Iterator<SeddellinjeType>{

    SeddellinjeType next = null;
    Iterator<SeddellinjeType> baseIterator = null;
    
    
    /**
     * Set the underlying iterator this reads from
     * @param baseIterator 
     */
    public void setBaseIterator(Iterator<SeddellinjeType> baseIterator){
        this.baseIterator = baseIterator;
        this.next = this.findNext(this.baseIterator);
    }
    
    /**
     * Finds the next item to be returned by call to next(). returns null, if no such element exists.
     * Implement filter by overriding this method
     * @return 
     */
    protected abstract SeddellinjeType findNext(Iterator<SeddellinjeType> baseIterator);
    
    @Override
    public boolean hasNext() {
        if (next!=null){
            return true;
        }
        return false;
    }

    @Override
    public SeddellinjeType next() {
        SeddellinjeType next =  this.next;
        this.next = this.findNext(this.baseIterator);
        return next;
    }
    
    
}
