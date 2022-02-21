package hu.webuni.nyilvantarto.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="akarmicsoda", collectionResourceRel="files")
public interface FileRepository extends JpaRepository<File, Long> {

}
