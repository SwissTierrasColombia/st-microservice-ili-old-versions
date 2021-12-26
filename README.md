# ILI MICROSERVICE OLD VERSIONS

Microservice that allows validating a xtf file in the survey submodel in version 1.0

## Running Development

```sh
$ mvn spring-boot:run
```

## Configuration

### How to disable eureka client?

Modify the **enabled** property in st-microservice-ili-old-versions/src/main/resources/**application.yml** file:

```yml
eureka:
  client:
    enabled: false
```

### How to disable config client?

Modify the **enabled** property in st-microservice-ili-old-versions/src/main/resources/**bootstrap.yml** file:

```yml
spring:
  application:
    name: st-microservice-ili-old-versions
  cloud:
    config:
      enabled: false
```

## Swagger Documentation?

See [http://localhost:9005/swagger-ui.html](http://localhost:7986/swagger-ui.html)

## License

GNU AFFERO GENERAL PUBLIC LICENSE 
 [Agencia de Implementación - BSF Swissphoto - INCIGE](https://github.com/SwissTierrasColombia/st-microservice-ili-old-versions/blob/master/LICENSE)