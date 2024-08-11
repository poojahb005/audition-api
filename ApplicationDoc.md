# Audition API

### Functionality

#### This application is used to fetch Audition posts based on certain criteria. It has 4 endpoints exposed for the user.

> 1. _/api/posts?userId={userId}_ : userId is query param, which is optional. Posts are fetched for a particular userId
     > else all the available posts are retrieved.
>2. _/api/posts/{postId}_ : posts are fetched based on particular postId
>3. /api/posts/{postID}/comments -> posts are fetched along with comments for a particular postId
>4. /api/comments?postId={postId} -> comments are fetched for a particular postId. All the comments are fetched if
    postId
    is null.

### Implementation

1. #### Service layer abstraction

Created a service layer interface to separate from the implementation details.

2. #### Client integration layer

This layer uses `RestTemplate` to make calls to Audition posts and retrieve the desired output.

3. #### validation

Implemented annotation-based Spring custom validation to ensure that the input field contains only digits. This approach
is more efficient for request objects with multiple attributes.

4. #### Spring AOP

Utilized aspect-oriented programming with annotations to log the request flow.

5. #### Exception Handling

Utilized controller Advice implementation to handle global exceptions.

6. #### Spring security

Configured spring security with basic authentication.
Enabled info and health actuator endpoints, we can still access other endpoints with basic authentication
Token-based authentication could be implemented for improved security.

7. #### Interceptors

Implemented interceptors to inject traceId and spanId into response headers, And additional interceptor to log details
of `RestTemplate` requests and responses.

8. #### Junit

Achieved over 80% test coverage by writing JUnit test cases.

9. #### Metrics

Instrumented metrics using Spring Boot Actuator. Metrics collected by Micrometer and Actuator are securely accessible
through the /actuator/metrics endpoint.


