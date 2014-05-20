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

import org.bigtesting.routd.Route;
import org.bigtesting.routd.TrieNode;
import org.bigtesting.routd.TrieRouter;
import org.junit.Ignore;
import org.junit.Test;


/**
 * 
 * @author Luis Antunes
 */
public class TestTrieRouter {

    @Ignore("In progress")
    public void singleRoute_RootPath() {
        
        TrieRouter router = new TrieRouter();

        Route r = new Route("/");
        router.add(r);
        
        TrieNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertEquals(r, root.getRoute());
        
        List<TrieNode> nodes = root.getChildren();
        assertEquals(0, nodes.size());
        
        assertEquals(r, router.route("/"));
        assertNull(router.route("/*"));
        assertNull(router.route("/blah"));
    }
    
    @Ignore("In progress")
    public void singleRoute_StaticSingleElementPath() {
        
        TrieRouter router = new TrieRouter();

        Route r = new Route("/hello");
        router.add(r);
        
        TrieNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        
        List<TrieNode> nodes = root.getChildren();
        assertEquals(1, nodes.size());
        
        TrieNode child = nodes.get(0);
        assertEquals("^hello$", child.toString());
        assertEquals(r, child.getRoute());
        
        assertNull(router.route("/"));
        assertEquals(r, router.route("/hello"));
        assertNull(router.route("/hello/"));
        assertNull(router.route("/blah"));
    }
    
    @Ignore("In progress")
    public void multipleRoutes_StaticSingleElementPath() {
        
        TrieRouter router = new TrieRouter();

        Route r1 = new Route("/");
        Route r2 = new Route("/hello");
        router.add(r1);
        router.add(r2);
        
        TrieNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertEquals(r1, root.getRoute());
        
        List<TrieNode> nodes = root.getChildren();
        assertEquals(1, nodes.size());
        
        TrieNode child = nodes.get(0);
        assertEquals("^hello$", child.toString());
        assertEquals(r2, child.getRoute());
        
        assertEquals(r1, router.route("/"));
        assertEquals(r2, router.route("/hello"));
        assertNull(router.route("/hello/"));
        assertNull(router.route("/blah"));
    }
    
    @Test
    public void placeholder() {}
}
