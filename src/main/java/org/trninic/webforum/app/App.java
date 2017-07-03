package org.trninic.webforum.app;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class App extends ResourceConfig {
    public App() {
        // Define the package which contains the service classes.
        packages("org.trninic.webforum");
    }
} 


