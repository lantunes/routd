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

import org.bigtesting.routd.NamedParameterElement;
import org.bigtesting.routd.PathElement;
import org.bigtesting.routd.Route;
import org.bigtesting.routd.SplatParameterElement;
import org.bigtesting.routd.StaticPathElement;
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
    public void getNamedParameterElements_NoneExist() {
        
        Route r = new Route("/actn");
        List<NamedParameterElement> params = r.getNamedParameterElements();
        
        assertTrue(params.isEmpty());
    }
    
    @Test
    public void getNamedParameterElements_OneExistsWithAction() {
        
        Route r = new Route("/actn/:id");
        
        List<NamedParameterElement> params = r.getNamedParameterElements();
        assertEquals(1, params.size());
        
        NamedParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(1, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void getNamedParameterElements_OneExistsAlone() {
        
        Route r = new Route("/:id");
        
        List<NamedParameterElement> params = r.getNamedParameterElements();
        assertEquals(1, params.size());
        
        NamedParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(0, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void getNamedParameterElements_ManyExistAlone() {
        
        Route r = new Route("/:id/:name");
        
        List<NamedParameterElement> params = r.getNamedParameterElements();
        assertEquals(2, params.size());
        
        NamedParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(0, elem.index());
        assertNull(elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(1, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void getNamedParameterElements_ManyExistWithControllerAndAction() {
        
        Route r = new Route("/cntrl/actn/:id/:name");
        
        List<NamedParameterElement> params = r.getNamedParameterElements();
        assertEquals(2, params.size());
        
        NamedParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(2, elem.index());
        assertNull(elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertNull(elem.regex());
    }
    
    @Test
    public void getNamedParameterElements_ManyExistWithRegexWithControllerAndAction() {
        
        Route r = new Route("/cntrl/actn/:id<[0-9]+>/:name<[a-z]+>");
        
        List<NamedParameterElement> params = r.getNamedParameterElements();
        assertEquals(2, params.size());
        
        NamedParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(2, elem.index());
        assertEquals("[0-9]+", elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertEquals("[a-z]+", elem.regex());
    }
    
    @Test
    public void getNamedParameterElements_OneExistsWithRegexWithSlashWithControllerAndAction() {
        
        Route r = new Route("/cntrl/actn/:id<[^/]+>/:name<[a-z]+>");
        
        List<NamedParameterElement> params = r.getNamedParameterElements();
        assertEquals(2, params.size());
        
        NamedParameterElement elem = params.get(0);
        assertEquals("id", elem.name());
        assertEquals(2, elem.index());
        assertEquals("[^/]+", elem.regex());
        
        elem = params.get(1);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertEquals("[a-z]+", elem.regex());
    }
    
    @Test
    public void getNamedParameter() {
        
        Route route = new Route("/customer/:id");
        String path = "/customer/1";
        
        assertEquals("1", route.getNamedParameter("id", path));
    }
    
    @Test
    public void getNamedParameter_HandlesUnicodeCorrectly() {
        
        Route route = new Route("/customer/:id");
        String path = "/customer/f%C3%B6%C3%B6";
        
        assertEquals("föö", route.getNamedParameter("id", path));
    }
    
    @Test
    public void getNamedParameter_HandlesEncodedSlashesCorrectly() {
        
        Route route = new Route("/:test");
        String path = "/foo%2Fbar";
        
        assertEquals("foo/bar", route.getNamedParameter("test", path));
    }
    
    @Test
    public void getNamedParameter_HandlesEncodedSlashesCorrectlyWhenInterjected() {
        
        Route route = new Route("/hello/:test/there");
        String path = "/hello/foo%2Fbar/there";
        
        assertEquals("foo/bar", route.getNamedParameter("test", path));
    }
    
    @Test
    public void getNamedParameter_WithMultipleParameters() {
        
        Route route = new Route("/customer/:id/named/:name");
        String path = "/customer/1/named/John";
        
        assertEquals("1", route.getNamedParameter("id", path));
        assertEquals("John", route.getNamedParameter("name", path));
    }
    
    @Test
    public void getNamedParameter_NotFound() {
        
        Route route = new Route("/customer/:id");
        String path = "/customer/1";
        
        assertNull(route.getNamedParameter("name", path));
    }
    
    @Test
    public void splat_NoWildcards() {
        
       Route route = new Route("/");
       String path = "/";
       
       assertEquals(0, route.splat(path).length);
    }
    
    @Test
    public void getSplatParameterElements_NoneOccurring() {
        
        Route route = new Route("/");
        
        List<SplatParameterElement> params = route.getSplatParameterElements();
        assertEquals(0, params.size());
    }
    
    @Test
    public void getSplatParameterElements_GeneralWildcard() {
        
        Route route = new Route("/*");
        
        List<SplatParameterElement> params = route.getSplatParameterElements();
        assertEquals(1, params.size());
        
        SplatParameterElement elem = params.get(0);
        assertEquals(0, elem.index());
    }
    
    @Test
    public void getSplatParameterElements_WithPrecedingResource() {
        
        Route route = new Route("/protected/*");
        
        List<SplatParameterElement> params = route.getSplatParameterElements();
        assertEquals(1, params.size());
        
        SplatParameterElement elem = params.get(0);
        assertEquals(1, elem.index());
    }
    
    @Test
    public void getSplatParameterElements_InterjectedBetweenResources() {
        
        Route route = new Route("/protected/*/content");
        
        List<SplatParameterElement> params = route.getSplatParameterElements();
        assertEquals(1, params.size());
        
        SplatParameterElement elem = params.get(0);
        assertEquals(1, elem.index());
    }
    
    @Test
    public void getSplatParameterElements_OccurringMultipleTimes() {
        
        Route route = new Route("/say/*/to/*");
        
        List<SplatParameterElement> params = route.getSplatParameterElements();
        assertEquals(2, params.size());
        
        SplatParameterElement elem = params.get(0);
        assertEquals(1, elem.index());
        
        elem = params.get(1);
        assertEquals(3, elem.index());
    }
    
    @Test
    public void getStaticPathElements_NoneOccurring() {
        
        Route route = new Route("/");
        
        List<StaticPathElement> elems = route.getStaticPathElements();
        assertEquals(0, elems.size());
    }
    
    @Test
    public void getStaticPathElements_OneOccurring() {
        
        Route route = new Route("/cntrl");
        
        List<StaticPathElement> elems = route.getStaticPathElements();
        assertEquals(1, elems.size());
        
        StaticPathElement elem = elems.get(0);
        assertEquals("cntrl", elem.name());
        assertEquals(0, elem.index());
    }
    
    @Test
    public void getStaticPathElements_TwoOccurring() {
        
        Route route = new Route("/cntrl/actn");
        
        List<StaticPathElement> elems = route.getStaticPathElements();
        assertEquals(2, elems.size());
        
        StaticPathElement elem = elems.get(0);
        assertEquals("cntrl", elem.name());
        assertEquals(0, elem.index());
        
        elem = elems.get(1);
        assertEquals("actn", elem.name());
        assertEquals(1, elem.index());
    }
    
    @Test
    public void getStaticPathElements_TwoOccurringWithParamElements() {
        
        Route route = new Route("/cntrl/*/actn/:name");
        
        List<StaticPathElement> elems = route.getStaticPathElements();
        assertEquals(2, elems.size());
        
        StaticPathElement elem = elems.get(0);
        assertEquals("cntrl", elem.name());
        assertEquals(0, elem.index());
        
        elem = elems.get(1);
        assertEquals("actn", elem.name());
        assertEquals(2, elem.index());
    }
    
    @Test
    public void getPathElements_NoneOccurring() {
        
        Route route = new Route("/");
        
        List<PathElement> elems = route.getPathElements();
        assertEquals(0, elems.size());
    }
    
    @Test
    public void getPathElements_MultipleOccurring() {
        
        Route route = new Route("/say/*/to/:name/:times<[0-9]+>/*");
        
        List<PathElement> elems = route.getPathElements();
        assertEquals(6, elems.size());
        
        PathElement elem = elems.get(0);
        assertTrue(elem instanceof StaticPathElement);
        assertEquals("say", elem.name());
        assertEquals(0, elem.index());
        
        elem = elems.get(1);
        assertTrue(elem instanceof SplatParameterElement);
        assertEquals(1, elem.index());
        
        elem = elems.get(2);
        assertTrue(elem instanceof StaticPathElement);
        assertEquals("to", elem.name());
        assertEquals(2, elem.index());
        
        elem = elems.get(3);
        assertTrue(elem instanceof NamedParameterElement);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertNull(((NamedParameterElement)elem).regex());
        
        elem = elems.get(4);
        assertTrue(elem instanceof NamedParameterElement);
        assertEquals("times", elem.name());
        assertEquals(4, elem.index());
        assertEquals("[0-9]+", ((NamedParameterElement)elem).regex());
        
        elem = elems.get(5);
        assertTrue(elem instanceof SplatParameterElement);
        assertEquals(5, elem.index());
    }
    
    @Test
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
       assertEquals("hello/", route.splat(path)[0]);
    }
    
    @Test
    public void splat_HandlesEncodedSlashesCorrectly() {
        
        Route route = new Route("/*");
        
        String path = "/foo%2Fbar";
        assertEquals(1, route.splat(path).length);
        assertEquals("foo/bar", route.splat(path)[0]);
    }
    
    @Test
    public void splat_HandlesEncodedSlashesCorrectlyWhenInterjected() {
        
        Route route = new Route("/hello/*/there");
        
        String path = "/hello/foo%2Fbar/there";
        assertEquals(1, route.splat(path).length);
        assertEquals("foo/bar", route.splat(path)[0]);
    }
    
    @Test
    public void splat_HandlesUnicodeCorrectly() {
        
        Route route = new Route("/*");
        
        String path = "/f%C3%B6%C3%B6";
        assertEquals(1, route.splat(path).length);
        assertEquals("föö", route.splat(path)[0]);
    }
    
    @Test
    public void splat_WithPrecedingResource() {
        
        Route route = new Route("/protected/*");
        
        String path = "/protected/1";
        assertEquals(1, route.splat(path).length);
        assertEquals("1", route.splat(path)[0]);
        
        path = "/protected/1/2";
        assertEquals(1, route.splat(path).length);
        assertEquals("1/2", route.splat(path)[0]);
    }
    
    @Test
    public void splat_InterjectedBetweenResources() {
        
        Route route = new Route("/protected/*/content");
        
        String path = "/protected/1/content";
        assertEquals(1, route.splat(path).length);
        assertEquals("1", route.splat(path)[0]);
        
        path = "/protected/blah/content";
        assertEquals(1, route.splat(path).length);
        assertEquals("blah", route.splat(path)[0]);
    }
    
    @Test
    public void splat_OccurringMultipleTimes() {
        
        Route route = new Route("/say/*/to/*");
        
        String path = "/say/hello/to/world";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("world", route.splat(path)[1]);
        
        path = "/say/bye/to/Tim";
        assertEquals(2, route.splat(path).length);
        assertEquals("bye", route.splat(path)[0]);
        assertEquals("Tim", route.splat(path)[1]);
        
        path = "/say/hello/to/John/Doe";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("John/Doe", route.splat(path)[1]);
    }
    
    @Test
    public void splat_VariousPathParams() {
        
        Route route = new Route("/say/*/to/:name/:times<[0-9]+>/*");
        
        String path = "/say/hello/to/Tim/1/time";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("time", route.splat(path)[1]);
        List<NamedParameterElement> params = route.getNamedParameterElements();
        assertEquals(2, params.size());
        NamedParameterElement elem = params.get(0);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertNull(elem.regex());
        elem = params.get(1);
        assertEquals("times", elem.name());
        assertEquals(4, elem.index());
        assertEquals("[0-9]+", elem.regex());
        assertEquals("Tim", route.getNamedParameter("name", path));
        assertEquals("1", route.getNamedParameter("times", path));
        
        path = "/say/hello/to/Tim/1/time/thanks";
        assertEquals(2, route.splat(path).length);
        assertEquals("hello", route.splat(path)[0]);
        assertEquals("time/thanks", route.splat(path)[1]);
        params = route.getNamedParameterElements();
        assertEquals(2, params.size());
        elem = params.get(0);
        assertEquals("name", elem.name());
        assertEquals(3, elem.index());
        assertNull(elem.regex());
        elem = params.get(1);
        assertEquals("times", elem.name());
        assertEquals(4, elem.index());
        assertEquals("[0-9]+", elem.regex());
        assertEquals("Tim", route.getNamedParameter("name", path));
        assertEquals("1", route.getNamedParameter("times", path));
    }
    
    @Test
    public void splat_ReturnsEmptyStringWithEmptyTerminalSplat() {
        
        Route route = new Route("/hello/*");
        
        String path = "/hello/";
        assertEquals(1, route.splat(path).length);
        assertEquals("", route.splat(path)[0]);
    }
    
    @Test
    public void urlDecodesNamedParametersAndSplats() {
     
        Route route = new Route("/:foo/*");
        
        String path = "/hello%20world/how%20are%20you";
        
        assertEquals("hello world", route.getNamedParameter("foo", path));
        assertEquals(1, route.splat(path).length);
        assertEquals("how are you", route.splat(path)[0]);
    }
    
    @Test
    public void doesNotCovertPlusSignIntoSpaceAsTheValueOfANamedParam() {
        
        Route route = new Route("/:test");
        
        String path = "/bob+ross";
        
        assertEquals("bob+ross", route.getNamedParameter("test", path));
    }
    
    @Test
    public void doesNotCovertPlusSignIntoSpaceAsTheValueOfASplatParam() {
        
        Route route = new Route("/hello/*");
        
        String path = "/hello/bob+ross";
        
        assertEquals("bob+ross", route.splat(path)[0]);
    }
    
    @Test
    public void hasPathElementsReturnsTrueIfItDoes() {
        
        Route route = new Route("/hello/*");
        assertTrue(route.hasPathElements());
    }
    
    @Test
    public void hasPathElementsReturnsTrueIfItDoesWithJustASplat() {
        
        Route route = new Route("/*");
        assertTrue(route.hasPathElements());
    }
    
    @Test
    public void hasPathElementsReturnsTrueIfItDoesWithJustANamedParameter() {
        
        Route route = new Route("/:named");
        assertTrue(route.hasPathElements());
    }
    
    @Test
    public void hasPathElementsReturnsFalseIfItDoesNot() {
        
        Route route = new Route("/");
        assertFalse(route.hasPathElements());
    }
    
    @Test
    public void getSplatParameter() {
        
        Route route = new Route("/customer/*");
        String path = "/customer/1";
        
        assertEquals("1", route.getSplatParameter(0, path));
    }
    
    @Test
    public void getSplatParameter_HandlesUnicodeCorrectly() {
        
        Route route = new Route("/customer/*");
        String path = "/customer/f%C3%B6%C3%B6";
        
        assertEquals("föö", route.getSplatParameter(0, path));
    }
    
    @Test
    public void getSplatParameter_HandlesEncodedSlashesCorrectly() {
        
        Route route = new Route("/*");
        String path = "/foo%2Fbar";
        
        assertEquals("foo/bar", route.getSplatParameter(0, path));
    }
    
    @Test
    public void getSplatParameter_HandlesEncodedSlashesCorrectlyWhenInterjected() {
        
        Route route = new Route("/hello/*/there");
        String path = "/hello/foo%2Fbar/there";
        
        assertEquals("foo/bar", route.getSplatParameter(0, path));
    }
    
    @Test
    public void getSplatParameter_WithMultipleParameters() {
        
        Route route = new Route("/customer/*/named/*");
        String path = "/customer/1/named/John";
        
        assertEquals("1", route.getSplatParameter(0, path));
        assertEquals("John", route.getSplatParameter(1, path));
    }
    
    @Test
    public void getSplatParameter_NotFound() {
        
        Route route = new Route("/customer/*");
        String path = "/customer/1";
        
        assertNull(route.getSplatParameter(1, path));
    }
}
