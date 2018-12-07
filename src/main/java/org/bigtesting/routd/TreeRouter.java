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
    
    public synchronized void add(Route route) {
        
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
            
            TreeNode matchingNode = currentNode.getMatchingChild(elem);
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
    
    /**
     * Returns a Route that matches the given URL path.
     * Note that the path is expected to be an undecoded URL path.
     * The router will handle any decoding that might be required.
     * 
     *  @param path an undecoded URL path
     *  @return the matching route, or null if none is found
     */
    public Route route(String path) {
        
        List<String> searchTokens = getPathAsSearchTokens(path);
        
        /* handle the case where path is '/' and route '/*' exists */
        if (searchTokens.isEmpty() && root.containsSplatChild() && !root.hasRoute()) {
            return root.getSplatChild().getRoute();
        }

        int remainingTokens = searchTokens.size();
        TreeNode currentMatchingNode = root;
        for (String token : searchTokens) {
            remainingTokens -= 1;

            List<TreeNode> matchingNodes = currentMatchingNode.getMatchingChildren(token);

            if (matchingNodes.isEmpty()) return null;

            currentMatchingNode = getMatchingNode(matchingNodes, remainingTokens);
            
            if (currentMatchingNode.isSplat() && 
                    !currentMatchingNode.hasChildren()) {
                return currentMatchingNode.getRoute();
            }
        }
        
        return currentMatchingNode.getRoute();
    }

    private TreeNode getMatchingNode(List<TreeNode> candidates, int remainingTokens) {
        /*
         * if there is more than one candidate and there are no more tokens to process,
         *   return the first candidate that has a route
         */
        if (candidates.size() > 1 && remainingTokens == 0) {
            for (TreeNode candidate : candidates) {
                if (candidate.hasRoute()) return candidate;
            }
        }
        return candidates.get(0);
    }
    
    private List<String> getPathAsSearchTokens(String path) {
        
        List<String> tokens = new ArrayList<String>();
        path = urlDecodeForRouting(path);
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
