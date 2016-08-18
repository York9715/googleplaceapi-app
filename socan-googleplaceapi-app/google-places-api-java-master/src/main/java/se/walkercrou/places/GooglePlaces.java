package se.walkercrou.places;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import se.walkercrou.places.exception.GooglePlacesException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Main class of API. Used for all entry web-api operations.
 */
public class GooglePlaces implements GooglePlacesInterface {

    /*
     * Argument #1: API Base URL
     * Argument #2: API Method
     * Argument #3: API Method arguments
     */
    public static String API_URL_FORMAT_STRING = "%s%s/json?%s";

    private String apiKey;
    private RequestHandler requestHandler;
    private boolean debugModeEnabled;

    /**
     * Creates a new GooglePlaces object using the specified API key and the specified {@link RequestHandler}.
     *
     * @param apiKey         that has been registered on the Google Developer Console
     * @param requestHandler to handle HTTP traffic
     */
    public GooglePlaces(String apiKey, RequestHandler requestHandler) {
        this.apiKey = apiKey;
        this.requestHandler = requestHandler;
    }

    /**
     * Creates a new GooglePlaces object using the specified API key.
     *
     * @param apiKey that has been registered on the Google Developer Console
     */
    public GooglePlaces(String apiKey) {
        this(apiKey, new DefaultRequestHandler());
    }

    /**
     * Creates a new GooglePlaces object using the specified API key and character encoding. Using a character encoding
     * other than UTF-8 is not advised.
     *
     * @param apiKey            that has been registered on the Google Developer Console
     * @param characterEncoding to parse data with
     */
    public GooglePlaces(String apiKey, String characterEncoding) {
        this(apiKey, new DefaultRequestHandler(characterEncoding));
    }

    private static String addExtraParams(String base, Param... extraParams) {
        for (Param param : extraParams)
            base += "&" + param.name + (param.value != null ? "=" + param.value : "");
        return base;
    }

    private static String buildUrl(String method, String params, Param... extraParams) {
        String url = String.format(Locale.ENGLISH, API_URL_FORMAT_STRING, API_URL, method, params);
        url = addExtraParams(url, extraParams);
        url = url.replace(' ', '+');
        return url;
    }

    protected static void checkStatus(String statusCode, String errorMessage) {
        GooglePlacesException e = GooglePlacesException.parse(statusCode, errorMessage);
        if (e != null)
            throw e;
    }

    /**
     * Parses the specified raw json String into a list of places.
     *
     * @param places to parse into
     * @param str    raw json
     * @param limit  the maximum amount of places to return
     * @return Next page token
     */
    public static String parse(GooglePlaces client, List<Place> places, String str, int limit) {
        // parse json
        JSONObject json = new JSONObject(str);

        // check root elements
        String statusCode = json.getString(STRING_STATUS);
        checkStatus(statusCode, json.optString(STRING_ERROR_MESSAGE));
        if (statusCode.equals(STATUS_ZERO_RESULTS))
            return null;

        JSONArray results = json.getJSONArray(ARRAY_RESULTS);
        parseResults(client, places, results, Math.min(limit, MAXIMUM_PAGE_RESULTS));

        return json.optString(STRING_NEXT_PAGE_TOKEN, null);
    }
    
    /**
     * Parses the specified Radar raw json String into a list of places.
     *
     * @param places to parse into
     * @param str    Radar raw json
     * @param limit  the maximum amount of places to return
     */
    public static void parseRadar(GooglePlaces client, List<Place> places, String str, int limit) {
      // parse json
      JSONObject json = new JSONObject(str);
      
      // check root elements
      String statusCode = json.getString(STRING_STATUS);
      checkStatus(statusCode, json.optString(STRING_ERROR_MESSAGE));
      if (statusCode.equals(STATUS_ZERO_RESULTS))
        return;
      
      JSONArray results = json.getJSONArray(ARRAY_RESULTS);
      parseResults(client, places, results, Math.min(limit, MAXIMUM_RADAR_RESULTS));
    }

