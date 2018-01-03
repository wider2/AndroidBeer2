package beer.api.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Beer {
 
    //public Integer count;
    //public String name;

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "beer_name")
    private String beerName;

    @ColumnInfo(name = "tagline")
    private String tagLine;

    @ColumnInfo(name = "first_brewed")
    private String firstBrewed;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "abv")
    private double abv;

    @ColumnInfo(name = "ibu")
    private double ibu;

    @ColumnInfo(name = "target_fg")
    private double targetFg;

    @ColumnInfo(name = "target_og")
    private double targetOg;

    @ColumnInfo(name = "ebc")
    private double ebc;

    @ColumnInfo(name = "srm")
    private double srm;

    @ColumnInfo(name = "ph")
    private double ph;

    @ColumnInfo(name = "attenuation_level")
    private double attenuationLevel;


    @ColumnInfo(name = "volume_value")
    private int volumeValue;

    @ColumnInfo(name = "volume_unit")
    private String volumeUnit;


    @ColumnInfo(name = "boil_volume")
    private int boilVolume;

    @ColumnInfo(name = "boil_volume_unit")
    private String boilVolumeUnit;



    @ColumnInfo(name = "mash_temp_value")
    private int mashTempValue;

    @ColumnInfo(name = "mash_temp_unit")
    private String mashTempUnit;

    @ColumnInfo(name = "mash_duration")
    private int mashDuration;

    @ColumnInfo(name = "fermentation_temp_value")
    private int fermentationTempValue;

    @ColumnInfo(name = "fermentation_temp_unit")
    private String fermentationTempUnit;



    @ColumnInfo(name = "yeast")
    private String yeast;

    @ColumnInfo(name = "food_pairing")
    private String foodPairing;

    @ColumnInfo(name = "brewers_tips")
    private String brewersTips;

    @ColumnInfo(name = "contributed_by")
    private String contributedBy;


    @ColumnInfo(name = "favorite")
    private int favorite;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getFirstBrewed() {
        return firstBrewed;
    }

    public void setFirstBrewed(String firstBrewed) {
        this.firstBrewed = firstBrewed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public double getIbu() {
        return ibu;
    }

    public void setIbu(double ibu) {
        this.ibu = ibu;
    }

    public double getTargetOg() {
        return targetOg;
    }

    public void setTargetOg(double targetOg) {
        this.targetOg = targetOg;
    }

    public double getTargetFg() {
        return targetFg;
    }

    public void setTargetFg(double targetFg) {
        this.targetFg = targetFg;
    }


    public double getEbc() {
        return ebc;
    }

    public void setEbc(double ebc) {
        this.ebc = ebc;
    }

    public double getSrm() {
        return srm;
    }

    public void setSrm(double srm) {
        this.srm = srm;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public double getAttenuationLevel() {
        return attenuationLevel;
    }

    public void setAttenuationLevel(double attenuationLevel) {
        this.attenuationLevel = attenuationLevel;
    }


    public int getVolumeValue() {
        return volumeValue;
    }

    public void setVolumeValue(int volumeValue) {
        this.volumeValue = volumeValue;
    }

    public String getVolumeUnit() {
        return volumeUnit;
    }

    public void setVolumeUnit(String volumeUnit) {
        this.volumeUnit = volumeUnit;
    }


    public int getBoilVolume() {
        return boilVolume;
    }

    public void setBoilVolume(int boilVolume) {
        this.boilVolume = boilVolume;
    }

    public String getBoilVolumeUnit() {
        return boilVolumeUnit;
    }

    public void setBoilVolumeUnit(String boilVolumeUnit) {
        this.boilVolumeUnit = boilVolumeUnit;
    }




    public int getMashTempValue() {
        return mashTempValue;
    }

    public void setMashTempValue(int mashTempValue) {
        this.mashTempValue = mashTempValue;
    }

    public String getMashTempUnit() {
        return mashTempUnit;
    }

    public void setMashTempUnit(String mashTempUnit) {
        this.mashTempUnit = mashTempUnit;
    }


    public int getMashDuration() {
        return mashDuration;
    }

    public void setMashDuration(int mashDuration) {
        this.mashDuration = mashDuration;
    }


    public int getFermentationTempValue() {
        return fermentationTempValue;
    }

    public void setFermentationTempValue(int fermentationTempValue) {
        this.fermentationTempValue = fermentationTempValue;
    }

    public String getFermentationTempUnit() {
        return fermentationTempUnit;
    }

    public void setFermentationTempUnit(String fermentationTempUnit) {
        this.fermentationTempUnit = fermentationTempUnit;
    }




    public String getYeast() {
        return yeast;
    }

    public void setYeast(String yeast) {
        this.yeast = yeast;
    }


    public String getBrewersTips() {
        return brewersTips;
    }

    public void setBrewersTips(String brewersTips) {
        this.brewersTips = brewersTips;
    }

    public String getFoodPairing() {
        return foodPairing;
    }

    public void setFoodPairing(String foodPairing) {
        this.foodPairing = foodPairing;
    }

    public String getContributedBy() {
        return contributedBy;
    }

    public void setContributedBy(String contributedBy) {
        this.contributedBy = contributedBy;
    }


    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

}