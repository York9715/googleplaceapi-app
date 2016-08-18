package se.walkercrou.places;

/**
 * Represents an extra, optional parameter that can be specified.
 */
public class Param {
    protected final String name;
    protected String value;

    public Param(String name) {
        this.name = name;
    }

    /**
     * Returns a new param with the specified name.
     *
     * @param name to create Param from
     * @return new param
     */
    public static Param name(String name) {
        return new Param(name);
    }

    /**
     * Sets the value of the Param.
     *
     * @param value of param
     * @return this param
     */
    public Param value(Object value) {
        this.value = value.toString();
        return this;
    }

    
    /**
     * Get the value of the Type Param for SOCAN google places AIP call.
     *
     * @param value of param
     * @return this all types for business units that may need a music copy license
     */    
    public static String getSocanTypePara(){
    	
		String ret = Types.TYPE_ART_GALLERY + "|";
		ret += Types.TYPE_BAKERY + "|";
		ret += Types.TYPE_BOOK_STORE + "|";
		ret += Types.TYPE_CASINO + "|";
		ret += Types.TYPE_DEPARTMENT_STORE + "|";
		ret += "casino|";
		ret += "department_store|";
		ret += "electronics_store|";
		ret += "funeral_home|";
		ret += "gym|";
		ret += "liquor_store|";
		ret += "movie_rental|";
		ret += "movie_theater|";
		ret += "night_club|";
		ret += "real_estate_agency|";
		ret += "restaurant|";
		ret += "shopping_mall|";
		ret += "spa|";
		ret += "stadium|";
		ret += "store|";
		ret += "travel_agency";
		return ret;
    }
    
}
