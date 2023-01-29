package nl.shashi.playground.artemis;

import net.datafaker.Faker;

public final class CustomDataFaker {

    private CustomDataFaker() {}

    private static final Faker faker = new Faker();

    public static String backToTheFuture() {
        return faker.backToTheFuture().character();
    }

    public static String starWars() {
        return faker.starWars().character();
    }

}
