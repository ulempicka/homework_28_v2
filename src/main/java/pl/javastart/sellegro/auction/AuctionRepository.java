package pl.javastart.sellegro.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query(value = "SELECT * FROM auction ORDER BY price DESC LIMIT 4", nativeQuery = true)
    List<Auction> find4MostExpensive();

    @Query(value = "SELECT * FROM auction WHERE :column LIKE '%:filter%'", nativeQuery = true)
    List<Auction> findAllForFilters(String column, String filter);

    @Query("SELECT a FROM Auction a ORDER BY :column")
    List<Auction> findAllSorted(String column);

    @Query(value = "UPDATE auction " +
            "SET title = :title WHERE id = :id", nativeQuery = true)
    @Transactional
    @Modifying
    void updateAuctionsWithTitle(String title, Long id);
}
