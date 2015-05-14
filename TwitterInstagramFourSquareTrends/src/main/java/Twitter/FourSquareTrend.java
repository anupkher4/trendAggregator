package Twitter;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FourSquareTrend {

	@Id
    public  String id;
	private String name;
	private String category;
    private String url;
    private long checkinsCounts;
    private long usersCount;
    private String address;
    private String crossStreet;
    private String city;
    private String state;
    private String fullAddress;
    private String zipCode;
    private String insertedTime;
    
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public long getCheckinsCounts() {
		return checkinsCounts;
	}
	public void setCheckinsCounts(long checkinsCounts) {
		this.checkinsCounts = checkinsCounts;
	}
	public long getUsersCount() {
		return usersCount;
	}
	public void setUsersCount(long usersCount) {
		this.usersCount = usersCount;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCrossStreet() {
		return crossStreet;
	}
	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getInsertedTime() {
		return insertedTime;
	}
	public void setInsertedTime(String insertedTime) {
		this.insertedTime = insertedTime;
	}

    
}
