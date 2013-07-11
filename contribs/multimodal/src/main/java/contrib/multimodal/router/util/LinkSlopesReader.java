/* *********************************************************************** *
 * project: org.matsim.*
 * LinkSlopesReader.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
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

package contrib.multimodal.router.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import contrib.multimodal.config.MultiModalConfigGroup;
import org.matsim.utils.objectattributes.ObjectAttributes;
import org.matsim.utils.objectattributes.ObjectAttributesXmlReader;

public class LinkSlopesReader {

	protected static final Logger log = Logger.getLogger(LinkSlopesReader.class);
	
	public static final String ATTRIBUTE_NAME = "slope";
	
	/**
	 * If height information for a network is available, the links' slopes
	 * is returned in a Map<LinkId, Slope in %>.
	 *  
	 * It is assumed that the heightInformation ObjectAttributes contain a field
	 * "slope" for each link Id in the network.
	 * 
	 * @return Map<LinkId, Slope in %>
	 */
	public Map<Id, Double> getLinkSlopes(MultiModalConfigGroup configGroup, Network network) {
		
		ObjectAttributes slopeInformation = this.getSlopeInformation(configGroup);
		
		Map<Id, Double> linkSlopes = this.getLinkSlopes(network, slopeInformation);
		
		int found = linkSlopes.size();
		int total = network.getLinks().size();
		log.info("Found slope information for " + found + " of " + total + " links.");
		
		return linkSlopes;
	}
	
	/**
	 * Reads height information from the file specified in the config group
	 * and returns them as ObjectAttributes.
	 */
	private ObjectAttributes getSlopeInformation(MultiModalConfigGroup configGroup) {
		
		ObjectAttributes objectAttributes = new ObjectAttributes();
		String slopeInformationFile = configGroup.getSlopeInformationFile();
		if (slopeInformationFile != null) {
			log.info("Loading slope information from " + slopeInformationFile);
			new ObjectAttributesXmlReader(objectAttributes).parse(slopeInformationFile);
		} else {
			log.warn("No slope information file specified in the multi modal config group!");
		}
		return objectAttributes;
	}
	
	private Map<Id, Double> getLinkSlopes(Network network, ObjectAttributes slopeInformation) {
		
		Map<Id, Double> linkSlopes = new HashMap<Id, Double>();
		
		for (Id linkId : network.getLinks().keySet()) {
			String slope = slopeInformation.getAttribute(linkId.toString(), ATTRIBUTE_NAME).toString();
			if (slope != null) linkSlopes.put(linkId, Double.valueOf(slope));
		}
		return linkSlopes;
	}
}
