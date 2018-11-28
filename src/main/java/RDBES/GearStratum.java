/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.util.Set;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class GearStratum {
    
    protected String name;
    protected Set<String> imr2letterCodes;
    protected Set<String> imr4letterCodes;
    
    public GearStratum(String name, Set<String> imr2letterCodes, Set<String> imr4letterCodes){
        this.name=name;
        this.imr2letterCodes = imr2letterCodes;
        this.imr4letterCodes = imr4letterCodes;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean inStratum(String gear){
        if (imr4letterCodes.contains(gear) || imr2letterCodes.contains(gear.substring(0, 2))){
            return true;
        }
        return false;
    }
    
    boolean overlap(GearStratum d) {
        for (String imr : this.imr2letterCodes){
            if (d.inStratum(imr)){
                return true;
            }
        }
        for (String imr : this.imr4letterCodes){
            if (d.inStratum(imr)){
                return true;
            }
        }

        return (false);
    }
    
}
