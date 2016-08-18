1. Import database (socandump0818.zip)

2. Create "googleapi"/"googleapi"@localhost user
   (Default db is socan2)

3. Open Project "google-places-api-java-master" and intall it
   places_api.key is the place to store the Googel Places API KEY
    

4. Open Project "socan-googleplaceapi-app-java", 
   I think we can put the all Canada and UsA post code into a table, run this job every day by pickcing up postcode from db.
   Google Places API only allow 1000 calls each account in a day.

   //Get post code list
   String sPostCodes="M3B2S6;M2H3H1";
   

3. The Main entry is AppMain.java


6. :
    We only pick up following business units:
    (1) GYM
    (2) Night 
    (3) Restaurant
    (4) spa
    
     See details in them methos of GooglePlaces.java: 
        public List<Place> getNearbySocanRelativePlaces(double lat,....
         ....

