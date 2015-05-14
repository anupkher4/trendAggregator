package Twitter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface FourSquareTrendRepository extends MongoRepository<FourSquareTrend, String> {
	  
 }
