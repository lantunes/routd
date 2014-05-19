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
    
    private List<TrieNode> children = new ArrayList<TrieNode>();
    
    /*
     * From the Java API documentation for the Pattern class:
     * Instances of this (Pattern) class are immutable and are safe for use by 
     * multiple concurrent threads. Instances of the Matcher class are not 
     * safe for such use.
     */
    private final Pattern pattern;
    
    public TrieNode(String token) {
        
        pattern = compilePattern(token);
    }
    
    private Pattern compilePattern(String token) {
        
        StringBuilder routeRegex = new StringBuilder("^").append(PATH_ELEMENT_SEPARATOR);
        
        routeRegex.append("$");
        return Pattern.compile(routeRegex.toString());
    }
    
    public List<TrieNode> getChildren() {
        
        return new ArrayList<TrieNode>(children);
    }
    
    public Pattern pattern() {
        
        return pattern;
    }
    
    public Route getRoute() {
        
        return null;
    }
    
    public String toString() {
        
        return pattern.toString();
    }
}
