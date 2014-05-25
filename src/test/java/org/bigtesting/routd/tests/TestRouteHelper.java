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

import java.util.Arrays;

import org.bigtesting.It;
import org.bigtesting.ItRunner;
import org.bigtesting.routd.RouteHelper;
import org.junit.runner.RunWith;

/**
 * 
 * @author Luis Antunes
 */
@RunWith(ItRunner.class)
public class TestRouteHelper {

    @It("returns an empty string if given a path with just '/'")
    void getPathElementsTest1() {
        
        String[] expected = new String[]{""};
        String[] actual = RouteHelper.getPathElements("/");
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @It("returns the correct strings if given a path with multiple " +
    		"static elements and a named parameter")
    void getPathElementsTest2() {
        
        String[] expected = new String[]{"cntrl","actn","clients",":id"};
        String[] actual = RouteHelper.getPathElements("/cntrl/actn/clients/:id");
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @It("returns the correct strings even if the path does not start with '/'")
    void getPathElementsTest2b() {
        
        String[] expected = new String[]{"cntrl","actn","clients",":id"};
        String[] actual = RouteHelper.getPathElements("cntrl/actn/clients/:id");
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @It("returns the correct strings if given a path with a single " +
            "static element and a named parameter")
    void getPathElementsTest3() {
        
        String[] expected = new String[]{"clients",":id"};
        String[] actual = RouteHelper.getPathElements("/clients/:id");
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @It("returns the correct strings if given a path with a single " +
            "static element and a named parameter with custom regex")
    void getPathElementsTest4() {
        
        String[] expected = new String[]{"clients",":id<[0-9]+>"};
        String[] actual = RouteHelper.getPathElements("/clients/:id<[0-9]+>");
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @It("escapes non-custom regex")
    void escapeNonCustomRegex() {
        
        String path = "/cntrl/[](){}*^?$.\\/a+b/:id<[^/]+>/:name<[a-z]+>";
        String expected = 
                "/cntrl/\\[\\]\\(\\)\\{\\}\\*\\^\\?\\$\\.\\\\/a\\+b/:id<[^/]+>/:name<[a-z]+>";
        String actual = RouteHelper.escapeNonCustomRegex(path);
        assertEquals(expected, actual);
    }
    
    @It("url decodes for routing correctly") void urlDecodeForRoutingTest() {
        
        String path = "/hello";
        assertEquals("/hello", RouteHelper.urlDecodeForRouting(path));
        
        path = "/hello/foo%2Fbar/there";
        assertEquals("/hello/foo%2fbar/there", RouteHelper.urlDecodeForRouting(path));        
                
        path = "/hello/foo%2fbar/there";
        assertEquals("/hello/foo%2fbar/there", RouteHelper.urlDecodeForRouting(path));
        
        path = "/hello/foo%2Fbar/there/foo%2Bbar/a+space";
        assertEquals("/hello/foo%2fbar/there/foo+bar/a space", RouteHelper.urlDecodeForRouting(path));
    }
    
    @It("url decodes for path params correctly") void urlDecodeForPathParamsTest() {
        
        String param = "hello";
        assertEquals("hello", RouteHelper.urlDecodeForPathParams(param));
        
        param = "foo%2Fbar";
        assertEquals("foo/bar", RouteHelper.urlDecodeForPathParams(param));        
        
        param = "foo+bar";
        assertEquals("foo+bar", RouteHelper.urlDecodeForPathParams(param));
        
        param = "foo%2Bbar";
        assertEquals("foo+bar", RouteHelper.urlDecodeForPathParams(param));
        
        param = "foo%20bar";
        assertEquals("foo bar", RouteHelper.urlDecodeForPathParams(param));
    }
    
}
