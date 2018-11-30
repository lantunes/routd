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
import org.bigtesting.routd.TreeNode;
import org.bigtesting.routd.TreeRouter;
import org.junit.runner.RunWith;

/**
 * 
 * @author Luis Antunes
 */
@RunWith(ItRunner.class)
public class TestTreeRouter extends RouterContractTest<TreeRouter> {
    
    protected TreeRouter newRouter() {
        return new TreeRouter();
    }
    
    @It("produces the correct tree when given the root route")
    void treeTest1() {
        
        Route r = new Route("/");
        
        router.add(r);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertEquals(r, root.getRoute());
        assertEquals(0, root.getChildren().size());
        
        assertEquals(r, router.route("/"));
        assertNull(router.route("/*"));
        assertNull(router.route("/blah"));
    }
    
    @It("produces the correct tree when given a single route " +
    		"with a single static path element")
    void treeTest2() {
        
        Route r = new Route("/hello");
        
        router.add(r);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^hello$", child.toString());
        assertEquals(r, child.getRoute());
        
        assertNull(router.route("/"));
        assertEquals(r, router.route("/hello"));
        assertNull(router.route("/hello/"));
        assertNull(router.route("/blah"));
    }
    
    @It("produces the correct tree when given the same route twice")
    void treeTest3() {
        
        Route r1 = new Route("/hello");
        Route r2 = new Route("/hello");
        
        router.add(r1);
        router.add(r2);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^hello$", child.toString());
        assertEquals(r1, child.getRoute());
        
        assertNull(router.route("/"));
        assertEquals(r1, router.route("/hello"));
        assertNull(router.route("/hello/"));
        assertNull(router.route("/blah"));
    }
    
