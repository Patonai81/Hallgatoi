package hu.webuni.nyilvantarto.web.controller;

import hu.webuni.nyilvantarto.api.ImageControllerApi;
import hu.webuni.nyilvantarto.repository.file.File;
import hu.webuni.nyilvantarto.repository.file.FileContentStore;
import hu.webuni.nyilvantarto.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController implements ImageControllerApi {

    @Autowired
    private FileRepository filesRepo;
    @Autowired
    private FileContentStore contentStore;

    @Override
    public ResponseEntity<Resource> downloadImage(Long id) {
        return getResourceResponseEntity(id,false);
    }

    @Override
    public ResponseEntity<Resource> deleteImage(Long id) {
        return  getResourceResponseEntity(id,true);
    }

    //ImageServicebe átrakni, átwrappelve
    private ResponseEntity<Resource> getResourceResponseEntity(Long id, Boolean needToDelete) {
        Optional<File> f = filesRepo.findById(id);
        if (f.isPresent()) {
            InputStreamResource inputStreamResource = new InputStreamResource(contentStore.getContent(f.get()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(f.get().getContentLength());
            headers.set("Content-Type", f.get().getContentMimeType());

            contentStore.unsetContent(f.get());
            filesRepo.delete(f.get());
            log.info("Entity successfully deleted");

            return new ResponseEntity<Resource>(inputStreamResource, headers, HttpStatus.OK);
        }
        return null;
    }

}
