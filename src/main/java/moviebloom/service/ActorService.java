package moviebloom.service;

import com.ibm.icu.text.Transliterator;
import moviebloom.domain.Actor;
import moviebloom.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;
    public static final String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
    @Value("${upload.path}")
    private String uploadPath;
    @Transactional(readOnly = true)
    public Actor findOne(Long id){
        return actorRepository.findById(id).orElseThrow();
    }
    @Transactional
    public Actor createActor(String firstName, String lastName, MultipartFile file) throws IOException {
        var newActor = new Actor(firstName, lastName);
        if(file!=null  && !file.getOriginalFilename().isEmpty()){
            uploadPhotoForActor(file, newActor);
        }
        else{
            newActor.setPhoto("default.png");
        }
        return actorRepository.save(newActor);
    }
    @Transactional(readOnly = true)
    public List<Actor> findAll() {
        return actorRepository.findAll();
    }
    @Transactional
    public Actor updateActor(Long id, String firstName, String lastName, MultipartFile file) throws IOException {
        //BeanUtils.copyProperties(actor, actorFromDb, "id");
        var actor = findOne(id);
        actor.setFirstName(firstName);
        actor.setLastName(lastName);
        if(file!=null  && !file.getOriginalFilename().isEmpty()){
            uploadPhotoForActor(file, actor);
        }
        return actorRepository.save(actor);
    }
    @Transactional
    public Actor deleteActor(Actor actor) {
        final Actor deleted = findOne(actor.getId());
        actorRepository.delete(actor);
        return deleted;
    }

    public boolean isCyrillic(String str){
        String regex = "[а-яёА-ЯЁ]+";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        return m.find();
    }

    public void uploadPhotoForActor(MultipartFile file, Actor actor) throws IOException {
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
        actor.setPhoto(resultFilename);
    }
}
