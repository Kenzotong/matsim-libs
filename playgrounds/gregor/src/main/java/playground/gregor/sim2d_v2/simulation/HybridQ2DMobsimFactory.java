package playground.gregor.sim2d_v2.simulation;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.events.SynchronizedEventsManagerImpl;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.mobsim.framework.MobsimFactory;
import org.matsim.core.mobsim.framework.Mobsim;
import org.matsim.core.mobsim.qsim.ActivityEngine;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.TeleportationEngine;
import org.matsim.core.mobsim.qsim.agents.AgentFactory;
import org.matsim.core.mobsim.qsim.agents.PopulationAgentSource;
import org.matsim.core.mobsim.qsim.qnetsimengine.DefaultQSimEngineFactory;
import org.matsim.core.mobsim.qsim.qnetsimengine.ParallelQNetsimEngineFactory;
import org.matsim.core.mobsim.qsim.qnetsimengine.QNetsimEngine;
import org.matsim.core.mobsim.qsim.qnetsimengine.QNetsimEngineFactory;

public class HybridQ2DMobsimFactory implements MobsimFactory {

	private final static Logger log = Logger.getLogger(HybridQ2DMobsimFactory.class);
	
	Sim2DEngine sim2DEngine = null;

	@Override
	public Mobsim createMobsim(Scenario sc, EventsManager eventsManager) {

		QSimConfigGroup conf = sc.getConfig().getQSimConfigGroup();
		if (conf == null) {
			throw new NullPointerException("There is no configuration set for the QSim. Please add the module 'qsim' to your config file.");
		}

		// Get number of parallel Threads
		int numOfThreads = conf.getNumberOfThreads();
		QNetsimEngineFactory netsimEngFactory;
		if (numOfThreads > 1) {
			eventsManager = new SynchronizedEventsManagerImpl(eventsManager);
			netsimEngFactory = new ParallelQNetsimEngineFactory();
			log.info("Using parallel QSim with " + numOfThreads + " threads.");
		} else {
			netsimEngFactory = new DefaultQSimEngineFactory();
		}
		QSim qSim1 = new QSim(sc, eventsManager);
		ActivityEngine activityEngine = new ActivityEngine();
		qSim1.addMobsimEngine(activityEngine);
		qSim1.addActivityHandler(activityEngine);
		QNetsimEngine netsimEngine = netsimEngFactory.createQSimEngine(qSim1, MatsimRandom.getRandom());
		qSim1.addMobsimEngine(netsimEngine);
		qSim1.addDepartureHandler(netsimEngine.getDepartureHandler());
		TeleportationEngine teleportationEngine = new TeleportationEngine();
		qSim1.addMobsimEngine(teleportationEngine);
		QSim qSim = qSim1;
		AgentFactory agentFactory;

		if (!sc.getConfig().controler().getMobsim().equals("hybridQ2D")) {
			throw new RuntimeException("This factory does not make sense for " + sc.getConfig().controler().getMobsim()  );
		} else {
			agentFactory = new Sim2DAgentFactory(qSim,sc);
		}

		PopulationAgentSource agentSource = new PopulationAgentSource(sc.getPopulation(), agentFactory, qSim);
		qSim.addAgentSource(agentSource);

		Sim2DEngine e = new Sim2DEngine(qSim);
		this.sim2DEngine = e;
		qSim.addMobsimEngine(e);
		Sim2DDepartureHandler d = new Sim2DDepartureHandler(e);
		qSim.addDepartureHandler(d);
		
		
//		//DEBUG
//		QLinkControl contr = new QLinkControl(qSim);
//		qSim.addMobsimEngine(contr);
//		//END DEBUG
		
		return qSim;
	}

	public Sim2DEngine getSim2DEngine() {
		return this.sim2DEngine;
	}
}
