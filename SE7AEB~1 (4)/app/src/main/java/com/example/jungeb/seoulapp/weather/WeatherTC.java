package com.example.jungeb.seoulapp.weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
retrofit 통신을 위한 클래스구조
오늘 날씨 상태 , 오늘 최고기온, 오늘 최저기온, 오늘 현재온도
 */
public class WeatherTC {
    @SerializedName("result")
    Result result;
    @SerializedName("weather")
    weather weather;

    public weather getWeather(){return weather;}

    public  class Result{
        @SerializedName("message") String message;
        @SerializedName("code") String code;

        public String getMessage() {return message;}
        public String getCode() {return code;}
    }
    public class weather{
        public List<minutely> minutely = new ArrayList<>();
        public List<minutely> getMinutely() {return minutely;}
        public class minutely{

            @SerializedName("sky") sky sky;
            @SerializedName("temperature") temperature temperature;

            public sky getSky(){return sky;}
            public temperature getTemperature(){return temperature;}

            public class sky{
                @SerializedName("name") String name;
                @SerializedName("code") String code;

                public String getName() {return name;}
                public String getCode() {return code;}
            }
            public class temperature{
                @SerializedName("tc") String tc;
                @SerializedName("tmax") String tmax;
                @SerializedName("tmin") String tmin;

                public String getTc() {return tc;}

            public String getTmax() {return tmax;}
            public String getTmin() {return tmin;}
        }
        }
    }

}
