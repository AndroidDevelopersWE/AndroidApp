package co.dtechsystem.carefer.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class SortByDistance{
//    public static void main(String[] args) {
//        // TODO: initialize these
//        List<Location> locations = new ArrayList<>();
//        final Location myLocation = null;
//
//        sort(locations, new ToComparable<Location, Double>() {
//            @Override
//            public Double toComparable(Location location) {
//                return Location.distance(location, myLocation);
//            }
//        });
//
//        for (Location location : locations)
//            System.out.println(location);
//    }

    public static class Location {
        private final double latitude, longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return String.format("[latitude = %f, longitude = %f]", latitude, longitude);
        }

        public static double distance(Location location1, Location location2) {
            // TODO: return the distance between location1 and location2
            return 0;
        }
    }

    public interface ToComparable<T, C extends Comparable<? super C>> {
        C toComparable(T t);
    }

    public static <T, C extends Comparable<? super C>> void sort(List<T> list, ToComparable<T, C> function) {
        class Pair implements Comparable<Pair> {
            final T original;
            final C comparable;

            Pair(T original, C comparable) {
                this.original = original;
                this.comparable = comparable;
            }

            @Override
            public int compareTo(Pair pair) {
                return comparable == null ?
                        pair.comparable == null ? 0 : -1 :
                        pair.comparable == null ? 1 : comparable.compareTo(pair.comparable);
            }
        }

        List<Pair> pairs = new ArrayList<>(list.size());
        for (T original : list)
            pairs.add(new Pair(original, function.toComparable(original)));

        Collections.sort(pairs);

        ListIterator<T> iter = list.listIterator();
        for (Pair pair : pairs) {
            iter.next();
            iter.set(pair.original);
        }
    }
}