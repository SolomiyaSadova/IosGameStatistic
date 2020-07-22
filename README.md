# IosGameStatistic
- read game charts statistic from URLs and save it in database
- refresh database every hour
- get game by some game type and limit

## Built with 
- Kotlin
- Maven
- MongoDB
- Spring

## Installation

```bash
mvn springboot:run
```
```bash
mvn package
```
Run jar file

```bash
java -jar ./target/demo-0.0.1-SNAPSHOT.jar
```

## Additional info

You can set URLs in /resources/fetchURLs file.

Also you can set another time to refresh database. Make it in application.properties. Update `delay.before.feed.games.to.database` value
