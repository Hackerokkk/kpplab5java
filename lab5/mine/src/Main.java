import java.io.*;
import java.util.*;

// Клас для представлення даних про станцію маршруту
class RouteStation implements Serializable {
    private String stationName;
    private String arrivalTime;
    private String departureTime;
    private int availableSeats;

    public RouteStation(String stationName, String arrivalTime, String departureTime, int availableSeats) {
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public String getStationName() {
        return stationName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    // Геттери і сеттери інших полів

    @Override
    public String toString() {
        return "Station: " + stationName + " (Arrival: " + arrivalTime + ", Departure: " + departureTime + ", Available Seats: " + availableSeats + ")";
    }
}

// Клас для представлення даних про маршрут
class Route implements Serializable {
    private int routeNumber;
    private String daysOfWeek;
    private int totalSeats;
    private List<RouteStation> stations;

    public Route(int routeNumber, String daysOfWeek, int totalSeats) {
        this.routeNumber = routeNumber;
        this.daysOfWeek = daysOfWeek;
        this.totalSeats = totalSeats;
        this.stations = new ArrayList<>();
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public List<RouteStation> getStations() {
        return stations;
    }

    // Геттери і сеттери інших полів

    public void addStation(RouteStation station) {
        stations.add(station);
    }

    @Override
    public String toString() {
        return "Route: " + routeNumber + " (Days: " + daysOfWeek + ", Total Seats: " + totalSeats + ")";
    }
}

// Власний клас-контейнер, що параметризується Generic Type
class MyContainer<T> implements Serializable {
    private List<T> elements;

    public MyContainer() {
        elements = new ArrayList<>();
    }

    public void add(T element) {
        elements.add(element);
    }

    public void remove(T element) {
        elements.remove(element);
    }

    public void clear() {
        elements.clear();
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public T get(int index) {
        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        return null;
    }

    public List<T> getElements() {
        return elements;
    }
}

// Клас-утиліта для обробки контейнерів
class ContainerUtil {
    // Параметризований метод для обробки контейнерів
    public static <T> void processContainer(MyContainer<T> container) {
        List<T> elements = container.getElements();
        for (T element : elements) {
            // Обробка кожного елемента
            System.out.println(element);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        MyContainer<Route> routeContainer = new MyContainer<>();

        // Додавання маршрутів до контейнера
        Route route1 = new Route(1, "Monday", 50);
        route1.addStation(new RouteStation("Station A", "08:00", "08:30", 30));
        route1.addStation(new RouteStation("Харків'", "18:00", "18:30", 20)); // Додано станцію "Харків'" з вечірнім часом відправлення
        routeContainer.add(route1);

        Route route2 = new Route(2, "Tuesday", 40);
        route2.addStation(new RouteStation("Station X", "10:00", "10:30", 15));
        route2.addStation(new RouteStation("Station Y", "11:00", "11:30", 25));
        routeContainer.add(route2);

        // Використання у циклі foreach
        ContainerUtil.processContainer(routeContainer);

        // Серіалізація даних
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("routes.ser"))) {
            outputStream.writeObject(routeContainer);
            System.out.println("Дані про маршрути збережено в routes.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Десеріалізація даних
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("routes.ser"))) {
            MyContainer<Route> restoredContainer = (MyContainer<Route>) inputStream.readObject();
            System.out.println("Дані про маршрути відновлено з routes.ser");
            ContainerUtil.processContainer(restoredContainer);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Пошук вечірніх маршрутів через Харків по понеділках і п'ятницях
        findEveningRoutes(routeContainer);
    }

    private static void findEveningRoutes(MyContainer<Route> container) {
        System.out.println("Вечірні маршрути через Харків по понеділках і п'ятницях:");
        for (Route route : container.getElements()) {
            if (route.getDaysOfWeek().contains("Monday") || route.getDaysOfWeek().contains("Friday")) {
                boolean hasKharkivTransit = false;
                for (RouteStation station : route.getStations()) {
                    if (station.getStationName().equals("Харків'") && station.getDepartureTime().compareTo("18:00") >= 0) {
                        hasKharkivTransit = true;
                        break;
                    }
                }
                if (hasKharkivTransit) {
                    System.out.println(route);
                }
            }
        }
    }
}