    private static void parseResults(GooglePlaces client, List<Place> places, JSONArray results, int limit) {
        for (int i = 0; i < limit; i++) {

            // reached the end of the page
            if (i >= results.length())
                return;

            JSONObject result = results.getJSONObject(i);

            // location
            JSONObject location = result.getJSONObject(OBJECT_GEOMETRY).getJSONObject(OBJECT_LOCATION);
            double lat = location.getDouble(DOUBLE_LATITUDE);
            double lon = location.getDouble(DOUBLE_LONGITUDE);

            String placeId = result.getString(STRING_PLACE_ID);
            String iconUrl = result.optString(STRING_ICON, null);
            String name = result.optString(STRING_NAME);
            String addr = result.optString(STRING_ADDRESS, null);
            double rating = result.optDouble(DOUBLE_RATING, -1);
            String vicinity = result.optString(STRING_VICINITY, null);

            // see if the place is open, fail-safe if opening_hours is not present
            JSONObject hours = result.optJSONObject(OBJECT_HOURS);
            boolean hoursDefined = hours != null && hours.has(BOOLEAN_OPENED);
            Status status = Status.NONE;
            if (hoursDefined) {
                boolean opened = hours.getBoolean(BOOLEAN_OPENED);
                status = opened ? Status.OPENED : Status.CLOSED;
            }

            // get the price level for the place, fail-safe if not defined
            boolean priceDefined = result.has(INTEGER_PRICE_LEVEL);
            Price price = Price.NONE;
            if (priceDefined) {
                price = Price.values()[result.getInt(INTEGER_PRICE_LEVEL)];
            }

            // the place "types"
            List<String> types = new ArrayList<>();
            JSONArray jsonTypes = result.optJSONArray(ARRAY_TYPES);
            if (jsonTypes != null) {
                for (int a = 0; a < jsonTypes.length(); a++) {
                    types.add(jsonTypes.getString(a));
                }
            }

            Place place = new Place();

            // build a place object
            places.add(place.setClient(client).setPlaceId(placeId).setLatitude(lat).setLongitude(lon).setIconUrl(iconUrl).setName(name)
                    .setAddress(addr).setRating(rating).setStatus(status).setPrice(price)
                    .addTypes(types).setVicinity(vicinity).setJson(result));
        }
    }

