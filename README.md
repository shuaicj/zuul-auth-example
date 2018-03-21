# Zuul Auth Example

Use Zuul and Spring Security for a global authentication via the popular
[JWT](https://jwt.io/introduction/) token.

#### Modules

##### 1. **auth-center**
The service to issue the `JWT` token.
- The client POST `{username,password}` to `/login`.
- This service will authenticate the username and password via `Spring Security`,
  generate the token, and issue it to client.

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

#### Run and Verify

##### 1. Compile and package
```bash
mvn clean package
```

##### 2. Start services
```bash
java -jar auth-center/target/auth-center-1.0.0.jar
java -jar backend-service/target/backend-service-1.0.0.jar
java -jar api-gateway/target/api-gateway-1.0.0.jar
```

##### 3. Get tokens
```bash
curl -i -H "Content-Type: application/json" -X POST -d '{"username":"shuaicj","password":"shuaicj"}' http://localhost:8080/login
```
You will see the token in response header for user `shuaicj`. Note that the status code `401` will be returned if you provide incorrect username or password. And similarly, get token for user `admin`:
```bash
curl -i -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"admin"}' http://localhost:8080/login
```
The user `admin` is defined with two roles: `USER` and `ADMIN`, while `shuaicj` is only a `USER`.

##### 4. Verify
The general command to verify if the auth works is as follows:
```bash
curl -i -H "Authorization: Bearer token-you-got-in-step-3" http://localhost:8080/backend/user
```
or without token:
```bash
curl -i http://localhost:8080/backend/user
```
You can change the token and the URL as need. To sum up, the following table represents all possible response status codes while sending requests to different URLs with different tokens:

|                                     | /backend/admin | /backend/user | /backend/guest |
| ----------------------------------- | -------------- | ------------- | -------------- |
| `admin` token (role `USER` `ADMIN`) |            200 |           200 |            200 |
| `shuaicj` token (role `USER`)       |            403 |           200 |            200 |
| no token                            |            401 |           401 |            200 |