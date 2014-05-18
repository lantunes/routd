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

import org.bigtesting.routd.Route;
import org.bigtesting.routd.RouteMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Luis Antunes
 */
public abstract class RouteMapContractTest {
    
    private RouteMap rm;
    
    @Before
    public void beforeEachTest() {
        
        rm = newRouteMap();
    }
    
    protected abstract RouteMap newRouteMap();
    
    @Test
    public void getRoute_Root() {
        
        Route r1 = new Route("/");
        
        rm.add(r1);
        
        assertEquals(r1, rm.getRoute("/"));
    }
    
    @Test
    public void getRoute_SimilarMatchesConstant() {
        
        Route r1 = new Route("/clients/all");
        Route r2 = new Route("/clients/:id");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r1, rm.getRoute("/clients/all"));
    }
    
    @Test
    public void getRoute_SimilarMatchesParam() {
        
        Route r1 = new Route("/clients/all");
        Route r2 = new Route("/clients/:id");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r2, rm.getRoute("/clients/123"));
    }
    
    @Test
    public void getRoute_IgnoresParamRegion() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl/clients/:id");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r1, rm.getRoute("/cntrl"));
    }
    
    @Test
    public void getRoute_FindsParamRegion() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl/clients/:id");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r2, rm.getRoute("/cntrl/clients/23455"));
    }
    
    @Test
    public void getRoute_DistinguishesBetweenDifferentRoutes() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/actn");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r2, rm.getRoute("/actn"));
    }
    
    @Test
    public void getRoute_NotFound() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/actn");
        
        rm.add(r1);
        rm.add(r2);
        
        assertNull(rm.getRoute("/test"));
    }
    
    @Test
    public void getRoute_MultiParamRegions_Multiple() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn/:id/:name");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r2, rm.getRoute("/cntrl/actn/123/bob"));
    }
    
    @Test
    public void getRoute_MultiParamRegions_Single() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn/:id/:name");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r1, rm.getRoute("/cntrl/actn/123"));
    }
    
    @Test
    public void getRoute_CustomRegexAlpha() {
        
        Route r1 = new Route("/cntrl/actn/:id<[0-9]+>");
        Route r2 = new Route("/cntrl/actn/:id<[a-z]+>");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r2, rm.getRoute("/cntrl/actn/bob"));
    }
    
    @Test
    public void getRoute_CustomRegexNumeric() {
        
        Route r1 = new Route("/cntrl/actn/:id<[0-9]+>");
        Route r2 = new Route("/cntrl/actn/:id<[a-z]+>");
        
        rm.add(r1);
        rm.add(r2);
        
        assertEquals(r1, rm.getRoute("/cntrl/actn/123"));
    }
    
    @Test
    public void getRoute_DoesNotMatchWithExtraPathElementAfter() {
        
        Route r1 = new Route("/cntrl/:name");
        
        rm.add(r1);
        
        assertEquals(r1, rm.getRoute("/cntrl/Tim"));
        assertNull(rm.getRoute("/cntrl/Tim/blah"));
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void getRoute_SplatPathParameterForAllRequests() throws Exception {
        
        Route r1 = new Route("/*");
        Route r2 = new Route("/specific");
        Route r3 = new Route("/:id<[0-9]+>");
        
        rm.add(r1);
        rm.add(r2);
        rm.add(r3);
        
        assertEquals(r1, rm.getRoute("/"));
        assertEquals(r1, rm.getRoute("/*"));
        assertEquals(r1, rm.getRoute("/cntrl"));
        assertEquals(r1, rm.getRoute("/actn"));
        assertEquals(r1, rm.getRoute("/cntrl/actn"));
        assertEquals(r1, rm.getRoute("/specific"));
        assertEquals(r1, rm.getRoute("/123"));
        assertEquals(r1, rm.getRoute("/hello/"));
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void getRoute_SplatPathParameterWithPrecedingResource() throws Exception {
        
        Route r1 = new Route("/protected/*");
        Route r2 = new Route("/protected/:id<[0-9]+>");
        Route r3 = new Route("/:name<[a-z]+>");
        
        rm.add(r1);
        rm.add(r2);
        rm.add(r3);
        
        assertNull(rm.getRoute("/hello"));
        assertEquals(r1, rm.getRoute("/protected/content"));
        assertEquals(r1, rm.getRoute("/protected/123"));
        assertEquals(r3, rm.getRoute("/john"));
        assertNull(rm.getRoute("/protected"));
        assertEquals(r1, rm.getRoute("/protected/"));
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void getRoute_SplatPathParameterInterjectedBetweenResources() throws Exception {
        
        Route r1 = new Route("/protected/*/content");
        
        rm.add(r1);
        
        assertNull(rm.getRoute("/hello"));
        assertEquals(r1, rm.getRoute("/protected/1/content"));
        assertEquals(r1, rm.getRoute("/protected/blah/content"));
        assertNull(rm.getRoute("/protected/blah/content/"));
        assertNull(rm.getRoute("/protected/1/blah/content"));
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void getRoute_SplatPathParametersOccuringMultipleTimes() throws Exception {
        
        Route r1 = new Route("/say/*/to/*");
        
        rm.add(r1);
        
        assertNull(rm.getRoute("/hello"));
        assertEquals(r1, rm.getRoute("/say/hello/to/world"));
        assertEquals(r1, rm.getRoute("/say/bye/to/Tim"));
        assertNull(rm.getRoute("/say/bye/bye/to/Tim"));
        assertEquals(r1, rm.getRoute("/say/bye/to/John/Doe"));
        assertNull(rm.getRoute("/say/hello/to"));
        assertEquals(r1, rm.getRoute("/say/hello/to/"));
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void getRoute_SplatPathParamsWithVariousPathParams() throws Exception {
        
        Route r1 = new Route("/say/*/to/:name/:times<[0-9]+>/*");
        
        rm.add(r1);
        
        assertNull(rm.getRoute("/hello"));
        assertNull(rm.getRoute("/say/hello/to/John"));
        assertNull(rm.getRoute("/say/hello/to/John/1"));
        assertEquals(r1, rm.getRoute("/say/hello/to/John/1/"));
        assertEquals(r1, rm.getRoute("/say/hello/to/Tim/1/time"));
        assertEquals(r1, rm.getRoute("/say/hello/to/Tim/1/time/thanks"));
    }
}
