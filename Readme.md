# Spring Boot Forecast Application

This is a Spring Boot application that retrieves weather forecast data based on a Place, State, Country Code. It uses Open Mateo API to fetch real-time weather information and caches responses for 30 minutes.

## Features
- Accepts an address place, state, country code  & units of temperature(Celsius / fahrenheit) as query params
- Retrieves Latitude and Longitude of the location from open mateo API 
- Gets the 7 days of forecast of weather from open mateo API
- Caches results for 30 minutes to improve performance
- Provides an indicator if data is retrieved from cache
- Swagger UI integration for API documentation

## Requirements
- Java 17
- Maven
- OpenWeatherMap API Key

## Setup Instructions
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/spring-boot-forecast.git
   ```
2. Navigate to the project directory:
   ```sh
   cd spring-boot-forecast
   ```
3. Add your OpenWeatherMap API key in `ForecastService.java`:
   ```java
   String apiUrl = "https://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + "&appid=YOUR_API_KEY";
   ```
4. Build and run the application:
   ```sh
   mvn spring-boot:run
   ```
5. Access the API at:
   ```sh
   http://localhost:8080/weatherService/address?place=Richmond&state=VA&countryCode=USA&units=celsius
   ```
6. Access Swagger UI:
   ```sh
   http://localhost:8080/swagger-ui.html
   ```

## Testing
Run unit tests using:
```sh
mvn test
```

## API Endpoints
| Method | Endpoint                                                                       | Description |
|--------|--------------------------------------------------------------------------------|-------------|
| GET | `/weatherService/address?place=Richmond&state=VA&countryCode=US&units=celsius` | Retrieves weather forecast for the given ZIP code |

## License
This project is open-source and available under the MIT License.

