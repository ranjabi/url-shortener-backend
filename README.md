# URL Shortener

## Features
- Authentication (register, login, logout)
- Create/edit/delete short link
- API documentation using Swagger
- Caching using Redis
- Containerization using Docker

## API Endpoints
### Authentication
- `POST /register`: Register a new user
- `POST /login`: Login and retrieve a JWT
- `POST /logout`: Logout

### URL Shortener
- `GET /urls`: Get all short url created by a user (authenticated)
- `GET /urls/{shortCode}`: Get original url with redirect (anonymous)
- `POST /urls`: Create a new short url (allow anonymous creation)
- `DELETE /urls/{id}`: Delete a short url (authenticated)
