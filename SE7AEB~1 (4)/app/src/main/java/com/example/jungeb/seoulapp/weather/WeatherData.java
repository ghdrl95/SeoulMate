package com.example.jungeb.seoulapp.weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
retrofit 통신을 위한 클래스구조
내일 날씨 상태, 내일 최고기온, 내일 최저기온
모레 날씨 상태, 모레 최고기온, 모레 최저기온

 */
public class WeatherData {
    @SerializedName("result")
    Result result;
    @SerializedName("weather")
    weather weather;

    public weather getWeather(){return weather;}

    public class Result {
        @SerializedName("message") String message;
        @SerializedName("code") String code;

        public String getMessage() {return message;}
        public String getCode() {return code;}
    }
    public class weather{
        public List<summary> summary = new ArrayList<>();
        public List<summary> getSummary() {return summary;}

        public class summary{
            @SerializedName("yesterday") yesterday yesterday;
            @SerializedName("today") today today;
            @SerializedName("tomorrow") tomorrow tomorrow;
            @SerializedName("dayAfterTomorrow") dayAftertomorrow dayAftertomorrow;

            public yesterday getYesterday(){return yesterday;}
            public today getToday(){return  today;}
            public tomorrow getTomorrow(){return  tomorrow;}
            public dayAftertomorrow getDayAftertomorrow(){return dayAftertomorrow;}

            public class yesterday{
                @SerializedName("sky") sky sky;
                @SerializedName("temperature") temperature temperature;

                public sky getSky(){return sky;}
                public temperature getTemperature(){return temperature;}

                public class sky {
                    @SerializedName("name") String name;
                    @SerializedName("code") String code;

                    public String getName() {return name;}
                    public String getCode() {return code;}
                }
                public class temperature{
                    @SerializedName("tmax") String tmax;
                    @SerializedName("tmin") String tmin;

                    public String getTmax() {return tmax;}
                    public String getTmin() {return tmin;}
                }
            }
            public class today{
                @SerializedName("sky") sky sky;
                @SerializedName("temperature") temperature temperature;

                public sky getSky(){return sky;}
                public temperature getTemperature(){return temperature;}
                public class sky {
                    @SerializedName("name") String name;
                    @SerializedName("code") String code;

                    public String getName() {return name;}
                    public String getCode() {return code;}
                }
                public class temperature{
                    @SerializedName("tmax") String tmax;
                    @SerializedName("tmin") String tmin;

                    public String getTmax() {return tmax;}
                    public String getTmin() {return tmin;}
                }
            }
            public class tomorrow{
                @SerializedName("sky") sky sky;
                @SerializedName("temperature") temperature temperature;

                public sky getSky(){return sky;}
                public temperature getTemperature(){return temperature;}
                public class sky {
                    @SerializedName("name") String name;
                    @SerializedName("code") String code;

                    public String getName() {return name;}
                    public String getCode() {return code;}
                }
                public class temperature{
                    @SerializedName("tmax") String tmax;
                    @SerializedName("tmin") String tmin;

                    public String getTmax() {return tmax;}
                    public String getTmin() {return tmin;}
                }
            }
            public class dayAftertomorrow{
                @SerializedName("sky") sky sky;
                @SerializedName("temperature") temperature temperature;

                public sky getSky(){return sky;}
                public temperature getTemperature(){return temperature;}
                public class sky {
                    @SerializedName("name") String name;
                    @SerializedName("code") String code;

                    public String getName() {return name;}
                    public String getCode() {return code;}
                }
                public class temperature{
                    @SerializedName("tmax") String tmax;
                    @SerializedName("tmin") String tmin;

                    public String getTmax() {return tmax;}
                    public String getTmin() {return tmin;}
                }
            }


        }

    }

}
