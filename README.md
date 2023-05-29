# *Simple spring boot application* 

Application was created to test the API for educational purposes. Used stack: java 11, spring boot, lombok, h2database.

## *How to use application:*

* Clone project 
> `git clone https://github.com/kartavenkopavel/Simple_api.git`
* Start application
> `mvn spring-boot:run`
* Access to the server at 
> http://localhost:8090
* Authorization via api key is configured on the server. You should use header
> `X-API-KEY : 118902c4-1990-4vxp-3g08-wq522f71a854`
* Access to Swagger documentation at
> http://localhost:8090/swagger-ui/index.html (public link)
* Access to the database at
> http://localhost:8090/h2-console/ (public link)
* Or you can use a docker container
> `docker run -d -p 8090:8090 pavelkartavenko/simple-api:1.0.1`
