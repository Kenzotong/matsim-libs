/* *********************************************************************** *
 * project: org.matsim.*
 * EquilTest.java
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

package org.matsim.examples;

import java.util.Arrays;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.api.internal.MatsimReader;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.FacilitiesConfigGroup;
import org.matsim.core.controler.PrepareForSimUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.algorithms.EventWriterXML;
import org.matsim.core.mobsim.qsim.QSimBuilder;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.MutableScenario;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.testcases.MatsimTestUtils;
import org.matsim.utils.eventsfilecomparison.EventsFileComparator;

@RunWith(Parameterized.class)
public class EquilTest  {
	private static final Logger log = LogManager.getLogger( EquilTest.class ) ;

	@Rule public MatsimTestUtils utils = new MatsimTestUtils();

	private final boolean isUsingFastCapacityUpdate;

	public EquilTest(boolean isUsingFastCapacityUpdate) {
		this.isUsingFastCapacityUpdate = isUsingFastCapacityUpdate;
	}

	@Parameters(name = "{index}: isUsingfastCapacityUpdate == {0}")
	public static Collection<Object> parameterObjects () {
		Object [] capacityUpdates = new Object [] { false, true };
		return Arrays.asList(capacityUpdates);
	}

	@Test
	public void testEquil() {
		Config config = ConfigUtils.createConfig() ;
		config.controller().setOutputDirectory( utils.getOutputDirectory() );
		config.qsim().setUsingFastCapacityUpdate(this.isUsingFastCapacityUpdate);
		config.facilities().setFacilitiesSource( FacilitiesConfigGroup.FacilitiesSource.onePerActivityLinkInPlansFile );

		String netFileName = "test/scenarios/equil/network.xml";
		String popFileName = "test/scenarios/equil/plans100.xml";

		System.out.println( utils.getInputDirectory() );
		String referenceFileName = utils.getInputDirectory() + "events.xml.gz";

		String eventsFileName = utils.getOutputDirectory() + "events.xml.gz";

		MutableScenario scenario = ScenarioUtils.createMutableScenario(config );

		new MatsimNetworkReader(scenario.getNetwork()).readFile(netFileName);

		MatsimReader plansReader = new PopulationReader(scenario);
		plansReader.readFile(popFileName);

		EventsManager events = EventsUtils.createEventsManager();
		EventWriterXML writer = new EventWriterXML(eventsFileName);
		events.addHandler(writer);

		//		SimulationTimer.setTime(0); // I don't think this is needed. kai, may'10
		PrepareForSimUtils.createDefaultPrepareForSim(scenario).run();

		new QSimBuilder(scenario.getConfig()) //
			.useDefaults() //
			.build(scenario, events) //
			.run();

		writer.closeFile();

		final EventsFileComparator.Result result = new EventsFileComparator().setIgnoringCoordinates( true ).runComparison( referenceFileName , eventsFileName );
		Assert.assertEquals("different event files.", EventsFileComparator.Result.FILES_ARE_EQUAL, result );
	}
}
