///* *********************************************************************** *
// * project: org.matsim.*
// * Trb09Analysis
// *                                                                         *
// * *********************************************************************** *
// *                                                                         *
// * copyright       : (C) 2009 by the members listed in the COPYING,        *
// *                   LICENSE and WARRANTY file.                            *
// * email           : info at matsim dot org                                *
// *                                                                         *
// * *********************************************************************** *
// *                                                                         *
// *   This program is free software; you can redistribute it and/or modify  *
// *   it under the terms of the GNU General Public License as published by  *
// *   the Free Software Foundation; either version 2 of the License, or     *
// *   (at your option) any later version.                                   *
// *   See also COPYING, LICENSE and WARRANTY file                           *
// *                                                                         *
// * *********************************************************************** */
//package playground.benjamin.analysis;
//
//import org.apache.log4j.Logger;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.xy.XYSeriesCollection;
//import org.matsim.api.core.v01.ScenarioImpl;
//
//import playground.benjamin.BkPaths;
//import playground.dgrether.analysis.charts.DgAvgDeltaMoneyGroupChart;
//import playground.dgrether.analysis.charts.DgAvgDeltaMoneyQuantilesChart;
//import playground.dgrether.analysis.charts.DgAvgDeltaUtilsGroupChart;
//import playground.dgrether.analysis.charts.DgAvgDeltaUtilsModeGroupChart;
//import playground.dgrether.analysis.charts.DgAvgDeltaUtilsModeQuantilesChart;
//import playground.dgrether.analysis.charts.DgAvgDeltaUtilsQuantilesChart;
//import playground.dgrether.analysis.charts.DgDeltaUtilsModeGroupChart;
//import playground.dgrether.analysis.charts.DgMixedDeltaUtilsModeGroupChart;
//import playground.dgrether.analysis.charts.DgMixedModeSwitcherOnlyDeltaScoreIncomeModeChoiceChart;
//import playground.dgrether.analysis.charts.DgModalSplitDiffQuantilesChart;
//import playground.dgrether.analysis.charts.DgModalSplitGroupChart;
//import playground.dgrether.analysis.charts.DgModalSplitQuantilesChart;
//import playground.dgrether.analysis.charts.utils.DgChartWriter;
//import playground.dgrether.analysis.io.DgAnalysisPopulationReader;
//import playground.dgrether.analysis.io.DgHouseholdsAnalysisReader;
//import playground.dgrether.analysis.population.DgAnalysisPopulation;
//
//
//public class DgTrbAnalysis {
//
//	private static final Logger log = Logger.getLogger(DgTrbAnalysis.class);
//	
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		String netfile, plans1file, plans2file, housholdsfile;
//		int threshold;
//		
////		boolean isTestscenario = true;
////		String runNumber1 = "860";
////		String runNumber2 = "864";
//		
//		boolean isTestscenario = false;		
//		String runNumber1 = "891";
//		String runNumber2 = "895";
//		
//		String runid1 = "run" + runNumber1;
//		String runid2 = "run" + runNumber2;
//			
//		String runiddot1 = runid1 + ".";
//		String runiddot2 = runid2 + ".";
//		
//		if (isTestscenario){
//			netfile = BkPaths.RUNSSVN + runid1 + "/" + runiddot1 + "output_network.xml.gz";
//			plans1file = BkPaths.RUNSSVN + runid1 + "/" + runiddot1 + "output_plans.xml.gz";
//			plans2file = BkPaths.RUNSSVN + runid2 + "/" + runiddot2 + "output_plans.xml.gz";			
//			housholdsfile = BkPaths.SHAREDSVN + "studies/bkick/oneRouteTwoModeIncomeTest/households.xml";
//			threshold = 4;
//		}
//		else {
//			netfile = BkPaths.RUNSSVN + runid1 + "/" + runNumber1 + ".output_network.xml.gz";
//			plans1file = BkPaths.RUNSSVN + runid1 + "/" + runNumber1 + ".output_plans.xml.gz";
//			plans2file = BkPaths.RUNSSVN + runid2 + "/" + runNumber2 + ".output_plans.xml.gz";
//			housholdsfile = BkPaths.SHAREDSVN + "studies/dgrether/einkommenSchweiz/households_all_zrh30km_transitincl_10pct.xml.gz";
//			threshold = 100;
//		}
//
//		
////		runid1 += "best";
////		runid2 += "best";
////		
////		File file = new File(DgPaths.RUNBASE + runid1);
////		file.mkdir();
////		File file2 = new File(DgPaths.RUNBASE + runid2);
////		file2.mkdir();
////		
//		String modalSplitGroupChartFileRun1 = BkPaths.RUNSSVN + runid1 + "/"+runNumber1+"modalSplitGroupChart";
//		String modalSplitGroupChartFileRun2 = BkPaths.RUNSSVN + runid2 + "/"+runNumber2+"modalSplitGroupChart";
//		
//		String deltaUtilsModeGroupChartFile = BkPaths.RUNSSVN + runid2 + "/deltaUtilsModeGroupChart"+runNumber1+"vs"+runNumber2;
//
//		String avgDeltaUtilsGroupChartFile = BkPaths.RUNSSVN + runid2 + "/avgDeltaUtilsGroupChart"+runNumber1+"vs"+runNumber2;
//		String avgDeltaUtilsModeGroupChartFile = BkPaths.RUNSSVN + runid2 + "/avgDeltaUtilsModeGroupChart"+runNumber1+"vs"+runNumber2;
//
//		String avgDeltaMoneyGroupChartFile = BkPaths.RUNSSVN + runid2 + "/avgDeltaMoneyGroupChart"+runNumber1+"vs"+runNumber2;
//
//		String mixedDeltaUtilsModeGroupChartFile = BkPaths.RUNSSVN + runid2 + "/mixedDeltaUtilsModeGroupChart"+runNumber1+"vs"+runNumber2;
//		String mixedMsoDeltaUtilsModeGroupChartFile = BkPaths.RUNSSVN + runid2 + "/mixedMsoDeltaUtilsModeGroupChart"+runNumber1+"vs"+runNumber2;
//
//		//quantile charts
//		String modalSplitQuantilesChartFileRun1 = BkPaths.RUNSSVN + runid1 + "/"+runNumber1+"modalSplitQuantilesChart";
//		String modalSplitQuantilesChartFileRun2 = BkPaths.RUNSSVN + runid2 + "/"+runNumber2+"modalSplitQuantilesChart";
//		String modalSplitDiffQuantilesChartFileRun2 = BkPaths.RUNSSVN + runid2 + "/modalSplitQuantilesChart" + runNumber1 + "vs" +runNumber2;
//
//		String avgDeltaUtilsQuantilesChartFile = BkPaths.RUNSSVN + runid2 + "/avgDeltaUtilsQuantilesChart" + runNumber1 + "vs" + runNumber2;
//		String avgDeltaUtilsModeQuantilesChartFile = BkPaths.RUNSSVN + runid2 + "/avgDeltaUtilsModeQuantilesChart"+runNumber1+"vs"+runNumber2;
//
//		String avgDeltaMoneyQuantilesChartFile = BkPaths.RUNSSVN + runid2 + "/avgDeltaMoneyQuantilesChart"+runNumber1+"vs"+runNumber2;
//
//		
//
//		ScenarioImpl sc = new ScenarioImpl();
//
//
//		DgAnalysisPopulationReader pc = new DgAnalysisPopulationReader(sc);
//		DgAnalysisPopulation ana = pc.doPopulationAnalysis(netfile, plans1file, plans2file);
//
//		DgHouseholdsAnalysisReader hhr = new DgHouseholdsAnalysisReader(ana);
//		hhr.readHousholds(housholdsfile);
//		ana.calculateIncomeData();
//			
//		DgModalSplitGroupChart modalSplitGroupChartRun1 = new DgModalSplitGroupChart(ana, DgAnalysisPopulation.RUNID1, threshold);
//		DgChartWriter.writeChart(modalSplitGroupChartFileRun1, modalSplitGroupChartRun1.createChart());
//		DgModalSplitGroupChart modalSplitGroupChartRun2 = new DgModalSplitGroupChart(ana, DgAnalysisPopulation.RUNID2, threshold);
//		DgChartWriter.writeChart(modalSplitGroupChartFileRun2, modalSplitGroupChartRun2.createChart());
//
//		
//		DgDeltaUtilsModeGroupChart deltaUtilsModeGroupChart = new DgDeltaUtilsModeGroupChart(ana);
////		DgChartFrame frame = new DgChartFrame("test", deltaUtilsModeGroupChart.createChart());
//		DgChartWriter.writeChart(deltaUtilsModeGroupChartFile, deltaUtilsModeGroupChart.createChart());
//
//		DgAvgDeltaUtilsGroupChart avgDeltaUtilsGroupChart = new DgAvgDeltaUtilsGroupChart(ana, threshold);
//		//			DgChartFrame frame = new DgChartFrame("test", avgDeltaUtilsGroupChart.createChart());
//		DgChartWriter.writeChart(avgDeltaUtilsGroupChartFile, avgDeltaUtilsGroupChart.createChart());
//
//		DgAvgDeltaUtilsModeGroupChart avgDeltaUtilsModeGroupChart = new DgAvgDeltaUtilsModeGroupChart(ana, threshold);
//		//			DgChartFrame frame = new DgChartFrame("test", avgDScoreIncomeChartData.createChart());
//		DgChartWriter.writeChart(avgDeltaUtilsModeGroupChartFile, avgDeltaUtilsModeGroupChart.createChart());
//
//		DgAvgDeltaMoneyGroupChart avgDeltaMoneyGroupChart = new DgAvgDeltaMoneyGroupChart(ana, threshold);
//		//		DgChartFrame frame = new DgChartFrame("test", avgDeltaUtilsGroupChart.createChart());
//		DgChartWriter.writeChart(avgDeltaMoneyGroupChartFile, avgDeltaMoneyGroupChart.createChart());
//
//
//
//		//quantile charts
//		DgModalSplitQuantilesChart modalSplitQuantilesChartRun1 = new DgModalSplitQuantilesChart(ana, DgAnalysisPopulation.RUNID1);
//		//			DgChartFrame frame = new DgChartFrame("test", modalSplitQuantilesChartRun1.createChart());
//		DgChartWriter.writeChart(modalSplitQuantilesChartFileRun1, modalSplitQuantilesChartRun1.createChart());
//		DgModalSplitQuantilesChart modalSplitQuantilesChartRun2 = new DgModalSplitQuantilesChart(ana, DgAnalysisPopulation.RUNID2);
//		DgChartWriter.writeChart(modalSplitQuantilesChartFileRun2, modalSplitQuantilesChartRun2.createChart());
//
//		DgModalSplitDiffQuantilesChart modalSplitDiffQuantilesChartRun2 = new DgModalSplitDiffQuantilesChart(ana, DgAnalysisPopulation.RUNID1,  DgAnalysisPopulation.RUNID2);
////		DgChartFrame frame = new DgChartFrame("test", modalSplitDiffQuantilesChartRun2.createChart());
//		DgChartWriter.writeChart(modalSplitDiffQuantilesChartFileRun2, modalSplitDiffQuantilesChartRun2.createChart());
//
//		
//		DgAvgDeltaUtilsQuantilesChart avgDeltaUtilsQuantilesChart = new DgAvgDeltaUtilsQuantilesChart(ana);
////					DgChartFrame frame = new DgChartFrame("test", avgDeltaUtilsQuantilesChart.createChart());
//		DgChartWriter.writeChart(avgDeltaUtilsQuantilesChartFile, avgDeltaUtilsQuantilesChart.createChart());
//
//		DgAvgDeltaUtilsModeQuantilesChart avgDeltaUtilesModeQuantilesChart = new DgAvgDeltaUtilsModeQuantilesChart(ana, threshold);
////		DgChartFrame frame = new DgChartFrame("test", avgDeltaUtilesModeQuantilesChart.createChart());
//		DgChartWriter.writeChart(avgDeltaUtilsModeQuantilesChartFile, avgDeltaUtilesModeQuantilesChart.createChart());
//		
//		DgAvgDeltaMoneyQuantilesChart avgDeltaMoneyQuantilesChart = new DgAvgDeltaMoneyQuantilesChart(ana);
//		//		DgChartFrame frame = new DgChartFrame("test", avgDeltaUtilsQuantilesChart.createChart());
//		JFreeChart jfChart = avgDeltaMoneyQuantilesChart.createChart();
//		DgChartWriter.writeChart(avgDeltaMoneyQuantilesChartFile, jfChart);
//
//		writeMixedDeltaUtilsModeGroupChart(deltaUtilsModeGroupChart, avgDeltaUtilesModeQuantilesChart, 
//				mixedDeltaUtilsModeGroupChartFile, mixedMsoDeltaUtilsModeGroupChartFile);
//		
//		log.debug("ya esta ;-)");
//			
//	}
//	
//  public static void writeMixedDeltaUtilsModeGroupChart(DgDeltaUtilsModeGroupChart deltaUtilsModeGroupChart, 
//  		DgAvgDeltaUtilsModeQuantilesChart avgDScoreModeIncomeChartData, 
//  		String mixedDeltaScoreIncomeChartFile, String mixedMsoDeltaScoreIncomeChartFile){
//		DgMixedDeltaUtilsModeGroupChart mixedDsIncomeChart = new DgMixedDeltaUtilsModeGroupChart();
//		XYSeriesCollection modeChoiceDataset = deltaUtilsModeGroupChart.createDeltaScoreIncomeModeChoiceDataset();
//		mixedDsIncomeChart.addIncomeModeChoiceDataSet(modeChoiceDataset);
//		XYSeriesCollection avgScoreDataset = avgDScoreModeIncomeChartData.getDataset();
//		mixedDsIncomeChart.addAvgDeltaScoreIncomeDs(avgScoreDataset);
////		DgChartFrame frame = new DgChartFrame("test", mixedDsIncomeChart.createChart());
//		DgChartWriter.writeChart(mixedDeltaScoreIncomeChartFile, mixedDsIncomeChart.createChart());
//		
//		XYSeriesCollection ds2 = new XYSeriesCollection();
//		ds2.addSeries(modeChoiceDataset.getSeries(2));
//		ds2.addSeries(modeChoiceDataset.getSeries(3));
//		XYSeriesCollection ds3 = new XYSeriesCollection();
//		ds3.addSeries(avgScoreDataset.getSeries(2));
//		ds3.addSeries(avgScoreDataset.getSeries(3));
//		DgMixedModeSwitcherOnlyDeltaScoreIncomeModeChoiceChart mixedSwichterOnlyDsIncomeChart = new DgMixedModeSwitcherOnlyDeltaScoreIncomeModeChoiceChart();
//		mixedSwichterOnlyDsIncomeChart.addIncomeModeChoiceDataSet(ds2);
//		mixedSwichterOnlyDsIncomeChart.addAvgDeltaScoreIncomeDs(ds3);
////		DgChartFrame frame = new DgChartFrame("test", mixedSwichterOnlyDsIncomeChart.createChart());
//		DgChartWriter.writeChart(mixedMsoDeltaScoreIncomeChartFile, mixedSwichterOnlyDsIncomeChart.createChart());
//  }
//
//}
