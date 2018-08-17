[DaData](http://dadata.ru) API Java library
===========================================

[![GitHub license](https://img.shields.io/github/license/redcom-internet/dadata-api-client.svg)](https://github.com/redcom-internet/dadata-api-client/blob/master/LICENSE.txt)
[![GitHub release](https://img.shields.io/github/release/redcom-internet/dadata-api-client.svg)](https://github.com/redcom-internet/dadata-api-client/releases)
[![Maven Central](https://img.shields.io/maven-central/v/ru.redcom.lib/dadata-api-client.svg)](https://search.maven.org/search?q=a:dadata-api-client)

This is a simple library for using the DaData Standardization API with Java language.  
It was written for a Spring application and uses Spring Framework to implement REST API client.
Required dependencies are `spring-boot-starter-json` and `lombok`.

[Standardization API Reference](https://dadata.ru/api/clean/)

## Installation

#### 1. Add a dependencies to the project:  
**Maven:**
```xml
<dependencies>
    <dependency>
        <groupId>ru.redcom.lib</groupId>
        <artifactId>dadata-api-client</artifactId>
        <version>0.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-json</artifactId>
        <version>2.0.4.RELEASE</version>
    </dependency>
</dependencies>
```
**Gradle:**
```groovy
dependencies {
    compile group: 'ru.redcom.lib', name: 'dadata-api-client', version: '0.0.1'
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-json', version: '2.0.4.RELEASE'
}
```
#### 2. Get a registration at [dadata.ru](https://dadata.ru) and acquire API credentials (API key and Secret key) at [your account profile](https://dadata.ru/profile/#info).   

#### 3. Use the library API to call DaData Standardization functions.
API for Location addresses, Phone and Passport numbers, E-mail addresses,
Person names and surnames, Person birthdates, and Vehicle numbers is implemented.  
Array requests and Composite records are supported as well.    
 
## Usage examples

### Clean an address:
```java
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;
import ru.redcom.lib.integration.api.client.dadata.dto.Address;

public class Standardization {
	private static final String API_KEY = "<dadata_api_key>";
	private static final String SECRET_KEY = "<dadata_secret_key>";
	private final DaDataClient dadata = DaDataClientFactory.getInstance(API_KEY, SECRET_KEY);

	public Address cleanAddress(final String source) throws DaDataException {
		final Address a = dadata.cleanAddress(source);
		System.out.println("cleaned address: " + a);
		return a;
	}
}
```

### Get the account balance
```java
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;

import java.math.BigDecimal;

public class Info {
	private static final String API_KEY = "<dadata_api_key>";
	private static final String SECRET_KEY = "<dadata_secret_key>";
	private final DaDataClient dadata = DaDataClientFactory.getInstance(API_KEY, SECRET_KEY);

	public BigDecimal accountBalance() throws DaDataException {
		final BigDecimal balance = dadata.getProfileBalance();
		System.out.println("Balance = " + balance);
		return balance;
	}
}
```

#### The same, as a Spring application component using injected RestTemplateBuilder instance:
```java
import org.springframework.stereotype.Component;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;

import java.math.BigDecimal;

@Component
public class Info {
	private static final String API_KEY = "<dadata_api_key>";
	private static final String SECRET_KEY = "<dadata_secret_key>";
	private final DaDataClient dadata;

	public Info(final RestTemplateBuilder restTemplateBuilder) {
		dadata = DaDataClientFactory.getInstance(API_KEY, SECRET_KEY, null, restTemplateBuilder);
	}
	
	public BigDecimal accountBalance() throws DaDataException {
		final BigDecimal balance = dadata.getProfileBalance();
		System.out.println("Balance = " + balance);
		return balance;
	}
}
```

See javadoc for library API description and tests code for more examples.