    @It("produces the correct tree when given a single route with a " +
    		"single static path element ending with a path separator")
    void treeTest4() {
        
        Route r = new Route("/hello/");
        
        router.add(r);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^hello$", child.toString());
        assertNull(child.getRoute());
        assertEquals(1, child.getChildren().size());
        
        child = child.getChildren().get(0);
        assertEquals("^/$", child.toString());
        assertEquals(r, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertNull(router.route("/"));
        assertEquals(r, router.route("/hello/"));
        assertNull(router.route("/hello"));
        assertNull(router.route("/blah"));
    }
    
    @It("produces the correct tree when given a single route with a " +
    		"single static path element with a regex symbol")
    void treeTest5() {
        
        Route r = new Route("/hello$.html");
        
        router.add(r);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^hello\\$\\.html$", child.toString());
        assertEquals(r, child.getRoute());
        
        assertNull(router.route("/"));
        assertEquals(r, router.route("/hello$.html"));
        assertNull(router.route("/hello/"));
        assertNull(router.route("/blah"));
    }
    
    @It("produces the correct tree when given the root route and a " +
    		"route with a single static path element")
    void treeTest6() {
        
        Route r1 = new Route("/");
        Route r2 = new Route("/hello");
        
        router.add(r1);
        router.add(r2);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertEquals(r1, root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^hello$", child.toString());
        assertEquals(r2, child.getRoute());
        
        assertEquals(r1, router.route("/"));
        assertEquals(r2, router.route("/hello"));
        assertNull(router.route("/hello/"));
        assertNull(router.route("/blah"));
    }
    
    @It("produces the correct tree when given multiple routes " +
    		"with static path elements")
    void treeTest7() {

        Route r1 = new Route("/");
        Route r2 = new Route("/hello");
        Route r3 = new Route("/hello/world");
        Route r4 = new Route("/protected");
        
        router.add(r1);
        router.add(r2);
        router.add(r3);
        router.add(r4);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertEquals(r1, root.getRoute());
        assertEquals(2, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^hello$", child.toString());
        assertEquals(r2, child.getRoute());
        assertEquals(1, child.getChildren().size());
        
        child = child.getChildren().get(0);
        assertEquals("^world$", child.toString());
        assertEquals(r3, child.getRoute());
        
        child = root.getChildren().get(1);
        assertEquals("^protected$", child.toString());
        assertEquals(r4, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertEquals(r1, router.route("/"));
        assertEquals(r2, router.route("/hello"));
        assertNull(router.route("/hello/"));
        assertEquals(r3, router.route("/hello/world"));
        assertEquals(r4, router.route("/protected"));
        assertNull(router.route("/blah"));
        assertNull(router.route("/protected/"));
        assertNull(router.route("/hello/world/"));
    }
    
    @It("produces the correct tree when given a sigle route with a " +
    		"single named path element")
    void treeTest8() {
        
        Route r1 = new Route("/:name");
        
        router.add(r1);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^([^/]+)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertNull(router.route("/"));
        assertEquals(r1, router.route("/john"));
        assertNull(router.route("/john/doe"));
        assertNull(router.route("/john/"));
    }
    
    @It("produces the correct tree when given a single route with " +
    		"multiple named path elements")
    void treeTest9() {
        
        Route r1 = new Route("/:name/:id");
        
        router.add(r1);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^([^/]+)$", child.toString());
        assertNull(child.getRoute());
        assertEquals(1, child.getChildren().size());
        
        child = child.getChildren().get(0);
        assertEquals("^([^/]+)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertNull(router.route("/"));
        assertNull(router.route("/john"));
        assertEquals(r1, router.route("/john/doe"));
        assertNull(router.route("/john/"));
        assertNull(router.route("/john/doe/"));
    }
    
    @It("produces the correct tree when given a single route with " +
            "a single named parameter with custom regex")
    void treeTest10() {
        
        Route r1 = new Route("/:id<[0-9]+>");
        
        router.add(r1);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^([0-9]+)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertNull(router.route("/"));
        assertEquals(r1, router.route("/123"));
        assertNull(router.route("/123/456"));
        assertNull(router.route("/123/"));
    }
    
    @It("produces the correct tree when given multiples routes with " +
            "multiple elements with a named parameter with custom regex")
    void treeTest11() {
        
        Route r1 = new Route("/cntrl/actn/:id<[0-9]+>");
        Route r2 = new Route("/cntrl/actn/:id<[a-z]+>");
        
        router.add(r1);
        router.add(r2);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode firstChild = root.getChildren().get(0);
        assertEquals("^cntrl$", firstChild.toString());
        assertNull(firstChild.getRoute());
        assertEquals(1, firstChild.getChildren().size());
        
        TreeNode secondChild = firstChild.getChildren().get(0);
        assertEquals("^actn$", secondChild.toString());
        assertNull(secondChild.getRoute());
        assertEquals(2, secondChild.getChildren().size());
        
        TreeNode child = secondChild.getChildren().get(0);
        assertEquals("^([0-9]+)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        child = secondChild.getChildren().get(1);
        assertEquals("^([a-z]+)$", child.toString());
        assertEquals(r2, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertEquals(r1, router.route("/cntrl/actn/123"));
        assertNull(router.route("/cntrl/actn/123/"));
        assertEquals(r2, router.route("/cntrl/actn/bob"));
        assertNull(router.route("/cntrl/actn/bob/"));
    }
    
    @It("produces the correct tree when given a single route with " +
    		"a splat that matches all paths")
    void treeTest12() {
        
        Route r1 = new Route("/*");
        
        router.add(r1);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^(.*)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertEquals(r1, router.route("/"));
        assertEquals(r1, router.route("/123"));
        assertEquals(r1, router.route("/123/456"));
        assertEquals(r1, router.route("/123/"));
    }
    
    @It("produces the correct tree when given a single route with " +
            "a splat that matches all paths, and the root route")
    void treeTest13() {
        
        Route r0 = new Route("/");
        Route r1 = new Route("/*");
        
        router.add(r0);
        router.add(r1);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertEquals(r0, root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^(.*)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertEquals(r0, router.route("/"));
        assertEquals(r1, router.route("/123"));
        assertEquals(r1, router.route("/123/456"));
        assertEquals(r1, router.route("/123/"));
    }
    
    @It("produces the correct tree when given a single route with " +
            "a splat with a preceding static element")
    void treeTest14() {
        
        Route r1 = new Route("/protected/*");
        
        router.add(r1);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^protected$", child.toString());
        assertNull(child.getRoute());
        assertEquals(1, child.getChildren().size());
        
        child = child.getChildren().get(0);
        assertEquals("^(.*)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertNull(router.route("/protected"));
        assertEquals(r1, router.route("/protected/content"));
        assertEquals(r1, router.route("/protected/"));
    }
    
    @It("produces the correct tree when given multiple routes, one with " +
            "a splat with a preceding static element")
    void treeTest15() {
        
        Route r1 = new Route("/protected/*");
        Route r2 = new Route("/protected/:id<[0-9]+>");
        Route r3 = new Route("/:name<[a-z]+>");
        
        router.add(r1);
        router.add(r2);
        router.add(r3);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(2, root.getChildren().size());
        
        TreeNode firstChild = root.getChildren().get(0);
        assertEquals("^protected$", firstChild.toString());
        assertNull(firstChild.getRoute());
        assertEquals(2, firstChild.getChildren().size());
        
        TreeNode child = firstChild.getChildren().get(0);
        assertEquals("^(.*)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        child = firstChild.getChildren().get(1);
        assertEquals("^([0-9]+)$", child.toString());
        assertEquals(r2, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        TreeNode secondChild = root.getChildren().get(1);
        assertEquals("^([a-z]+)$", secondChild.toString());
        assertEquals(r3, secondChild.getRoute());
        assertEquals(0, secondChild.getChildren().size());
        
        assertEquals(r1, router.route("/protected/content"));
        assertEquals(r1, router.route("/protected/123"));
        assertEquals(r3, router.route("/john"));
        assertEquals(r1, router.route("/protected/"));
    }
    
    @It("produces the correct tree when given a single route with " +
            "multiple splat elements")
    void treeTest16() {
        
        Route r1 = new Route("/say/*/to/*");
        
        router.add(r1);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertNull(root.getRoute());
        assertEquals(1, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^say$", child.toString());
        assertNull(child.getRoute());
        assertEquals(1, child.getChildren().size());
        
        child = child.getChildren().get(0);
        assertEquals("^(.*)$", child.toString());
        assertNull(child.getRoute());
        assertEquals(1, child.getChildren().size());
        
        child = child.getChildren().get(0);
        assertEquals("^to$", child.toString());
        assertNull(child.getRoute());
        assertEquals(1, child.getChildren().size());
        
        child = child.getChildren().get(0);
        assertEquals("^(.*)$", child.toString());
        assertEquals(r1, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        assertNull(router.route("/hello"));
        assertEquals(r1, router.route("/say/hello/to/world"));
        assertEquals(r1, router.route("/say/bye/to/Tim"));
        assertNull(router.route("/say/bye/bye/to/Tim"));
        assertEquals(r1, router.route("/say/bye/to/John/Doe"));
        assertNull(router.route("/say/hello/to"));
        assertEquals(r1, router.route("/say/hello/to/"));
    }
    
    @It("matches a similar route containing a splat before the " +
    		"route containing the static element")
    void treeTest17() {
        
        Route r1  = new Route("/abc/*/def");
        Route r2  = new Route("/abc/123/def");
        
        router.add(r2);
        router.add(r1);
        
        assertEquals(r1, router.route("/abc/123/def"));
    }
    
    @It("matches a similar route containing a static element before the " +
            "route containing the named parameter")
    void treeTest18() {
        
        Route r1  = new Route("/abc/123/def");
        Route r2  = new Route("/abc/:name/def");
        
        router.add(r2);
        router.add(r1);
        
        assertEquals(r1, router.route("/abc/123/def"));
    }
    
    @It("matches a similar route containing a splat before the " +
            "route containing the named parameter")
    void treeTest19() {
        
        Route r1  = new Route("/abc/*/def");
        Route r2  = new Route("/abc/:name/def");
        
        router.add(r2);
        router.add(r1);
        
        assertEquals(r1, router.route("/abc/123/def"));
    }
    
    @It("produces a tree with multiple routes with children sorted properly")
    void treeTest20() {
        
        Route r1  = new Route("/");
        Route r2  = new Route("/*");
        Route r3  = new Route("/1");
        Route r4  = new Route("/x");
        Route r5  = new Route("/y");
        Route r6  = new Route("/:id");
        
        router.add(r6);
        router.add(r5);
        router.add(r3);
        router.add(r4);
        router.add(r1);
        router.add(r2);
        
        TreeNode root = router.getRoot();
        assertEquals("^/$", root.toString());
        assertEquals(r1, root.getRoute());
        assertEquals(5, root.getChildren().size());
        
        TreeNode child = root.getChildren().get(0);
        assertEquals("^(.*)$", child.toString());
        assertEquals(r2, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        child = root.getChildren().get(1);
        assertEquals("^1$", child.toString());
        assertEquals(r3, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        child = root.getChildren().get(2);
        assertEquals("^x$", child.toString());
        assertEquals(r4, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        child = root.getChildren().get(3);
        assertEquals("^y$", child.toString());
        assertEquals(r5, child.getRoute());
        assertEquals(0, child.getChildren().size());
        
        child = root.getChildren().get(4);
        assertEquals("^([^/]+)$", child.toString());
        assertEquals(r6, child.getRoute());
        assertEquals(0, child.getChildren().size());
    }

    @It("produces the correct trees when the routes are added in a non-deterministic way")
    public void treeTest21() {
        final Route r1  = new Route("/abc/:param1/:param2/:param4/def/:param5");
        final Route r2  = new Route("/abc/:param1/:param2/:param3");

        router.add(r1);
        router.add(r2);

        assertEquals(r2, router.route("/abc/dummy1/dummy2/dummy3"));
        assertEquals(r1, router.route("/abc/dummy1/dummy2/dummy3/def/dummy4"));

        assertNull(router.route("/abc"));
        assertNull(router.route("/abc/dummy1"));
        assertNull(router.route("/abc/dummy1/dummy2"));
        assertNull(router.route("/abc/dummy1/dummy2/dummy3/def"));
    }
}
