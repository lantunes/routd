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
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 
 * @author Luis Antunes
 */
public class Route {

    private final String resourcePath;
    private final List<PathParameterElement> pathParamElements;
    
    public Route(String paramPath) {
        
        if (paramPath == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        this.resourcePath = paramPath;
        this.pathParamElements = extractPathParamElements();
    }
    
    private List<PathParameterElement> extractPathParamElements() {
        
        List<PathParameterElement> elements = new ArrayList<PathParameterElement>();
        Matcher m = CUSTOM_REGEX_PATTERN.matcher(resourcePath);
        LinkedList<String> regexes = new LinkedList<String>();
        while (m.find()) {
            regexes.add(resourcePath.substring(m.start() + 1, m.end() - 1));
        }
        String path = m.replaceAll("");
        String[] pathElements = getPathElements(path);
        for (int i = 0; i < pathElements.length; i++) {
            String currentElement = pathElements[i];
            if (currentElement.startsWith(PARAM_PREFIX)) {
                currentElement = currentElement.substring(1);
                String regex = null;
                if (!regexes.isEmpty()) {
                    regex = regexes.removeFirst();
                }
                elements.add(new PathParameterElement(currentElement, i, regex));
            }
        }
        return elements;
    }
    
    public String getResourcePath() {
        
        return resourcePath;
    }
    
    public String toString() {
        
        return resourcePath;
    }
    
    public List<PathParameterElement> pathParameterElements() {
        
        return pathParamElements;            
    }
    
    public String getPathParameter(String paramName, String path) {
        
        List<PathParameterElement> pathParams = pathParameterElements();
        String[] pathTokens = RouteHelper.getPathElements(path);
        
        for (PathParameterElement pathParam : pathParams) {
            
            if (pathParam.name().equals(paramName)) return pathTokens[pathParam.index()];
        }
        
        return null;
    }
    
    public String[] splat(String path) {
        
        //TODO implement splat parameters (issue #1)
        return new String[]{};
    }
    
    public int hashCode() {
        
        int hash = 1;
        hash = hash * 13 + (resourcePath == null ? 0 : resourcePath.hashCode());
        return hash;
    }
    
    public boolean equals(Object o) {
        
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Route)) return false;
        Route that = (Route)o;
        return 
                (this.resourcePath == null ? that.resourcePath == null : 
                    this.resourcePath.equals(that.resourcePath));
    }
}
