/*
 * Copyright (C) 2014 BigTesting.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bigtesting.routd.tests;

import static org.junit.Assert.*;

import org.bigtesting.routd.RegexRoute;
import org.bigtesting.routd.Route;
import org.junit.Test;

/**
 * 
 * @author Luis Antunes
 */
public class TestRegexRoute {

    @Test
    public void pattern_Root() {
        
        RegexRoute r = new RegexRoute(new Route("/"));
        String pattern = r.pattern().toString();
        assertEquals("^/$", pattern);
    }
    
    @Test
    public void pattern_WithController_NoAction_NoParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/cntrl"));
        String pattern = r.pattern().toString();
        assertEquals("^/cntrl$", pattern);
    }
    
    @Test
    public void pattern_WithController_WithAction_NoParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/cntrl/actn"));
        String pattern = r.pattern().toString();
        assertEquals("^/cntrl/actn$", pattern);
    }
    
    @Test
    public void pattern_WithController_WithAction_WithParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/cntrl/actn/clients/:id"));
        String pattern = r.pattern().toString();
        assertEquals("^/cntrl/actn/clients/([^/]+)$", pattern);
    }
    
    @Test
    public void pattern_WithInterjectedParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/cntrl/:id/actn"));
        String pattern = r.pattern().toString();
        assertEquals("^/cntrl/([^/]+)/actn$", pattern);
    }
    
    @Test
    public void pattern_NoController_WithAction_WithParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/actn/clients/:id"));
        String pattern = r.pattern().toString();
        assertEquals("^/actn/clients/([^/]+)$", pattern);
    }
    
    @Test
    public void pattern_NoController_NoAction_WithParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/clients/:id"));
        String pattern = r.pattern().toString();
        assertEquals("^/clients/([^/]+)$", pattern);
    }
    
    @Test
    public void pattern_WithController_WithAction_WithRegexParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/cntrl/actn/clients/:id<[0-9]+>"));
        String pattern = r.pattern().toString();
        assertEquals("^/cntrl/actn/clients/([0-9]+)$", pattern);
    }
    
    @Test
    public void pattern_WithController_WithAction_WithMultiRegexParamPath() {
        
        RegexRoute r = new RegexRoute(new Route("/cntrl/actn/clients/:id<[0-9]+>/:name"));
        String pattern = r.pattern().toString();
        assertEquals("^/cntrl/actn/clients/([0-9]+)/([^/]+)$", pattern);
    }
    
    @Test
    public void pattern_WithController_WithAction_WithRegexSymbols() {
        
        RegexRoute r = new RegexRoute(new Route("/cntrl/actn/a+b/:id<[0-9]+>/:name"));
        String pattern = r.pattern().toString();
        assertEquals("^/cntrl/actn/a\\+b/([0-9]+)/([^/]+)$", pattern);
    }
    
    @Test
    public void pattern_SplatGeneralWildcard() {
        
        RegexRoute r = new RegexRoute(new Route("/*"));
        String pattern = r.pattern().toString();
        assertEquals("^/(.*)$", pattern);
    }
    
    @Test
    public void pattern_SplatWithPrecedingResource() {
        
        RegexRoute r = new RegexRoute(new Route("/protected/*"));
        String pattern = r.pattern().toString();
        assertEquals("^/protected/(.*)$", pattern);
    }
    
    @Test
    public void pattern_SplatInterjectedBetweenResources() {
        
        RegexRoute r = new RegexRoute(new Route("/protected/*/content"));
        String pattern = r.pattern().toString();
        assertEquals("^/protected/([^/]*)/content$", pattern);
    }
    
    @Test
    public void pattern_SplatOccurringMultipleTimes() {
        
        RegexRoute r = new RegexRoute(new Route("/say/*/to/*"));
        String pattern = r.pattern().toString();
        assertEquals("^/say/([^/]*)/to/(.*)$", pattern);
    }
    
    @Test
    public void pattern_SplatVariousPathParams() {
        
        RegexRoute r = new RegexRoute(new Route("/say/*/to/:name/:times<[0-9]+>/*"));
        String pattern = r.pattern().toString();
        assertEquals("^/say/([^/]*)/to/([^/]+)/([0-9]+)/(.*)$", pattern);
    }
}
