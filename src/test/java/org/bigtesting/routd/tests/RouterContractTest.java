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

import org.bigtesting.It;
import org.bigtesting.ItRunner;
import org.bigtesting.routd.Route;
import org.bigtesting.routd.Router;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * 
 * @author Luis Antunes
 */
@RunWith(ItRunner.class)
public abstract class RouterContractTest<R extends Router> {
    
    protected R router;
    
    @Before
    public void beforeEachTest() {
        
        router = newRouter();
    }
    
    protected abstract R newRouter();
    
    @It("matches the root route") void routeTest1() {
        
        Route r1 = new Route("/");
        
        router.add(r1);
        
        assertEquals(r1, router.route("/"));
    }
    
    @It("distinguishes between similar static and named param routes; case 1")
    void routeTest2() {
        
        Route r1 = new Route("/clients/all");
        Route r2 = new Route("/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/clients/all"));
    }
    
    @It("distinguishes between similar static and named param routes; case 2")
    void routeTest3() {
        
        Route r1 = new Route("/clients/all");
        Route r2 = new Route("/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/clients/123"));
    }
    
    @It("distinguishes between dissimilar static and named param routes; case 1")
    void routeTest4() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/cntrl"));
    }
    
    @It("distinguishes between dissimilar static and named param routes; case 2") 
    void routeTest5() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl/clients/:id");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/cntrl/clients/23455"));
    }
    
    @It("distinguishes between two different static routes") void routeTest6() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/actn");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/actn"));
    }
    
    @It("returns null when no route is found") void routeTest7() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/actn");
        
        router.add(r1);
        router.add(r2);
        
        assertNull(router.route("/test"));
    }
    
    @It("distinguishes between routes with multiple named path parameters; case 1")
    void routeTest8() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn/:id/:name");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/cntrl/actn/123/bob"));
    }

    @It("distinguishes between routes with multiple named path parameters; case 2")
    void routeTest9() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn/:id/:name");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/cntrl/actn/123"));
    }
    
    @It("distinguishes between named parameters with custom regex; alpha case")
    void routeTest10() {
        
        Route r1 = new Route("/cntrl/actn/:id<[0-9]+>");
        Route r2 = new Route("/cntrl/actn/:id<[a-z]+>");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r2, router.route("/cntrl/actn/bob"));
        assertNull(router.route("/cntrl/actn/bob/"));
    }
    
    @It("distinguishes between named parameters with custom regex; numeric case")
    void routeTest11() {
        
        Route r1 = new Route("/cntrl/actn/:id<[0-9]+>");
        Route r2 = new Route("/cntrl/actn/:id<[a-z]+>");
        
        router.add(r1);
        router.add(r2);
        
        assertEquals(r1, router.route("/cntrl/actn/123"));
        assertNull(router.route("/cntrl/actn/123/"));
    }
    
    @It("matches a named parameter, but not if there is an extra path element after it")
    void routetest12() {
        
        Route r1 = new Route("/cntrl/:name");
        
        router.add(r1);
        
        assertEquals(r1, router.route("/cntrl/Tim"));
        assertNull(router.route("/cntrl/Tim/blah"));
    }
    
    @It("handles the splat path parameter for all requests") 
    void routeTest13() {
        
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
    
    @It("handles splat parameter for all requests with root route present")
    void routetest14() {
        
        Route r0 = new Route("/");
        Route r1 = new Route("/*");
        
        router.add(r0);
        router.add(r1);
        
        assertEquals(r0, router.route("/"));
        assertEquals(r1, router.route("/blah"));
    }
    
    @It("handles splat parameters with a preceding resource") void routeTest15() {
        
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
    
    @It("handles splat parameters interjected between resources") void routetest16() {
        
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
    
    @It("handles paths with splat parameters occurring multiple times") 
    void routeTest17() {
        
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
    
    @It("handles splat path params that are part of paths with various path params")
    void routeTest18() {
        
        Route r1 = new Route("/say/*/to/:name/:times<[0-9]+>/*");
        
        router.add(r1);
        
        assertNull(router.route("/hello"));
        assertNull(router.route("/say/hello/to/John"));
        assertNull(router.route("/say/hello/to/John/1"));
        assertEquals(r1, router.route("/say/hello/to/John/1/"));
        assertEquals(r1, router.route("/say/hello/to/Tim/1/time"));
        assertEquals(r1, router.route("/say/hello/to/Tim/1/time/thanks"));
    }
    
    @It("handles paths containing regex symbols") void routeTest19() {
        
        Route r = new Route("/hello$.html");
        router.add(r);
        assertEquals(r, router.route("/hello$.html"));
    }
    
    @It("allows using unicode") void routeTest20() {
        
        Route r = new Route("/föö");
        router.add(r);
        assertEquals(r, router.route("/f%C3%B6%C3%B6"));
    }
    
    @It("handles encoded '/' correctly") void routeTest21() {
        
        Route r = new Route("/foo/bar");
        router.add(r);
        assertEquals(r, router.route("/foo%2Fbar"));
    }
    
    @It("handles encoded '/' correctly with named params") void routeTest22() {
        
        Route r = new Route("/:test");
        router.add(r);
        assertEquals(r, router.route("/foo%2Fbar"));
    }
    
    @It("literally matches '+' in path") void routeTest23() {
        
        Route r = new Route("/foo+bar");
        router.add(r);
        assertEquals(r, router.route("/foo%2Bbar"));
    }
    
    @It("literally matches '$' in path") void routeTest24() {
        
        Route r = new Route("/test$/");
        router.add(r);
        assertEquals(r, router.route("/test$/"));
    }
    
    @It("literally matches '.' in path") void routeTest25() {
        
        Route r = new Route("/test.bar");
        router.add(r);
        assertEquals(r, router.route("/test.bar"));
    }
    
    @It("matches paths that include spaces encoded with '%20'") void routeTest26() {
        
        Route r = new Route("/path with spaces");
        router.add(r);
        assertEquals(r, router.route("/path%20with%20spaces"));
    }
    
    @It("matches paths that include spaces encoded with '+'") void routeTest27() {
        
        Route r = new Route("/path with spaces");
        router.add(r);
        assertEquals(r, router.route("/path+with+spaces"));
    }
    
    @It("matches paths that include ampersands") void routeTest28() {
        
        Route r = new Route("/:name");
        router.add(r);
        assertEquals(r, router.route("/foo&bar"));
    }
    
    @It("matches a dot as part of a named param") void routeTest29() {
        
        Route r = new Route("/:foo/:bar");
        router.add(r);
        assertEquals(r, router.route("/user@example.com/name"));
    }
    
    @It("literally matches parens in path") void routeTest30() {
        
        Route r = new Route("/test(bar)/");
        router.add(r);
        assertEquals(r, router.route("/test(bar)/"));
    }
}
