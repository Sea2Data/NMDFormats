/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.SeddellinjeType;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class ProductFilter extends SeddellinjerFilter{

    Set<String> conservationCodes;
    
    public ProductFilter(Set<String> codes){
        super();
        this.conservationCodes = codes;
    }
    
    @Override
    protected SeddellinjeType findNext(Iterator<SeddellinjeType> baseIterator) {
        SeddellinjeType next = null;
        while (baseIterator.hasNext() && next == null){
            SeddellinjeType nextbase = baseIterator.next();
            if (this.conservationCodes.contains(nextbase.getProdukt().getKonserveringsmåteKode())){
                next=nextbase;
            }
        }
        return next;
    }
    
}
