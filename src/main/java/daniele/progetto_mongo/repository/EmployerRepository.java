package daniele.progetto_mongo.repository;

import daniele.progetto_mongo.model.Employer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends MongoRepository<Employer, String> {


}
