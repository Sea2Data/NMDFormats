/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class CoordinateChecker {

    public static void main(String[] args) throws FactoryException {
String wkt = "";
CoordinateReferenceSystem sourceCRS = CRS.parseWKT(wkt);
    }

}
