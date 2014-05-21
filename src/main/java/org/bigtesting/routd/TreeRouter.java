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
public class TreeRouter implements Router {

    private TreeNode root; 
    
    public void add(Route route) {
        
        List<PathElement> pathElements = route.getPathElements();
        if (!pathElements.isEmpty() && route.endsWithPathSeparator()) {
            pathElements.add(
                    new StaticPathElement(PATH_ELEMENT_SEPARATOR, pathElements.size() - 1));
        }
        
        if (root == null) {
            root = new TreeNode(new StaticPathElement(PATH_ELEMENT_SEPARATOR, 0));
        }
        
        TreeNode currentNode = root;
        for (PathElement elem : pathElements) {
            
            TreeNode matchingNode = getMatchingNode(elem, currentNode.getChildren());
            if (matchingNode == null) {
                TreeNode newChild = new TreeNode(elem);
                currentNode.addChild(newChild);
                currentNode = newChild;
            } else {
                currentNode = matchingNode;
            }
        }
        currentNode.setRoute(route);
    }
    
    private TreeNode getMatchingNode(PathElement elem, List<TreeNode> nodes) {
        
        for (TreeNode node : nodes) {
            if (node.matches(elem)) return node;
        }
        return null;
    }
    
    public Route route(String path) {
        
        List<String> searchTokens = getPathAsSearchTokens(path);
        
        TreeNode currentMatchingNode = root;
        for (String token : searchTokens) {
            
            TreeNode matchingNode = 
                    getFirstMatchingNode(token, currentMatchingNode.getChildren());
            if (matchingNode == null) return null;
            currentMatchingNode = matchingNode;
            
            if (currentMatchingNode.isSplat() && 
                    currentMatchingNode.getChildren().isEmpty()) {
                return currentMatchingNode.getRoute();
            }
        }
        
        return currentMatchingNode.getRoute();
    }
    
    private TreeNode getFirstMatchingNode(String token, List<TreeNode> nodes) {
        
        /*
         * TODO
         * do the children have to be sorted? so that * matches before static, and named?
         */
        for (TreeNode node : nodes) {
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
        if (!tokens.isEmpty() && 
                path.trim().endsWith(PATH_ELEMENT_SEPARATOR)) {
            tokens.add(PATH_ELEMENT_SEPARATOR);
        }
        return tokens;
    }
    
    public TreeNode getRoot() {
        
        return root;
    }
}
