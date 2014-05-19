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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.bigtesting.routd.RegexRoute;
import org.bigtesting.routd.RegexRouteComparator;
import org.bigtesting.routd.Route;
import org.junit.Test;

/**
 * 
 * @author Luis Antunes
 */
public class TestRegexRouteComparator {

    @Test
    public void testRegexRoutesAreSortedCorrectly() {
        
        RegexRoute r1  = new RegexRoute(new Route("/"));
        RegexRoute r2  = new RegexRoute(new Route("/*"));
        RegexRoute r3  = new RegexRoute(new Route("/1"));
        RegexRoute r4  = new RegexRoute(new Route("/a"));
        RegexRoute r5  = new RegexRoute(new Route("/b"));
        RegexRoute r6  = new RegexRoute(new Route("/:id"));
        RegexRoute r7  = new RegexRoute(new Route("/1/"));
        RegexRoute r8  = new RegexRoute(new Route("/1/*"));        
        RegexRoute r9  = new RegexRoute(new Route("/1/2"));
        RegexRoute r10  = new RegexRoute(new Route("/1/a"));
        RegexRoute r11  = new RegexRoute(new Route("/1/b"));
        RegexRoute r12 = new RegexRoute(new Route("/1/:id"));
        RegexRoute r13 = new RegexRoute(new Route("/1/*/3"));
        RegexRoute r14 = new RegexRoute(new Route("/1/2/"));
        RegexRoute r15 = new RegexRoute(new Route("/1/2/*"));
        RegexRoute r16 = new RegexRoute(new Route("/1/2/3"));
        RegexRoute r17 = new RegexRoute(new Route("/1/2/:id<[0-9]+>"));
        RegexRoute r18 = new RegexRoute(new Route("/1/2/:id<[a-b]+>"));
        RegexRoute r19 = new RegexRoute(new Route("/1/:times/3"));
        
        Set<RegexRoute> routes = new TreeSet<RegexRoute>(new RegexRouteComparator());
        routes.add(r1);
        routes.add(r2);
        routes.add(r3);
        routes.add(r4);
        routes.add(r5);
        routes.add(r6);
        routes.add(r7);
        routes.add(r8);
        routes.add(r9);
        routes.add(r10);
        routes.add(r11);
        routes.add(r12);
        routes.add(r13);
        routes.add(r14);
        routes.add(r15);
        routes.add(r16);
        routes.add(r17);
        routes.add(r18);
        routes.add(r19);
        
        Iterator<RegexRoute> itr = routes.iterator();
        assertEquals(r1, itr.next());
        assertEquals(r2, itr.next());
        assertEquals(r3, itr.next());
        assertEquals(r4, itr.next());
        assertEquals(r5, itr.next());
        assertEquals(r6, itr.next());
        assertEquals(r7, itr.next());
        assertEquals(r8, itr.next());
        assertEquals(r9, itr.next());
        assertEquals(r10, itr.next());
        assertEquals(r11, itr.next());
        assertEquals(r12, itr.next());
        assertEquals(r13, itr.next());
        assertEquals(r14, itr.next());
        assertEquals(r15, itr.next());
        assertEquals(r16, itr.next());
        assertEquals(r17, itr.next());
        assertEquals(r18, itr.next());
        assertEquals(r19, itr.next());
    }
}
