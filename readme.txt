1. Import database (socandump0818.zip)

2. Create "googleapi"/"googleapi"@localhost
   (Default db is socan2)

3. Open Project "google-places-api-java-master" and intall it

4. Open Project "socan-googleplaceapi-app-java", 
   I think we can put the all Canada and UsA post code into a table, run this job every day by pickcing up postcode from db.
   Google Places API only allow 1000 calls each account in a day.

   //Get post code list
   String sPostCodes="M3B2S6;M2H3H1";
   

3. The Main entry is AppMain.java

