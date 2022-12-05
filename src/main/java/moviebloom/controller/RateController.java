package moviebloom.controller;

import moviebloom.domain.Rate;
import moviebloom.domain.User;
import moviebloom.dto.RegisterDto;
import moviebloom.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/rate")
@CrossOrigin(origins = "*")
public class RateController {
    @Autowired
    private RateService rateService;

    @PostMapping( "/{movieId}")
    public Rate rate(@PathVariable Long movieId, @RequestParam Float rateAmount, @AuthenticationPrincipal User user) {
        return rateService.createOrUpdateRate(movieId, user.getId(), rateAmount);
    }
    @DeleteMapping("/{movieId}")
    public Rate unrate(@PathVariable Long movieId, @AuthenticationPrincipal User user) {
        return rateService.deleteRate(movieId, user.getId());
    }
}
