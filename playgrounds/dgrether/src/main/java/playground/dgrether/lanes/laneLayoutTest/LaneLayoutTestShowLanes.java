/* *********************************************************************** *
 * project: org.matsim.*
 * LaneLayoutTestShowNetwork
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
package playground.dgrether.lanes.laneLayoutTest;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.QSimFactory;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vis.otfvis.OTFClientLive;
import org.matsim.vis.otfvis.OnTheFlyServer;

import playground.dgrether.utils.LogOutputEventHandler;


/**
 * @author dgrether
 *
 */
public class LaneLayoutTestShowLanes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config config = ConfigUtils.createConfig();
		config.network().setInputFile(LaneLayoutTestFileNames.NETWORK);
		config.network().setLaneDefinitionsFile(LaneLayoutTestFileNames.LANEDEFINITIONSV2);
		config.plans().setInputFile(LaneLayoutTestFileNames.POPULATION);
		config.scenario().setUseLanes(true);
		config.addQSimConfigGroup(new QSimConfigGroup());
//		config.otfVis().setLinkWidth(150.0f);
		config.otfVis().setDrawLinkIds(true);
		config.otfVis().setNodeOffset(30);
		config.getQSimConfigGroup().setSnapshotStyle("queue");

		Scenario sc = ScenarioUtils.loadScenario(config);
		EventsManager events = EventsUtils.createEventsManager();
		events.addHandler(new LogOutputEventHandler());
		QSim otfVisQSim = (QSim) new QSimFactory().createMobsim(sc, events);
		OnTheFlyServer server = OTFVis.startServerAndRegisterWithQSim(config, sc, events, otfVisQSim);
		OTFClientLive.run(config, server);
		otfVisQSim.run();
	
	}

}
