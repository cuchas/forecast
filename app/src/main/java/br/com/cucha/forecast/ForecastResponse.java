package br.com.cucha.forecast;

import java.util.List;

/**
 * Created by eduardocucharro on 20/04/17.
 */

public class ForecastResponse {

    /**
     * message : accurate
     * cod : 200
     * count : 10
     * list : [{"id":495260,"name":"Shcherbinka","coord":{"lat":55.4997,"lon":37.5597},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":564517,"name":"Dubrovitsy","coord":{"lat":55.4397,"lon":37.4867},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":570578,"name":"Butovo","coord":{"lat":55.5483,"lon":37.5797},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":545782,"name":"Kommunarka","coord":{"lat":55.5695,"lon":37.4893},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":6417490,"name":"Lesparkkhoz","coord":{"lat":55.5431,"lon":37.6014},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":526736,"name":"Sed\u2019moy Mikrorayon","coord":{"lat":55.5622,"lon":37.5797},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":473051,"name":"Vlas\u2019yevo","coord":{"lat":55.4603,"lon":37.3794},"main":{"temp":277.91,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":578680,"name":"Bachurino","coord":{"lat":55.58,"lon":37.52},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":554629,"name":"Shestoy Mikrorayon","coord":{"lat":55.5667,"lon":37.5833},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]},{"id":508101,"name":"Podolsk","coord":{"lat":55.4242,"lon":37.5547},"main":{"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15},"dt":1492689600,"wind":{"speed":7,"deg":350},"sys":{"country":""},"rain":null,"snow":null,"clouds":{"all":0},"weather":[{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]}]
     */

    private String message;
    private String cod;
    private float count;
    private List<ForecastBean> list;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public List<ForecastBean> getList() {
        return list;
    }

    public void setList(List<ForecastBean> list) {
        this.list = list;
    }

    public static class ForecastBean {
        /**
         * id : 495260
         * name : Shcherbinka
         * coord : {"lat":55.4997,"lon":37.5597}
         * main : {"temp":277.9,"pressure":1022,"humidity":27,"temp_min":277.15,"temp_max":278.15}
         * dt : 1492689600
         * wind : {"speed":7,"deg":350}
         * sys : {"country":""}
         * rain : null
         * snow : null
         * clouds : {"all":0}
         * weather : [{"id":800,"main":"Clear","description":"Sky is Clear","icon":"01d"}]
         */

        private float id;
        private String name;
        private CoordBean coord;
        private MainBean main;
        private float dt;
        private WindBean wind;
        private SysBean sys;
        private Object rain;
        private Object snow;
        private CloudsBean clouds;
        private List<WeatherBean> weather;

        public float getId() {
            return id;
        }

        public void setId(float id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CoordBean getCoord() {
            return coord;
        }

        public void setCoord(CoordBean coord) {
            this.coord = coord;
        }

        public MainBean getMain() {
            return main;
        }

        public void setMain(MainBean main) {
            this.main = main;
        }

        public float getDt() {
            return dt;
        }

        public void setDt(float dt) {
            this.dt = dt;
        }

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
        }

        public SysBean getSys() {
            return sys;
        }

        public void setSys(SysBean sys) {
            this.sys = sys;
        }

        public Object getRain() {
            return rain;
        }

        public void setRain(Object rain) {
            this.rain = rain;
        }

        public Object getSnow() {
            return snow;
        }

        public void setSnow(Object snow) {
            this.snow = snow;
        }

        public CloudsBean getClouds() {
            return clouds;
        }

        public void setClouds(CloudsBean clouds) {
            this.clouds = clouds;
        }

        public List<WeatherBean> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherBean> weather) {
            this.weather = weather;
        }

        public static class CoordBean {
            /**
             * lat : 55.4997
             * lon : 37.5597
             */

            private double lat;
            private double lon;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }
        }

        public static class MainBean {
            /**
             * temp : 277.9
             * pressure : 1022
             * humidity : 27
             * temp_min : 277.15
             * temp_max : 278.15
             */

            private double temp;
            private float pressure;
            private float humidity;
            private double temp_min;
            private double temp_max;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
            }

            public float getPressure() {
                return pressure;
            }

            public void setPressure(float pressure) {
                this.pressure = pressure;
            }

            public float getHumidity() {
                return humidity;
            }

            public void setHumidity(float humidity) {
                this.humidity = humidity;
            }

            public double getTemp_min() {
                return temp_min;
            }

            public void setTemp_min(double temp_min) {
                this.temp_min = temp_min;
            }

            public double getTemp_max() {
                return temp_max;
            }

            public void setTemp_max(double temp_max) {
                this.temp_max = temp_max;
            }
        }

        public static class WindBean {
            /**
             * speed : 7
             * deg : 350
             */

            private float speed;
            private float deg;

            public float getSpeed() {
                return speed;
            }

            public void setSpeed(float speed) {
                this.speed = speed;
            }

            public float getDeg() {
                return deg;
            }

            public void setDeg(float deg) {
                this.deg = deg;
            }
        }

        public static class SysBean {
            /**
             * country :
             */

            private String country;

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }
        }

        public static class CloudsBean {
            /**
             * all : 0
             */

            private float all;

            public float getAll() {
                return all;
            }

            public void setAll(float all) {
                this.all = all;
            }
        }

        public static class WeatherBean {
            /**
             * id : 800
             * main : Clear
             * description : Sky is Clear
             * icon : 01d
             */

            private float id;
            private String main;
            private String description;
            private String icon;

            public float getId() {
                return id;
            }

            public void setId(float id) {
                this.id = id;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
