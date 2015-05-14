package Twitter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface TwitterAvailableTrendRepository extends MongoRepository<TwitterAvailableTrend, String> {

}
