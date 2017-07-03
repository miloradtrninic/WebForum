package org.trninic.webforum.filters;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.trninic.webforum.beans.User;
import org.trninic.webforum.resources.Secured;
import org.trninic.webforum.services.UserServices;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it

    	
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<User.RoleEnum> classRoles = extractRoles(resourceClass);
        
        
        
        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        java.lang.reflect.Method resourceMethod = resourceInfo.getResourceMethod();
        List<User.RoleEnum> methodRoles = extractRoles(resourceMethod);
        
        
        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles, requestContext.getSecurityContext().getUserPrincipal().getName());
            } else {
                checkPermissions(methodRoles, requestContext.getSecurityContext().getUserPrincipal().getName());
            }
        } catch(IOException e){
        	e.printStackTrace();
        	requestContext.abortWith(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
        } catch (Exception e) {
        	e.printStackTrace();
            requestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN).build());
        } 
    }

    // Extract the roles from the annotated element
    private List<User.RoleEnum> extractRoles(AnnotatedElement annotatedElement) {
    
        if (annotatedElement == null) {
            return new ArrayList<User.RoleEnum>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<User.RoleEnum>();
            } else {
            	User.RoleEnum[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<User.RoleEnum> allowedRoles, String username) throws Exception {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
    	UserServices userService = new UserServices();
    	User user = userService.getUser(username);
    	for(User.RoleEnum role:allowedRoles ){
    		if(user.getRole().equals(role)){
    			return;
    		}
    	}
    	
    	throw new Exception();
    }
}