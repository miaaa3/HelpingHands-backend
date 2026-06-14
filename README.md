# HelpingHands Backend

Spring Boot backend for the HelpingHands volunteering social network.

## Project Summary

HelpingHands connects volunteers and organizations through a social platform. This backend provides authentication, user profiles, feed posts, media uploads, likes, comments, follows, notifications, real-time private messaging, and Stripe donation support.

The API is designed for the Angular frontend in the companion `HelpingHands` repository. Authentication uses JWT tokens, most `/api/**` endpoints require either `VOLUNTEER` or `ORGANIZATION` authority, uploaded media is exposed from `/uploads/**`, and chat uses STOMP over SockJS/WebSocket at `/ws`.

## Tech Stack

- Java 17
- Spring Boot 3.1.3
- Spring Security
- Spring Data JPA
- MySQL
- JWT authentication with `java-jwt`
- Spring WebSocket/STOMP
- Stripe Java SDK
- Lombok
- Maven wrapper

## Main Capabilities

- Register volunteers and organizations
- Authenticate users and issue JWT responses
- Load the current user's profile, follower counts, following counts, and notifications
- Search users with follow status
- Update volunteer profile details
- Create, list, update, and delete posts
- Upload and serve post media files
- Like and comment on posts
- Follow and unfollow users
- Create notifications for social interactions
- Create Stripe donation intents
- Process Stripe donation webhooks
- List donations by organization or current donor
- Track total raised for an organization
- Send and receive real-time private chat messages
- List conversation summaries and message history

## Project Structure

```text
src/main/java/com/example/HelpingHands/
  AuthenticationRequestsAndResponses/  Auth request and response DTOs
  Configuration/                       Security, CORS, WebSocket, Stripe, MVC config
  Controller/                          REST and STOMP API entry points
  DTO/                                 API response/request DTOs
  Entity/                              JPA entities
  Exception/                           Custom exceptions
  Listener/                            Entity timestamp listener
  Repository/                          Spring Data repositories
  Service/                             Service interfaces
  ServiceImpl/                         Service implementations
```

## Requirements

- Java 17
- MySQL
- Maven wrapper from this repository
- Stripe test keys for donation testing

## Local Setup

Create a MySQL database:

```sql
CREATE DATABASE HelpingHands;
```

Configure environment variables as needed, or use the local defaults in `src/main/resources/application.properties`.

Recommended local variables:

```bash
export JWT_SECRET="change-this-local-secret"
export DB_URL="jdbc:mysql://localhost:8889/HelpingHands"
export DB_USERNAME="root"
export DB_PASSWORD="root"
export UPLOAD_DIR="./uploads"
export CORS_ALLOWED_ORIGINS="http://localhost:4200"
export STRIPE_SECRET_KEY="sk_test_your_key"
export STRIPE_PUBLISHABLE_KEY="pk_test_your_key"
export STRIPE_WEBHOOK_SECRET="whsec_your_secret"
```

Run the backend:

```bash
./mvnw spring-boot:run
```

The API runs on `http://localhost:8080/` by default.

## Default Local Ports

- Backend API: `8080`
- MySQL (MAMP default): `8889`
- Frontend dev server (companion repo): `4200`

## Configuration

Configuration lives in:

```text
src/main/resources/application.properties
```

Supported environment-backed settings:

- `JWT_SECRET` for JWT signing
- `DB_URL` for MySQL JDBC URL
- `DB_USERNAME` for database username
- `DB_PASSWORD` for database password
- `UPLOAD_DIR` for uploaded media storage
- `CORS_ALLOWED_ORIGINS` for allowed frontend origins
- `STRIPE_SECRET_KEY` for Stripe server-side calls
- `STRIPE_PUBLISHABLE_KEY` returned to the frontend
- `STRIPE_WEBHOOK_SECRET` for webhook verification

## API Overview

### Authentication

- `POST /auth/register/volunteer`
- `POST /auth/register/organization`
- `POST /auth/login`
- `POST /auth/logout`

### Users

- `GET /api/users/getUser`
- `GET /api/users/search?keyword=...`
- `PUT /api/users/updateVolunteer`

### Posts And Media

- `POST /api/posts/createPost`
- `GET /api/posts/getAllPosts`
- `GET /api/posts/getPost?id=...`
- `GET /api/posts/getMediaForPost?postId=...`
- `PUT /api/posts/updatePost?id=...`
- `DELETE /api/posts/deletePost/{postId}`
- `DELETE /api/posts/deletePost?id=...`
- `GET /uploads/{fileName}`

### Likes And Comments

- `POST /api/likes/createLike?postId=...`
- `DELETE /api/likes/deleteLike?likeId=...`
- `POST /api/comments/createComment?postId=...&content=...`
- `GET /api/comments/getCommentsByPostId?postId=...`
- `PUT /api/comments/updateComment`
- `DELETE /api/comments/deleteComment?commentId=...`

### Follows

- `POST /api/follow/follow?userId=...`
- `GET /api/follow/getFollowers`
- `GET /api/follow/getFollowing`
- `DELETE /api/follow/unfollow?userId=...`

### Messages

- `GET /api/messages/conversations`
- `GET /api/messages/conversation/{userId}` (also marks the partner's pending messages as read)
- `PUT /api/messages/conversation/{userId}/read` (marks pending messages as read without reloading history)
- WebSocket endpoint: `/ws`
- STOMP send destination: `/app/chat.send`
- STOMP user receive destination: `/user/queue/messages`

### Donations

- `POST /api/donations/create-intent`
- `POST /api/donations/webhook`
- `GET /api/donations/organization/{organizationId}`
- `GET /api/donations/organization/{organizationId}/total`
- `GET /api/donations/my-donations`
- `GET /api/donations/config`

## Security Notes

- `/auth/**`, `/uploads/**`, `/ws/**`, `/api/donations/webhook`, and `/api/donations/config` are publicly reachable.
- Most `/api/**` endpoints require authenticated `VOLUNTEER` or `ORGANIZATION` authority.
- CORS is controlled through `CORS_ALLOWED_ORIGINS`.
- Do not commit real JWT secrets, database passwords, or Stripe live keys.

## Testing

Run the test suite:

```bash
./mvnw test
```

Current tests are minimal, so important flows should be manually verified while more automated coverage is added.

## Suggested Next Improvements

- Add validation annotations to request DTOs and use `@Valid` in controllers.
- Add integration tests for auth, posts, messages, and donations.
- Add pagination for feeds, comments, conversations, notifications, users, and donation lists.
- Replace direct entity responses with DTOs consistently.
- Add role-specific authorization checks for organization-only and volunteer-only actions.
- Add production profile configuration and deployment documentation.
