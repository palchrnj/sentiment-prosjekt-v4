package newsAPI;

/*CLASS REPRESENTING NEWS ARTICLE JSON OBJECT*/
public class NewsArticleRaw {
	public String title;
	public String id;
	public String published;
	public String [] cat;
	public String publisher;
	public String [] links;
	public String text;
	public String lead_text;
	public String feed_image_url;
	public String sentiment_value;
	public String [] tags;
	public String signature;
	public String version;
	public String last_modified;
	
	public NewsArticleRaw(){
		
	}

	public NewsArticleRaw(String title, String id, String published,String [] cat, String publisher, String []  links, String text, String lead_text,String imageUrl, String sentimentValue, String []  tags, String signature, String version, String last_modified){
		this.title = title;
		this.id = id;
		this.published = published;
		this.cat = cat;
		this.publisher = publisher;
		this.links = links;
		this.text = text;
		this.lead_text = lead_text;
		this.feed_image_url = imageUrl;
		this.sentiment_value = sentimentValue;
		this.tags = tags;
		this.signature = signature;
		this.version = version;
		this.last_modified = last_modified;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpublished() {
		return published;
	}
	public void setpublished(String published) {
		this.published = published;
	}
	public String []  getcat() {
		return cat;
	}
	public void setcat(String []  cat) {
		this.cat = cat;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String []  getLinks() {
		return links;
	}
	public void setLinks(String []  links) {
		this.links = links;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getlead_text() {
		return lead_text;
	}
	public void setlead_text(String lead_text) {
		this.lead_text = lead_text;
	}
	public String getImageUrl() {
		return feed_image_url;
	}
	public void setImageUrl(String imageUrl) {
		this.feed_image_url = imageUrl;
	}
	public String getSentimentValue() {
		return sentiment_value;
	}
	public void setSentimentValue(String sentimentValue) {
		this.sentiment_value = sentimentValue;
	}
	public String []  getTags() {
		return tags;
	}
	public void setTags(String []  tags) {
		this.tags = tags;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getversion() {
		return version;
	}
	public void setversion(String version) {
		this.version = version;
	}
	public String getlast_modified() {
		return last_modified;
	}
	public void setlast_modified(String last_modified) {
		this.last_modified = last_modified;
	}
}

