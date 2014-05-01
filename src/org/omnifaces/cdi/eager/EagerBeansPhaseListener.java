/*
 * Copyright 2014 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.omnifaces.cdi.eager;

import static java.util.Collections.unmodifiableMap;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static org.omnifaces.util.Faces.getViewId;

import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;

import org.omnifaces.eventlistener.DefaultPhaseListener;

/**
 * 
 * @author Arjan Tijms
 * @since 1.8
 *
 */
public class EagerBeansPhaseListener extends DefaultPhaseListener {
	
	private static final long serialVersionUID = -7252366571645029385L;
	
	private static BeanManager beanManager;
	private static Map<String, List<Bean<?>>> beans;

	public EagerBeansPhaseListener() {
		super(RESTORE_VIEW);
	}
	
	@Override
	public void afterPhase(PhaseEvent event) {
		if (beans == null || beanManager == null || beans.get(getViewId()) == null) {
			return;
		}
		
		for (Bean<?> bean : beans.get(getViewId())) {
			beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean)).toString();
		}
	}
	
	static void init(BeanManager beanManager, Map<String, List<Bean<?>>> beans) {
		EagerBeansPhaseListener.beanManager = beanManager;
		EagerBeansPhaseListener.beans = unmodifiableMap(beans);
	}
	
}