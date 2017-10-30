/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Biotic.Biotic3;

import Biotic.Biotic1.Biotic1Handler;
import XMLHandling.NamespaceVersionHandler;
import BioticTypes.v3_beta.PreyType;
import BioticTypes.v3_beta.AgedeterminationType;
import BioticTypes.v3_beta.CatchsampleType;
import BioticTypes.v3_beta.CopepodedevstageType;
import BioticTypes.v3_beta.FishstationType;
import BioticTypes.v3_beta.IndividualType;
import BioticTypes.v3_beta.MissionType;
import BioticTypes.v3_beta.MissionsType;
import BioticTypes.v3_beta.ObjectFactory;
import BioticTypes.v3_beta.PreylengthType;
import BioticTypes.v3_beta.TagType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class Biotic3Handler extends NamespaceVersionHandler<MissionsType> {

    private ObjectFactory biotic3factory;

    public Biotic3Handler() {
        this.latestNamespace = "http://www.imr.no/formats/nmdbiotic/v3_beta";
        this.latestBioticClass = MissionsType.class;
        this.compatibleNamespaces = null;
    }

    @Override
    public MissionsType read(InputStream xml) throws JAXBException, XMLStreamException, ParserConfigurationException, SAXException, IOException {
        return super.read(xml);
    }

    /**
     * As read, but converts from biotic 1, if acceptBiotic1 is true
     *
     * @param xml
     * @return
     * @throws javax.xml.bind.JAXBException If biotic 1 is correctly formatted,
     * but conversion could not be done due to data consistency errors.
     */
    public MissionsType readOldBiotic(InputStream xml) throws JAXBException, XMLStreamException, ParserConfigurationException, SAXException, IOException, BioticConversionException {

        BioticTypes.v1_4.MissionsType missions = (new Biotic1Handler()).read(xml);
        return this.convertBiotic1(missions);

    }
    
    @Override
    /**
     * Reads biotic from file. Converts data if precious version of biotic is detected.
     */
    public MissionsType read(File xml) throws JAXBException, XMLStreamException, FileNotFoundException, ParserConfigurationException, SAXException, IOException{
        MissionsType result;
        try (InputStream is = new FileInputStream(xml)) {
            result = this.read(is);
        } catch (javax.xml.bind.UnmarshalException e){
            Biotic1Handler h = new Biotic1Handler();
            try {
                result = this.convertBiotic1(h.read(xml));
            } catch (BioticConversionException ex) {
                throw new IOException("Error in conversion from biotic 1" + ex.getMessage());
            }
        }
        return result;
    }

    @Override
    public void save(OutputStream xml, MissionsType data) throws JAXBException {
        super.save(xml, data);
    }

    /**
     * @param missionsBiotic1 data to be converted
     * @return converted data
     * @throws Biotic.BioticConversionException
     */
    protected MissionsType convertBiotic1(BioticTypes.v1_4.MissionsType missionsBiotic1) throws BioticConversionException {
        this.biotic3factory = new ObjectFactory();
        MissionsType missions = this.biotic3factory.createMissionsType();
        for (BioticTypes.v1_4.MissionType m : missionsBiotic1.getMission()) {
            MissionType mission = createMissionFromBiotic1(m);
            missions.getMission().add(mission);
        }
        return missions;
    }

    private MissionType createMissionFromBiotic1(BioticTypes.v1_4.MissionType m) throws BioticConversionException {
        MissionType mission = this.biotic3factory.createMissionType();
        mission.setCallsignal(m.getCallsignal());
        mission.setCruise(m.getCruise());
        mission.setMissionnumber(m.getMissionnumber());
        mission.setMissiontype(m.getMissiontype());
        mission.setMissiontypename(m.getMissiontypename());
        mission.setPlatformname(m.getPlatformname());
        mission.setPurpose(m.getPurpose());
        mission.setPlatform(m.getPlatform());
        mission.setStartdate(this.convertDateFromBiotic1(m.getStartdate()));
        mission.setStopdate(this.convertDateFromBiotic1(m.getStopdate()));
        mission.setStartyear(m.getYear());

        for (BioticTypes.v1_4.FishstationType f : m.getFishstation()) {
            mission.getFishstation().add(createFishstationFromBiotic1(f));
        }

        return mission;
    }

    private FishstationType createFishstationFromBiotic1(BioticTypes.v1_4.FishstationType f) throws BioticConversionException {
        FishstationType fishstation = this.biotic3factory.createFishstationType();
        
        fishstation.setYear(((BioticTypes.v1_4.MissionType)f.getParent()).getYear());
        fishstation.setArea(f.getArea());
        fishstation.setBottomdepthstart(f.getBottomdepthstart());
        fishstation.setBottomdepthstop(f.getBottomdepthstop());
        fishstation.setCatchplatform(createStringFromBiotic1(f.getPlatform()));
        fishstation.setClouds(createStringFromBiotic1(f.getClouds()));
        fishstation.setComment(f.getComment());
        fishstation.setCountofvessels(f.getCountofvessels());
        fishstation.setDataquality(createStringFromBiotic1(f.getDataquality()));
        fishstation.setDirection(f.getDirection());
        fishstation.setDistance(f.getDistance());
        fishstation.setDoorspread(f.getDoorspread());
        fishstation.setDoorspreadsd(f.getDoorspreadsd());
        fishstation.setFishabundance(createStringFromBiotic1(f.getFishabundance()));
        fishstation.setFishdistribution(createStringFromBiotic1(f.getFishdistribution()));

        fishstation.setFishingdepthmax(f.getFishingdepthmax());
        fishstation.setFishingdepthmean(f.getFishingdepthmean());
        fishstation.setFishingdepthmin(f.getFishingdepthmin());
        fishstation.setFishingground(createStringFromBiotic1(f.getFishingground()));
        fishstation.setFixedstation(createStringFromBiotic1(f.getFixedstation()));
        fishstation.setFlora(createStringFromBiotic1(f.getFlora()));
        fishstation.setGear(createStringFromBiotic1(f.getGear()));
        fishstation.setGearcondition(createStringFromBiotic1(f.getGearcondition()));
        fishstation.setGearcount(f.getGearcount());
        fishstation.setGearno(f.getGearno());
        fishstation.setGearspeed(f.getGearspeed());
        fishstation.setHaulvalidity(createStringFromBiotic1(f.getHaulvalidity()));
        fishstation.setLandingsite(createStringFromBiotic1(f.getLandingsite()));
        fishstation.setLatitudeend(f.getLatitudeend());
        fishstation.setLatitudestart(f.getLatitudestart());
        fishstation.setLocation(f.getLocation());
        fishstation.setLongitudeend(f.getLongitudeend());
        fishstation.setLongitudestart(f.getLongitudestart());
        fishstation.setNation(createStringFromBiotic1(f.getNation()));
        fishstation.setSamplequality(createStringFromBiotic1(f.getTrawlquality()));
        fishstation.setSea(createStringFromBiotic1(f.getSea()));
        fishstation.setSerialno(f.getSerialno());
        fishstation.setSoaktime(f.getSoaktime());
        fishstation.setStartdate(this.convertDateFromBiotic1(f.getStartdate()));
        fishstation.setStartlog(f.getStartlog());
        fishstation.setStarttime(this.convertTimeBiotic1(f.getStarttime()));
        fishstation.setStation(f.getStation());
        fishstation.setStationtype(createStringFromBiotic1(f.getStationtype()));
        fishstation.setStopdate(this.convertDateFromBiotic1(f.getStopdate()));
        fishstation.setStoplog(f.getStoplog());
        fishstation.setStoptime(this.convertTimeBiotic1(f.getStoptime()));
        fishstation.setSystem(f.getSystem());
        fishstation.setVerticaltrawlopening(f.getTrawlopening());
        fishstation.setVerticaltrawlopeningsd(f.getTrawlopeningsd());
        fishstation.setTripno(f.getTripno());
        fishstation.setVegetationcover(createStringFromBiotic1(f.getVegetationcover()));
        fishstation.setVisibility(createStringFromBiotic1(f.getVisibility()));
        fishstation.setWaterlevel(f.getWaterlevel());
        fishstation.setWeather(createStringFromBiotic1(f.getWeather()));
        fishstation.setWinddirection(f.getWinddirection());
        fishstation.setWindspeed(f.getWindspeed());
        fishstation.setWirelength(this.convertIntegerToDecimal(f.getWirelength()));

        for (BioticTypes.v1_4.CatchsampleType c : f.getCatchsample()) {
            fishstation.getCatchsample().add(this.createCatchsampleFromBiotic1(c));
        }

        return fishstation;
    }

    private CatchsampleType createCatchsampleFromBiotic1(BioticTypes.v1_4.CatchsampleType c) throws BioticConversionException {
        checkIndividualKeyingBiotic1(c);

        CatchsampleType catchsample = this.biotic3factory.createCatchsampleType();
        catchsample.setAbundancecategory(this.createStringFromBiotic1(c.getAbundancecategory()));
        catchsample.setAphia(c.getAphia());
        catchsample.setComment(c.getComment());
        catchsample.setConservation(this.createStringFromBiotic1(c.getConservation()));
        catchsample.setCount(c.getCount());
        catchsample.setForeignobject(this.createStringFromBiotic1(c.getForeignobject()));
        catchsample.setTissuesample(this.createStringFromBiotic1(c.getGenetics()));
        catchsample.setGroup(this.createStringFromBiotic1(c.getGroup()));
        catchsample.setAgingstructure(this.createStringFromBiotic1(c.getAgingstructure()));
        catchsample.setLengthmeasurement(this.createStringFromBiotic1(c.getLengthmeasurement()));
        catchsample.setLengthsamplecount(c.getLengthsamplecount());
        catchsample.setLengthsamplevolume(c.getLengthsamplevolume());
        catchsample.setLengthsampleweight(c.getLengthsampleweight());
        catchsample.setNorwegianname(c.getNoname());
        catchsample.setParasite(this.createStringFromBiotic1(c.getParasite()));
        catchsample.setProducttype(this.createStringFromBiotic1((c.getProducttype())));
        catchsample.setRaisingfactor(c.getRaisingfactor());
        catchsample.setSampleproducttype(this.createStringFromBiotic1((c.getSampleproducttype())));
        catchsample.setSamplenumber(c.getSamplenumber());
        catchsample.setSampletype(this.createStringFromBiotic1(c.getSampletype()));

        catchsample.setSpecies(c.getSpecies());
        catchsample.setSpecimensamplecount(c.getSpecimensamplecount());
        catchsample.setStomach(this.createStringFromBiotic1(c.getStomach()));
        catchsample.setVolume(c.getVolume());
        catchsample.setWeight(c.getWeight());

        for (BioticTypes.v1_4.IndividualType i : c.getIndividual()) {
            catchsample.getIndividual().add(this.createIndividualFromBiotic1(i));
        }

        return catchsample;
    }

    private IndividualType createIndividualFromBiotic1(BioticTypes.v1_4.IndividualType i) {
        IndividualType individual = this.biotic3factory.createIndividualType();
        individual.setAbdomenwidth(i.getAbdomenwidth());
        individual.setBlackspot(this.createStringFromBiotic1(i.getBlackspot()));
        individual.setCarapacelength(i.getCarapacelength());
        individual.setCarapacewidth(i.getCarapacewidth());
        individual.setComment(i.getComment());
        individual.setDiameter(i.getDiameter());
        individual.setDigestion(this.createStringFromBiotic1(i.getDigestion()));
        individual.setEggstage(this.createStringFromBiotic1(i.getEggstage()));
        individual.setFat(this.createStringFromBiotic1(i.getFat()));
        individual.setFatpercent(i.getFatpercent());
        individual.setForklength(i.getForklength());
        individual.setFungusheart(this.createStringFromBiotic1(i.getFungusheart()));
        individual.setFungusouter(this.createStringFromBiotic1(i.getFungusouter()));
        individual.setFungusspores(this.createStringFromBiotic1(i.getFungusspores()));
        individual.setTissuesamplenumber(i.getGeneticsnumber());
        individual.setGillworms(this.createStringFromBiotic1(i.getGillworms()));
        individual.setGonadweight(i.getGonadweight());
        individual.setHeadlength(i.getHeadlength());
        individual.setJapanesecut(i.getJapanesecut());
        individual.setLength(i.getLength());
        individual.setLengthresolution(this.createStringFromBiotic1(i.getLengthunit()));
        individual.setLengthwithouthead(i.getLengthwithouthead());
        individual.setLiver(this.createStringFromBiotic1(i.getLiver()));
        individual.setLiverparasite(this.createStringFromBiotic1(i.getLiverparasite()));
        individual.setLiverweight(i.getLiverweight());
        individual.setMantlelength(i.getMantlelength());
        individual.setMeroslength(i.getMeroslength());
        individual.setMeroswidth(i.getMeroswidth());
        individual.setMoultingstage(this.createStringFromBiotic1(i.getMoultingstage()));
        individual.setProducttype(this.createStringFromBiotic1(i.getProducttype()));
        individual.setRightclawlength(i.getRightclawlength());
        individual.setRightclawwidth(i.getRightclawwidth());
        individual.setSex(this.createStringFromBiotic1(i.getSex()));
        individual.setSnouttoanalfin(i.getSnouttoanalfin());
        individual.setSnouttoboneknob(i.getSnouttoboneknob());
        individual.setSnouttodorsalfin(i.getSnouttodorsalfin());
        individual.setSnouttoendoftail(i.getSnouttoendoftail());
        individual.setSnouttoendsqueezed(i.getSnouttoendsqueezed());

        individual.setSpecialstage(this.createStringFromBiotic1(i.getSpecialstage()));
        individual.setSpecimenno(i.getSpecimenno());
        individual.setMaturationstage(this.createStringFromBiotic1(i.getStage()));
        individual.setStomachfillfield(this.createStringFromBiotic1(i.getStomachfillfield()));
        individual.setStomachfilllab(this.createStringFromBiotic1(i.getStomachfilllab()));
        individual.setStomachweight(i.getStomachweight());
        individual.setSwollengills(this.createStringFromBiotic1(i.getSwollengills()));
        individual.setVertebrae(i.getVertebrae());
        individual.setVolume(i.getVolume());
        individual.setWeight(i.getWeight());

        for (BioticTypes.v1_4.AgedeterminationType a : i.getAgedetermination()) {
            individual.getAgedetermination().add(this.createAgedeterminationFromBiotic1(a));
        }

        //handles moving of prey from catchsample
        for (BioticTypes.v1_4.PreyType p : this.getPreyForIndividualBiotic1(i)) {
            individual.getPrey().add(createPreyFromBiotic1(p));
        }

        for (BioticTypes.v1_4.TagType t : i.getTag()) {
            individual.getTag().add(createTagFromBiotic1(t));
        }

        return individual;
    }

    private AgedeterminationType createAgedeterminationFromBiotic1(BioticTypes.v1_4.AgedeterminationType a) {
        AgedeterminationType age = this.biotic3factory.createAgedeterminationType();
        age.setAge(a.getAge());

        BioticTypes.v1_4.CatchsampleType catchsample = (BioticTypes.v1_4.CatchsampleType) a.getParent().getParent();

        age.setCalibration(a.getCalibration());
        age.setCoastalannuli(a.getCoastalannuli());
        age.setGrowthzone1(a.getGrowthzone1());
        age.setGrowthzone2(a.getGrowthzone2());
        age.setGrowthzone3(a.getGrowthzone3());
        age.setGrowthzone4(a.getGrowthzone4());
        age.setGrowthzone5(a.getGrowthzone5());
        age.setGrowthzone6(a.getGrowthzone6());
        age.setGrowthzone7(a.getGrowthzone7());
        age.setGrowthzone8(a.getGrowthzone8());
        age.setGrowthzone9(a.getGrowthzone9());
        age.setGrowthzonestotal(a.getGrowthzonestotal());
        age.setNo(a.getNo());
        age.setOceanicannuli(a.getCoastalannuli());
        age.setOtolithcentre(this.createStringFromBiotic1(a.getOtolithcentre()));
        age.setOtolithedge(this.createStringFromBiotic1(a.getOtolithedge()));
        age.setOtolithtype(this.createStringFromBiotic1(a.getOtolithtype()));
        age.setReadability(this.createStringFromBiotic1(a.getReadability()));
        age.setSpawningage(a.getSpawningage());
        age.setSpawningzones(a.getSpawningzones());

        return age;
    }

    private PreyType createPreyFromBiotic1(BioticTypes.v1_4.PreyType p) {
        PreyType prey = this.biotic3factory.createPreyType();
        prey.setDevstage(this.createStringFromBiotic1(p.getDevstage()));
        prey.setDigestion(this.createStringFromBiotic1(p.getDigestion()));
        prey.setInterval(this.createStringFromBiotic1(p.getInterval()));
        prey.setLengthmeasurement(this.createStringFromBiotic1(p.getLengthmeasurement()));
        prey.setPartno(p.getPartno());
        prey.setSpecies(p.getSpecies());
        prey.setTotalcount(p.getTotalcount());
        prey.setTotalweight(p.getTotalweight());
        prey.setWeightresolution(this.createStringFromBiotic1(p.getWeightresolution()));

        int linennumber = 1;
        for (BioticTypes.v1_4.PreylengthType pl : p.getPreylength()) {
            prey.getPreylength().add(this.convertPreyLengthFromBiotic1(pl, linennumber));
            linennumber++;
        }

        for (BioticTypes.v1_4.CopepodedevstageType co : p.getCopepodedevstage()) {
            prey.getCopepodedevstage().add(this.convertCopepodedevstageFromBiotic1(co));
        }

        return prey;
    }

    private TagType createTagFromBiotic1(BioticTypes.v1_4.TagType t) {
        TagType tag = this.biotic3factory.createTagType();
        tag.setTagno(t.getTagno());
        tag.setTagtype(this.createStringFromBiotic1(t.getTagtype()));

        return tag;
    }

    private PreylengthType convertPreyLengthFromBiotic1(BioticTypes.v1_4.PreylengthType pl, int linenumber) {
        PreylengthType preylength = this.biotic3factory.createPreylengthType();
        preylength.setNo(new BigInteger(""+linenumber));
        preylength.setCount(pl.getCount());
        preylength.setLength(pl.getLength());

        return preylength;
    }

    private CopepodedevstageType convertCopepodedevstageFromBiotic1(BioticTypes.v1_4.CopepodedevstageType co) {
        CopepodedevstageType cop = this.biotic3factory.createCopepodedevstageType();
        cop.setCopepodedevstage(co.getCopepodedevstage());
        cop.setCount(co.getCount());

        return cop;
    }

    private String createStringFromBiotic1(BioticTypes.v1_4.StringDescriptionType s) {
        if (s==null){
            return null;
        }
        return s.getValue();        
    }

    // Checks if individuals are correctly keyed in biotic 1 data
    private void checkIndividualKeyingBiotic1(BioticTypes.v1_4.CatchsampleType c) throws BioticConversionException {
        Set<BigInteger> keys = new HashSet<>();
        for (BioticTypes.v1_4.IndividualType i : c.getIndividual()) {
            if (keys.contains(i.getSpecimenno())) {
                throw new BioticConversionException("Individuals not correctly keyed (specimenno " + i.getSpecimenno() + " repeated)");
            } else {
                keys.add(i.getSpecimenno());
            }
        }

        for (BioticTypes.v1_4.PreyType p : c.getPrey()) {
            if (!keys.contains(p.getFishno())) {
                throw new BioticConversionException("Individuals not correctly keyed (fishno " + p.getFishno() + " does not match any specimenno)");
            }
        }
    }

    // Finds all individuals from catchsample that are assosiated with this individual.
    private Iterable<BioticTypes.v1_4.PreyType> getPreyForIndividualBiotic1(BioticTypes.v1_4.IndividualType i) {
        List<BioticTypes.v1_4.PreyType> preys = new LinkedList<>();

        BioticTypes.v1_4.CatchsampleType catchsample = (BioticTypes.v1_4.CatchsampleType) i.getParent();
        for (BioticTypes.v1_4.PreyType p : catchsample.getPrey()) {
            if (p.getFishno().equals(i.getSpecimenno())) {
                preys.add(p);
            }
        }

        return preys;
    }

    private XMLGregorianCalendar convertDateFromBiotic1(String startdate) throws BioticConversionException {
        if (startdate == null){
            return null;
        }
        try {
            String[] date = startdate.split("/");
            if (date.length != 3) {
                throw new BioticConversionException("Malformed date: " + startdate);
            }
            XMLGregorianCalendar newDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(date[2] + "-" + date[1] + "-" + date[0] + "Z");
            newDate.setTime(DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
            return newDate;
        } catch (DatatypeConfigurationException ex) {
            throw new BioticConversionException("Malformed date: " + startdate);
        }
    }

    private XMLGregorianCalendar convertTimeBiotic1(String starttime) throws BioticConversionException {
        if (starttime == null){
            return null;
        }
        try {
            XMLGregorianCalendar newTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(starttime + "Z");
            newTime.setDay(DatatypeConstants.FIELD_UNDEFINED);
            newTime.setMonth(DatatypeConstants.FIELD_UNDEFINED);
            newTime.setYear(DatatypeConstants.FIELD_UNDEFINED);
            return newTime;
        } catch (DatatypeConfigurationException ex) {
            throw new BioticConversionException("Malformed date: " + starttime);
        }
    }

    private BigDecimal convertIntegerToDecimal(BigInteger integer) {
        if (integer == null){
            return null;
        }
        else{
            return new BigDecimal(integer);
        }
    }

}
