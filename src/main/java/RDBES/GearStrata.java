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
    
    public GearStrata(List<GearStratum> strata) throws StrataException{
        for (int i=0; i<strata.size(); i++){
            for (int j=i+1; j<strata.size(); j++){
                GearStratum gs1 = strata.get(i);
                GearStratum gs2 = strata.get(j);
                if (gs1.getName().equals(gs2.getName())){
                    throw new StrataException("Some strata have same name");
                }
                if (gs2.overlap(gs1) || gs1.overlap(gs2)){
                    throw new StrataException("Some strata overlap");
                }
            }
        }
        this.strata = strata;
    }
    
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
