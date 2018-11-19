/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.util.List;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class GearStrata {
    
    protected List<GearStratum> strata;
    
    /**
     * Returns the stratum of the given 2 or 4 letter imr gear code
     * @param imrCode
     * @return 
     */
    public GearStratum getStratum(String imrCode) throws StrataException{
        for (GearStratum g : strata){
            if (g.inStratum(imrCode)){
                return g;
            }
        }
         throw new StrataException("Gear: " + imrCode + "Not in any strata");
    }
}
