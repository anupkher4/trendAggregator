package Twitter;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstagramTrend {

	@Id
    public  String id;	
    private String url;
    private String created_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
    
}

