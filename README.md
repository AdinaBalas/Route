# Shortest land route between 2 countries

---

## Task
Create a simple Spring Boot service, that is able to calculate any possible land
route from one country to another. The objective is to take a list of country data in JSON format
and calculate the route by utilizing individual countries border information.

---

## Specifications
- Spring Boot, Maven
- Data link: https://raw.githubusercontent.com/mledoze/countries/master/countries.json
- The application exposes REST endpoint /routing/{origin}/{destination} that
returns a list of border crossings to get from origin to destination
- Single route is returned if the journey is possible
- Algorithm needs to be efficient
- If there is no land crossing, the endpoint returns HTTP 400
- Countries are identified by cca3 field in country data
- HTTP request sample (land route from Czech Republic to Italy):

<code>
GET /routing/CZE/ITA HTTP/1.0 :
{
"route": ["CZE", "AUT", "ITA"]
}
</code>

---

## Implementation
- Java 17
- BFS Algorithm
- Spring Boot
- Maven
- Open API
- Mockito
- TestNG

---

## Build and run the application
### 1. Command line
- To build the application run the following command in a terminal window

<code>mvnw clean install</code>

- To run the application run the following command in a terminal window

<code>mvnw spring-boot:run</code>

### 2. IDE
- Open your IDE (IntelliJ)
- Maven: clean + package
- Navigate to RouteApplication file and run main method

## Test the application

Application will run on port **8087**.

### Check exposed endpoints
You can access the following URL to check the exposed endpoints:
http://localhost:8087/swagger

### Test countries route
You can use a command line, Insomnia or Postman 

<code>
curl --request GET \
--url http://localhost:8087/routing/origin/destination
</code>

URL example:
http://localhost:8087/routing/CZE/IT
