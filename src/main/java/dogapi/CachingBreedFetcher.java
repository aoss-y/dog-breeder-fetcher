package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private BreedFetcher fetcher;
    private Map<String, List<String>> map;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.map = new HashMap<String, List<String>>();
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        //If its cached
        if (this.map.containsKey(breed))
            return this.map.get(breed);

        // Else cache it.
        List<String> out;
        this.callsMade++;
        try {
            out = this.fetcher.getSubBreeds(breed);
            this.map.put(breed, out);
        } catch (BreedNotFoundException e) {
            throw new BreedNotFoundException(breed);
        }

        return out;
    }

    public int getCallsMade() {
        return callsMade;
    }
}
