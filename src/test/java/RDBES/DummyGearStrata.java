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
public class DummyGearStrata extends GearStrata {

    public DummyGearStrata(List<GearStratum> strata) throws StrataException {
        super(strata);
    }

    @Override
    public GearStratum getStratum(String imrCode) throws StrataException {
        return new DummyGearStratum(imrCode, null, null);
    }

    
}