    @Override
    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    @Override
    public void setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    private void debug(String msg) {
        if (debugModeEnabled)
            System.out.println(msg);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%s,%s&radius=%s",
                    apiKey, String.valueOf(lat), String.valueOf(lng), String.valueOf(radius)), extraParams);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    
    public Location getLocationByPostCode(String postCode) {
 
    	if (postCode.isEmpty() || apiKey.isEmpty() )
    		return null;
    	
    	try {
			String uri="https://maps.googleapis.com/maps/api/geocode/xml?address=" + postCode + "&key=" + apiKey;
			String raw = requestHandler.get(uri);

			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(raw));
			Document doc = db.parse(is);		    
			
			doc.getDocumentElement().normalize();	
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	
			NodeList nList = doc.getElementsByTagName("geometry");
			if (nList == null) return null;
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
	
				Node nNode = nList.item(temp);	
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
	
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Location location = new Location();    					
					Element eElement = (Element) nNode;
					
					if (eElement.getElementsByTagName("lat").item(0) !=null) {
						location.setLat(Double.parseDouble(eElement.getElementsByTagName("lat").item(0).getTextContent()));
						location.setLng(Double.parseDouble(eElement.getElementsByTagName("lng").item(0).getTextContent()));
						
						return location;
					}
				}
	
			}
			
    	}catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
		
		return null;

    }  
    
    public List<Place> getNearbySocanRelativePlacesByPostCode(String postCode, double radius ) {
    	Location location = getLocationByPostCode(postCode);
    	if ( location ==  null)
    		return null;
    	
    	return getNearbySocanRelativePlaces(location.getLat(),location.getLng(), radius,MAXIMUM_RESULTS);    	
    }
    
    public List<Place> getNearbySocanRelativePlaces(double lat, double lng, double radius,int nLimit ) {
        try {  
            //uri+="types=art_gallery|bakery|book_store|casino|gym|liquor_store";
            //|movie_rental|movie_theater|night_club|real_estate_agency|restaurant|shopping_mall|spa|stadium|store|travel_agency";
       	
        	List<Place> listPlaces= new ArrayList<Place>();
        	List<Place> ret;
        	
        	//1. art_gallery
        	String type="art_gallery";
        	ret=null;//ret=getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret != null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}
        	
        	//2. bakery
        	type="bakery";
        	ret=null;//ret=getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret !=null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}       	

        	//3. book_store
        	type="book_store";
        	ret=null;//ret=getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret !=null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}

        	//4. casino
        	type="casino";
        	ret=null;//getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret !=null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}
        	
        	//5. gym
        	//Thread.sleep(3000);
        	type="gym";
        	ret=getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret !=null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}

        	//5. night_club
        	//Thread.sleep(3000);
        	type="night_club";
        	ret=getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret !=null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}

        	//6. restaurant
        	//Thread.sleep(3000);
        	type="restaurant";
        	ret=getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret !=null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}

        	//7. spa
        	//Thread.sleep(3000);
        	type="spa";
        	ret=getNearbySocanRelativePlacesByType(lat,lng,radius,nLimit,type);
        	if (ret !=null ) {
        		debug(ret,type);
        		listPlaces.addAll(ret);
        	}else{
        		System.out.println("No " + type + " found");
        	}        	
            
        	return listPlaces;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    public List<Place> getNearbySocanRelativePlacesByType(double lat, double lng, double radius, int nLimit,String type ) {
        try {  
        	String uri="";
        	uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%s,%s&radius=%s",
                    apiKey, String.valueOf(lat), String.valueOf(lng), String.valueOf(radius)));
           
        	if (!type.isEmpty()) 
        		uri += "&type=" + type;            
        	
            return getPlaces(uri, METHOD_NEARBY_SEARCH, nLimit);
        	//return getPlacesBypaging(uri, METHOD_NEARBY_SEARCH);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	return null;
            //hrow new GooglePlacesException(e);
        }
    }
    
    
    @Override
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, Param... extraParams) {
        return getNearbyPlaces(lat, lng, radius, DEFAULT_RESULTS, extraParams);
    }

    @Override
    public List<Place> getNearbyPlacesRankedByDistance(double lat, double lng, int limit, Param... params) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%s,%s&rankby=distance",
                    apiKey, String.valueOf(lat), String.valueOf(lng)), params);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getNearbyPlacesRankedByDistance(double lat, double lng, Param... params) {
        return getNearbyPlacesRankedByDistance(lat, lng, DEFAULT_RESULTS, params);
    }

    @Override
    public List<Place> getPlacesByQuery(String query, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_TEXT_SEARCH, String.format("query=%s&key=%s", query, apiKey),
                    extraParams);
            return getPlaces(uri, METHOD_TEXT_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getPlacesByQuery(String query, Param... extraParams) {
        return getPlacesByQuery(query, DEFAULT_RESULTS, extraParams);
    }

    @Override
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_RADAR_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return getRadarPlaces(uri, METHOD_RADAR_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams) {
        return getPlacesByRadar(lat, lng, radius, MAXIMUM_RESULTS, extraParams);
    }

    @Override
    public Place getPlaceById(String placeId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DETAILS, String.format("key=%s&placeid=%s", apiKey, placeId), extraParams);
            return Place.parseDetails(this, requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public Place addPlace(PlaceBuilder builder, boolean returnPlace, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_ADD, String.format("key=%s", apiKey));
            JSONObject input = builder.toJson();
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            String status = response.getString(STRING_STATUS);
            checkStatus(status, response.optString(STRING_ERROR_MESSAGE));
            return returnPlace ? getPlaceById(response.getString(STRING_PLACE_ID)) : null;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public void deletePlaceById(String placeId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DELETE, String.format("key=%s", apiKey), extraParams);
            JSONObject input = new JSONObject().put(STRING_PLACE_ID, placeId);
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            String status = response.getString(STRING_STATUS);
            checkStatus(status, response.optString(STRING_ERROR_MESSAGE));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public void deletePlace(Place place, Param... extraParams) {
        deletePlaceById(place.getPlaceId(), extraParams);
    }

    protected InputStream download(String uri) {
        try {
            InputStream in = requestHandler.getInputStream(uri);
            if (in == null)
                throw new GooglePlacesException("Could not attain input stream at " + uri);
            debug("Successfully attained InputStream at " + uri);
            return in;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    protected InputStream downloadPhoto(Photo photo, int maxWidth, int maxHeight, Param... extraParams) {
        try {
            String uri = String.format("%sphoto?photoreference=%s&key=%s", API_URL, photo.getReference(),
                    apiKey);

            List<Param> params = new ArrayList<>(Arrays.asList(extraParams));
            if (maxHeight != -1) params.add(Param.name("maxheight").value(maxHeight));
            if (maxWidth != -1) params.add(Param.name("maxwidth").value(maxWidth));
            extraParams = params.toArray(new Param[params.size()]);
            uri = addExtraParams(uri, extraParams);

            return download(uri);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    private List<Prediction> getPredictions(String input, String method, Param... extraParams) {
        try {
            String uri = buildUrl(method, String.format("input=%s&key=%s", input, apiKey),
                    extraParams);
            String response = requestHandler.get(uri);
            return Prediction.parse(this, response);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, int offset, int lat, int lng, int radius,
                                                Param... extraParams) {
        List<Param> params = new ArrayList<>();
        if (offset != -1)
            params.add(Param.name("offset").value(offset));
        if (lat != -1 && lng != -1)
            params.add(Param.name("location").value(lat + "," + lng));
        params.addAll(new ArrayList<>(Arrays.asList(extraParams)));

        return getPredictions(input, METHOD_AUTOCOMPLETE, params.toArray(new Param[params.size()]));
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, int offset, Param... extraParams) {
        return getPlacePredictions(input, offset, -1, -1, -1, extraParams);
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, Param... extraParams) {
        return getPlacePredictions(input, -1, extraParams);
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, int offset, int lat, int lng, int radius,
                                                Param... extraParams) {
        List<Param> params = new ArrayList<>();
        if (offset != -1)
            params.add(Param.name("offset").value(offset));
        if (lat == -1 && lng == -1)
            params.add(Param.name("location").value(lat + "," + lng));
        params.addAll(new ArrayList<>(Arrays.asList(extraParams)));

        return getPredictions(input, METHOD_QUERY_AUTOCOMPLETE, params.toArray(new Param[params.size()]));
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, int offset, Param... extraParams) {
        return getQueryPredictions(input, offset, -1, -1, -1, extraParams);
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, Param... extraParams) {
        return getQueryPredictions(input, -1, extraParams);
    }

    private List<Place> getPlaces(String uri, String method, int limit) throws IOException {
        limit = Math.min(limit, MAXIMUM_RESULTS); // max of 60 results possible
        int pages = (int) Math.ceil(limit / (double) MAXIMUM_PAGE_RESULTS);

        List<Place> places = new ArrayList<>();
        // new request for each page
        for (int i = 0; i < pages; i++) {
            debug("Page: " + (i + 1));
            String raw = requestHandler.get(uri);
            debug(raw);
            String nextPage = parse(this, places, raw, limit);
            // reduce the limit, update the uri and wait for token, but only if there are more pages to read
            if (nextPage != null && i < pages - 1) {
                limit -= MAXIMUM_PAGE_RESULTS;
                uri = String.format("%s%s/json?pagetoken=%s&key=%s",
                        API_URL, method, nextPage, apiKey);
                sleep(3000); // Page tokens have a delay before they are available
            } else {
                break;
            }
        }

        return places;
    }

    private List<Place> getPlacesBypaging(String uri, String method) throws IOException {
        int pages = MAXIMUM_PAGE_RESULTS;
        int limit=400;
        List<Place> places = new ArrayList<>();
        // new request for each page
        for (int i = 0; i < pages; i++) {
            debug("Page: " + (i + 1));
            System.out.println("Page: " + (i + 1));
            String raw = requestHandler.get(uri);
            System.out.println("url: " + uri);
            debug(raw);
            String nextPage = parse(this, places, raw, limit);
            // reduce the limit, update the uri and wait for token, but only if there are more pages to read
            if (nextPage != null && i < pages - 1) {
                limit -= MAXIMUM_PAGE_RESULTS;
                uri = String.format("%s%s/json?pagetoken=%s&key=%s",
                        API_URL, method, nextPage, apiKey);
                sleep(3000); // Page tokens have a delay before they are available
            } else {
                break;
            }
        }

        return places;
    }
    
    private List<Place> getRadarPlaces(String uri, String method, int limit) throws IOException {
      limit = Math.min(limit, MAXIMUM_RADAR_RESULTS); // max of 200 results possible

      List<Place> places = new ArrayList<>();
      String raw = requestHandler.get(uri);
      debug(raw);
      parseRadar(this, places, raw, limit);

      return places;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void debug(List<Place> places,String type) {
     	System.out.println("type=" + type);
     	debug(places);
    }
    
    public static void debug(List<Place> places) {
		int idx=0;
		for (Place place : places) {
			place.debug(place, idx);
			idx++;
		}
    }
}
