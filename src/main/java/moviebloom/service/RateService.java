package moviebloom.service;

import moviebloom.domain.Movie;
import moviebloom.domain.Rate;
import moviebloom.domain.id.RateId;
import moviebloom.repository.MovieRepository;
import moviebloom.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserService userService;
    public Rate findOne(Long movieId, Long userId){
        return rateRepository.findById(new RateId(movieId, userId)).orElse(null);
    }

    public Rate createOrUpdateRate(Long movieId, Long userId, float rateAmount){
        Rate rate = findOne(movieId, userId);
        var movie = movieService.findOne(movieId);
        if (rate==null){
            final Rate newRate = new Rate(movie, userService.findOne(userId), rateAmount);
            rate = rateRepository.save(newRate);
        }
        else{
            rate.setRate(rateAmount);
            rateRepository.save(rate);
        }
        movieRepository.save(movie);
        return rate;
    }

    public Rate deleteRate(Long movieId, Long userId){
        var rate = findOne(movieId, userId);
        rateRepository.delete(rate);
        var movie = movieService.findOne(movieId);
        movieRepository.save(movie);
        return rate;
    }
}
