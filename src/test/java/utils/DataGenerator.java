package utils;

import com.github.javafaker.Faker;


import jdk.jfr.ContentType;
import lombok.Value;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }



    public static String generateData(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    private static Faker faker;

    public static String generateCity() {
        var random = new Random();
        var list = Arrays.asList("Москва", "Омск", "Новосибирск", "Иваново", "Тюмень", "Волгоград", "Иваново", "Воронеж", "Пенза", "Пермь", "Ульяновск", "Великий Новгород");
        var randomCity = list.get(random.nextInt(list.size()));
        return randomCity;
    }

    public static String generateName() {
        faker = new Faker(new Locale("ru"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone() {
        faker = new Faker(new Locale("ru"));
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static class Registration {
        private Registration() {

        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(generateName(), generatePhone(), generateCity());
        }
    }

    @Value
    public static class UserInfo {
        private final String name;
        private final String phone;
        private final String city;
    }

}

