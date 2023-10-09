package org.katas.graph;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;

import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.OrientDBConfigBuilder;

public class DbService implements AutoCloseable
{
	private static final String URL_REMOTE_PREFIX = "remote:";
	private static final String DATABASE_NAME = "kata1";
	private static DbService INSTANCE;
	private final OrientGraph db;

	public static DbService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DbService();
		}
		return INSTANCE;
	}

	private DbService() {
		this.db = newOrientDB();
	}

	public OrientGraph getDb() {
		return this.db;
	}

	private static OrientGraph newOrientDB() {
		OrientDBConfigBuilder builder = new OrientDBConfigBuilder();
		OrientDBConfig orientDBConfig = builder.build();
		String dbAddress = URL_REMOTE_PREFIX + "localhost" + ":" + "2424" + "/" + DATABASE_NAME;
		OrientDB orientDB = new OrientDB(dbAddress, orientDBConfig);
		var factory = new OrientGraphFactory(orientDB, DATABASE_NAME, null, "root", "root4me");
		var graph = newOrientGraph(factory);
		return graph;
	}

	private static OrientGraph newOrientGraph(OrientGraphFactory factory)
	{
		BaseConfiguration config = new BaseConfiguration();
		config.setProperty("orient-transactional", true); // enables transactions support
//		config.setProperty("blueprints.orientdb.autoStartTx", false); // disables auto-start of transaction
		OrientGraph	graph = new OrientGraph(factory, config, true);
//		graph.tx().onReadWrite(Transaction.READ_WRITE_BEHAVIOR.MANUAL); // disables auto-start of transaction
		return graph;
	}

	public void close() {
		if (this.db != null) {
			this.db.close();
		}
		INSTANCE = null;
	}
}
