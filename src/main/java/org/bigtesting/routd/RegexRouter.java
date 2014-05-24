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
package org.bigtesting.routd;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;

/**
 * 
 * @author Luis Antunes
 */
public class RegexRouter implements Router {

    private final Set<RegexRoute> routes = 
            new ConcurrentSkipListSet<RegexRoute>(new RegexRouteComparator());
    
    public void add(Route route) {
        routes.add(new RegexRoute(route));
    }
    
    public Route route(String path) {
    
        path = RouteHelper.urlDecode(path, false);
        
        for (RegexRoute route : routes) {
            Matcher m = route.pattern().matcher(path);
            if (m.find()) {
                return route.getRoute();
            }
        }
        
        return null;
    }
}
