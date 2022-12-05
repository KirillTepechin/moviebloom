package moviebloom.service;

import com.ibm.icu.text.Transliterator;
import moviebloom.domain.Actor;
import moviebloom.domain.Movie;
import moviebloom.domain.enums.Genre;
import moviebloom.dto.MovieDto;
import moviebloom.repository.ActorRepository;
import moviebloom.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    public static final String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
    @Value("${upload.path}")
    private String uploadPath;
    @Transactional(readOnly = true)
    public Movie findOne(Long id){
        return movieRepository.findById(id).orElseThrow();
    }
    @Transactional(readOnly = true)
    public List<Movie> findTop20(){
        return movieRepository.findTop20ByOrderByAverageRateDesc();
    }
    @Transactional(readOnly = true)
    @Async
    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void findTop(){
        var movie = movieRepository.findTopByOrderByAverageRateDesc();
        if(movie!=null)
        simpMessagingTemplate.convertAndSend("/topic/activity", movie);
    }
    @Transactional
    public Movie createMovie(MovieDto movieDto) {
        List<Genre> enumGenres = new ArrayList<>();
        if(movieDto.getGenres()!=null){
            enumGenres = movieDto.getGenres().stream().map(Genre::valueOf).collect(Collectors.toList());
        }
        List<Actor> actorsDb = new ArrayList<>();
        if(movieDto.getActorsIds()!=null){
            actorsDb = movieDto.getActorsIds().stream()
                    .map(actor->actorRepository.findById(actor).get())
                    .collect(Collectors.toList());
        }
        var movie = new Movie(movieDto.getName(),movieDto.getYear(),movieDto.getDescription());

        actorsDb.forEach(movie::addActor);
        movie.setActors(actorsDb);
        movie.setGenres(enumGenres);
        movie.setPoster("default.png");

        return movieRepository.save(movie);
    }
    @Transactional
    public Movie updateMovie(Long id,MovieDto movieDto){
        Movie movie = findOne(id);
        List<Genre> enumGenres;
        if(movieDto.getGenres()!=null){
            enumGenres = movieDto.getGenres().stream().map(Genre::valueOf).collect(Collectors.toList());
            movie.setGenres(enumGenres);
        }
        List<Actor> actorsDb = new ArrayList<>();
        if(movieDto.getActorsIds()!=null) {
            actorsDb = movieDto.getActorsIds().stream()
                    .map(actor -> actorRepository.findById(actor)
                            .get()).toList();

        };
        if(movieDto.getName()!=null){
            movie.setName(movieDto.getName());
        }
        if(movieDto.getDescription()!=null){
            movie.setDescription(movieDto.getDescription());
        }
        if(movieDto.getYear()!=null){
            movie.setYear(movieDto.getYear());
        }
        movie.getActors().clear();
        actorsDb.forEach(movie::addActor);
        return movieRepository.save(movie);
    }
    @Transactional
    public Movie uploadPhotoForMovie(MultipartFile file, Long id) throws IOException {
        Path destinationFile = Path.of(uploadPath).resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();

        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename;
        if(isCyrillic(file.getOriginalFilename())){
            Transliterator transliterator = Transliterator.getInstance(CYRILLIC_TO_LATIN);
            resultFilename = uuidFile + "." + transliterator.transliterate(file.getOriginalFilename());
        }
        else{
            resultFilename = uuidFile + "." +file.getOriginalFilename();
        }

        File newFile = new File(String.valueOf(destinationFile));
        newFile.renameTo(new File(uploadPath + "/" + resultFilename));
        var movie = movieRepository.findById(id).get();
        movie.setPoster(resultFilename);
        movieRepository.save(movie);
        return movie;
    }
    public boolean isCyrillic(String str){
        String regex = "[а-яёА-ЯЁ]+";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        return m.find();
    }
    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }
    @Transactional
    public Long deleteMovie(Movie movie){
        final Movie deleted = findOne(movie.getId());
        movieRepository.delete(movie);
        return deleted.getId();
    }
}
