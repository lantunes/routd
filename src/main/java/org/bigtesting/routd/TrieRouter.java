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


/**
 * 
 * @author Luis Antunes
 */
public class TrieRouter implements Router {

    private TrieNode root; 
    
    public void add(Route route) {
        
        List<PathElement> pathElements = route.getPathElements();
        
        if (root == null) {
            root = new TrieNode(PATH_ELEMENT_SEPARATOR);
        }
        
        TrieNode currentNode = root;
        for (PathElement elem : pathElements) {
            
            TrieNode matchingNode = getMatchingNode(elem, currentNode.getChildren());
            if (matchingNode == null) {
                TrieNode newChild = new TrieNode(elem);
                currentNode.addChild(newChild);
                currentNode = newChild;
            } else {
                currentNode = matchingNode;
            }
        }
        currentNode.setRoute(route);
    }
    
    private TrieNode getMatchingNode(PathElement elem, List<TrieNode> nodes) {
        
        for (TrieNode node : nodes) {
            if (node.matches(elem)) return node;
        }
        return null;
    }
    
    public Route route(String path) {
        
        List<String> searchTokens = getPathAsSearchTokens(path);
        
        TrieNode currentMatchingNode = root;
        for (String token : searchTokens) {
            
            TrieNode matchingNode = 
                    getFirstMatchingNode(token, currentMatchingNode.getChildren());
            if (matchingNode == null) return null;
            currentMatchingNode = matchingNode;
        }
        
        return currentMatchingNode.getRoute();
    }
    
    private TrieNode getFirstMatchingNode(String token, List<TrieNode> nodes) {
        
        for (TrieNode node : nodes) {
            if (node.matches(token)) return node;
        }
        return null;
    }
    
    private List<String> getPathAsSearchTokens(String path) {
        
        List<String> tokens = new ArrayList<String>();
        String[] pathElements = getPathElements(path);
        for (int i = 0; i < pathElements.length; i++) {
            String token = pathElements[i];
            if (token != null && token.trim().length() > 0) {
                tokens.add(token);
            }
        }
        return tokens;
    }
    
    public TrieNode getRoot() {
        
        return root;
    }
}
