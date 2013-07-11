/* *********************************************************************** *
 * project: org.matsim.*
 * MultiModalDepartureHandler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2010 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package contrib.multimodal.simengine;

import java.util.HashSet;
import java.util.Set;

import org.matsim.api.core.v01.Id;
import contrib.multimodal.config.MultiModalConfigGroup;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.qsim.interfaces.DepartureHandler;
import org.matsim.core.utils.collections.CollectionUtils;

public class MultiModalDepartureHandler implements DepartureHandler {

	private MultiModalSimEngine simEngine;
	private Set<String> handledModes = new HashSet<String>();
	
	public MultiModalDepartureHandler(MultiModalSimEngine simEngine, MultiModalConfigGroup multiModalConfigGroup) {
		this.simEngine = simEngine;
		this.handledModes = CollectionUtils.stringToSet(multiModalConfigGroup.getSimulatedModes());
	}
	
	@Override
	public boolean handleDeparture(double now, MobsimAgent mobsimAgent, Id linkId) {

		if (handledModes.contains(mobsimAgent.getMode())) {
			if (mobsimAgent instanceof MobsimDriverAgent) {
				handleMultiModalDeparture(now, (MobsimDriverAgent)mobsimAgent, linkId);
				return true;
			} else {
				throw new UnsupportedOperationException("MobsimAgent is not from type MobsimDriverAgent - cannot handle departure. Found PersonAgent class is " + mobsimAgent.getClass().toString());
			}
		}
		
		return false;
	}
	
	private void handleMultiModalDeparture(double now, MobsimDriverAgent personAgent, Id linkId) {
		
		MultiModalQLinkExtension extension = simEngine.getMultiModalQLinkExtension(linkId);
		
		if ((personAgent.getDestinationLinkId().equals(linkId)) && (personAgent.chooseNextLinkId() == null)) {
			personAgent.endLegAndComputeNextState(now);
			this.simEngine.internalInterface.arrangeNextAgentState(personAgent);
			/* yyyy The "non-departure" should be caught in the framework.  kai, dec'11 */  
		} else {
			extension.addDepartingAgent(personAgent, now);
		}				
	}
}	
