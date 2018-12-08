/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.ArtType;
import LandingsTypes.v2.DellandingType;
import LandingsTypes.v2.FangstdataType;
import LandingsTypes.v2.FartøyType;
import LandingsTypes.v2.FiskerType;
import LandingsTypes.v2.KvoteType;
import LandingsTypes.v2.LandingOgProduksjonType;
import LandingsTypes.v2.LandingsdataType;
import LandingsTypes.v2.MottakendeFartøyType;
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
            linje.setFartøy(this.processFartøy(seddel));
            linje.setFisker(this.processFisker(seddel));
            linje.setMottakendefartøy(this.processMottakendeFartøy(seddel));
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
            linje.setDokumenttypeBokmål(parseString(seddel[this.indexMap.get("Dokumenttype (bokmål)")]));
            linje.setDokumentVersjonsnummer(this.parseBigInteger(seddel[this.indexMap.get("Dokument versjonsnummer")]));
            linje.setDokumentFormulardato(parseString(seddel[this.indexMap.get("Dokument formulardato")]));
            linje.setDokumentElektroniskDato(parseString(seddel[this.indexMap.get("Dokument elektronisk dato")]));
            linje.setFangstår(this.parseBigInteger(seddel[this.indexMap.get("Fangstår")]));
            linje.setHovedområdeKode(seddel[this.indexMap.get("Hovedområde (kode)")]);
            linje.setKystHavKode(this.parseBigInteger(seddel[this.indexMap.get("Kyst/hav (kode)")]));
            linje.setLokasjonKode(parseString(seddel[this.indexMap.get("Lokasjon (kode)")]));
            linje.setSisteFangstdato(parseDate(seddel[this.indexMap.get("Siste fangstdato")]));
            linje.setFartøynasjonalitetKode(parseString(seddel[this.indexMap.get("Fartøynasjonalitet (kode)")]));
            linje.setRegistreringsmerkeSeddel(parseString(seddel[this.indexMap.get("Registreringsmerke (seddel)")]));
            linje.setStørsteLengde(this.parseDouble(seddel[this.indexMap.get("Største lengde")]));
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
            fangst.setHovedområdeBokmål(parseString(seddel[this.indexMap.get("Hovedområde (bokmål)")]));
            fangst.setHovedområdeFAOBokmål(parseString(seddel[this.indexMap.get("Hovedområde FAO (bokmål)")]));
            fangst.setHovedområdeFAOKode(parseString(seddel[this.indexMap.get("Hovedområde FAO (kode)")]));

            fangst.setNordSørFor62GraderNord(parseString(seddel[this.indexMap.get("Nord/sør for 62 grader nord")]));
            fangst.setOmrådegrupperingBokmål(parseString(seddel[this.indexMap.get("Områdegruppering (bokmål)")]));
            fangst.setSoneBokmål(parseString(seddel[this.indexMap.get("Sone (bokmål)")]));
            fangst.setSoneKode(parseString(seddel[this.indexMap.get("Sone (kode)")]));

            return fangst;
        }

        private FartøyType processFartøy(String[] seddel) {
            FartøyType fartøy = this.landingsfactory.createFartøyType();
            fartøy.setBruttotonnasje1969(this.parseBigInteger(seddel[this.indexMap.get("Bruttotonnasje 1969")]));
            fartøy.setBruttotonnasjeAnnen(this.parseBigInteger(seddel[this.indexMap.get("Bruttotonnasje annen")]));
            fartøy.setByggeår(this.parseBigInteger(seddel[this.indexMap.get("Byggeår")]));
            fartøy.setFartøyGjelderFraDato(parseString(seddel[this.indexMap.get("Fartøy gjelder fra dato")]));
            fartøy.setFartøyGjelderTilDato(parseString(seddel[this.indexMap.get("Fartøy gjelder til dato")]));
            fartøy.setFartøyID(parseString(seddel[this.indexMap.get("Fartøy ID")]));
            fartøy.setFartøyfylke(parseString(seddel[this.indexMap.get("Fartøyfylke")]));
            fartøy.setFartøyfylkeKode(this.parseBigInteger(seddel[this.indexMap.get("Fartøyfylke (kode)")]));
            fartøy.setFartøykommune(parseString(seddel[this.indexMap.get("Fartøykommune")]));
            fartøy.setFartøykommuneKode(this.parseBigInteger(seddel[this.indexMap.get("Fartøykommune (kode)")]));
            fartøy.setFartøynasjonalitetBokmål(parseString(seddel[this.indexMap.get("Fartøynasjonalitet (bokmål)")]));
            fartøy.setFartøynavn(parseString(seddel[this.indexMap.get("Fartøynavn")]));
            fartøy.setFartøytypeBokmål(parseString(seddel[this.indexMap.get("Fartøytype (bokmål)")]));
            fartøy.setFartøytypeKode(parseString(seddel[this.indexMap.get("Fartøytype (kode)")]));
            fartøy.setLengdegruppeKode(parseString(seddel[this.indexMap.get("Lengdegruppe (kode)")]));
            fartøy.setLengdegruppeBokmål(parseString(seddel[this.indexMap.get("Lengdegruppe (bokmål)")]));
            fartøy.setMotorbyggeår(this.parseBigInteger(seddel[this.indexMap.get("Motorbyggeår")]));
            fartøy.setMotorkraft(this.parseBigInteger(seddel[this.indexMap.get("Motorkraft")]));
            fartøy.setOmbyggingsår(this.parseBigInteger(seddel[this.indexMap.get("Ombyggingsår")]));
            fartøy.setRadiokallesignalSeddel(parseString(seddel[this.indexMap.get("Radiokallesignal (seddel)")]));
            return fartøy;
        }

        private FiskerType processFisker(String[] seddel) {
            FiskerType fisker = this.landingsfactory.createFiskerType();
            fisker.setFiskerkommune(parseString(seddel[this.indexMap.get("Fiskerkommune")]));
            fisker.setFiskerkommuneKode(this.parseBigInteger(seddel[this.indexMap.get("Fiskerkommune (kode)")]));
            fisker.setFiskernasjonalitetBokmål(parseString(seddel[this.indexMap.get("Fiskernasjonalitet (bokmål)")]));
            fisker.setFiskernasjonalitetKode(parseString(seddel[this.indexMap.get("Fiskernasjonalitet (kode)")]));

            return fisker;
        }

        private MottakendeFartøyType processMottakendeFartøy(String[] seddel) {
            MottakendeFartøyType mfart = this.landingsfactory.createMottakendeFartøyType();
            mfart.setMottakendeFartøyRKAL(parseString(seddel[this.indexMap.get("Mottakende fartøy rkal")]));
            mfart.setMottakendeFartøyRegMerke(parseString(seddel[this.indexMap.get("Mottakende fartøy reg.merke")]));
            mfart.setMottakendeFartøynasjBokmål(parseString(seddel[this.indexMap.get("Mottakende fart.nasj (bokmål)")]));
            mfart.setMottakendeFartøynasjKode(parseString(seddel[this.indexMap.get("Mottakende fartøynasj. (kode)")]));
            mfart.setMottakendeFartøytypeBokmål(parseString(seddel[this.indexMap.get("Mottakende fart.type (bokmål)")]));
            mfart.setMottakendeFartøytypeKode(parseString(seddel[this.indexMap.get("Mottakende fartøytype (kode)")]));

            return mfart;
        }

        private MottakerType processMottaker(String[] seddel) {
            MottakerType mottaker = this.landingsfactory.createMottakerType();
            mottaker.setMottakernasjonalitetBokmål(parseString(seddel[this.indexMap.get("Mottakernasjonalitet (bokmål)")]));
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
            produksjon.setLandingsnasjonBokmål(parseString(seddel[this.indexMap.get("Landingsnasjon (bokmål)")]));
            produksjon.setLandingsnasjonKode(parseString(seddel[this.indexMap.get("Landingsnasjon (kode)")]));
            produksjon.setProduksjonsanlegg(parseString(seddel[this.indexMap.get("Produksjonsanlegg")]));
            produksjon.setProduksjonskommune(parseString(seddel[this.indexMap.get("Produksjonskommune")]));
            produksjon.setProduksjonskommuneKode(parseString(seddel[this.indexMap.get("Produksjonskommune (kode)")]));

            return produksjon;
        }

        private ProduktType processProdukt(String[] seddel) {
            ProduktType produkt = this.landingsfactory.createProduktType();
            produkt.setAntallStykk(this.parseBigInteger(seddel[this.indexMap.get("Antall stykk")]));
            produkt.setAnvendelseBokmål(parseString(seddel[this.indexMap.get("Anvendelse (bokmål)")]));
            produkt.setAnvendelseKode(parseString(seddel[this.indexMap.get("Anvendelse (kode)")]));
            produkt.setBruttovekt(this.parseDouble(seddel[this.indexMap.get("Bruttovekt")]));
            produkt.setHovedgruppeAnvendelseBokmål(parseString(seddel[this.indexMap.get("Hovedgr anvendelse (bokmål)")]));
            produkt.setHovedgruppeAnvendelseKode(parseString(seddel[this.indexMap.get("Hovedgruppe anvendelse (kode)")]));
            produkt.setKonserveringsmåteBokmål(parseString(seddel[this.indexMap.get("Konserveringsmåte (bokmål)")]));
            produkt.setKonserveringsmåteKode(parseString(seddel[this.indexMap.get("Konserveringsmåte (kode)")]));
            produkt.setKvalitetBokmål(parseString(seddel[this.indexMap.get("Kvalitet (bokmål)")]));
            produkt.setKvalitetKode(parseString(seddel[this.indexMap.get("Kvalitet (kode)")]));
            produkt.setLandingsmåteBokmål(parseString(seddel[this.indexMap.get("Landingsmåte (bokmål)")]));
            produkt.setLandingsmåteKode(parseString(seddel[this.indexMap.get("Landingsmåte (kode)")]));
            produkt.setProdukttilstandBokmål(parseString(seddel[this.indexMap.get("Produkttilstand (bokmål)")]));
            produkt.setProdukttilstandKode(parseString(seddel[this.indexMap.get("Produkttilstand (kode)")]));
            produkt.setProduktvekt(this.parseDouble(seddel[this.indexMap.get("Produktvekt")]));
            produkt.setRundvekt(this.parseDouble(seddel[this.indexMap.get("Rundvekt")]));
            produkt.setStørrelsesgrupperingKode(parseString(seddel[this.indexMap.get("Størrelsesgruppering (kode)")]));

            return produkt;
        }

        private RedskapType processRedskap(String[] seddel) {
            RedskapType redskap = this.landingsfactory.createRedskapType();
            redskap.setHovedgruppeRedskapBokmål(parseString(seddel[this.indexMap.get("Hovedgruppe redskap (bokmål)")]));
            redskap.setRedskapBokmål(parseString(seddel[this.indexMap.get("Redskap (bokmål)")]));

            return redskap;
        }

        private KvoteType processKvote(String[] seddel) {
            KvoteType kvote = this.landingsfactory.createKvoteType();
            kvote.setKvotefartøyRegMerke(parseString(seddel[this.indexMap.get("Kvotefartøy reg.merke")]));
            kvote.setKvotetypeBokmål(parseString(seddel[this.indexMap.get("Kvotetype (bokmål)")]));
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
            art.setArtBokmål(parseString(seddel[this.indexMap.get("Art (bokmål)")]));
            art.setArtFAOBokmål(parseString(seddel[this.indexMap.get("Art FAO (bokmål)")]));
            art.setArtFAOKode(parseString(seddel[this.indexMap.get("Art FAO (kode)")]));
            art.setArtsgruppeHistoriskBokmål(parseString(seddel[this.indexMap.get("Artsgruppe historisk (bokmål)")]));
            art.setArtsgruppeHistoriskKode(parseString(seddel[this.indexMap.get("Artsgruppe historisk (kode)")]));
            art.setHovedgruppeArtBokmål(parseString(seddel[this.indexMap.get("Hovedgruppe art (bokmål)")]));
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
