package pl.javastart.sellegro.auction;

import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuctionService {

    private AuctionRepository auctionRepository;

    private List<Auction> auctions;

    private static final String[] ADJECTIVES = {"Niesamowity", "Jedyny taki", "IGŁA", "HIT", "Jak nowy",
            "Perełka", "OKAZJA", "Wyjątkowy"};

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
        try {
            loadData();
            updateAuctionsWithTitle();
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadData() throws IOException {
        auctions = new ArrayList<>();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("dane.csv");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        Random random = new Random();

        String line = bufferedReader.readLine(); // skip first line
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            long id = Long.parseLong(data[0]);
            String randomAdjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
            String title = randomAdjective + " " + data[1] + " " + data[2];
            BigDecimal price = new BigDecimal(data[4].replace("\\.", ","));
            LocalDate endDate = LocalDate.parse(data[5]);
            Auction auction = new Auction(id, title, data[1], data[2], data[3], price, endDate);
            auctions.add(auction);
        }
    }

    public List<Auction> find4MostExpensive() {
        return auctionRepository.find4MostExpensive();
    }

    public List<Auction> findAllForFilters(String column, String filter) {
        return auctionRepository.findAllForFilters(column, filter);
    }

    public List<Auction> findAllSorted(String column) {
        return auctionRepository.findAllSorted(column);
    }

    public void updateAuctionsWithTitle(){
        for (Auction auction : auctions) {
            auctionRepository.updateAuctionsWithTitle(auction.getTitle(), auction.getId());
        }
    }
}
