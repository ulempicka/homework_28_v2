package pl.javastart.sellegro.auction;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuctionController {

    private AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/auctions")
    public String auctions(Model model, String column, @RequestParam(required = false) String filter) {
        List<Auction> auctions;
        if(filter != null) {
            auctions = auctionService.findAllForFilters(column, filter);
        } else {
            auctions = auctionService.findAllSorted(column);
        }

        model.addAttribute("cars", auctions);
        model.addAttribute("filters", filter);
        model.addAttribute("column", column);
        return "auctions";
    }
}
