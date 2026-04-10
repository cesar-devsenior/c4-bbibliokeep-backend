# Memoria de estado del proyecto BiblioKeep

## 1. Información General
- Proyecto: bibliokeep-backend
- Stack: Java 21, Spring Boot 4, Spring Security, JWT, JPA, Redis, PostgreSQL.
- Objetivo: API REST para gestión de biblioteca personal (Libro/Préstamo/Estadísticas).

## 2. Módulos implementados
- Entidades: User, Book, Loan, BookStatus.
- Repositorios: UserRepository, BookRepository, LoanRepository.
- DTOs: Auth (UserRegisterRequest, UserLoginRequest, AuthResponse), BookRequest/BookResponse, LoanRequest/LoanResponse, Stats response.
- Mappers: MapStruct (UserMapper, BookMapper, LoanMapper).
- Seguridad: JwtTokenProvider, CustomUserDetailsService, JwtAuthenticationFilter, SecurityConfig.
- Controladores: AuthController, BookController, LoanController, StatsController.
- Servicios: AuthServiceImpl, BookServiceImpl, LoanServiceImpl, StatsServiceImpl.
- Config: application.yml + app configs + Redis + mail.

## 3. Funcionalidad agregada recien
- Búsqueda híbrida en BookServiceImpl (local -> redis -> Google Books), con repositorio y JSON cache.
- GoogleBooksClient + GoogleBooksClientImpl (usando RestClient).
- Scheduler para notificaciones de préstamos vencidos (OverdueLoanNotificationScheduler).
- @ResponseStatus en controladores (no ResponseEntity en rutas principales).

## 4. Problemas conocidos
- GoogleBooksClientImpl usa RestClient y necesita ajustar la llamada correcta (no existe bodyToMono en la API actual).
- Sin tests de integración de login y búsqueda híbrida todavía.

## 5. TODO siguiente
1. Corregir GoogleBooksClientImpl a RestClient API compatible: `.retrieve().bodyTo...` no existe; usar `exchange()` + `toEntity(...).getBody()` o usar WebClient en su lugar.
2. Añadir tests:
   - @SpringBootTest + @AutoConfigureMockMvc para login / register / protected routes.
   - @DataJpaTest para búsquedas locales y préstamos.
   - Unit tests de BookService.searchBooks.
3. Ejecutar:
   - `./gradlew clean test`
   - `docker-compose up` con postgres/redis/mailhog para pruebas completas.

## 6. Estado de la memoria del agente
- La conversación continúa con atención al plan actual.
- El documento `MEMORIA_PROYECTO.md` está generado en el workspace.
