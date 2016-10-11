package com.exodus.daoGen;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

public class DaoGeneratorK {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.exodus.weather.store");

        Entity listCity = schema.addEntity("ListCity");
        listCity.addIdProperty();
        listCity.addStringProperty("name");
        listCity.addStringProperty("country");

        Entity city = schema.addEntity("City");
        city.addIdProperty();
        city.addStringProperty("name");
        city.addIntProperty("temperature");
        city.addIntProperty("temp_min");
        city.addIntProperty("temp_max");
        city.addDateProperty("updated_at");
        city.addIntProperty("wind_speed");
        city.addIntProperty("humidity");
        city.addStringProperty("weather_text");
        city.addStringProperty("weather_icon");

        Entity futureWeather = schema.addEntity("FutureWeather");
        futureWeather.addIdProperty();
        futureWeather.addStringProperty("weather_icon");
        futureWeather.addDateProperty("date");
        futureWeather.addIntProperty("temp");

        Property cityId = futureWeather.addLongProperty("city_id").getProperty();
        ToMany city_to_futureWeather = city.addToMany(futureWeather, cityId);
        futureWeather.addToOne(city, cityId);
        city_to_futureWeather.setName("FutureWeather");

        //Generate all classes
        DaoGenerator dg = new DaoGenerator();
        dg.generateAll(schema, "./app/src/main/java");
    }
}
