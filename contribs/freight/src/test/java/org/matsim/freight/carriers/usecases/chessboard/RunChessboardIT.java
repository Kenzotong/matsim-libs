/*
 *   *********************************************************************** *
 *   project: org.matsim.*
 *   *********************************************************************** *
 *                                                                           *
 *   copyright       : (C)  by the members listed in the COPYING,        *
 *                     LICENSE and WARRANTY file.                            *
 *   email           : info at matsim dot org                                *
 *                                                                           *
 *   *********************************************************************** *
 *                                                                           *
 *     This program is free software; you can redistribute it and/or modify  *
 *     it under the terms of the GNU General Public License as published by  *
 *     the Free Software Foundation; either version 2 of the License, or     *
 *     (at your option) any later version.                                   *
 *     See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                           *
 *   ***********************************************************************
 *
 */

package org.matsim.freight.carriers.usecases.chessboard;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.population.routes.PopulationComparison;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.testcases.MatsimTestUtils;
import org.matsim.utils.eventsfilecomparison.EventsFileComparator;

public class RunChessboardIT {

	@Rule public MatsimTestUtils utils = new MatsimTestUtils() ;

	@Test
	public void runChessboard() {
		String [] args = { IOUtils.extendUrl( ExamplesUtils.getTestScenarioURL( "freight-chessboard-9x9" ), "config.xml" ).toString()
				, "--config:controler.outputDirectory", utils.getOutputDirectory()
				, "--config:controler.lastIteration", "1"
		};

		try{
			RunChessboard.main( args );
			{
				Population expected = PopulationUtils.createPopulation( ConfigUtils.createConfig() ) ;
				PopulationUtils.readPopulation( expected, utils.getInputDirectory() + "/output_plans.xml.gz" );

				Population actual = PopulationUtils.createPopulation( ConfigUtils.createConfig() ) ;
				PopulationUtils.readPopulation( actual, utils.getOutputDirectory() + "/output_plans.xml.gz" );

				PopulationComparison.Result result = new PopulationComparison().compare(expected, actual);
				Assert.assertSame(PopulationComparison.Result.equal, result);
			}
			{
				String expected = utils.getInputDirectory() + "/output_events.xml.gz" ;
				String actual = utils.getOutputDirectory() + "/output_events.xml.gz" ;
				EventsFileComparator.Result result = EventsUtils.compareEventsFiles( expected, actual );
				Assert.assertEquals( EventsFileComparator.Result.FILES_ARE_EQUAL, result );
			}
		} catch (Exception ee ) {
			ee.printStackTrace();
			Assert.fail("something went wrong");
		}

	}

}
