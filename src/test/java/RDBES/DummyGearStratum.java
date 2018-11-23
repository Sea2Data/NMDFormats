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
public class DummyGearStratum extends GearStratum {

    public DummyGearStratum(String name, Set<String> imr2letterCodes, Set<String> imr4letterCodes) {
        super(name, imr2letterCodes, imr4letterCodes);
    }


    @Override
    public boolean inStratum(String gear) {
        return gear == getName();
    }

    @Override
    public String getName() {
        return super.getName(); //To change body of generated methods, choose Tools | Templates.
    }

    
}
