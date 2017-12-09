# Spring logging exceptions
There are many ways to log and handle exceptions in spring. I prefer the @ControllerAdvice [method](https://lankydanblog.com/2017/09/12/global-exception-handling-with-controlleradvice).

### Using @ControllerAdvice
@ControllerAdvice (see GlobalExceptionHandler.class) enables us to catch any exceptions such as NoHandlerFoundException and decide what to do with it.

> NoHandlerFoundException requires in application.properties: 
> - _spring.mvc.throw-exception-if-no-handler-found=true_  
> - _spring.resources.add-mappings=false_  

We can for example handle the exception based on it's type and log it to console.

### Overriding default response on exception
If we don't need to handle the exception using @ControllerAdvice, we can configure spring to return a custom response using configurations (see ApplicationConfiguration.class).  
This gives us opportunity to log and create a custom response.  

> Note that this method can't be combined with @ControllerAdvice if we override the response model. Spring will not call `getErrorAttributes` in our example. 
 
