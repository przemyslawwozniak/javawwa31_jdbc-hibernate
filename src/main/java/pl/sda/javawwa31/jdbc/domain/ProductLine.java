package pl.sda.javawwa31.jdbc.domain;

/*
Classic Cars
Motorcycles
Planes
Ships
Trains
Trucks
Buses
Vintage Cars
 */
public enum ProductLine {

    CLASSIC_CARS {
        @Override
        public String toString() {
            return "Classic Cars";
        }
    },
    VINTAGE_CARS {
        @Override
        public String toString() {
            return "Vintage Cars";
        }
    },
    //lamiemy konwencje zapisu, ale potrzebujemy aby toString=name zwracal wartosc taka jak oczekiwana przez db
    Motorcycles,
    Planes,
    Ships,
    Trains,
    Trucks,
    Buses

}
