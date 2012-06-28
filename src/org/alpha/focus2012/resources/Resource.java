package org.alpha.focus2012.resources;


public class Resource {

    public static enum Type {
        SpeakerImageSmall,
        SpeakerImageMedium,
        SpeakerImageLarge,
        VenueImageSmall,
        VenueImageMedium,
        VenueImageLarge,
        VenueFloorplan,
        ConferenceImage,
        TwitterAvatar
    }

    
    final String key;
    final Type type;
    
    public Resource(String key, Type type) {
        this.key = key;
        this.type = type;
    }
    

    public String url() {
        String baseUrl = "http://static.alpha.org/acs/conferences/";
        
        switch (type) {
        case SpeakerImageSmall:
            return baseUrl + "speakers/"+key+"/100.jpg";
        case SpeakerImageMedium:
            return baseUrl + "speakers/"+key+"/150.jpg";
        case SpeakerImageLarge:
            return baseUrl + "speakers/"+key+"/200.jpg";
        case VenueImageSmall:
            return baseUrl + "venues/"+key+"/100.jpg";
        case VenueImageMedium:
            return baseUrl + "venues/"+key+"/150.jpg";    
        case VenueImageLarge:
            return baseUrl + "venues/"+key+"/200.jpg";
        case VenueFloorplan:
            return baseUrl + "venues/"+key+".pdf"; 
        case ConferenceImage:
            return baseUrl +  "branding/"+key+"/288.jpg";
        case TwitterAvatar:
            return "http://api.twitter.com/1/users/profile_image?screen_name="+key+"&size=bigger";
        default:
            return null;
        }
    }
    
    public String cacheFilename() {
        return "resource-"+key+"-"+type.ordinal();
    }

}
