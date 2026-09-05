package edu.farmingdale.csc311.fleet;

/**
 * A named group of vehicles stored in a plain array.
 * No ArrayList, no HashMap. Arrays and loops only.
 *
 * @author Rochelle Elliott
 */
public class Fleet {

    public static final int MAX_VEHICLES = 25;

    private final String name;
    private final Vehicle[] vehicles = new Vehicle[MAX_VEHICLES];
    private int count;

    /* ------------------------------------------------------------------
     * TODO-08     commit: TODO-08: implement Fleet storage
     *
     * 1. Add three private fields:
     *        name        String, final
     *        vehicles    Vehicle[], final, sized MAX_VEHICLES
     *        count       int, how many slots are actually used
     *
     * 2. The constructor checks name (not null, not blank) and trims it.
     *
     * 3. Methods:
     *
     *    contains(Vehicle v)
     *        loop over the used slots and return true if one equals v.
     *        Use the equals you wrote in TODO-05, not ==.
     *
     *    add(Vehicle v)
     *        null argument           throw IllegalArgumentException
     *        already in the fleet    return false, store nothing
     *        array full              return false
     *        otherwise               store at index count, count++, return true
     *
     *    removeByVin(String vin)
     *        find the slot whose VIN matches, ignoring case. Shift every
     *        later element one place left, null out the old last slot,
     *        count--, return true. Return false when nothing matched or
     *        the vin was null or blank.
     *
     *    findByVin(String vin)
     *        return the matching Vehicle, ignoring case, or null.
     *
     *    size()
     *        return count.
     *
     *    toArray()
     *        return a NEW array of length count holding the vehicles in
     *        insertion order. Returning the internal array lets a caller
     *        overwrite your slots, so copy it.
     * ------------------------------------------------------------------ */

    public Fleet(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name: " + name);
        } else {
            this.name = name.trim();
        }
    }

    public String getName() {
        return name;
    }

    public boolean contains(Vehicle vehicle) {
        for (int i = 0; i < count; i++) {
            if (vehicles[i].equals(vehicle)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle: " + vehicle);
        } else if (contains(vehicle)) {
            return false;
        } else if (count >= MAX_VEHICLES) {
            return false;
        } else {
            vehicles[count] = vehicle;
            count++;
            return true;
        }
    }

    public boolean removeByVin(String vin) {
        if (vin == null || vin.isBlank()) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            if (vehicles[i].getVin().equalsIgnoreCase(vin)) {

                for (int j = i; j < count - 1; j++) {
                    vehicles[j] = vehicles[j + 1];
                }
                vehicles[count - 1] = null;
                count--;
                return true;
            }
        }

        return false;
    }

    public Vehicle findByVin(String vin) {
        if  (vin == null || vin.isBlank()) {
            return null;
        }

        for (int i = 0; i < count; i++) {
            if (vehicles[i].getVin().equalsIgnoreCase(vin)) {
                return vehicles[i];
            }
        }

        return null;
    }

    public int size() {
        return count;
    }

    public Vehicle[] toArray() {

        Vehicle[] copy = new Vehicle[count];

        for (int i = 0; i < count; i++) {
            copy[i] = vehicles[i];
        }

        return copy;
    }

    /* ------------------------------------------------------------------
     * TODO-09     commit: TODO-09: implement Fleet reports
     *
     * None of these may reorder or change the internal array. Start from
     * toArray() when you need a different order.
     *
     *    sortedByYear()
     *        a new array ordered by year, oldest first. When two years
     *        match, order by make A to Z ignoring case
     *        (String.compareToIgnoreCase). Write the sort yourself:
     *        selection sort or insertion sort, your choice. No Arrays.sort,
     *        no Comparator.
     *
     *    countWithFuelType(FuelType fuel)
     *        how many vehicles use that fuel.
     *
     *    averageEngineSize()
     *        average engine size over the vehicles whose fuel type has an
     *        engine. Electrics are left out, otherwise their 0.0 drags the
     *        number down and it means nothing. Return 0.0 when the count is
     *        zero, and watch the division.
     *
     *    longestRange()
     *        the vehicle with the largest rangeInMiles(), or null when the
     *        fleet is empty. On a tie keep the one added first. Note that
     *        this compares cars against trucks without a single if about
     *        the type: rangeInMiles() already knows which formula to run.
     * ------------------------------------------------------------------ */

    public Vehicle[] sortedByYear() {
        Vehicle[] yearSort = toArray();

        for (int i = 0; i < yearSort.length; i++) {
            int minIndex = i;
            for (int j = i; j < count; j++) {
                if (yearSort[j].getYear() < yearSort[minIndex].getYear()
                || yearSort[j].getYear() == yearSort[minIndex].getYear()
                && yearSort[j].getMake().compareToIgnoreCase(yearSort[minIndex].getMake()) < 0) {
                    minIndex = j;
                }
            }

            Vehicle temp = yearSort[i];
            yearSort[i] = yearSort[minIndex];
            yearSort[minIndex] = temp;
        }

        return yearSort;
    }

    public int countWithFuelType(FuelType fuel) {
        int total = 0;
        for (int i = 0; i < count; i++) {
            if (vehicles[i].getFuelType().equals(fuel)) {
                total++;
            }
        }

        return total;
    }

    public double averageEngineSize() {
        double total = 0.0;
        int engineCount = 0;
        for (int i = 0; i < count; i++) {
            if (vehicles[i].getFuelType().hasEngine()) {
                total+= vehicles[i].getEngineSize();
                engineCount++;
            }
        }

        if (engineCount == 0) {
            return 0.0;
        }

        return total/engineCount;
    }

    public Vehicle longestRange() {
        if (count==0) {
            return null;
        }

        Vehicle longest = vehicles[0];

        for (int i = 1; i < count; i++) {
            if (vehicles[i].rangeInMiles() > longest.rangeInMiles()) {
                longest = vehicles[i];
            }
        }

        return longest;
    }
}
