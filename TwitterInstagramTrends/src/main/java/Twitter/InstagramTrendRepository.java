package Twitter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface InstagramTrendRepository extends MongoRepository<InstagramTrend, String> {
   
 }
