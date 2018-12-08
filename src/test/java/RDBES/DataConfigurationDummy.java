/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DataConfigurationDummy extends DataConfigurations {

    @Override
    public List<String> getTargetSpeciesBioVarPb(int year, int missionnumber){
        List<String> l = new ArrayList<>();
        l.add("126436");
        l.add("126441");
        l.add("126437");
        return l;
    }

    
    
    @Override
    public String getSpeciesAssemblage(List<FishWeight> l, String gear){
        return "DEF";
    }
    
    
    @Override
    public String getAgingstructureSampled(String agingstructure){
        return "otolith";
    }

    @Override
    public String getAgingstructureRead(String aphia, String agingstructure){
        return "otolith";
    }


    @Override
    public String getLengthMeasurement(String lengthmeasurement){
        return "ForkLength";
    }

    @Override
    public int getVesselSize(String catchplatform) {
        return 1000;
    }

    @Override
    public int getVesselPower(String catchplatform) {
        return 100;
    }

    @Override
    public int getVesselLength(String catchplatform) {
        return 10;
    }

    @Override
    public String getVesselFlag(String catchplatform) {
        return "NOR";
    }

    public DataConfigurationDummy(File resourcefiles) {
        super(resourcefiles);
    }

    @Override
    public String getOtolithType(String aphia, String type) {
        return type;
    }


    @Override
    public String getMaturity(String imrMaturity){
        return imrMaturity;
    }

    @Override
    public String getLengthUnit(String lengthresolution) {
        return "1cm";
    }

    @Override
    public double getLengthFactor(String lengthresolution) {
        return 1.0;
    }

    @Override
    public double getScalingFactor(String aphia, String fromCode, String toCode){
        return 1.0;
    }

    @Override
    public String getPresentation(String sampleproducttype){
        return sampleproducttype;
    }

    @Override
    public Integer getImrGearSelDevMeshSize(String imrGear){
        return 10;
    }

    @Override
    public int getImrGearSelDev(String imrGear){
        return 2;
    }

    @Override
    public Integer getImrGearMeshSize(String imrGear){
        return 10;
    }


    @Override
    public String getImrGearFAO(String imrGear){
        return imrGear;
    }

    @Override
    public String getHomrICES3(String homr){
        return homr;
    }

    @Override
    public GearStrata getLandingStratificationPb(int year) throws StrataException {
        GearStrata g = new DummyGearStrata(new LinkedList<>());
        
        return g;
    }

    @Override
    public TemporalStrata getPortStratificationPb(int year) throws StrataException {
        return getQstrat();
    }

    @Override
    public String getMetaDataPb(int year, String field) {
        if (field.equals("species")){
            return "126436";
        }else{
            return field;
        }

    }

    @Override
    public String getLandingsSiteLoCode(String imrCode){
        return imrCode;
    }
    
}
