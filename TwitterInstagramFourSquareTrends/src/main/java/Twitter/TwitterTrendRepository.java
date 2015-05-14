package Twitter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface TwitterTrendRepository extends MongoRepository<TwitterTrend, String> {

}