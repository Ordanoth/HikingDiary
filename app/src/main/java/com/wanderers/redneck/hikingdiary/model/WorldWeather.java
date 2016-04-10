package com.wanderers.redneck.hikingdiary.model;

/**
 * Created by Ari Iivari on 3.4.2015.
 */
public class WorldWeather {

    public WorldLocation worldlocation;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Weather weather = new Weather();
    public Area area = new Area();


    public byte[] iconData;

    public  class CurrentCondition {
        String windDir16, weatherDesc, icon, weatherDescEn;
        int humidity;
        int pressure, visibility, cloudcover, tempCur, windSpeed, windSpeedMiles, windDir, tempFeel, tempFeelF;
        float weatherPrecip;

        public int getTempCur(){return tempCur;}
        public void setTempCur(int tempCur){this.tempCur = tempCur;}
        public int getTempFeel(){return tempFeel;}
        public void setTempFeel(int tempFeel){this.tempFeel = tempFeel;}
        public int getTempFeelF(){return tempFeelF;}
        public void setTempFeelF(int tempFeelF){this.tempFeelF = tempFeelF;}

        public int getWindspeed(){return windSpeed;}
        public void setWindspeed(int windSpeed){this.windSpeed = windSpeed;}
        public int getWindspeedMiles(){return windSpeedMiles;}
        public void setWindspeedMiles(int windSpeedMiles){this.windSpeedMiles = windSpeedMiles;}
        public int getWindDir(){return windDir;}
        public void setWindDir(int windDir){this.windDir = windDir;}
        public String getWindDir16(){return windDir16;}
        public void setWindDir16(String windDir16){this.windDir16 = windDir16;}

        public int getHumidity(){return humidity;}
        public void setHumidity(int humidity){this.humidity = humidity;}
        public int getPressure(){return pressure;}
        public void setPressure(int pressure){this.pressure = pressure;}
        public int getVisibility(){return visibility;}
        public void setVisibility(int visibility){this.visibility = visibility;}
        public int getCloudCover(){return cloudcover;}
        public void setCloudCover(int cloudcover){this.cloudcover = cloudcover;}

        public String getWeatherDesc(){return weatherDesc;}
        public void setWeatherDesc(String weatherDesc){this.weatherDesc = weatherDesc;}
        public String getWeatherDescEn(){return weatherDescEn;}
        public void setWeatherDescEn(String weatherDescEn){this.weatherDescEn = weatherDescEn;}
        public float getWeatherPrecip(){return weatherPrecip;}
        public void setWeatherPrecip(float weatherPrecip){this.weatherPrecip = weatherPrecip;}
        public String getIcon(){return icon;}
        public void setIcon(String icon){this.icon = icon;}
    }

    public class Weather{
        private int tempMax, tempMin, uv, tempMaxF, tempMinF;
        private String sunrise, sunset, moonrise, moonset;

        public int getTempMax(){return tempMax;}
        public void setTempMax(int tempMax){this.tempMax = tempMax;}
        public int getTempMin(){return tempMin;}
        public void setTempMin(int tempMin){this.tempMin = tempMin;}
        public int getTempMaxF(){return tempMaxF;}
        public void setTempMaxF(int tempMaxF){this.tempMaxF = tempMaxF;}
        public int getTempMinF(){return tempMinF;}
        public void setTempMinF(int tempMinF){this.tempMinF = tempMinF;}

        public String getSunrise(){return sunrise;}
        public void setSunrise(String sunrise){this.sunrise = sunrise;}

        public String getSunset(){return sunset;}
        public void setSunset(String sunset){this.sunset = sunset;}

        public String getMoonrise(){return moonrise;}
        public void setMoonrise(String moonrise){this.moonrise = moonrise;}

        public String getMoonset(){return moonset;}
        public void setMoonset(String moonset){this.moonset = moonset;}

        public int getUv(){return uv;}
        public void setUv(int uv){this.uv = uv;}
    }

    public class Area {
        String areaName, country, region;

        public String getAreaName(){return areaName;}
        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getCountry(){return country;}
        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {return region;}
        public void setRegion(String region) {
            this.region = region;
        }

    }

}
