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

/**
 * 
 * @author Luis Antunes
 */
public class PathParameterElement {
    
    private final String name;
    private final int index;
    private final String regex; 
    
    public PathParameterElement(String name, int index, String regex) {
        this.name = name;
        this.index = index;
        this.regex = regex;
    }
    
    /**
     * Returns the name of the element in the route.
     * Elements are prefixed by a colon.
     * 
     * @return the name of the element in the route
     */
    public String name() {
        return name;
    }
    
    /**
     * Returns the absolute position of the element
     * in the route.
     * 
     * @return the index of the element in the route
     */
    public int index() {
        return index;
    }
    
    /**
     * Returns the regex pattern for the element if it exists,
     * or null if no regex pattern was provided.
     * 
     * @return the regex pattern for the element if it exists, 
     * or null otherwise
     */
    public String regex() {
        return regex;
    }
}