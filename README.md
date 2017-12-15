# zuul-authentication-example
Use Zuul and Spring Security for a global authentication via the popular `JWT` token.

#### Modules
##### 1. **auth-center**
The service to issue the `JWT` token.
- The client POST `{username,password}` to `/login`.
- This service will authenticate the username and password via `Spring Security`, generate the token, and issue it to client.

##### 2. **backend-service**
Provide three simple services:
- `/admin`
- `/user`
- `/guest`
 
##### 3. **api-gateway**
The `Zuul` gateway:
- Define `Zuul` routes to `auth-center` and `backend-service`.
- Verify `JWT` token.
- Define role-based auth via `Spring Security`:
    - `/login` is public to all.
    - `/backend/admin` can only be accessed by role `ADMIN`.
    - `/backend/user` can only be accessed by role `USER`.
    - `/backend/guest` is public to all.
