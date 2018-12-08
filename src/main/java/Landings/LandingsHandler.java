/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.ArtType;
import LandingsTypes.v2.DellandingType;
import LandingsTypes.v2.FangstdataType;
import LandingsTypes.v2.Fart�yType;
import LandingsTypes.v2.FiskerType;
import LandingsTypes.v2.KvoteType;
import LandingsTypes.v2.LandingOgProduksjonType;
import LandingsTypes.v2.LandingsdataType;
import LandingsTypes.v2.MottakendeFart�yType;
import LandingsTypes.v2.MottakerType;
import LandingsTypes.v2.ObjectFactory;
import LandingsTypes.v2.ProduktType;
import LandingsTypes.v2.RedskapType;
import LandingsTypes.v2.SalgslagdataType;
import LandingsTypes.v2.SeddellinjeType;
import XMLHandling.NamespaceVersionHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class LandingsHandler extends NamespaceVersionHandler<LandingsdataType> {

    public LandingsHandler() {
        this.latestNamespace = "http://www.imr.no/formats/landinger/v2";
        this.latestBioticClass = LandingsdataType.class;
        this.compatibleNamespaces = null;
    }

    public Iterator<SeddellinjeType> getPSViterator(BufferedReader reader) throws IOException {
        return new PSVierator(reader);
    }

    private static class PSVierator implements Iterator<SeddellinjeType> {

        BufferedReader reader;
        Map<String, Integer> indexMap;
        String delim = "\\|";
        String nextline;
        ObjectFactory landingsfactory = new ObjectFactory();

        /**
         * Initializes iterator. Assumes first call to reader.readLine() will
         * give header line.
         *
         * @param reader
         * @throws IOException
         */
        public PSVierator(BufferedReader reader) throws IOException {
            this.reader = reader;
            String headerstring = reader.readLine();
            if (headerstring == null | headerstring.split(delim).length <= 1) {
                throw new IOException("Bad file format.");
            }
            String[] header = headerstring.split(this.delim);
            header[header.length - 1] = header[header.length - 1].trim();
            this.indexMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                this.indexMap.put(header[i].trim(), i);
            }
            this.nextline = reader.readLine();
        }

        @Override
        public boolean hasNext() {
            if (this.nextline != null) {
                return true;
            }
            return false;
        }

        @Override
        public SeddellinjeType next() {

            SeddellinjeType seddellinje = convertStringToSeddellinje(this.nextline);

            try {
                this.nextline = reader.readLine();
            } catch (IOException ex) {
                throw new NoSuchElementException("Could not advance to next line");
            }
            return seddellinje;
        }

        private SeddellinjeType convertStringToSeddellinje(String seddellinjestring) {
            if (seddellinjestring == null || seddellinjestring.split(delim).length <= 1) {
                throw new NoSuchElementException("Bad format");
            }

            String[] seddel = seddellinjestring.split(this.delim);
            seddel[seddel.length - 1] = seddel[seddel.length - 1].trim();
            SeddellinjeType linje = this.landingsfactory.createSeddellinjeType();
            linje.setLinjenummer(this.parseLong(seddel[this.indexMap.get("Linjenummer")]));
            linje.setDellanding(this.processDellanding(seddel));
            linje.setFangstdata(this.processFangsData(seddel));
            linje.setFart�y(this.processFart�y(seddel));
            linje.setFisker(this.processFisker(seddel));
            linje.setMottakendefart�y(this.processMottakendeFart�y(seddel));
            linje.setMottaker(this.processMottaker(seddel));
            linje.setProduksjon(this.processProduksjon(seddel));
            linje.setProdukt(this.processProdukt(seddel));
            linje.setRedskap(this.processRedskap(seddel));
            linje.setKvote(this.processKvote(seddel));
            linje.setArtKode(parseString(seddel[this.indexMap.get("Art (kode)")]));
            linje.setSalgslagdata(this.processSalgslag(seddel));
            linje.setArt(this.processArt(seddel));
            linje.setDokumentnummer(parseString(seddel[this.indexMap.get("Dokumentnummer")]));
            linje.setDokumenttypeKode(this.parseBigInteger(seddel[this.indexMap.get("Dokumenttype (kode)")]));
            linje.setDokumenttypeBokm�l(parseString(seddel[this.indexMap.get("Dokumenttype (bokm�l)")]));
            linje.setDokumentVersjonsnummer(this.parseBigInteger(seddel[this.indexMap.get("Dokument versjonsnummer")]));
            linje.setDokumentFormulardato(parseString(seddel[this.indexMap.get("Dokument formulardato")]));
            linje.setDokumentElektroniskDato(parseString(seddel[this.indexMap.get("Dokument elektronisk dato")]));
            linje.setFangst�r(this.parseBigInteger(seddel[this.indexMap.get("Fangst�r")]));
            linje.setHovedomr�deKode(seddel[this.indexMap.get("Hovedomr�de (kode)")]);
            linje.setKystHavKode(this.parseBigInteger(seddel[this.indexMap.get("Kyst/hav (kode)")]));
            linje.setLokasjonKode(parseString(seddel[this.indexMap.get("Lokasjon (kode)")]));
            linje.setSisteFangstdato(parseDate(seddel[this.indexMap.get("Siste fangstdato")]));
            linje.setFart�ynasjonalitetKode(parseString(seddel[this.indexMap.get("Fart�ynasjonalitet (kode)")]));
            linje.setRegistreringsmerkeSeddel(parseString(seddel[this.indexMap.get("Registreringsmerke (seddel)")]));
            linje.setSt�rsteLengde(this.parseDouble(seddel[this.indexMap.get("St�rste lengde")]));
            linje.setHovedgruppeRedskapKode(parseString(seddel[this.indexMap.get("Hovedgruppe redskap (kode)")]));
            linje.setRedskapKode(parseString(seddel[this.indexMap.get("Redskap (kode)")]));
            return linje;
        }

        private Long parseLong(String string) {
            if ("".equals(string)) {
                return null;
            }
            return Long.parseLong(string);
        }

        private DellandingType processDellanding(String[] seddel) {
            DellandingType dell = this.landingsfactory.createDellandingType();
            dell.setDellandingSignal(this.parseBigInteger(seddel[this.indexMap.get("Dellanding (signal)")]));
            dell.setForrigeMottakstasjon(parseString(seddel[this.indexMap.get("Neste mottaksstasjon")]));
            dell.setNesteMottaksstasjon(parseString(seddel[this.indexMap.get("Forrige mottakstasjon")]));

            return dell;
        }

        private FangstdataType processFangsData(String[] seddel) {
            FangstdataType fangst = this.landingsfactory.createFangstdataType();
            fangst.setFangstdagbokNummer(this.parseLong(seddel[this.indexMap.get("Fangstdagbok (nummer)")]));
            fangst.setFangstdagbokTurnummer(this.parseLong(seddel[this.indexMap.get("Fangstdagbok (turnummer)")]));
            fangst.setFangstfeltKode(parseString(seddel[this.indexMap.get("Fangstfelt (kode)")]));
            fangst.setHovedomr�deBokm�l(parseString(seddel[this.indexMap.get("Hovedomr�de (bokm�l)")]));
            fangst.setHovedomr�deFAOBokm�l(parseString(seddel[this.indexMap.get("Hovedomr�de FAO (bokm�l)")]));
            fangst.setHovedomr�deFAOKode(parseString(seddel[this.indexMap.get("Hovedomr�de FAO (kode)")]));

            fangst.setNordS�rFor62GraderNord(parseString(seddel[this.indexMap.get("Nord/s�r for 62 grader nord")]));
            fangst.setOmr�degrupperingBokm�l(parseString(seddel[this.indexMap.get("Omr�degruppering (bokm�l)")]));
            fangst.setSoneBokm�l(parseString(seddel[this.indexMap.get("Sone (bokm�l)")]));
            fangst.setSoneKode(parseString(seddel[this.indexMap.get("Sone (kode)")]));

            return fangst;
        }

        private Fart�yType processFart�y(String[] seddel) {
            Fart�yType fart�y = this.landingsfactory.createFart�yType();
            fart�y.setBruttotonnasje1969(this.parseBigInteger(seddel[this.indexMap.get("Bruttotonnasje 1969")]));
            fart�y.setBruttotonnasjeAnnen(this.parseBigInteger(seddel[this.indexMap.get("Bruttotonnasje annen")]));
            fart�y.setBygge�r(this.parseBigInteger(seddel[this.indexMap.get("Bygge�r")]));
            fart�y.setFart�yGjelderFraDato(parseString(seddel[this.indexMap.get("Fart�y gjelder fra dato")]));
            fart�y.setFart�yGjelderTilDato(parseString(seddel[this.indexMap.get("Fart�y gjelder til dato")]));
            fart�y.setFart�yID(parseString(seddel[this.indexMap.get("Fart�y ID")]));
            fart�y.setFart�yfylke(parseString(seddel[this.indexMap.get("Fart�yfylke")]));
            fart�y.setFart�yfylkeKode(this.parseBigInteger(seddel[this.indexMap.get("Fart�yfylke (kode)")]));
            fart�y.setFart�ykommune(parseString(seddel[this.indexMap.get("Fart�ykommune")]));
            fart�y.setFart�ykommuneKode(this.parseBigInteger(seddel[this.indexMap.get("Fart�ykommune (kode)")]));
            fart�y.setFart�ynasjonalitetBokm�l(parseString(seddel[this.indexMap.get("Fart�ynasjonalitet (bokm�l)")]));
            fart�y.setFart�ynavn(parseString(seddel[this.indexMap.get("Fart�ynavn")]));
            fart�y.setFart�ytypeBokm�l(parseString(seddel[this.indexMap.get("Fart�ytype (bokm�l)")]));
            fart�y.setFart�ytypeKode(parseString(seddel[this.indexMap.get("Fart�ytype (kode)")]));
            fart�y.setLengdegruppeKode(parseString(seddel[this.indexMap.get("Lengdegruppe (kode)")]));
            fart�y.setLengdegruppeBokm�l(parseString(seddel[this.indexMap.get("Lengdegruppe (bokm�l)")]));
            fart�y.setMotorbygge�r(this.parseBigInteger(seddel[this.indexMap.get("Motorbygge�r")]));
            fart�y.setMotorkraft(this.parseBigInteger(seddel[this.indexMap.get("Motorkraft")]));
            fart�y.setOmbyggings�r(this.parseBigInteger(seddel[this.indexMap.get("Ombyggings�r")]));
            fart�y.setRadiokallesignalSeddel(parseString(seddel[this.indexMap.get("Radiokallesignal (seddel)")]));
            return fart�y;
        }

        private FiskerType processFisker(String[] seddel) {
            FiskerType fisker = this.landingsfactory.createFiskerType();
            fisker.setFiskerkommune(parseString(seddel[this.indexMap.get("Fiskerkommune")]));
            fisker.setFiskerkommuneKode(this.parseBigInteger(seddel[this.indexMap.get("Fiskerkommune (kode)")]));
            fisker.setFiskernasjonalitetBokm�l(parseString(seddel[this.indexMap.get("Fiskernasjonalitet (bokm�l)")]));
            fisker.setFiskernasjonalitetKode(parseString(seddel[this.indexMap.get("Fiskernasjonalitet (kode)")]));

            return fisker;
        }

        private MottakendeFart�yType processMottakendeFart�y(String[] seddel) {
            MottakendeFart�yType mfart = this.landingsfactory.createMottakendeFart�yType();
            mfart.setMottakendeFart�yRKAL(parseString(seddel[this.indexMap.get("Mottakende fart�y rkal")]));
            mfart.setMottakendeFart�yRegMerke(parseString(seddel[this.indexMap.get("Mottakende fart�y reg.merke")]));
            mfart.setMottakendeFart�ynasjBokm�l(parseString(seddel[this.indexMap.get("Mottakende fart.nasj (bokm�l)")]));
            mfart.setMottakendeFart�ynasjKode(parseString(seddel[this.indexMap.get("Mottakende fart�ynasj. (kode)")]));
            mfart.setMottakendeFart�ytypeBokm�l(parseString(seddel[this.indexMap.get("Mottakende fart.type (bokm�l)")]));
            mfart.setMottakendeFart�ytypeKode(parseString(seddel[this.indexMap.get("Mottakende fart�ytype (kode)")]));

            return mfart;
        }

        private MottakerType processMottaker(String[] seddel) {
            MottakerType mottaker = this.landingsfactory.createMottakerType();
            mottaker.setMottakernasjonalitetBokm�l(parseString(seddel[this.indexMap.get("Mottakernasjonalitet (bokm�l)")]));
            mottaker.setMottakernasjonalitetKode(parseString(seddel[this.indexMap.get("Mottakernasjonalitet (kode)")]));
            mottaker.setMottaksstasjon(parseString(seddel[this.indexMap.get("Mottaksstasjon")]));

            return mottaker;
        }

        private LandingOgProduksjonType processProduksjon(String[] seddel) {
            LandingOgProduksjonType produksjon = this.landingsfactory.createLandingOgProduksjonType();
            produksjon.setLandingsdato(parseDate(seddel[this.indexMap.get("Landingsdato")]));
            produksjon.setLandingsfylke(parseString(seddel[this.indexMap.get("Landingsfylke")]));
            produksjon.setLandingsfylkeKode(this.parseBigInteger(seddel[this.indexMap.get("Landingsfylke (kode)")]));
            produksjon.setLandingsklokkeslett(parseString(seddel[this.indexMap.get("Landingsklokkeslett")]));
            produksjon.setLandingskommune(parseString(seddel[this.indexMap.get("Landingskommune")]));
            produksjon.setLandingskommuneKode(this.parseBigInteger(seddel[this.indexMap.get("Landingskommune (kode)")]));
            produksjon.setLandingsnasjonBokm�l(parseString(seddel[this.indexMap.get("Landingsnasjon (bokm�l)")]));
            produksjon.setLandingsnasjonKode(parseString(seddel[this.indexMap.get("Landingsnasjon (kode)")]));
            produksjon.setProduksjonsanlegg(parseString(seddel[this.indexMap.get("Produksjonsanlegg")]));
            produksjon.setProduksjonskommune(parseString(seddel[this.indexMap.get("Produksjonskommune")]));
            produksjon.setProduksjonskommuneKode(parseString(seddel[this.indexMap.get("Produksjonskommune (kode)")]));

            return produksjon;
        }

        private ProduktType processProdukt(String[] seddel) {
            ProduktType produkt = this.landingsfactory.createProduktType();
            produkt.setAntallStykk(this.parseBigInteger(seddel[this.indexMap.get("Antall stykk")]));
            produkt.setAnvendelseBokm�l(parseString(seddel[this.indexMap.get("Anvendelse (bokm�l)")]));
            produkt.setAnvendelseKode(parseString(seddel[this.indexMap.get("Anvendelse (kode)")]));
            produkt.setBruttovekt(this.parseDouble(seddel[this.indexMap.get("Bruttovekt")]));
            produkt.setHovedgruppeAnvendelseBokm�l(parseString(seddel[this.indexMap.get("Hovedgr anvendelse (bokm�l)")]));
            produkt.setHovedgruppeAnvendelseKode(parseString(seddel[this.indexMap.get("Hovedgruppe anvendelse (kode)")]));
            produkt.setKonserveringsm�teBokm�l(parseString(seddel[this.indexMap.get("Konserveringsm�te (bokm�l)")]));
            produkt.setKonserveringsm�teKode(parseString(seddel[this.indexMap.get("Konserveringsm�te (kode)")]));
            produkt.setKvalitetBokm�l(parseString(seddel[this.indexMap.get("Kvalitet (bokm�l)")]));
            produkt.setKvalitetKode(parseString(seddel[this.indexMap.get("Kvalitet (kode)")]));
            produkt.setLandingsm�teBokm�l(parseString(seddel[this.indexMap.get("Landingsm�te (bokm�l)")]));
            produkt.setLandingsm�teKode(parseString(seddel[this.indexMap.get("Landingsm�te (kode)")]));
            produkt.setProdukttilstandBokm�l(parseString(seddel[this.indexMap.get("Produkttilstand (bokm�l)")]));
            produkt.setProdukttilstandKode(parseString(seddel[this.indexMap.get("Produkttilstand (kode)")]));
            produkt.setProduktvekt(this.parseDouble(seddel[this.indexMap.get("Produktvekt")]));
            produkt.setRundvekt(this.parseDouble(seddel[this.indexMap.get("Rundvekt")]));
            produkt.setSt�rrelsesgrupperingKode(parseString(seddel[this.indexMap.get("St�rrelsesgruppering (kode)")]));

            return produkt;
        }

        private RedskapType processRedskap(String[] seddel) {
            RedskapType redskap = this.landingsfactory.createRedskapType();
            redskap.setHovedgruppeRedskapBokm�l(parseString(seddel[this.indexMap.get("Hovedgruppe redskap (bokm�l)")]));
            redskap.setRedskapBokm�l(parseString(seddel[this.indexMap.get("Redskap (bokm�l)")]));

            return redskap;
        }

        private KvoteType processKvote(String[] seddel) {
            KvoteType kvote = this.landingsfactory.createKvoteType();
            kvote.setKvotefart�yRegMerke(parseString(seddel[this.indexMap.get("Kvotefart�y reg.merke")]));
            kvote.setKvotetypeBokm�l(parseString(seddel[this.indexMap.get("Kvotetype (bokm�l)")]));
            kvote.setKvotetypeKode(this.parseString(seddel[this.indexMap.get("Kvotetype (kode)")]));

            return kvote;
        }

        private String parseString(String string) {
            if (string == null) {
                return null;
            }
            return string.trim();
        }

        private SalgslagdataType processSalgslag(String[] seddel) {
            SalgslagdataType salgslag = this.landingsfactory.createSalgslagdataType();
            salgslag.setSalgslag(parseString(seddel[this.indexMap.get("Salgslag")]));
            salgslag.setSalgslagID(this.parseBigInteger(seddel[this.indexMap.get("Salgslag ID")]));
            salgslag.setSalgslagKode(parseString(seddel[this.indexMap.get("Salgslag (kode)")]));
            return salgslag;
        }

        private ArtType processArt(String[] seddel) {
            ArtType art = this.landingsfactory.createArtType();
            art.setArtBokm�l(parseString(seddel[this.indexMap.get("Art (bokm�l)")]));
            art.setArtFAOBokm�l(parseString(seddel[this.indexMap.get("Art FAO (bokm�l)")]));
            art.setArtFAOKode(parseString(seddel[this.indexMap.get("Art FAO (kode)")]));
            art.setArtsgruppeHistoriskBokm�l(parseString(seddel[this.indexMap.get("Artsgruppe historisk (bokm�l)")]));
            art.setArtsgruppeHistoriskKode(parseString(seddel[this.indexMap.get("Artsgruppe historisk (kode)")]));
            art.setHovedgruppeArtBokm�l(parseString(seddel[this.indexMap.get("Hovedgruppe art (bokm�l)")]));
            art.setHovedgruppeArtKode(parseString(seddel[this.indexMap.get("Hovedgruppe art (kode)")]));

            return art;
        }

        private Integer parseBigInteger(String string) {
            if ("".equals(string)) {
                return null;
            }
            return Integer.parseInt(string);
        }

        private LocalDate parseDate(String string) {
            if ("".equals(string)) {
                return null;
            }
            LocalDate d = null;
            try {
                d = (new adapters.DateStringDMYAdapter()).unmarshal(string);
            } catch (Exception e) {
                throw new NoSuchElementException("Bad date format");
            }
            return d;
        }

        private Double parseDouble(String string) {
            if ("".equals(string)) {
                return null;
            }
            return Double.parseDouble(string.replace(",", "."));
        }
    }
}
