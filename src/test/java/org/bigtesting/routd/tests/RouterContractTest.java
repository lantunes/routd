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
import org.bigtesting.routd.Router;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Luis Antunes
 */
public abstract class RouterContractTest<R extends Router> {
    
    protected R router;
    
    @Before
    public void beforeEachTest() {
        
        router = newRouter();
    }
    
    protected abstract R newRouter();
    
    @Test
    public void route_Root() {
        
        Route r1 = new Route("/");
        
        router.add(r1);
        
        assertEquals(r1, router.route("/"));
    }
    
    @Test
    public void route_SimilarMatchesConstant() {
        
        Route r1 = new Route("/clients/all");
        Route r2 = new Route("/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/clients/all"));
    }
    
    @Test
    public void route_SimilarMatchesParam() {
        
        Route r1 = new Route("/clients/all");
        Route r2 = new Route("/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/clients/123"));
    }
    
    @Test
    public void route_IgnoresParamRegion() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/cntrl"));
    }
    
    @Test
    public void route_FindsParamRegion() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/cntrl/clients/23455"));
    }
    
    @Test
    public void route_DistinguishesBetweenDifferentRoutes() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/actn");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/actn"));
    }
    
    @Test
    public void route_NotFound() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/actn");
        
        router.add(r1);
        router.add(r2);
        
        assertNull(router.route("/test"));
    }
    
    @Test
    public void route_MultiParamRegions_Multiple() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn/:id/:name");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/cntrl/actn/123/bob"));
    }
    
    @Test
    public void route_MultiParamRegions_Single() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn/:id/:name");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/cntrl/actn/123"));
    }
    
    @Test
    public void route_CustomRegexAlpha() {
        
        Route r1 = new Route("/cntrl/actn/:id<[0-9]+>");
        Route r2 = new Route("/cntrl/actn/:id<[a-z]+>");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/cntrl/actn/bob"));
        assertNull(router.route("/cntrl/actn/bob/"));
    }
    
    @Test
    public void route_CustomRegexNumeric() {
        
        Route r1 = new Route("/cntrl/actn/:id<[0-9]+>");
        Route r2 = new Route("/cntrl/actn/:id<[a-z]+>");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/cntrl/actn/123"));
        assertNull(router.route("/cntrl/actn/123/"));
    }
    
    @Test
    public void route_DoesNotMatchWithExtraPathElementAfter() {
        
        Route r1 = new Route("/cntrl/:name");
        
        router.add(r1);
        
        assertEquals(r1, router.route("/cntrl/Tim"));
        assertNull(router.route("/cntrl/Tim/blah"));
    }
    
    @Test
    public void route_SplatPathParameterForAllRequests() throws Exception {
        
        Route r1 = new Route("/*");
        Route r2 = new Route("/specific");
        Route r3 = new Route("/:id<[0-9]+>");
        
        router.add(r1);
        router.add(r2);
        router.add(r3);
        
        assertEquals(r1, router.route("/"));
        assertEquals(r1, router.route("/*"));
        assertEquals(r1, router.route("/cntrl"));
        assertEquals(r1, router.route("/actn"));
        assertEquals(r1, router.route("/cntrl/actn"));
        assertEquals(r1, router.route("/specific"));
        assertEquals(r1, router.route("/123"));
        assertEquals(r1, router.route("/hello/"));
    }
    
    @Test
    public void route_SplatPathParameterForAllRequestsWithRootRoute() throws Exception {
        
        Route r0 = new Route("/");
        Route r1 = new Route("/*");
        
        router.add(r0);
        router.add(r1);
        
        assertEquals(r0, router.route("/"));
        assertEquals(r1, router.route("/blah"));
    }
    
    @Test
    public void route_SplatPathParameterWithPrecedingResource() throws Exception {
        
        Route r1 = new Route("/protected/*");
        Route r2 = new Route("/protected/:id<[0-9]+>");
        Route r3 = new Route("/:name<[a-z]+>");
        
        router.add(r1);
        router.add(r2);
        router.add(r3);
        
        assertEquals(r1, router.route("/protected/content"));
        assertEquals(r1, router.route("/protected/123"));
        assertEquals(r3, router.route("/john"));
        assertEquals(r1, router.route("/protected/"));
    }
    
    @Test
    public void route_SplatPathParameterInterjectedBetweenResources() throws Exception {
        
        Route r1 = new Route("/protected/*/content");
        Route r2 = new Route("/protected/user/content");
        
        router.add(r1);
        router.add(r2);
        
        assertNull(router.route("/hello"));
        assertEquals(r1, router.route("/protected/1/content"));
        assertEquals(r1, router.route("/protected/blah/content"));
        assertNull(router.route("/protected/blah/content/"));
        assertNull(router.route("/protected/1/blah/content"));
        assertEquals(r1, router.route("/protected/user/content"));
    }
    
    @Test
    public void route_SplatPathParametersOccurringMultipleTimes() throws Exception {
        
        Route r1 = new Route("/say/*/to/*");
        
        router.add(r1);
        
        assertNull(router.route("/hello"));
        assertEquals(r1, router.route("/say/hello/to/world"));
        assertEquals(r1, router.route("/say/bye/to/Tim"));
        assertNull(router.route("/say/bye/bye/to/Tim"));
        assertEquals(r1, router.route("/say/bye/to/John/Doe"));
        assertNull(router.route("/say/hello/to"));
        assertEquals(r1, router.route("/say/hello/to/"));
    }
    
    @Test
    public void route_SplatPathParamsWithVariousPathParams() throws Exception {
        
        Route r1 = new Route("/say/*/to/:name/:times<[0-9]+>/*");
        
        router.add(r1);
        
        assertNull(router.route("/hello"));
        assertNull(router.route("/say/hello/to/John"));
        assertNull(router.route("/say/hello/to/John/1"));
        assertEquals(r1, router.route("/say/hello/to/John/1/"));
        assertEquals(r1, router.route("/say/hello/to/Tim/1/time"));
        assertEquals(r1, router.route("/say/hello/to/Tim/1/time/thanks"));
    }
    
    @Test
    public void route_PathContainsRegexSymbols() throws Exception {
        
        Route r = new Route("/hello$.html");
        
        router.add(r);
        
        assertEquals(r, router.route("/hello$.html"));
    }
    
    @Test
    public void route_AllowsUsingUnicode() throws Exception {
        
        Route r = new Route("/föö");
        
        router.add(r);
        
        assertEquals(r, router.route("/f%C3%B6%C3%B6"));
    }
    
    @Test
    public void route_HandlesEncodedSlashedCorrectly() throws Exception {
        
        Route r = new Route("/foo/bar");
        
        router.add(r);
        
        assertEquals(r, router.route("/foo%2Fbar"));
    }
    
    @Test
    public void route_HandlesEncodedSlashedCorrectlyWithNamedParam() throws Exception {
        
        Route r = new Route("/:test");
        
        router.add(r);
        
        assertEquals(r, router.route("/foo%2Fbar"));
    }
    
    @Test
    public void route_LiterallyMatchesPlusSignInPath() throws Exception {
        
        Route r = new Route("/foo+bar");
        
        router.add(r);
        
        assertEquals(r, router.route("/foo%2Bbar"));
    }
    
    @Test
    public void route_LiterallyMatchesDollarSignInPath() throws Exception {
        
        Route r = new Route("/test$/");
        
        router.add(r);
        
        assertEquals(r, router.route("/test$/"));
    }
    
    @Test
    public void route_LiterallyMatchesDotInPath() throws Exception {
        
        Route r = new Route("/test.bar");
        
        router.add(r);
        
        assertEquals(r, router.route("/test.bar"));
    }
    
    @Test
    public void route_MatchesPathsThatIncludeSpacesEncodedWithPercent20() throws Exception {
        
        Route r = new Route("/path with spaces");
        
        router.add(r);
        
        assertEquals(r, router.route("/path%20with%20spaces"));
    }
    
    @Test
    public void route_MatchesPathsThatIncludeSpacesEncodedWithPlus() throws Exception {
        
        Route r = new Route("/path with spaces");
        
        router.add(r);
        
        assertEquals(r, router.route("/path+with+spaces"));
    }
    
    @Test
    public void route_MatchesPathsThatIncludeAmpersands() throws Exception {
        
        Route r = new Route("/:name");
        
        router.add(r);
        
        assertEquals(r, router.route("/foo&bar"));
    }
    
    @Test
    public void route_MatchesADotAsPartOfANamedParam() throws Exception {
        
        Route r = new Route("/:foo/:bar");
        
        router.add(r);
        
        assertEquals(r, router.route("/user@example.com/name"));
    }
    
    @Test
    public void route_LiterallyMatchesParensInPath() throws Exception {
        
        Route r = new Route("/test(bar)/");
        
        router.add(r);
        
        assertEquals(r, router.route("/test(bar)/"));
    }
}
