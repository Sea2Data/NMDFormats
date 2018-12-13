/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.SeddellinjeType;
import java.util.Iterator;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class NoFilter extends SeddellinjerFilter{

    @Override
    protected SeddellinjeType findNext(Iterator<SeddellinjeType> baseIterator) {
        if (baseIterator.hasNext()){
            return baseIterator.next();
        }
        return null;
    }
    
}
