# Spring logging exceptions  
There are many ways to log and handle exceptions in spring. I prefer the @ControllerAdvice [method](https://lankydanblog.com/2017/09/12/global-exception-handling-with-controlleradvice).

### Logging any 4xx and 5xx 
We can combine filter and ControllerAdvice to log request and response (including payload) even when exception is thrown.
ControllerAdvice is required in order for the filter to catch the error response which is normally handled by Spring.

### Filters
Enables us to log and filter requests.
Filters are chained together and processes the request.
This means that we can put a custom filter to the chain for logging purpose.
  
### Using @ControllerAdvice
@ControllerAdvice (see GlobalExceptionHandler.class) enables us to catch any exceptions such as NoHandlerFoundException and decide what to do with it.

> NoHandlerFoundException requires in application.properties: 
> - _spring.mvc.throw-exception-if-no-handler-found=true_  
> - _spring.resources.add-mappings=false_   

 
