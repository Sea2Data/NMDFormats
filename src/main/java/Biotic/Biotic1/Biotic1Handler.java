/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Biotic.Biotic1;

import Biotic.BioticConversionException;
import BioticTypes.v1_4.AgedeterminationType;
import BioticTypes.v1_4.CatchsampleType;
import BioticTypes.v1_4.CopepodedevstageType;
import BioticTypes.v1_4.FishstationType;
import BioticTypes.v1_4.IndividualType;
import BioticTypes.v1_4.MissionType;
import XMLHandling.NamespaceVersionHandler;
import BioticTypes.v1_4.MissionsType;
import BioticTypes.v1_4.ObjectFactory;
import BioticTypes.v1_4.PreyType;
import BioticTypes.v1_4.PreylengthType;
import BioticTypes.v1_4.StringDescriptionType;
import BioticTypes.v1_4.TagType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class Biotic1Handler extends NamespaceVersionHandler<MissionsType> {

    private ObjectFactory biotic1factory;

    public Biotic1Handler() {
        this.latestNamespace = "http://www.imr.no/formats/nmdbiotic/v1.4";
        this.latestBioticClass = MissionsType.class;
        this.compatibleNamespaces = new HashSet<>();
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1.3");
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1.2");
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1.1");
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1");
    }

    @Override
    public MissionsType read(InputStream xml) throws JAXBException, XMLStreamException, ParserConfigurationException, SAXException, IOException {
        return super.read(xml);
    }

    @Override
    public void save(OutputStream xml, MissionsType data) throws JAXBException {
        super.save(xml, data);
    }

    /**
     * Converts BioticTypes.v3_beta.MissionsType to
     * BioticTypes.v1_4.MissionsType Silently ignores fields that are new in
     * biotic v3, but throws exception if data that has been restructured in
     * biotic v3 can not be converted to biotic v 1.4. Conversion from decimal
     * to integer is done by discarding fractional part for the value returned
     * by fishstation.getWireLength()
     *
     * @param missionsBiotic3
     * @return
     * @throws BioticConversionException
     */
    public MissionsType convertBiotic3(BioticTypes.v3_beta.MissionsType missionsBiotic3) throws BioticConversionException {
        this.biotic1factory = new ObjectFactory();
        MissionsType ms = this.biotic1factory.createMissionsType();
        for (BioticTypes.v3_beta.MissionType m : missionsBiotic3.getMission()) {
            ms.getMission().add(this.convertMissionFromBiotic3(m));
        }
        return ms;
    }

    private MissionType convertMissionFromBiotic3(BioticTypes.v3_beta.MissionType missionBiotic3) throws BioticConversionException {

        int stopYear;
        stopYear = Integer.parseInt(missionBiotic3.getStopdate().toString().split("-")[0]);
        if (stopYear != missionBiotic3.getStartyear().intValue()) {
            throw new BioticConversionException("Missions can not cross years in Biotic v1.4");
        }

        MissionType m = this.biotic1factory.createMissionType();
        m.setCallsignal(missionBiotic3.getCallsignal());
        m.setCruise(missionBiotic3.getCruise());
        m.setMissionnumber(missionBiotic3.getMissionnumber());
        m.setMissiontype(missionBiotic3.getMissiontype());
        m.setMissiontypename(missionBiotic3.getMissiontypename());
        m.setPlatform(missionBiotic3.getPlatform());
        m.setPlatformname(missionBiotic3.getPlatformname());
        m.setPurpose(missionBiotic3.getPurpose());
        m.setStartdate(this.convertDateFromBiotic3(missionBiotic3.getStartdate()));
        m.setStopdate(this.convertDateFromBiotic3(missionBiotic3.getStopdate()));
        m.setYear(missionBiotic3.getStartyear());

        for (BioticTypes.v3_beta.FishstationType fs : missionBiotic3.getFishstation()) {
            m.getFishstation().add(this.convertFishstationFromBiotic3(fs, stopYear));
        }

        return m;
    }

    private FishstationType convertFishstationFromBiotic3(BioticTypes.v3_beta.FishstationType f, int year) throws BioticConversionException {
        FishstationType fishstation = this.biotic1factory.createFishstationType();

        if (f.getYear().intValue() != year) {
            throw new BioticConversionException("Missions can not cross years in Biotic v1.4");
        }

        if (!(f.getFishingdepthcount() == null || f.getFishingdepthcount().intValue() == 1)) {
            throw new BioticConversionException("Multiple trawling depths are not handled in Biotic v1.4 conversion");
        }

        fishstation.setArea(f.getArea());
        fishstation.setBottomdepthstart(f.getBottomdepthstart());
        fishstation.setBottomdepthstop(f.getBottomdepthstop());
        fishstation.setPlatform(createStringDescriptionTypeFromBiotic3(f.getCatchplatform()));
        fishstation.setClouds(createStringDescriptionTypeFromBiotic3(f.getClouds()));
        fishstation.setComment(f.getComment());
        fishstation.setCountofvessels(f.getCountofvessels());
        fishstation.setDataquality(createStringDescriptionTypeFromBiotic3(f.getDataquality()));
        fishstation.setDirection(f.getDirection());
        fishstation.setDistance(f.getDistance());
        fishstation.setDoorspread(f.getDoorspread());
        fishstation.setDoorspreadsd(f.getDoorspreadsd());
        fishstation.setFishabundance(createStringDescriptionTypeFromBiotic3(f.getFishabundance()));
        fishstation.setFishdistribution(createStringDescriptionTypeFromBiotic3(f.getFishdistribution()));

        fishstation.setFishingdepthmax(f.getFishingdepthmax());
        fishstation.setFishingdepthmean(f.getFishingdepthmean());
        fishstation.setFishingdepthmin(f.getFishingdepthmin());
        fishstation.setFishingground(createStringDescriptionTypeFromBiotic3(f.getFishingground()));
        fishstation.setFixedstation(createStringDescriptionTypeFromBiotic3(f.getFixedstation()));
        fishstation.setFlora(createStringDescriptionTypeFromBiotic3(f.getFlora()));
        fishstation.setGear(createStringDescriptionTypeFromBiotic3(f.getGear()));
        fishstation.setGearcondition(createStringDescriptionTypeFromBiotic3(f.getGearcondition()));
        fishstation.setGearcount(f.getGearcount());
        fishstation.setGearno(f.getGearno());
        fishstation.setGearspeed(f.getGearflow());
        fishstation.setHaulvalidity(createStringDescriptionTypeFromBiotic3(f.getHaulvalidity()));
        fishstation.setLandingsite(createStringDescriptionTypeFromBiotic3(f.getLandingsite()));
        fishstation.setLatitudeend(f.getLatitudeend());
        fishstation.setLatitudestart(f.getLatitudestart());
        fishstation.setLocation(f.getLocation());
        fishstation.setLongitudeend(f.getLongitudeend());
        fishstation.setLongitudestart(f.getLongitudestart());
        fishstation.setNation(createStringDescriptionTypeFromBiotic3(f.getNation()));
        fishstation.setTrawlquality(createStringDescriptionTypeFromBiotic3(f.getSamplequality()));
        fishstation.setSea(createStringDescriptionTypeFromBiotic3(f.getSea()));
        fishstation.setSerialno(f.getSerialno());
        fishstation.setSoaktime(f.getSoaktime());
        fishstation.setStartdate(this.convertDateFromBiotic3(f.getStartdate()));
        fishstation.setStartlog(f.getStartlog());
        fishstation.setStarttime(this.convertTimeFromBiotic3(f.getStarttime()));
        fishstation.setStation(f.getStation());
        fishstation.setStationtype(createStringDescriptionTypeFromBiotic3(f.getStationtype()));
        fishstation.setStopdate(this.convertDateFromBiotic3(f.getStopdate()));
        fishstation.setStoplog(f.getStoplog());
        fishstation.setStoptime(this.convertTimeFromBiotic3(f.getStoptime()));
        fishstation.setSystem(f.getSystem());
        fishstation.setTrawlopening(f.getVerticaltrawlopening());
        fishstation.setTrawlopeningsd(f.getVerticaltrawlopeningsd());
        fishstation.setTripno(f.getTripno());
        fishstation.setVegetationcover(createStringDescriptionTypeFromBiotic3(f.getVegetationcover()));
        fishstation.setVisibility(createStringDescriptionTypeFromBiotic3(f.getVisibility()));
        fishstation.setWaterlevel(f.getWaterlevel());
        fishstation.setWeather(createStringDescriptionTypeFromBiotic3(f.getWeather()));
        fishstation.setWinddirection(f.getWinddirection());
        fishstation.setWindspeed(f.getWindspeed());
        if (f.getWirelength() != null) {
            fishstation.setWirelength(f.getWirelength().toBigInteger());
        } else {
            fishstation.setWirelength(null);
        }

        for (BioticTypes.v3_beta.CatchsampleType c : f.getCatchsample()) {
            fishstation.getCatchsample().add(convertCatchsampleFromBiotic3(c));
        }

        return fishstation;
    }

    private StringDescriptionType createStringDescriptionTypeFromBiotic3(String string) {
        if (string == null) {
            return null;
        }
        StringDescriptionType sd = this.biotic1factory.createStringDescriptionType();
        sd.setValue(string);

        return sd;
    }

    private CatchsampleType convertCatchsampleFromBiotic3(BioticTypes.v3_beta.CatchsampleType c) throws BioticConversionException {
        CatchsampleType catchsample = this.biotic1factory.createCatchsampleType();

        catchsample.setAbundancecategory(this.createStringDescriptionTypeFromBiotic3(c.getAbundancecategory()));
        catchsample.setAphia(c.getAphia());
        catchsample.setComment(c.getComment());
        catchsample.setConservation(this.createStringDescriptionTypeFromBiotic3(c.getConservation()));
        catchsample.setCount(c.getCount());
        catchsample.setForeignobject(this.createStringDescriptionTypeFromBiotic3(c.getForeignobject()));
        catchsample.setGenetics(this.createStringDescriptionTypeFromBiotic3(c.getTissuesample()));
        catchsample.setGroup(this.createStringDescriptionTypeFromBiotic3(c.getGroup()));
        catchsample.setAgingstructure(this.createStringDescriptionTypeFromBiotic3(c.getAgingstructure()));
        catchsample.setLengthmeasurement(this.createStringDescriptionTypeFromBiotic3(c.getLengthmeasurement()));
        catchsample.setLengthsamplecount(c.getLengthsamplecount());
        catchsample.setLengthsamplevolume(c.getLengthsamplevolume());
        catchsample.setLengthsampleweight(c.getLengthsampleweight());
        catchsample.setNoname(c.getNorwegianname());
        catchsample.setParasite(this.createStringDescriptionTypeFromBiotic3(c.getParasite()));
        catchsample.setProducttype(this.createStringDescriptionTypeFromBiotic3((c.getProducttype())));
        catchsample.setRaisingfactor(c.getRaisingfactor());
        catchsample.setSampleproducttype(this.createStringDescriptionTypeFromBiotic3((c.getSampleproducttype())));
        catchsample.setSamplenumber(c.getSamplenumber());
        catchsample.setSampletype(this.createStringDescriptionTypeFromBiotic3(c.getSampletype()));

        catchsample.setSpecies(c.getSpecies());
        catchsample.setSpecimensamplecount(c.getSpecimensamplecount());
        catchsample.setStomach(this.createStringDescriptionTypeFromBiotic3(c.getStomach()));
        catchsample.setVolume(c.getVolume());
        catchsample.setWeight(c.getWeight());

        //get individuals
        for (BioticTypes.v3_beta.IndividualType i : c.getIndividual()) {
            catchsample.getIndividual().add(this.convertIndividualFromBiotic3(i));
        }

        // get prey
        for (BioticTypes.v3_beta.IndividualType i : c.getIndividual()) {
            for (BioticTypes.v3_beta.PreyType p3 : i.getPrey()) {
                PreyType p = this.convertPreyFromBiotic3(p3);
                p.setFishno(i.getSpecimenno());
                catchsample.getPrey().add(p);
            }
        }

        return catchsample;
    }

    private PreyType convertPreyFromBiotic3(BioticTypes.v3_beta.PreyType p) throws BioticConversionException {
        PreyType prey = this.biotic1factory.createPreyType();
        prey.setDevstage(this.createStringDescriptionTypeFromBiotic3(p.getDevstage()));
        prey.setDigestion(this.createStringDescriptionTypeFromBiotic3(p.getDigestion()));
        prey.setInterval(this.createStringDescriptionTypeFromBiotic3((p.getInterval())));
        prey.setLengthmeasurement(this.createStringDescriptionTypeFromBiotic3(p.getLengthmeasurement()));
        prey.setPartno(p.getPartno());
        prey.setSpecies(p.getSpecies());
        prey.setTotalcount(p.getTotalcount());
        prey.setTotalweight(p.getTotalweight());
        prey.setWeightresolution(this.createStringDescriptionTypeFromBiotic3(p.getWeightresolution()));

        Set<BigDecimal> lengths = new HashSet<>();
        for (BioticTypes.v3_beta.PreylengthType pl : p.getPreylength()) {
            prey.getPreylength().add(this.convertPreyLengthFromBiotic3(pl));

            if (lengths.contains(pl.getLength())) {
                throw new BioticConversionException("Conversion results in non.unique keys for preylength");
            }
            lengths.add(pl.getLength());
        }

        for (BioticTypes.v3_beta.CopepodedevstageType cds : p.getCopepodedevstage()) {
            prey.getCopepodedevstage().add(this.convertCopepodedevstageFromBiotic3(cds));
        }

        return prey;
    }

    private IndividualType convertIndividualFromBiotic3(BioticTypes.v3_beta.IndividualType i) {
        IndividualType individual = this.biotic1factory.createIndividualType();
        individual.setAbdomenwidth(i.getAbdomenwidth());
        individual.setBlackspot(this.createStringDescriptionTypeFromBiotic3(i.getBlackspot()));
        individual.setCarapacelength(i.getCarapacelength());
        individual.setCarapacewidth(i.getCarapacewidth());
        individual.setComment(i.getComment());
        individual.setDiameter(i.getDiameter());
        individual.setDigestion(this.createStringDescriptionTypeFromBiotic3(i.getDigestion()));
        individual.setEggstage(this.createStringDescriptionTypeFromBiotic3(i.getEggstage()));
        individual.setFat(this.createStringDescriptionTypeFromBiotic3(i.getFat()));
        individual.setFatpercent(i.getFatpercent());
        individual.setForklength(i.getForklength());
        individual.setFungusheart(this.createStringDescriptionTypeFromBiotic3(i.getFungusheart()));
        individual.setFungusouter(this.createStringDescriptionTypeFromBiotic3(i.getFungusouter()));
        individual.setFungusspores(this.createStringDescriptionTypeFromBiotic3(i.getFungusspores()));
        individual.setGeneticsnumber(i.getTissuesamplenumber());
        individual.setGillworms(this.createStringDescriptionTypeFromBiotic3(i.getGillworms()));
        individual.setGonadweight(i.getGonadweight());
        individual.setHeadlength(i.getHeadlength());
        individual.setJapanesecut(i.getJapanesecut());
        individual.setLength(i.getLength());
        individual.setLengthunit(this.createStringDescriptionTypeFromBiotic3(i.getLengthresolution()));
        individual.setLengthwithouthead(i.getLengthwithouthead());
        individual.setLiver(this.createStringDescriptionTypeFromBiotic3(i.getLiver()));
        individual.setLiverparasite(this.createStringDescriptionTypeFromBiotic3(i.getLiverparasite()));
        individual.setLiverweight(i.getLiverweight());
        individual.setMantlelength(i.getMantlelength());
        individual.setMeroslength(i.getMeroslength());
        individual.setMeroswidth(i.getMeroswidth());
        individual.setMoultingstage(this.createStringDescriptionTypeFromBiotic3(i.getMoultingstage()));
        individual.setProducttype(this.createStringDescriptionTypeFromBiotic3(i.getProducttype()));
        individual.setRightclawlength(i.getRightclawlength());
        individual.setRightclawwidth(i.getRightclawwidth());
        individual.setSex(this.createStringDescriptionTypeFromBiotic3(i.getSex()));
        individual.setSnouttoanalfin(i.getSnouttoanalfin());
        individual.setSnouttoboneknob(i.getSnouttoboneknob());
        individual.setSnouttodorsalfin(i.getSnouttodorsalfin());
        individual.setSnouttoendoftail(i.getSnouttoendoftail());
        individual.setSnouttoendsqueezed(i.getSnouttoendsqueezed());
        individual.setSpecialstage(this.createStringDescriptionTypeFromBiotic3(i.getSpecialstage()));
        individual.setSpecimenno(i.getSpecimenno());
        individual.setStage(this.createStringDescriptionTypeFromBiotic3(i.getMaturationstage()));
        individual.setStomachfillfield(this.createStringDescriptionTypeFromBiotic3(i.getStomachfillfield()));
        individual.setStomachfilllab(this.createStringDescriptionTypeFromBiotic3(i.getStomachfilllab()));
        individual.setStomachweight(i.getStomachweight());
        individual.setSwollengills(this.createStringDescriptionTypeFromBiotic3(i.getSwollengills()));
        individual.setVertebrae(i.getVertebrae());
        individual.setVolume(i.getVolume());
        individual.setWeight(i.getWeight());

        for (BioticTypes.v3_beta.AgedeterminationType a : i.getAgedetermination()) {
            individual.getAgedetermination().add(this.convertAgedeterminationFromBiotic3(a));
        }

        for (BioticTypes.v3_beta.TagType t : i.getTag()) {
            individual.getTag().add(this.convertTagFromBiotic3(t));
        }

        return individual;
    }

    private PreylengthType convertPreyLengthFromBiotic3(BioticTypes.v3_beta.PreylengthType pl) {
        PreylengthType preylength = this.biotic1factory.createPreylengthType();
        preylength.setCount(pl.getCount());
        preylength.setLength(pl.getLength());

        return preylength;
    }

    private CopepodedevstageType convertCopepodedevstageFromBiotic3(BioticTypes.v3_beta.CopepodedevstageType co) {
        CopepodedevstageType cop = this.biotic1factory.createCopepodedevstageType();
        cop.setCopepodedevstage(co.getCopepodedevstage());
        cop.setCount(co.getCount());
        return cop;
    }

    private AgedeterminationType convertAgedeterminationFromBiotic3(BioticTypes.v3_beta.AgedeterminationType a) {
        AgedeterminationType age = this.biotic1factory.createAgedeterminationType();
        age.setAge(a.getAge());
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
        age.setOtolithcentre(this.createStringDescriptionTypeFromBiotic3(a.getOtolithcentre()));
        age.setOtolithedge(this.createStringDescriptionTypeFromBiotic3(a.getOtolithedge()));
        age.setOtolithtype(this.createStringDescriptionTypeFromBiotic3(a.getOtolithtype()));
        age.setReadability(this.createStringDescriptionTypeFromBiotic3(a.getReadability()));
        age.setSpawningage(a.getSpawningage());
        age.setSpawningzones(a.getSpawningzones());

        return age;
    }

    private TagType convertTagFromBiotic3(BioticTypes.v3_beta.TagType t) {
        TagType tag = this.biotic1factory.createTagType();
        tag.setTagno(t.getTagno());
        tag.setTagtype(this.createStringDescriptionTypeFromBiotic3(t.getTagtype()));
        return tag;
    }

    private String convertDateFromBiotic3(XMLGregorianCalendar datev3) throws BioticConversionException {

        if (datev3 == null) {
            return null;
        }

        String date = datev3.toString();
        if (date.endsWith("Z")) {
            date = date.substring(0, date.length()-1);
        }
        String[] datetab = date.split("-");
        if (datetab.length != 3) {
            throw new BioticConversionException("Malformed date: " + datev3.toString());
        }
        date = datetab[2] + "/" + datetab[1] + "/" + datetab[0];
        return date;
    }

    private String convertTimeFromBiotic3(XMLGregorianCalendar timev3) {
        if (timev3 == null) {
            return null;
        }
        String time = timev3.toString();
        if (time.endsWith("Z")) {
            time = time.substring(0, time.length()-1);
        }
        return (time);
    }
}
