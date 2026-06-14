# MiniShop — Lab 12: Pruebas de Integración

Curso: Construcción y Pruebas de Software — IV Ciclo
Alumno: Leo Pinto Garate
Docente: Mg. Edwin Córdova Benavente
Semana: 12

## Descripción
API REST para gestionar productos de una tienda, desarrollada con Spring Boot.
El objetivo del laboratorio es implementar pruebas de integración en tres capas.

## Tecnologías
- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- H2 Database (en memoria)
- Lombok
- JUnit 5 + MockMvc + Mockito

## Pruebas de integración
| Clase | Anotación | Tests |
|---|---|---|
| ProductRepositoryIntegrationTest | @DataJpaTest | 4 |
| ProductServiceIntegrationTest | @SpringBootTest + @MockBean | 5 |
| ProductControllerIntegrationTest | @SpringBootTest + MockMvc | 4 |

## Conclusiones
1. Las pruebas de integración verifican que los contratos entre capas funcionen correctamente.
2. @DataJpaTest permite probar el repositorio de forma aislada contra H2.
3. @MockBean junto con @SpringBootTest verifica la lógica del servicio sin depender de la base de datos real.
4. MockMvc permite probar el flujo completo desde la petición HTTP hasta la base de datos.
5. En Spring Boot 3.x es necesario un @RestControllerAdvice para convertir excepciones en respuestas HTTP correctas.
