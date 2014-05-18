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

import java.util.List;

import org.bigtesting.routd.PathParameterElement;
import org.bigtesting.routd.Route;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Luis Antunes
 */
public class TestRoute {

    @Test
    public void newRoute_NullPathThrowsException() {
        
        try {
            new Route(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void equals_NotEqual() {
        
        Route r1 = new Route("/");
        Route r2 = new Route("/cntrl");
        
        assertFalse(r1.equals(r2));
        assertFalse(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void equals_NotEqual_WithController() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl2");
        
        assertFalse(r1.equals(r2));
        assertFalse(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void equals_NotEqual_WithControllerAndAction() {
        
        Route r1 = new Route("/cntrl/actn");
        Route r2 = new Route("/cntrl/actn2");
        
        assertFalse(r1.equals(r2));
        assertFalse(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void equals_NotEqual_WithControllerAndActionAndParams() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn2/:id");
        
        assertFalse(r1.equals(r2));
        assertFalse(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void equals_Equal_Root() {
        
        Route r1 = new Route("/");
        Route r2 = new Route("/");
        
        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void equals_Equal_WithController() {
        
        Route r1 = new Route("/cntrl");
        Route r2 = new Route("/cntrl");
        
        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void equals_Equal_WithControllerAndAction() {
        
        Route r1 = new Route("/cntrl/actn");
        Route r2 = new Route("/cntrl/actn");
        
        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void equals_Equal_WithControllerAndActionAndParams() {
        
        Route r1 = new Route("/cntrl/actn/:id");
        Route r2 = new Route("/cntrl/actn/:id");
        
        assertTrue(r1.equals(r2));
        assertTrue(r1.hashCode() == r2.hashCode());
    }
    
    @Test
    public void toString_WithController_NoAction_NoParamPath() {
        
        Route r = new Route("/cntrl");
        assertEquals("/cntrl", r.toString());
    }
    
    @Test
    public void toString_WithController_WithAction_NoParamPath() {
        
        Route r = new Route("/cntrl/actn");
        assertEquals("/cntrl/actn", r.toString());
    }
    
    @Test
    public void toString_WithController_WithAction_WithParamPath() {
        
        Route r = new Route("/cntrl/actn/clients/:id");
        assertEquals("/cntrl/actn/clients/:id", r.toString());
    }

    @Test
    public void toString_WithSplat() {
        
        Route r = new Route("/*");
        assertEquals("/*", r.toString());
    }
    
    @Test
    public void pathParameterElements_NoneExist() {
        
        Route r = new Route("/actn");
        List<PathParameterElement> params = r.pathParameterElements();
        
        assertTrue(params.isEmpty());
    }
    
    @Test
    public void pathParameterElements_OneExistsWithAction() {
        
        Route r = new Route("/actn/:id");
        
        List<PathParameterElement> params = r.pathParameterElements();
        assertEquals(1, params.size());
        
        PathParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(1, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void pathParameterElements_OneExistsAlone() {
        
        Route r = new Route("/:id");
        
        List<PathParameterElement> params = r.pathParameterElements();
        assertEquals(1, params.size());
        
        PathParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(0, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void pathParameterElements_ManyExistAlone() {
        
        Route r = new Route("/:id/:name");
        
        List<PathParameterElement> params = r.pathParameterElements();
        assertEquals(2, params.size());
        
        PathParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(0, elem.index());
        assertNull(elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(1, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void pathParameterElements_ManyExistWithControllerAndAction() {
        
        Route r = new Route("/cntrl/actn/:id/:name");
        
        List<PathParameterElement> params = r.pathParameterElements();
        assertEquals(2, params.size());
        
        PathParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(2, elem.index());
        assertNull(elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void pathParameterElements_ManyExistWithRegexWithControllerAndAction() {
        
        Route r = new Route("/cntrl/actn/:id<[0-9]+>/:name<[a-z]+>");
        
        List<PathParameterElement> params = r.pathParameterElements();
        assertEquals(2, params.size());
        
        PathParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(2, elem.index());
        assertEquals("[0-9]+", elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertEquals("[a-z]+", elem.regex());
    }
    
    @Test
    public void pathParameterElements_OneExistsWithRegexWithSlashWithControllerAndAction() {
        
        Route r = new Route("/cntrl/actn/:id<[^/]+>/:name<[a-z]+>");
        
        List<PathParameterElement> params = r.pathParameterElements();
        assertEquals(2, params.size());
        
        PathParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(2, elem.index());
        assertEquals("[^/]+", elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertEquals("[a-z]+", elem.regex());
    }
    
    @Test
    public void getPathParameter() {
        
        Route route = new Route("/customer/:id");
        String path = "/customer/1";
        
        assertEquals("1", route.getPathParameter("id", path));
    }
    
    @Test
    public void getPathParameter_WithMultipleParameters() {
        
        Route route = new Route("/customer/:id/named/:name");
        String path = "/customer/1/named/John";
        
        assertEquals("1", route.getPathParameter("id", path));
        assertEquals("John", route.getPathParameter("name", path));
    }
    
    @Test
    public void getPathParameter_NotFound() {
        
        Route route = new Route("/customer/:id");
        String path = "/customer/1";
        
        assertNull(route.getPathParameter("name", path));
    }
    
    @Test
    public void splat_NoWildcards() {
        
       Route route = new Route("/");
       String path = "/";
       
       assertEquals(0, route.splat(path).length);
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void splat_GeneralWildcard() {
        
       Route route = new Route("/*");
       
       String path = "/hello/there";
       assertEquals(1, route.splat(path).length);
       assertEquals("hello/there", route.splat(path)[0]);
       
       path = "/hello";
       assertEquals(1, route.splat(path).length);
       assertEquals("hello", route.splat(path)[0]);
       
       path = "/hello/";
       assertEquals(1, route.splat(path).length);
       assertEquals("hello", route.splat(path)[0]);
       
       path = "/";
       assertEquals(0, route.splat(path).length);
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void splat_WithPrecedingResource() {
        
        Route route = new Route("/protected/*");
        
        String path = "/protected/1";
        assertEquals(1, route.splat(path).length);
        assertEquals("1", route.splat(path)[0]);
        
        path = "/protected/1/2";
        assertEquals(1, route.splat(path).length);
        assertEquals("1/2", route.splat(path)[0]);
        
        path = "/protected";
        assertEquals(0, route.splat(path).length);
        
        path = "/protected/";
        assertEquals(0, route.splat(path).length);
        
        path = "/hello";
        assertEquals(0, route.splat(path).length);
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void splat_InterjectedBetweenResources() {
        
        Route route = new Route("/protected/*/content");
        
        String path = "/protected/1/content";
        assertEquals(1, route.splat(path).length);
        assertEquals("1", route.splat(path)[0]);
        
        path = "/protected/blah/content";
        assertEquals(1, route.splat(path).length);
        assertEquals("blah", route.splat(path)[0]);
        
        path = "/protected/1/blah/content";
        assertEquals(0, route.splat(path).length);
        
        path = "/hello";
        assertEquals(0, route.splat(path).length);
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void splat_OccuringMultipleTimes() {
        
        Route route = new Route("/say/*/to/*");
        
        String path = "/say/hello/to/world";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("world", route.splat(path)[1]);
        
        path = "/say/bye/to/Tim";
        assertEquals(2, route.splat(path).length);
        assertEquals("bye", route.splat(path)[0]);
        assertEquals("Tim", route.splat(path)[1]);
        
        path = "/say/bye/bye/to/Tim";
        assertEquals(0, route.splat(path).length);
        
        path = "/say/hello/to/John/Doe";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("John/Doe", route.splat(path)[1]);
        
        path = "/hello";
        assertEquals(0, route.splat(path).length);
    }
    
    /*
     * TODO implement splat parameters (issue #1)
     */
    @Ignore("implement splat parameters (issue #1)")
    public void splat_VariousPathParams() {
        
        Route route = new Route("/say/*/to/:name/:times<[0-9]+>/*");
        
        String path = "/say/hello/to/John";
        assertEquals(0, route.splat(path).length);
        
        path = "/say/hello/to/John/1";
        assertEquals(0, route.splat(path).length);
        
        path = "/say/hello/to/Tim/1/time";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("time", route.splat(path)[1]);
        List<PathParameterElement> params = route.pathParameterElements();
        assertEquals(2, params.size());
        PathParameterElement elem = params.get(0);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertNull(elem.regex());
        elem = params.get(1);
        assertEquals("times", elem.name());
        assertEquals(4, elem.index());
        assertEquals("[0-9]+", elem.regex());
        assertEquals("Tim", route.getPathParameter("name", path));
        assertEquals("1", route.getPathParameter("times", path));
        
        path = "/say/hello/to/Tim/1/time/thanks";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("time/thanks", route.splat(path)[1]);
        params = route.pathParameterElements();
        assertEquals(2, params.size());
        elem = params.get(0);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertNull(elem.regex());
        elem = params.get(1);
        assertEquals("times", elem.name());
        assertEquals(4, elem.index());
        assertEquals("[0-9]+", elem.regex());
        assertEquals("Tim", route.getPathParameter("name", path));
        assertEquals("1", route.getPathParameter("times", path));
        
        path = "/hello";
        assertEquals(0, route.splat(path).length);
    }
}
