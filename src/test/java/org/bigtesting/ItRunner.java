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
package org.bigtesting;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * 
 * @author Luis Antunes
 */
public class ItRunner extends BlockJUnit4ClassRunner {

    public ItRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methods = super.computeTestMethods();
        List<FrameworkMethod> itMethods = getTestClass().getAnnotatedMethods(It.class);
        for (FrameworkMethod itMethod : itMethods) {
            if (!methods.contains(itMethod)) {
                itMethod.getMethod().setAccessible(true);
                methods.add(itMethod);
            }
        }
        return methods;
    }
    
    @Override
    protected String testName(FrameworkMethod method) {
        Annotation itAnnotation = method.getAnnotation(It.class);
        return itAnnotation != null ? 
                "It " + ((It)itAnnotation).value() : super.testName(method);
    }
}