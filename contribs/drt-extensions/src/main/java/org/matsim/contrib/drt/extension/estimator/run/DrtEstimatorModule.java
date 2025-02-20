package org.matsim.contrib.drt.extension.estimator.run;

import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;
import org.matsim.contrib.drt.analysis.DrtEventSequenceCollector;
import org.matsim.contrib.drt.extension.estimator.BasicDrtEstimator;
import org.matsim.contrib.drt.extension.estimator.DrtEstimateAnalyzer;
import org.matsim.contrib.drt.extension.estimator.DrtEstimator;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.DvrpMode;
import org.matsim.contrib.dvrp.run.DvrpModes;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;

import java.util.Optional;

/**
 * Main module that needs to be installed if any estimator is to be used.
 */
public class DrtEstimatorModule extends AbstractModule {

	@Override
	public void install() {

		MultiModeDrtConfigGroup drtConfigs = MultiModeDrtConfigGroup.get(getConfig());
		MultiModeDrtEstimatorConfigGroup configs = ConfigUtils.addOrGetModule(getConfig(), MultiModeDrtEstimatorConfigGroup.class);

		for (DrtConfigGroup cfg : drtConfigs.getModalElements()) {

			Optional<DrtEstimatorConfigGroup> estCfg = configs.getModalElement(cfg.mode);

			estCfg.ifPresent(drtEstimatorConfigGroup -> install(new ModeModule(cfg, drtEstimatorConfigGroup)));
		}
	}

	static final class ModeModule extends AbstractDvrpModeModule {

		private final DrtConfigGroup cfg;
		private final DrtEstimatorConfigGroup group;

		public ModeModule(DrtConfigGroup cfg, DrtEstimatorConfigGroup group) {
			super(group.mode);
			this.cfg = cfg;
			this.group = group;
		}

		@Override
		public void install() {

			// try with default injections and overwrite
			if (group.estimator == DrtEstimatorConfigGroup.EstimatorType.BASIC) {
				bindModal(DrtEstimator.class).toProvider(modalProvider(
					getter -> new BasicDrtEstimator(getter.getModal(DrtEventSequenceCollector.class), group, cfg)
				)).in(Singleton.class);
			}

			// DRT Estimators will be available as Map<DvrpMode, DrtEstimator>
			MapBinder.newMapBinder(this.binder(), DvrpMode.class, DrtEstimator.class)
				.addBinding(DvrpModes.mode(getMode()))
				.to(modalKey(DrtEstimator.class));

			addControlerListenerBinding().to(modalKey(DrtEstimator.class));

			bindModal(DrtEstimatorConfigGroup.class).toInstance(group);

			// Needs to run before estimators
			bindModal(DrtEstimateAnalyzer.class)
				.toProvider(
					modalProvider(getter -> new DrtEstimateAnalyzer(getter.getModal(DrtEstimator.class), getter.getModal(DrtEventSequenceCollector.class), group))
				)
				.in(Singleton.class);

			addControlerListenerBinding().to(modalKey(DrtEstimateAnalyzer.class));

		}

	}
}
