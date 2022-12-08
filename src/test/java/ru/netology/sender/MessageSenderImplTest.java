package ru.netology.sender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import java.util.Map;


class MessageSenderImplTest {

    private final String rusIp = "172.123.12.19"; //рус
    private final String engIp = "96.44.183.149"; //амер

    @BeforeEach
    void setUp() {
        System.out.println("Вызываюсь до выполнения теста");
    }

    @Test
    void MessageSenderTest() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Mockito.when(geoService.byIp(rusIp))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(geoService.byIp(engIp))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать ");
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome ");

        Assertions.assertEquals("Добро пожаловать ",
                messageSender.send(Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, rusIp)));
        Assertions.assertEquals("Welcome ",
                messageSender.send(Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, engIp)));

    }

    @Test
    void GeoServiceImpl() {
        GeoService geoService = new GeoServiceImpl();
        Assertions.assertEquals(Country.RUSSIA, geoService.byIp(rusIp).getCountry());
        Assertions.assertEquals(Country.USA, geoService.byIp(engIp).getCountry());
    }

    @Test
    void LocalizationServiceImpl() {
        LocalizationServiceImpl localizationService1 = new LocalizationServiceImpl();
        Assertions.assertEquals("Добро пожаловать", localizationService1.locale(Country.RUSSIA));
        Assertions.assertEquals("Welcome", localizationService1.locale(Country.USA));

    }

    @AfterEach
    void tearDown() {
        System.out.println();
        System.out.println("Вызываюсь после вызова теста");
    }

}
