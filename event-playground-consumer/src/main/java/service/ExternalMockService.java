package service;

import jakarta.enterprise.context.ApplicationScoped;
import model.MountainInfo;
import org.jboss.logging.Logger;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ExternalMockService {

    private static final Logger log = Logger.getLogger(ExternalMockService.class);

    private final Map<String, MountainInfo> mountains = new HashMap<>();

    public ExternalMockService() {
        // Julian Alps
        mountains.put("Triglav", new MountainInfo(2864L, "46.3783,13.8369", "Julian Alps"));
        mountains.put("Škrlatica", new MountainInfo(2740L, "46.4330,13.8365", "Julian Alps"));
        mountains.put("Mangart", new MountainInfo(2679L, "46.4417,13.6564", "Julian Alps"));
        mountains.put("Jalovec", new MountainInfo(2645L, "46.4356,13.6652", "Julian Alps"));
        mountains.put("Razor", new MountainInfo(2601L, "46.4091,13.7834", "Julian Alps"));
        mountains.put("Kanjavec", new MountainInfo(2569L, "46.3731,13.8002", "Julian Alps"));
        mountains.put("Prisojnik", new MountainInfo(2547L, "46.4215,13.7941", "Julian Alps"));
        mountains.put("Rombon", new MountainInfo(2208L, "46.3740,13.4894", "Julian Alps"));
        mountains.put("Krn", new MountainInfo(2244L, "46.2667,13.6667", "Julian Alps"));
        mountains.put("Velika Baba", new MountainInfo(2127L, "46.3833,13.6667", "Julian Alps"));
        mountains.put("Viš", new MountainInfo(2666L, "46.4000,13.5333", "Julian Alps"));
        mountains.put("Pihavec", new MountainInfo(2419L, "46.3667,13.7667", "Julian Alps"));
        mountains.put("Vogel", new MountainInfo(1922L, "46.2667,13.8000", "Julian Alps"));
        mountains.put("Rodica", new MountainInfo(1966L, "46.2667,13.9167", "Julian Alps"));
        mountains.put("Veliki Draški vrh", new MountainInfo(2243L, "46.3667,13.8833", "Julian Alps"));
        mountains.put("Debela peč", new MountainInfo(2014L, "46.3500,13.9000", "Julian Alps"));
        mountains.put("Tosc", new MountainInfo(2275L, "46.3667,13.8667", "Julian Alps"));
        mountains.put("Mala Mojstrovka", new MountainInfo(2332L, "46.4333,13.7333", "Julian Alps"));
        mountains.put("Velika Mojstrovka", new MountainInfo(2366L, "46.4333,13.7333", "Julian Alps"));
        mountains.put("Špik", new MountainInfo(2472L, "46.4500,13.7833", "Julian Alps"));
        mountains.put("Krnčica", new MountainInfo(2142L, "46.2833,13.7000", "Julian Alps"));
        mountains.put("Bovški Gamsovec", new MountainInfo(2392L, "46.3667,13.7667", "Julian Alps"));
        mountains.put("Veliki Lemež", new MountainInfo(2042L, "46.2833,13.6667", "Julian Alps"));

        // Karavanke
        mountains.put("Stol", new MountainInfo(2236L, "46.4410,14.1833", "Karavanke"));
        mountains.put("Kepa", new MountainInfo(2143L, "46.5333,13.8833", "Karavanke"));
        mountains.put("Dobrča", new MountainInfo(1634L, "46.3667,14.2500", "Karavanke"));
        mountains.put("Begunjščica", new MountainInfo(2060L, "46.4000,14.2167", "Karavanke"));
        mountains.put("Veliki vrh (Košuta)", new MountainInfo(2088L, "46.4167,14.4000", "Karavanke"));
        mountains.put("Tolsti vrh", new MountainInfo(1715L, "46.3833,14.3333", "Karavanke"));

        // Kamnik-Savinja Alps
        mountains.put("Grintovec", new MountainInfo(2558L, "46.3589,14.6047", "Kamnik-Savinja Alps"));
        mountains.put("Kočna", new MountainInfo(2540L, "46.3630,14.5972", "Kamnik-Savinja Alps"));
        mountains.put("Skuta", new MountainInfo(2532L, "46.3667,14.6000", "Kamnik-Savinja Alps"));
        mountains.put("Brana", new MountainInfo(2253L, "46.3500,14.6000", "Kamnik-Savinja Alps"));
        mountains.put("Ojstrica", new MountainInfo(2350L, "46.3833,14.6333", "Kamnik-Savinja Alps"));
        mountains.put("Planjava", new MountainInfo(2394L, "46.3667,14.6333", "Kamnik-Savinja Alps"));
        mountains.put("Raduha", new MountainInfo(2062L, "46.4000,14.8333", "Kamnik-Savinja Alps"));
        mountains.put("Krvavec", new MountainInfo(1853L, "46.3000,14.5333", "Kamnik-Savinja Alps"));
        mountains.put("Šmarna gora", new MountainInfo(669L, "46.1167,14.4667", "Kamnik-Savinja Alps"));
        mountains.put("Krvavka", new MountainInfo(1785L, "46.3500,14.5667", "Kamnik-Savinja Alps"));
        mountains.put("Savinjska dolina peak", new MountainInfo(2050L, "46.3667,14.8000", "Kamnik-Savinja Alps"));
        mountains.put("Velika planina", new MountainInfo(1666L, "46.3167,14.6167", "Kamnik-Savinja Alps"));

        // Dinaric Alps & other Slovenian mountains
        mountains.put("Lisco", new MountainInfo(948L, "46.0333,15.2833", "Posavje Hills"));
        mountains.put("Veliki Rog", new MountainInfo(1099L, "45.6667,15.0333", "Kočevski Rog / Dinaric Alps"));
        mountains.put("Snežnik", new MountainInfo(1796L, "45.5833,14.4667", "Dinaric Alps"));
        mountains.put("Slivnica", new MountainInfo(1114L, "45.7500,14.4000", "Dinaric Alps"));
        mountains.put("Nanos", new MountainInfo(1313L, "45.7833,14.0500", "Dinaric Alps / Primorska"));
        mountains.put("Vremščica", new MountainInfo(1027L, "45.7000,14.0167", "Dinaric Alps / Primorska"));
        mountains.put("Čaven", new MountainInfo(1185L, "45.9167,13.7833", "Primorska / Trnovo Forest"));
        mountains.put("Kucelj", new MountainInfo(1237L, "45.9167,13.8167", "Primorska / Trnovo Forest"));
        mountains.put("Trstelj", new MountainInfo(643L, "45.8500,13.6667", "Primorska / Karst Plateau"));
    }

    public Optional<MountainInfo> getMountainInfo(String mountain) {
        return Optional.ofNullable(mountains.get(mountain));
    }

    public Optional<List<MountainInfo>> getMountainsInfo(List<String> mountainNames) {
        List<MountainInfo> infos = mountainNames.stream()
                .map(mountains::get)
                .toList();

        if (infos.contains(null)) {
            return Optional.empty();
        }

        return Optional.of(infos);
    }

    public Optional<Long> scrapeMountainElevation(String mountain) {
        log.infof("Scraping elevation data for mountain: %s", mountain);
        simulateNetworkDelay();
        return getMountainInfo(mountain).map(MountainInfo::elevation);
    }

    public Optional<String> scrapeMountainCoordinates(String mountain) {
        log.infof("Scraping coordinates data for mountain: %s", mountain);
        simulateNetworkDelay();
        return getMountainInfo(mountain).map(MountainInfo::coordinates);
    }

    public Optional<String> scrapeMountainRegion(String mountain) {
        log.infof("Scraping region data for mountain: %s", mountain);
        simulateNetworkDelay();
        return getMountainInfo(mountain).map(MountainInfo::region);
    }

    private void simulateNetworkDelay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
