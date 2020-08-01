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
    MOTORCYCLES {
        @Override
        public String toString() {
            return "Motorcycles";
        }
    }

}
