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
import org.bigtesting.routd.TreeNode;
import org.bigtesting.routd.TreeRouter;
import org.junit.Test;


/**
 * 
 * @author Luis Antunes
 */
public class TestTreeRouter extends RouterContractTest<TreeRouter> {
    
    protected TreeRouter newRouter() {
        return new TreeRouter();
    }
    
    @Test
    public void singleRoute_RootPath() {
        
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
    
    @Test
    public void singleRoute_StaticSingleElementPath() {
        
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
    
    @Test
    public void addingTheSameRouteTwiceHasNoEffect() {
        
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
    
    @Test
    public void singleRoute_StaticSingleElementPathEndsWithSeparator() {
        
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
    
    @Test
    public void singleRoute_StaticSingleElementPathWithRegexSymbol() {
        
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
    
    @Test
    public void multipleRoutes_StaticSingleElementPath() {
        
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
    
    @Test
    public void multipleRoutes_StaticMultipleElementPath() {

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
    
    @Test
    public void singleRoute_NamedSingleElementPath() {
        
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
    
    @Test
    public void singleRoute_NamedMultipleElementPath() {
        
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
    
    @Test
    public void singleRoute_NamedCustomRegexSingleElementPath() {
        
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
    
    @Test
    public void multipleRoutes_DifferentCustomRegexWithSameNames() {
        
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
    
    @Test
    public void singleRoute_SplatSingleElementPath() {
        
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
    
    @Test
    public void twoRoutes_SplatSingleElementPath() {
        
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
    
    @Test
    public void singleRoute_SplatWithPrecedingStaticElement() {
        
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
    
    @Test
    public void multipleRoutes_SplatWithPrecedingStaticElement() {
        
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
    
    @Test
    public void singleRoute_SplatMultipleElementPath() {
        
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
    
    @Test
    public void multipleSimilarRoutesMatchSplatBeforeStatic() {
        
        Route r1  = new Route("/abc/*/def");
        Route r2  = new Route("/abc/123/def");
        
        router.add(r2);
        router.add(r1);
        
        assertEquals(r1, router.route("/abc/123/def"));
    }
    
    @Test
    public void multipleSimilarRoutesMatchStaticBeforeNamed() {
        
        Route r1  = new Route("/abc/123/def");
        Route r2  = new Route("/abc/:name/def");
        
        router.add(r2);
        router.add(r1);
        
        assertEquals(r1, router.route("/abc/123/def"));
    }
    
    @Test
    public void multipleRoutesResultInProperlySortedChildren() {
        
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
}
