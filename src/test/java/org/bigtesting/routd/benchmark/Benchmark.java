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
package org.bigtesting.routd.benchmark;

import static org.junit.Assert.*;

import org.bigtesting.routd.RegexRouter;
import org.bigtesting.routd.Route;
import org.bigtesting.routd.Router;
import org.bigtesting.routd.TreeRouter;
import org.junit.Test;

/**
 * 
 * @author Luis Antunes
 */
public class Benchmark {

    @Test
    public void benchmark() {
        
        compareRouters("/abc/def/ghi/john", new Route("/abc/def/ghi/:name<[a-z]+>"), 
                new RegexRouter(), new TreeRouter());
        
        compareRouters("/user/john/account/github", new Route("/user/*/account/:name"), 
                new RegexRouter(), new TreeRouter());
    }
    
    /*-------------------------------------------*/
    
    private void compareRouters(String path, Route expected, Router...routers) {
        
        for (Router router : routers) {
            routeAndReport(router, path, expected);
        }
    }
    
    private void routeAndReport(Router router, String path, Route expected) {
        
        addRoutes(router);
        RoutingResult result = route(router, path);
        assertEquals(expected, result.route);
        System.out.println("----------------");
        System.out.println("Router: " + router.getClass().getName());
        System.out.println("Path: " + path);
        System.out.println("Elapsed time for search: " + result.elapsed + " ns");
        System.out.println("----------------");
    }
    
    private void addRoutes(Router router) {
        
        router.add(new Route("/"));
        
        router.add(new Route("/a"));
        for (int i  = 0; i < 100; i++) {
            router.add(new Route("/a/" + i));
        }
        router.add(new Route("/b"));
        for (int i  = 0; i < 100; i++) {
            router.add(new Route("/b/" + i));
        }
        router.add(new Route("/c"));
        for (int i  = 0; i < 100; i++) {
            router.add(new Route("/c/" + i));
        }
        
        router.add(new Route("/abc/def/ghi/:name<[a-z]+>"));
        router.add(new Route("/abc/def/ghi/:id<[0-9]+>"));
        router.add(new Route("/abc/def/jkl"));
        router.add(new Route("/abc/def/mno"));
        router.add(new Route("/abc/pqr"));
        router.add(new Route("/abc/pqr/stu"));
        
        router.add(new Route("/user/*/account"));
        router.add(new Route("/user/*/account/:name"));
        router.add(new Route("/user/*/:id<[0-9]+>"));
    }
    
    private RoutingResult route(Router router, String path) {
        
        RoutingResult result = new RoutingResult();
        long start = System.nanoTime();
        result.route = router.route(path);
        long end = System.nanoTime();
        result.elapsed = end - start;
        return result;
    }
    
    private static class RoutingResult {
        public long elapsed;
        public Route route;
    }
}
