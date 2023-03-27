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

import java.util.Comparator;

/**
 * 
 * @author Luis Antunes
 */
public class PathElementComparator implements Comparator<String> {

    public int compare(String r1Elem, String r2Elem) {
        
        if (r1Elem.equals("")) return -1;
        if (r2Elem.equals("")) return 1;
        
        if (r1Elem.equals(WILDCARD) && !r2Elem.equals("")) return 1;
        if (r2Elem.equals(WILDCARD) && !r1Elem.equals("")) return -1;
        
        if (r1Elem.equals(WILDCARD) && r2Elem.equals("")) return 1;
        if (r2Elem.equals(WILDCARD) && r1Elem.equals("")) return -1;
        
        if (r1Elem.startsWith(PARAM_PREFIX) && !r2Elem.equals("") && !r2Elem.equals(WILDCARD)) return 1;
        if (r2Elem.startsWith(PARAM_PREFIX) && !r1Elem.equals("") && !r1Elem.equals(WILDCARD)) return -1;
        
        if (r1Elem.startsWith(PARAM_PREFIX) && (r2Elem.equals(WILDCARD) || r2Elem.equals(""))) return -1;
        if (r2Elem.startsWith(PARAM_PREFIX) && (r1Elem.equals(WILDCARD) || r1Elem.equals(""))) return 1;
        
        return r1Elem.compareTo(r2Elem);
    }

}
