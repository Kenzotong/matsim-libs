/* *********************************************************************** *
 * project: org.matsim.*
 * ReactRouteGuidance.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

/**
 * 
 */
package playground.johannes.eut;

import org.matsim.network.Link;
import org.matsim.network.NetworkLayer;
import org.matsim.plans.Route;
import org.matsim.router.Dijkstra;
import org.matsim.router.util.LeastCostPathCalculator;
import org.matsim.router.util.TravelCostI;
import org.matsim.router.util.TravelTimeI;
import org.matsim.withinday.routeprovider.RouteProvider;

/**
 * @author illenberger
 *
 */
public class ReactRouteGuidance implements RouteProvider {
	
	private LeastCostPathCalculator algorithm;
	
	private RoutableLinkCost linkcost;
	
	public ReactRouteGuidance(NetworkLayer network, TravelTimeI traveltimes) {
		linkcost = new RoutableLinkCost();
		linkcost.traveltimes = traveltimes;
		algorithm = new Dijkstra(network, linkcost, linkcost);	
	}
	
	public int getPriority() {
		return 10;
	}

	public boolean providesRoute(Link currentLink, Route subRoute) {
		return true;
	}

	public synchronized Route requestRoute(Link departureLink, Link destinationLink,
			double time) {
		return algorithm.calcLeastCostPath(departureLink.getToNode(),
					destinationLink.getFromNode(), time);
	}

	public void setPriority(int p) {

	}

	private class RoutableLinkCost implements TravelTimeI, TravelCostI {

		private TravelTimeI traveltimes;
		
		public double getLinkTravelTime(Link link, double time) {
			return traveltimes.getLinkTravelTime(link, time);
		}

		public double getLinkTravelCost(Link link, double time) {
			return traveltimes.getLinkTravelTime(link, time);
		}
		
	}
}
