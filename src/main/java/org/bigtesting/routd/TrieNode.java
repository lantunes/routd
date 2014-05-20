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

import static org.bigtesting.routd.RouteHelper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * @author Luis Antunes
 */
public class TrieNode {
    
    private final List<TrieNode> children = new ArrayList<TrieNode>();
    
    /*
     * From the Java API documentation for the Pattern class:
     * Instances of this (Pattern) class are immutable and are safe for use by 
     * multiple concurrent threads. Instances of the Matcher class are not 
     * safe for such use.
     */
    private final Pattern pattern;
    
    private Route route;
    
    private PathElement pathElement;
    
    public TrieNode(PathElement elem) {
        
        this(elem, null);
    }
    
    public TrieNode(PathElement elem, Route route) {
        
        this.pattern = compilePattern(elem);
        this.pathElement = elem;
        this.route = route;
    }
    
    public TrieNode(String token) {
        
        this(token, null);
    }
    
    public TrieNode(String token, Route route) {
        
        this.pattern = compilePattern(token);
        this.route = route;
    }
    
    private Pattern compilePattern(String token) {
        
        StringBuilder routeRegex = new StringBuilder("^");
        routeRegex.append(token); //TODO do we need to escape regex symbols?
        routeRegex.append("$");
        return Pattern.compile(routeRegex.toString());
    }
    
    private Pattern compilePattern(PathElement elem) {
        
        StringBuilder routeRegex = new StringBuilder("^");
        
        if (elem instanceof NamedParameterElement) {
            
            NamedParameterElement namedElem = (NamedParameterElement)elem;
            if (namedElem.hasRegex()) {
                routeRegex.append("(").append(namedElem.regex()).append(")");
            } else {
                routeRegex.append("([^").append(PATH_ELEMENT_SEPARATOR).append("]+)");
            }
            
        } else if (elem instanceof SplatParameterElement) {
            
            //TODO
//            routeRegex.append("(");
//            if ((i + 1) == tokens.length) {
//                /* this is the last token */
//                routeRegex.append(".");
//            } else {
//                routeRegex.append("[^").append(PATH_ELEMENT_SEPARATOR).append("]");
//            }
//            routeRegex.append("*)");
            
        } else {
            
            routeRegex.append(elem.name());
        }
        
        routeRegex.append("$");
        return Pattern.compile(routeRegex.toString());
    }
    
    public boolean matches(String token) {
        
        return pattern().matcher(token).find();
    }
    
    public boolean matches(PathElement elem) {
        
        if (pathElement != null) {
            return pathElement.equals(elem);
        }
        
        return false;
    }
    
    public void addChild(TrieNode node) {
        
        this.children.add(node);
    }
    
    public List<TrieNode> getChildren() {
        
        return new ArrayList<TrieNode>(children);
    }
    
    public Pattern pattern() {
        
        return pattern;
    }
    
    public Route getRoute() {
        
        return route;
    }
    
    public void setRoute(Route route) {
        
        this.route = route;
    }
    
    public String toString() {
        
        return pattern.toString();
    }
}
