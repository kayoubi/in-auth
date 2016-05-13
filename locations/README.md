# InAuth

Steps to install 
----------------

* follow the instructions in `db-schema.sql`
* load US boundaries (downloaded from [here](ftp://ftp2.census.gov/geo/tiger/TIGER2012/STATE/tl_2012_us_state.zip)) into the DB using the following command (I used `locate shp2pgsql` on Mac to find the utility)

   `shp2pgsql -s 4269 -g geom_4269 -I -W "latin1" "{path-to-resource}/tl_2012_us_state" inauth.tl_2012_states | psql -h localhost -p 5432 -d inauth_locations -U postgres`

API usage
---------
* `GET /inauth/locations` returns all locations in DB, optional params:
  * `details=true` returns all locations in DB with `inUSA` and `distances` details
  * `foramt=xml` must be used with `details=true` converts the result into xls file

* `GET /inauth/locations/search?lng={lng}&lat={lat}` returns a particular location if found, optional params:
  * `details=true` returns the found location in DB with `inUSA` and `distances` details
  * `foramt=xml` must be used with `details=true` converts the result into xls file
 
* `POST /inauth/locations` create a new location if doesn't already exist
  * header `Content-Type=application/json;charset=UTF-8`
  * body `{"lng": 46.6915985,"lat": 14.7064375}`
  * returns `201` and location header for newly created location `/inauth/locations/search?lng=46.6915985&lat=14.7064375`
  * returns `400` if location already exists
   
Start the application
---------------------
   
* Embedded Tomcat: go to the root directory and run `mvn spring-boot:run` (the server starts at port `7777`)
* Stand alone container: either build the war file using `mvn clean install` and copy `target/inuath.war` to tomcat or download from [here](https://www.dropbox.com/s/881ruklijziqk2u/inauth.war?dl=0)
* The application uses Google Geocoding API to get the list of cities locations (in `application.properties`) and store them in the DB at startup time, so the system has to be online, another appoach could have been do store these in a fixture and load to the DB, but I though it was nice to be able to add cities by modifying the config file     




